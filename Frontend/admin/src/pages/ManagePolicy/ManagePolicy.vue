<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus, faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import Pagination from '@/components/Pagination/BasePagination.vue';
import { computed, ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import ManagePolicyEdit from '../ManagePolicyEdit/ManagePolicyEdit.vue';
import styles from './ManagePolicy.module.scss';
import Swal from 'sweetalert2';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface PolicyDetail {
  id: string;
  name: string;
  symbol: string;
  slug: string;
  detail: string;
  createAt: string;
}

const searchText = ref('');
const results = ref<PolicyDetail[]>([]);
const currentPage = ref(1);
const pageSize = ref(10);
const isEdit = ref(false);
const isLoading = ref(true);

const itemEdit = ref();
const editType = ref(1);

const paramAxios = ref();

const getListPolicy = useAxios<DataResponse>('get', '/policy', {}, {}, paramAxios.value);

const removeAccents = (str: string) => {
  return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '');
};

const filteredProducts = computed(() => {
  if (searchText.value.trim() === '') {
    return results.value;
  } else {
    const searchTerm = searchText.value.toLowerCase();
    return results.value.filter((e: { name: string }) =>
      removeAccents(e.name.toLowerCase()).includes(removeAccents(searchTerm))
    );
  }
});

const displayNews = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  if (filteredProducts.value.length > pageSize.value)
    return filteredProducts.value.slice(start, end);
  else return filteredProducts.value;
});

const handleEditPolicy = (item: PolicyDetail) => {
  itemEdit.value = item;
  isEdit.value = true;
  editType.value = 2;
};

const handleAddPolicy = () => {
  isEdit.value = true;
  editType.value = 1;
  itemEdit.value = null;
};

const handleUpdatePolicy = (item: PolicyDetail, editTypeUpdate = 1) => {
  if (editTypeUpdate === 1) displayNews.value.push(item);
  else {
    results.value.splice(
      results.value.findIndex((e: PolicyDetail) => e.id === item.id),
      1,
      item
    );
  }
};

const handleDeletePolicy = (item: PolicyDetail) => {
  Swal.fire({
    title: `Bạn có chắc chắn muốn xóa "${item.name}" không?`,
    icon: 'warning',
    showDenyButton: true,
    confirmButtonText: 'Hủy',
    denyButtonText: 'Đồng ý',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    if (result.isDenied) {
      const deleteCate = useAxios<DataResponse>(
        'delete',
        `/policy/${item.id}`,
        {},
        {},
        paramAxios.value
      );

      watch(deleteCate.response, () => {
        results.value.splice(
          results.value.findIndex((e: PolicyDetail) => e.id === item.id),
          1
        );
        // console.log(results.value, displayNews.value);

        Swal.fire({
          title: 'Xóa chính sách thành công!',
          icon: 'success',
          customClass: {
            popup: styles['container-popup'],
            confirmButton: styles['confirm-button'],
            denyButton: styles['deny-button']
          }
        });
      });

      watch(deleteCate.error, () => {
        // console.log(value);
        Swal.fire({
          title: 'Lỗi!',
          icon: 'error',
          customClass: {
            popup: styles['container-popup'],
            confirmButton: styles['confirm-button'],
            denyButton: styles['deny-button']
          }
        });
      });
    }
  });
};

//Pagination Handle
const scrollToTop = (top: number) => {
  window.scrollTo({
    top: top,
    behavior: 'smooth'
  });
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  scrollToTop(0);
};

watch(getListPolicy.response, (value) => {
  results.value = value?.data;
});

watch(getListPolicy.isLoading, (value) => {
  isLoading.value = value;
});

// watch(displayNews, (value) => {
//   // console.log(filteredProducts.value.length);
// });
</script>
<template>
  <div>
    <div v-if="isEdit">
      <manage-policy-edit
        :content="itemEdit"
        :edit-type="editType"
        @handle-update-policy="handleUpdatePolicy"
        @close="isEdit = false"
      />
    </div>
    <div v-else :class="$style.mn_policy">
      <div :class="$style['mn_policy-heading']">
        <h3>Quản lý chính sách</h3>
        <div :class="$style['mn_policy-heading-customize']">
          <button @click="handleAddPolicy">
            <font-awesome-icon :icon="faPlus" />
            Thêm chính sách
          </button>
          <div :class="$style['mn_policy-heading-customize-search']">
            <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['icon-search']" />
            <input type="text" v-model="searchText" placeholder="Tìm kiếm" />
          </div>
        </div>
      </div>
      <div :class="$style['mn_policy-body']">
        <table :class="$style['mn_policy-body-table']">
          <thead>
            <tr>
              <th
                :class="[$style['mn_policy-body-table-no'], $style['mn_policy-body-table-title']]"
              >
                STT
              </th>
              <th
                :class="[$style['mn_policy-body-table-name'], $style['mn_policy-body-table-title']]"
              >
                Tên Chính Sách
              </th>
              <th
                :class="[$style['mn_policy-body-table-date'], $style['mn_policy-body-table-title']]"
              >
                Ngày Cập Nhật
              </th>
              <th
                :class="[$style['mn_policy-body-table-ion'], $style['mn_policy-body-table-title']]"
              >
                Biểu Tượng
              </th>
              <th
                :class="[
                  $style['mn_policy-body-table-customize'],
                  $style['mn_policy-body-table-title']
                ]"
              ></th>
            </tr>
          </thead>
        </table>

        <div :class="$style['mn_policy-body-table-list']">
          <loading-component v-if="isLoading" />
          <table v-else :class="$style['mn_policy-body-table']">
            <tbody>
              <tr v-for="(item, index) in displayNews" :key="index">
                <th
                  :class="[$style['mn_policy-body-table-no'], $style['mn_policy-body-table-item']]"
                >
                  {{ index + 1 }}
                </th>
                <th
                  :class="[
                    $style['mn_policy-body-table-name'],
                    $style['mn_policy-body-table-item']
                  ]"
                >
                  {{ item.name }}
                </th>
                <th
                  :class="[
                    $style['mn_policy-body-table-date'],
                    $style['mn_policy-body-table-item']
                  ]"
                >
                  {{ item.createAt.split('T')[0] }}
                </th>
                <th
                  :class="[
                    $style['mn_policy-body-table-icon'],
                    $style['mn_policy-body-table-item']
                  ]"
                >
                  <img :src="item.symbol" alt="none" />
                </th>
                <th
                  :class="[
                    $style['mn_policy-body-table-customize'],
                    $style['mn_policy-body-table-item']
                  ]"
                >
                  <button :class="$style['btn-room-trash']" @click="handleDeletePolicy(item)">
                    <font-awesome-icon :icon="faTrash" />
                  </button>

                  <button :class="$style['btn-room-edit']" @click="handleEditPolicy(item)">
                    <font-awesome-icon :icon="faPen" />
                  </button>
                </th>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div :class="$style['mn_policy-footer']">
        <pagination
          :total="Math.ceil(filteredProducts.length / pageSize)"
          :current-page="currentPage"
          :page-size="pageSize"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ManagePolicy.module.scss';
</style>

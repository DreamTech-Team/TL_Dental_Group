<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus, faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import { RouterLink } from 'vue-router';
import Pagination from '@/components/Pagination/BasePagination.vue';
import ic_private from '@/assets/imgs/Policy/icon/ic_private.svg';
import { computed, ref } from 'vue';

const datebase = [
  { name: '1Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: '2Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: '3Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: '4Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private },
  { name: 'Chính sách bảo mật', date: '01/01/2001', icon: ic_private }
];

const searchText = ref('');
// const tmpResult = ref(datebase);
const results = ref(datebase);

const currentPage = ref(1);
const pageSize = ref(10);

const removeAccents = (str: string) => {
  return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '');
};

const filteredProducts = computed(() => {
  if (searchText.value.trim() === '') {
    return results.value;
  } else {
    const searchTerm = searchText.value.toLowerCase();
    return results.value.filter((datebase) =>
      removeAccents(datebase.name.toLowerCase()).includes(removeAccents(searchTerm))
    );
  }
});

const displayNews = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  console.log(start, end);

  return filteredProducts.value.slice(start, end);
});

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
</script>
<template>
  <div :class="$style.mn_policy">
    <div :class="$style['mn_policy-heading']">
      <h3>Quản lý chính sách</h3>
      <div :class="$style['mn_policy-heading-customize']">
        <button>
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
            <th :class="[$style['mn_policy-body-table-no'], $style['mn_policy-body-table-title']]">
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
            <th :class="[$style['mn_policy-body-table-ion'], $style['mn_policy-body-table-title']]">
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
        <table :class="$style['mn_policy-body-table']">
          <tbody>
            <tr v-for="(item, index) in displayNews" :key="index">
              <th :class="[$style['mn_policy-body-table-no'], $style['mn_policy-body-table-item']]">
                {{ index + 1 }}
              </th>
              <th
                :class="[$style['mn_policy-body-table-name'], $style['mn_policy-body-table-item']]"
              >
                {{ item.name }}
              </th>
              <th
                :class="[$style['mn_policy-body-table-date'], $style['mn_policy-body-table-item']]"
              >
                {{ item.date }}
              </th>
              <th
                :class="[$style['mn_policy-body-table-ion'], $style['mn_policy-body-table-item']]"
              >
                <img :src="item.icon" alt="none" />
              </th>
              <th
                :class="[
                  $style['mn_policy-body-table-customize'],
                  $style['mn_policy-body-table-item']
                ]"
              >
                <button :class="$style['btn-room-trash']">
                  <font-awesome-icon :icon="faTrash" />
                </button>
                <router-link :to="`mnpolicy/${item.name}`">
                  <button :class="$style['edit-room-btn']">
                    <font-awesome-icon :icon="faPen" />
                  </button>
                </router-link>
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
</template>

<style module scoped lang="scss">
@import './ManagePolicy.module.scss';
</style>

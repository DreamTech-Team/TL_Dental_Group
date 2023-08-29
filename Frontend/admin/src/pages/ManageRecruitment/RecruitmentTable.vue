<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import Pagination from '@/components/Pagination/BasePagination.vue';
import ModalUpdate from './ModalRecruitment/ModalUpdate.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import Loading from '@/components/LoadingComponent/LoadingComponent.vue';

interface RecruitmentItem {
  id: string;
  title: string;
  position: string;
  location: string;
  department: string;
  working_form: string;
  description: string;
  requirements: string;
  benefit: string;
  contact: string;
  treatment: string;
  slug: string;
  createAt: string;
}

interface Item {
  id: string;
  title: string;
  position: string;
  type: string;
}

//Array to store initial values
const tempArrays = ref<RecruitmentItem[]>([]);
const products = ref<Item[]>([]);
const chosenObject = ref<RecruitmentItem | undefined>();

const isOpen1 = ref(false); //Open Modal Update
const searchText = ref('');
const results = ref(products); //Final Render
const scrollContainer = ref<HTMLElement | null>(null); //Scroll table to top when change page
const debounceTimer = ref<number | null>(null); //searchData delay
const loadingStatus = ref(false);

const totalPage = ref(0);
const currentPage = ref(0);
const deps = ref([]);

//Props
const context = defineProps({
  total: {
    type: Number,
    required: false
  }
});

watch(context, () => {
  if (context.total !== undefined) {
    totalPage.value = context.total;
  }
});

//GET DATA
const { response, isLoading } = useAxios<DataResponse>(
  'get',
  `/recruitment/?page=${currentPage.value}&pageSize=10`,
  {},
  {},
  deps.value
);

//Convert array to compatible with data
watch(response, () => {
  tempArrays.value = response.value?.data?.data;
  results.value = tempArrays.value.map((item: RecruitmentItem) => {
    return {
      id: item.id,
      title: item.title,
      position: item.position,
      type: item.working_form
    };
  });
});

//Data from Modal Update
const onUpdateContent2 = (data: { ItemAdd: RecruitmentItem }) => {
  results.value.forEach((item) => {
    if (item.id === data.ItemAdd.id) {
      item.title = data.ItemAdd.title;
      item.position = data.ItemAdd.position;
      item.type = data.ItemAdd.working_form;
    }
  });
  tempArrays.value.forEach((item, index) => {
    if (item.id === data.ItemAdd.id) {
      tempArrays.value[index] = data.ItemAdd;
    }
  });
};

// Trong phần code xử lý sự kiện
const updateRecruitment = (id: string) => {
  chosenObject.value = tempArrays.value.find((obj: RecruitmentItem) => obj.id === id);
  isOpen1.value = true;
};

//Function call API Search
const searchProduct = () => {
  loadingStatus.value = true;
  const searchProduct = useAxios<DataResponse>(
    'get',
    `/recruitment/?q=${searchText.value}`,
    {},
    {},
    deps.value
  );

  watch(searchProduct.response, () => {
    tempArrays.value = searchProduct.response.value?.data?.data;
    results.value = tempArrays.value.map((item: RecruitmentItem) => {
      return {
        id: item.id,
        title: item.title,
        position: item.position,
        type: item.working_form
      };
    });
  });

  watch(searchProduct.isLoading, () => {
    loadingStatus.value = searchProduct.isLoading.value;
  });
};
//Search Products
watch(searchText, () => {
  if (debounceTimer.value) {
    clearTimeout(debounceTimer.value);
  }

  // Create a new timeout to call API after 1 second
  debounceTimer.value = setTimeout(() => {
    searchProduct();
  }, 500);
});

// Computed property for displaying filtered products
const filteredRecruitments = computed(() => {
  return results.value;
});

//Pagination Handle
const scrollToTop = () => {
  if (scrollContainer.value && scrollContainer.value instanceof HTMLElement) {
    scrollContainer.value.scrollTop = 0;
  }
};

//Change Page
const handlePageChange = (page: number) => {
  currentPage.value = page - 1;
  scrollToTop();
};

//Limit Character
const truncateText = (text: string, maxLength: number) => {
  if (text.length > maxLength) {
    return text.slice(0, maxLength) + '...';
  }
  return text;
};

watch(currentPage, () => {
  const { response, isLoading } = useAxios<DataResponse>(
    'get',
    `/recruitment/?page=${currentPage.value}&pageSize=10`,
    {},
    {},
    deps.value
  );

  //Convert array to compatible with data
  watch(response, () => {
    tempArrays.value = response.value?.data?.data;
    results.value = tempArrays.value.map((item: RecruitmentItem) => {
      return {
        id: item.id,
        title: item.title,
        position: item.position,
        type: item.working_form
      };
    });
  });

  watch(isLoading, () => {
    loadingStatus.value = isLoading.value;
  });
});

watch(isLoading, () => {
  loadingStatus.value = isLoading.value;
});

//Delete Product
const deleteRecruitment = (id: string) => {
  Swal.fire({
    title: 'Bạn có chắc muốn xóa?',
    text: 'Dữ liệu sẽ không thể khôi phục sau khi xóa!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Xóa',
    cancelButtonText: 'Hủy'
  }).then((result) => {
    if (result.isConfirmed) {
      const deleteProduct = useAxios<DataResponse>(
        'delete',
        `/recruitment/${id}`,
        {},
        {},
        deps.value
      );

      watch(deleteProduct.response, () => {
        if (deleteProduct.response.value?.status === 'ok') {
          Swal.fire({
            title: 'Xóa thành công',
            icon: 'success',
            confirmButtonText: 'Hoàn tất',
            width: '30rem'
          }).then((result) => {
            if (result.isConfirmed) {
              Swal.close();
              const { response } = useAxios<DataResponse>(
                'get',
                `/recruitment/?page=${currentPage.value}&pageSize=10`,
                {},
                {},
                deps.value
              );

              //Convert array to compatible with data
              watch(response, () => {
                tempArrays.value = response.value?.data?.data;
                results.value = tempArrays.value.map((item: RecruitmentItem) => {
                  return {
                    id: item.id,
                    title: item.title,
                    position: item.position,
                    type: item.working_form
                  };
                });
              });
            }
          });
        }
      });
    }
  });
};
</script>
<template>
  <div>
    <div :class="$style.mn_activityTable">
      <div :class="$style.mn_activity_control">
        <div :class="$style['mn_activity_control-input1']">
          <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['mn_activity_control-ic2']" />
          <input v-model="searchText" placeholder="Tìm kiếm" />
        </div>
      </div>
      <div :class="$style.mn_activity_body">
        <table :class="$style.mn_activity_table1">
          <thead>
            <tr>
              <th style="width: 5%">STT</th>
              <th style="width: 35%">Tên bài tuyển dụng</th>
              <th style="width: 25%">Vị trí</th>
              <th style="width: 20%">Thời gian làm</th>
              <th style="width: 15%">Chỉnh sửa</th>
            </tr>
          </thead>
        </table>
        <div :class="$style.mn_activity_table_ctn">
          <loading v-if="loadingStatus" />
          <table :class="$style.mn_activity_table">
            <tbody>
              <template v-if="filteredRecruitments.length > 0">
                <tr v-for="(item, index) in filteredRecruitments" :key="index">
                  <td style="width: 5%">{{ currentPage * 10 + index + 1 }}</td>
                  <td style="width: 35%">{{ truncateText(item.title, 45) }}</td>
                  <td style="width: 25%">{{ truncateText(item.position, 35) }}</td>
                  <td style="width: 20%">{{ truncateText(item.type, 30) }}</td>
                  <td style="width: 15%">
                    <button :class="$style['btn-room-trash']" @click="deleteRecruitment(item.id)">
                      <font-awesome-icon :icon="faTrash" />
                    </button>
                    <button @click="updateRecruitment(item.id)" :class="$style['edit-room-btn']">
                      <font-awesome-icon :icon="faPen" />
                    </button>
                  </td>
                </tr>
              </template>
              <template v-else>
                <tr>
                  <td colspan="5">DANH SÁCH TRỐNG</td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
      </div>
      <div :class="$style['mn_activity_pagination']">
        <pagination
          :total="totalPage"
          :current-page="currentPage"
          :page-size="10"
          @current-change="handlePageChange"
        />
      </div>
    </div>
    <div :class="$style.activity_overlay" v-if="isOpen1">
      <modal-update
        v-if="isOpen1"
        @close="isOpen1 = false"
        :update-object="chosenObject ? chosenObject : null"
        @update-content="onUpdateContent2"
      />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './MNRecruitment.mudole.scss';
</style>

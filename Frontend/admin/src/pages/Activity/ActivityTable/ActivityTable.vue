<script setup lang="ts">
import { ref, computed, watch, watchEffect, type Ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import { AxiosError } from 'axios';
import { format } from 'date-fns';
// import ModalAdd from './components/ModalAdd.vue';
import Pagination from '@/components/Pagination/BasePagination.vue';
import UpdateActivity from './ModalActivity/UpdateActivity.vue';
// import { activities } from '../Activity';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const searchText = ref('');

const currentPage = ref(0);
const pageSize = ref(10);
const isModalOpen = ref(false);
// const isLoading: Ref<boolean> = ref(false);
// const response: Ref<DataResponse | null> = ref(null);
// const error = ref<AxiosError | null>(null);
const activeTab = ref('activity');

interface Item {
  news: {
    id: string;
    title: string;
    img: string;
    slug: string;
    summary: string;
    detail: string;
    detailMobile: string;
    highlight: number;
    createAt: string;
  };
  tags: [
    {
      id: string;
      name: string;
      slug: string;
      createAt: string;
    }
  ];
}

const api = ref(`/news?pageSize=${pageSize.value}&page=${currentPage.value}`);
const deps = ref([]);
const { response, error, isLoading } = useAxios<DataResponse>('get', api.value, {}, {}, deps.value);

watch(
  currentPage,
  () => {
    console.log(12345);
    const {
      response: responseChanged,
      error,
      isLoading
    } = useAxios<DataResponse>(
      'get',
      `/news?pageSize=${pageSize.value}&page=${currentPage.value}`,
      {},
      {},
      deps.value
    );
    watch(responseChanged, () => {
      response.value = responseChanged.value;
    });
  },
  { immediate: false }
);

// watch(res, () => {
//   response.value = res.value;
// });

const activitiesResList = ref();
const activitiesRes = ref([
  {
    id: '',
    title: '',
    img: '',
    slug: '',
    summary: '',
    detail: '',
    detailMobile: '',
    highlight: 1,
    createAt: ''
  }
]);
const uniqueTags = ref([
  {
    id: '',
    name: '',
    slug: '',
    createAt: ''
  }
]);

const closeModal = () => {
  isModalOpen.value = false;
};

const selectedActivity = ref<Record<string, never> | Item>({});

// Trong phần code xử lý sự kiện
const editActivity = (activity: Item) => {
  selectedActivity.value = activity;
  isModalOpen.value = true; // Mở modal
  console.log(activity);
};

const filteredActivitiess = computed(() => {
  if (searchText.value.trim() === '') {
    return response.value?.data?.data;
  } else {
    // const searchTerm = searchText.value.toLowerCase();
    return response.value?.data?.data;
  }
});

//Pagination Handle
const scrollToTop = (top: number) => {
  window.scrollTo({
    top: top,
    behavior: 'smooth'
  });
};

const handlePageChange = (page: number) => {
  currentPage.value = page - 1;
  scrollToTop(0);
  console.log('Vinh');
};

const displayNews = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredActivitiess.value.slice(start, end);
});

const truncateText = (text: string, maxLength: number) => {
  if (text.length > maxLength) {
    return text.slice(0, maxLength) + '...';
  }
  return text;
};

const formatDateTime = (dateTimeString: string, outputFormat = 'dd/MM/yyyy HH:mm:ss'): string => {
  return format(new Date(dateTimeString), outputFormat);
};

const deleteActivity = async (id: string) => {
  Swal.fire({
    title: 'Bạn có chắc muốn xóa?',
    text: 'Dữ liệu sẽ không thể khôi phục sau khi xóa!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Xóa',
    cancelButtonText: 'Hủy'
  }).then(async (result) => {
    if (result.isConfirmed) {
      try {
        // Gọi API DELETE bằng cách sử dụng `useAxios`
        const { response, error, isLoading } = useAxios<DataResponse>(
          'delete',
          `/news/${id}`, // Đặt endpoint URL xóa phần tử cụ thể dựa vào id
          {},
          {},
          []
        );

        // Xóa phần tử trong activitiesResList
        activitiesResList.value = activitiesResList.value.filter(
          (item: { news: { id: string } }) => item.news.id !== id
        );
        console.log(activitiesResList.value);

        Swal.fire({
          title: 'Xóa thành công',
          icon: 'success',
          confirmButtonText: 'Hoàn tất',
          width: '30rem'
        });

        setTimeout(function () {
          Swal.close();
        }, 1200);
      } catch (error) {
        console.error('Error deleting item:', error);

        // Xử lý lỗi nếu cần thiết
        Swal.fire({
          title: 'Xóa không thành công',
          text: 'Có lỗi xảy ra khi xóa!',
          icon: 'error',
          width: '30rem'
        });
      }
    }
  });
};
watch(response, () => {
  activitiesResList.value = response?.value?.data?.data;
  currentPage.value;
  console.log(response.value?.data);
  // console.log(currentPage.value);
});

// watchEffect(() => {
//   console.log(currentPage.value);
//   console.log(activitiesResList.value);
// });
</script>
<template>
  <div>
    <div :class="$style.mn_activityTable">
      <div :class="$style.mn_activity_control">
        <span></span>
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
              <th style="width: 25%">Tên hoạt động</th>
              <th style="width: 30%">Tags</th>
              <th style="width: 25%">Thời gian diễn ra</th>
              <th style="width: 15%">Chỉnh sửa</th>
            </tr>
          </thead>
        </table>
        <div :class="$style.mn_activity_table_ctn">
          <table :class="$style.mn_activity_table">
            <tbody>
              <template v-if="filteredActivitiess?.length > 0">
                <tr v-for="(item, index) in activitiesResList" :key="index">
                  <td style="width: 5%">{{ index + 1 }}</td>
                  <td style="max-width: 25%">
                    {{ truncateText(item?.news?.title, 25) }}
                  </td>
                  <td style="width: 30%">
                    <span v-for="(tag, idx) in item?.tags" :key="idx">
                      {{ truncateText(tag.name, 40) }}
                      <span v-if="idx !== item.tags.length - 1">{{ ', ' }}</span>
                    </span>
                  </td>

                  <td style="width: 25%">{{ formatDateTime(item?.news?.createAt) }}</td>
                  <td style="width: 15%">
                    <button
                      :class="$style['btn-room-trash']"
                      @click="deleteActivity(item?.news?.id)"
                    >
                      <font-awesome-icon :icon="faTrash" />
                    </button>
                    <button @click="editActivity(item)" :class="$style['edit-room-btn']">
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
          :total="Math.ceil(filteredActivitiess?.length / pageSize)"
          :current-page="currentPage"
          :page-size="pageSize"
          @current-change="handlePageChange"
        />
      </div>
    </div>
    <div :class="$style.activity_overlay" v-if="isModalOpen">
      <update-activity
        :selectedActivity="selectedActivity"
        :isModalOpen="isModalOpen"
        :closeModal="closeModal"
        v-if="activeTab === 'activity'"
        @click.stop
        @close="closeModal"
      />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../Activity.module.scss';
</style>

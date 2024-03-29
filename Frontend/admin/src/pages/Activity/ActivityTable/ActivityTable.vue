<script setup lang="ts">
import { ref, watch, type PropType } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import UpdateActivity from './ModalActivity/UpdateActivity.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import styles from '../Activity.module.scss';
import Loading from '@/components/LoadingComponent/LoadingComponent.vue';

interface News {
  id: string;
  title: string;
  img: string;
  slug: string;
  summary: string;
  detail: string;
  detailMobile: string;
  highlight: number;
  createAt: string;
  tags: [
    {
      id: string;
      name: string;
      slug: string;
      createAt: string;
    }
  ];
}
interface Tags {
  id: string;
  name: string;
  slug: string;
  createAt: string;
}

interface MyErrorResponse {
  response?: {
    data?: {
      message?: string;
    };
  };
}

interface dataUpdate {
  createAt: string;
  detail: string;
  detailMobile: string;
  highlight: 0;
  id: string;
  img: string;
  slug: string;
  summary: string;
  title: string;
}

const props = defineProps({
  change: {
    type: Function,
    required: true
  },
  dataNews: {
    type: Array as PropType<News[]>,
    required: true
  },
  totalPage: {
    type: Number,
    required: false
  }
});

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', newsAdd: News): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'current-page', numberPage: number): void;
}>();

const searchText = ref('');
const currentPage = ref(0);
const isModalOpen = ref(false);
const activeTab = ref('activity');

const deps = ref([]);
const depsTag = ref([]);
const debounceTimer = ref<ReturnType<typeof setTimeout> | null>(null);
const resultListTags = ref<Array<Tags>>([]);
const isLoading = ref(false);

const activitiesResList = ref<News[]>(props.dataNews);
watch(props, () => {
  activitiesResList.value = props.dataNews;
  currentPage.value;
});

const {
  response: tagsList,
  error: tagError,
  isLoading: tagLoading
} = useAxios<DataResponse>('get', '/tags', {}, {}, depsTag.value);

watch(tagsList, () => {
  resultListTags.value = tagsList?.value?.data;
});

const closeModal = () => {
  isModalOpen.value = false;
};

const selectedActivity = ref<Record<string, never> | News>({});

// Trong phần code xử lý sự kiện
const editActivity = (activity: News) => {
  selectedActivity.value = activity;
  isModalOpen.value = true; // Mở modal
  // console.log(activity);
};

//Function call API Search
const searchNews = () => {
  const searchNews = useAxios<DataResponse>(
    'get',
    `/news?key=${searchText.value}`,
    {},
    {},
    deps.value
  );

  watch(searchNews.response, () => {
    activitiesResList.value = searchNews.response.value?.data?.data;
  });
};

const handleUpdateNews = (data: dataUpdate) => {
  activitiesResList.value.forEach((item) => {
    if (item.id === data.id) {
      item.title = data.title;
      item.img = data.img;
      item.slug = data.slug;
      item.summary = data.summary;
      item.detail = data.detail;
    }
  });
};

// watch(activitiesResList, () => {
//   console.log(activitiesResList.value);
// });

//Search Products
watch(searchText, () => {
  if (debounceTimer.value) {
    clearTimeout(debounceTimer.value);
  }

  // Create a new timeout to call API after 1 second
  debounceTimer.value = setTimeout(() => {
    searchNews();
  }, 400);
});

//Hàm giới hạn chiều dài khung
const truncateText = (text: string, maxLength: number) => {
  if (text && text.length > maxLength) {
    return text.slice(0, maxLength) + '...';
  }
  return text;
};

const formatDateTime = (dateTimeStr: string) => {
  const singaporeDateTime = new Date(dateTimeStr); // Chuyển chuỗi thành đối tượng Date
  const singaporeOffset = singaporeDateTime.getTimezoneOffset() / 60;

  // Chuyển đổi thời gian từ múi giờ Singapore sang múi giờ Việt Nam
  const vietnamDateTime = new Date(singaporeDateTime.getTime() + singaporeOffset * 3600000);

  const formattedDateTime = vietnamDateTime.toLocaleString('en-US', {
    timeZone: 'Asia/Ho_Chi_Minh',
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  });

  return formattedDateTime;
};

// Hàm lấy dữ liệu trả về khi update Highlight công ty
const handleUpdateHighLight = (index: number) => {
  const checkbox = document.getElementById(`myCheckbox${index}`);
  // console.log(props.dataNews[index].slug);

  if (checkbox instanceof HTMLInputElement) {
    // Gọi hàm useAxios để lấy response, error, và isLoading
    const { response } = useAxios<DataResponse>(
      'patch',
      '/news/highlight/' +
        props.dataNews[index].slug +
        '?' +
        'highlight=' +
        (checkbox.checked ? 1 : 0),
      {},
      {},
      deps.value
    );

    watch(response, () => {
      if (!isLoading.value) {
        if (response.value?.status === 'ok') {
          Swal.fire({
            title: 'Cập nhật sản phẩm nổi bậc thành công',
            icon: 'success',
            confirmButtonText: 'Hoàn tất',
            timer: 2000,
            width: '50rem',
            padding: '0 2rem 2rem 2rem',
            customClass: {
              confirmButton: styles['confirm-button'],
              cancelButton: styles['cancel-button'],
              title: styles['title']
            }
          });
        } else {
          Swal.fire({
            title: 'Cập nhật công ty nổi bậc thất bại',
            icon: 'success',
            confirmButtonText: 'Hoàn tất',
            timer: 2000,
            width: '50rem',
            padding: '0 2rem 2rem 2rem',
            customClass: {
              confirmButton: styles['confirm-button'],
              cancelButton: styles['cancel-button'],
              title: styles['title']
            }
          });
        }
      }
    });
  }
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
        const deleteNews = useAxios<DataResponse>(
          'delete',
          `/news/${id}`, // Đặt endpoint URL xóa phần tử cụ thể dựa vào id
          {},
          {},
          deps.value
        );
        // console.log(id);

        watch(deleteNews.isLoading, () => {
          isLoading.value = deleteNews.isLoading.value;
        });

        watch(deleteNews.response, () => {
          // console.log(deleteNews.response.value);

          if (deleteNews.response.value?.status === 'ok') {
            // Xóa phần tử trong activitiesResList
            if (activitiesResList.value) {
              activitiesResList.value = activitiesResList.value.filter(
                (item: { id: string }) => item.id !== id
              );
            }
            props.change(id);

            Swal.fire({
              title: 'Xóa thành công',
              icon: 'success',
              confirmButtonText: 'Hoàn tất',
              width: '30rem'
            }).then((result) => {
              if (result.isConfirmed) {
                setTimeout(function () {
                  Swal.close();
                }, 1200);
              }
            });
          }
        });

        watch(deleteNews.error, () => {
          const errorValue: MyErrorResponse | null = deleteNews.error
            .value as MyErrorResponse | null;
          if (errorValue !== null) {
            Swal.fire({
              title: 'Xóa không thành công!',
              icon: 'error',
              confirmButtonText: 'Đóng',
              width: '50rem',
              padding: '0 2rem 2rem 2rem'
            });
          }
        });
      } catch (error) {
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
              <th style="width: 28%">Tên hoạt động</th>
              <th style="width: 30%">Tags</th>
              <th style="width: 18%">Thời gian diễn ra</th>
              <th style="width: 9%">Nổi bật</th>
              <th style="width: 10%">Chỉnh sửa</th>
            </tr>
          </thead>
        </table>
        <div v-if="!isLoading" :class="$style.mn_activity_table_ctn">
          <table :class="$style.mn_activity_table">
            <tbody>
              <template v-if="activitiesResList?.length > 0">
                <tr v-for="(item, index) in activitiesResList" :key="index">
                  <td style="width: 5%">{{ index + 1 }}</td>
                  <td style="max-width: 28%">
                    {{ truncateText(item?.title, 50) }}
                  </td>
                  <td style="width: 30%">
                    <span v-for="(tag, idx) in item?.tags" :key="idx">
                      {{ truncateText(tag.name, 40) }}
                      <span style="width: 30%" v-if="idx !== item.tags.length - 1">{{ ', ' }}</span>
                    </span>
                  </td>

                  <td style="width: 18%">{{ formatDateTime(item?.createAt) }}</td>
                  <td style="width: 9%">
                    <input
                      :title="
                        item.highlight === 0 ? 'Hoạt động không nổi bật' : 'Hoạt động nổi bật'
                      "
                      :id="`myCheckbox${index}`"
                      @change="handleUpdateHighLight(index)"
                      type="checkbox"
                      :checked="item.highlight !== 0 ? true : false"
                    />
                  </td>
                  <td style="width: 10%">
                    <button :class="$style['btn-room-trash']" @click="deleteActivity(item.id)">
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
        <Loading v-else style="height: calc(100vh - 200px)" />
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
        @update="handleUpdateNews"
      />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../Activity.module.scss';
</style>

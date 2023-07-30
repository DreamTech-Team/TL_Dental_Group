<script setup lang="ts">
import { ref, computed, watchEffect, watch } from 'vue';
import { format } from 'date-fns';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
// import ModalAdd from './components/ModalAdd.vue';
import UpdateTag from './ModalTag/UpdateTag.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { flattedChildren } from 'element-plus/es/utils';

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

const searchKeyword = ref('');
const isModalOpen = ref(false);
const activeTab = ref('tags');
// const props = defineProps(['listTags', 'handleTagDeleted']);
const props = defineProps<{
  listTags: Tags[];
  handleTagDeleted: (id: string) => void;
}>();
const deps = ref([]);
const resultTags = ref(props.listTags);

const selectedTag = ref<Record<string, never> | Tags>({});

// Trong phần code xử lý sự kiện
const editTag = (tags: Tags) => {
  selectedTag.value = tags;
  isModalOpen.value = true; // Mở modal
  console.log(tags);
};

const closeModal = () => {
  isModalOpen.value = false;
};

const truncateText = (text: string, maxLength: number) => {
  if (text.length > maxLength) {
    return text.slice(0, maxLength) + '...';
  }
  return text;
};

const formatDateTime = (dateTimeString: string, outputFormat = 'dd/MM/yyyy HH:mm:ss'): string => {
  return format(new Date(dateTimeString), outputFormat);
};

const handleChangeAdd = (dataAdded: Tags) => {
  console.log(resultTags);

  // resultTags.value.unshift(dataAdded);
  resultTags.value.forEach((item) => {
    if (item.id === dataAdded.id) {
      item.name = dataAdded.name;
    }
  });
};

watch(props.listTags, () => {
  resultTags.value = props.listTags;
});

const filteredTags = computed(() => {
  // Lọc danh sách Tags dựa trên từ khoá tìm kiếm
  return props.listTags.filter((tag) =>
    tag.name.toLowerCase().includes(searchKeyword.value.toLowerCase())
  );
});

const deleteTag = async (id: string) => {
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
        const deleteTagRes = useAxios<DataResponse>(
          'delete',
          `/tags/${id}`, // Đặt endpoint URL xóa phần tử cụ thể dựa vào id
          {},
          {},
          deps.value
        );

        watch(deleteTagRes.response, () => {
          console.log(deleteTagRes.response.value);

          if (deleteTagRes.response.value?.status === 'ok') {
            props.handleTagDeleted(id);
            console.log('vinh' + props.listTags);
            Swal.fire({
              title: 'Thêm thành công',
              icon: 'success',
              confirmButtonText: 'Hoàn tất',
              width: '30rem'
            }).then((result) => {
              if (result.isConfirmed) {
                Swal.close();
              }
            });
          }
        });

        watch(deleteTagRes.error, () => {
          const errorValue: MyErrorResponse | null = deleteTagRes.error
            .value as MyErrorResponse | null;
          if (errorValue !== null) {
            if (errorValue?.response?.data?.message === "Tag's name already taken") {
              Swal.fire({
                title: 'Tag đã tồn tại',
                icon: 'error',
                confirmButtonText: 'Đóng',
                width: '50rem',
                padding: '0 2rem 2rem 2rem'
              });
              return;
            }
          }
        });
      } catch (error) {
        console.error('Error deleting item:', error);
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

// watchEffect(() => {
//   console.log('kcsdkfsdkfksdhfkj' + resultTags.value);
// });
</script>
<template>
  <div>
    <div>
      <div :class="$style.mn_activity_control">
        <span></span>
        <div :class="$style['mn_activity_control-input1']">
          <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['mn_activity_control-ic2']" />
          <input v-model="searchKeyword" placeholder="Tìm kiếm" />
        </div>
      </div>
      <div :class="$style.mn_activity_body">
        <table :class="$style.mn_activity_table1">
          <thead>
            <tr>
              <th style="width: 5%">STT</th>
              <th style="width: 25%">Tên tag</th>
              <th style="width: 30%">Ngày tạo tag</th>
              <th style="width: 16%">Chỉnh sửa</th>
            </tr>
          </thead>
        </table>

        <div :class="$style.mn_activity_table_ctn">
          <table :class="$style.mn_activity_table">
            <tbody>
              <template v-if="filteredTags?.length > 0">
                <tr v-for="(item, index) in filteredTags" :key="index">
                  <td style="width: 5%">{{ index + 1 }}</td>
                  <td style="width: 25%">
                    {{ truncateText(item.name, 40) }}
                  </td>
                  <td style="width: 30%">{{ formatDateTime(item.createAt) }}</td>
                  <td style="width: 15%">
                    <button :class="$style['btn-room-trash']" @click="deleteTag(item.id)">
                      <font-awesome-icon :icon="faTrash" />
                    </button>
                    <button @click="editTag(item)" :class="$style['edit-room-btn']">
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
    </div>

    <div :class="$style.activity_overlay" v-if="isModalOpen">
      <update-tag
        :selectedTag="selectedTag"
        :closeModal="closeModal"
        v-if="activeTab === 'tags'"
        @click.stop
        @close="closeModal"
        :change="handleChangeAdd"
      />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../Activity.module.scss';
</style>

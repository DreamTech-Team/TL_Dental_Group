<script setup lang="ts">
import { ref, watch, type PropType } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import { tags } from '../../Activity';
import Swal from 'sweetalert2';
import styles from './ModalTag.module.scss';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const tagExist = ref(true);
const nameTagInput = ref('');
const _MAX_WORD_TITLE = 70;
const countWordTitle = ref(_MAX_WORD_TITLE);
const deps = ref([]);

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

export interface CloseModalFn {
  (...payload: any[]): void;
}
const props = defineProps({
  selectedTag: {
    type: Object, // Kiểu dữ liệu của prop
    required: true // Bắt buộc phải truyền giá trị cho prop
  },
  closeModal: {
    type: Function as PropType<CloseModalFn>,
    required: true
  },
  change: {
    type: Function as PropType<(newData: Tags) => void>,
    required: true
  }
});

const dataAdded = ref<Tags>({
  id: '',
  name: '',
  slug: '',
  createAt: ''
});
const tagName = ref(props.selectedTag.name);

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update', data: { dataTagAdded: Tags }): void;
}>();

// Các hàm update dữ liệu cho thẻ input
const updateTagName = (e: Event) => {
  const target = e.target as HTMLInputElement;
  countWordTitle.value = _MAX_WORD_TITLE - target.value.length;
  nameTagInput.value = target.value;
};

// Hàm submit dữ liệu, đẩy dữ liệu lên database
const submitForm = () => {
  if (nameTagInput.value.length < 1) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '50rem',
      padding: '0 2rem 2rem 2rem',
      customClass: {
        confirmButton: styles['confirm-button'],
        cancelButton: styles['cancel-button'],
        title: styles['title']
      }
    });
  } else {
    const object = {
      name: nameTagInput.value
    };

    const updateTag = useAxios<DataResponse>(
      'patch',
      `/tags/${props.selectedTag?.id}?name=${nameTagInput.value}`,
      {},
      {},
      deps.value
    );

    watch(updateTag.response, () => {
      if (updateTag.response.value?.status === 'ok') {
        Swal.fire({
          title: 'Cập nhật thành công',
          icon: 'success',
          confirmButtonText: 'Hoàn tất',
          width: '30rem'
        }).then((result) => {
          if (result.isConfirmed) {
            Swal.close();
            dataAdded.value = {
              id: updateTag.response.value?.data?.id,
              name: updateTag.response.value?.data?.name,
              slug: updateTag.response.value?.data?.slug,
              createAt: updateTag.response.value?.data?.createAt
            };
            props.change(dataAdded.value);
            emits('update', {
              dataTagAdded: updateTag.response.value?.data
            });
            emits('close');
          }
        });
      }
    });

    watch(updateTag.error, () => {
      const errorValue: MyErrorResponse | null = updateTag.error.value as MyErrorResponse | null;
      if (errorValue !== null) {
        if (errorValue?.response?.data?.message === "Tag's name already taken") {
          Swal.fire({
            title: 'Tag đã tồn tại',
            icon: 'error',
            confirmButtonText: 'Đóng',
            width: '50rem',
            padding: '0 2rem 2rem 2rem',
            customClass: {
              confirmButton: styles['confirm-button'],
              cancelButton: styles['cancel-button'],
              title: styles['title']
            }
          });
          return;
        }
      }
    });
  }
};
</script>
<template>
  <div :class="$style.tag_container">
    <div>
      <div :class="$style['tag_container--title']">
        <p>CHỈNH SỬA HOẠT ĐỘNG</p>
        <font-awesome-icon @click="closeModal" :icon="faXmark" :class="$style.tag_cancel" />
      </div>
      <div :class="$style.wrap_title">
        <label :class="$style['wrap_title--text']" for="activity-title">Tên tag</label>
        <input
          id="activity-title"
          :value="tagName"
          @input="updateTagName"
          :maxlength="_MAX_WORD_TITLE"
          placeholder="Nhập tên tag"
          :class="$style['custom-input']"
        />
      </div>
    </div>
    <div :class="$style['tag_container--btn']">
      <button @click="$emit('close')" :class="$style.button_destroy">Hủy</button>
      <button @click="submitForm" :class="$style.button">Cập nhật</button>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalTag.module.scss';
</style>

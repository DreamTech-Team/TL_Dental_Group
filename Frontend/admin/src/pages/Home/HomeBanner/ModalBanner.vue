<script setup lang="ts">
import { ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const content = defineProps({
  uuid: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  context: {
    type: String,
    required: true
  }
});

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', data: { title: string; context: string }): void;
}>();

//Validate form
const titleInput = ref(content.title);
const updateTitle = (e: Event) => {
  const target = e.target as HTMLInputElement;
  titleInput.value = target.value;
};

const textareaInput = ref(content.context);
const updateContext = (e: Event) => {
  const target = e.target as HTMLInputElement;
  textareaInput.value = target.value;
};

const submitForm = () => {
  if (textareaInput.value.length < 15 || titleInput.value.length < 4) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '30rem'
    });
  } else if (textareaInput.value.length > 100 || titleInput.value.length > 25) {
    Swal.fire({
      title: 'Thông tin quá dài',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '30rem'
    });
  } else {
    const deps = ref([]);
    const object = {
      id: content.uuid,
      title: titleInput.value,
      content: textareaInput.value
    };
    const { response } = useAxios<DataResponse>('patch', '/home/header', object, {}, deps.value);

    watch(response, () => {
      if (response.value?.status === 'ok') {
        Swal.fire({
          title: 'Cập nhật thành công',
          icon: 'success',
          confirmButtonText: 'Hoàn tất',
          width: '30rem'
        }).then((result) => {
          if (result.isConfirmed) {
            Swal.close();
            emits('update-content', {
              title: titleInput.value,
              context: textareaInput.value
            });
            emits('close');
          }
        });
      }
    });
  }
};
</script>

<template>
  <div :class="$style.banner__overlay">
    <div :class="$style.banner__modal">
      <div :class="$style.banner__modal__heading">
        CẬP NHẬT NỘI DUNG
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['banner__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.banner__modal__body">
        <h4>Tiêu đề chính</h4>
        <input type="text" placeholder="Nhập tiêu đề" :value="titleInput" @input="updateTitle" />
        <h4>Nội dung</h4>
        <textarea placeholder="Nhập mô tả" :value="textareaInput" @input="updateContext" />
        <div :class="$style['modal__buttons']">
          <button @click="$emit('close')">Hủy</button>
          <button @click="submitForm">Cập nhật</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalBanner.module.scss';
</style>

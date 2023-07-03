<script setup lang="ts">
import { ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';

const content = defineProps({
  title: {
    type: String,
    required: true
  },
  context: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['inFocus', 'close']);

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
  } else {
    Swal.fire({
      title: 'Cập nhật thành công',
      icon: 'success',
      confirmButtonText: 'Hoàn tất',
      width: '30rem'
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.close();
        emit('close');
      }
    });
  }
};
</script>

<template>
  <div :class="$style.banner__overlay">
    <div :class="$style.banner__modal">
      <div :class="$style.banner__modal__heading">
        CẬP NHẬT PHƯƠNG CHÂM
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
          <button>Hủy</button>
          <button @click="submitForm">Cập nhật</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalBanner.module.scss';
</style>

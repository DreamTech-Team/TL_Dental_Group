<script setup lang="ts">
import { ref } from 'vue';
import Swal from 'sweetalert2';
import styles from './CategoryModalAdd.module.scss';

const emit = defineEmits(['isFocus', 'close']);

const handleModalCancel = () => {
  Swal.fire({
    title: 'Nội dung của bạn chưa được lưu!',
    icon: 'warning',
    showDenyButton: true,
    confirmButtonText: 'Trở lại',
    denyButtonText: 'Thoát',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    if (result.isDenied) {
      emit('close');
    }
  });
};

const handleModalUpdate = () => {
  Swal.fire({
    title: 'Bạn có chắc chắn muốn cập nhật nội dung không?',
    icon: 'question',
    showDenyButton: true,
    confirmButtonText: 'Cập nhật',
    denyButtonText: 'Hủy',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      //   props.handleUpdated(edited.value);
      emit('close');
      Swal.fire({
        title: 'Cập nhật nội dung thành công!',
        icon: 'success',
        customClass: {
          popup: styles['container-popup'],
          confirmButton: styles['confirm-button'],
          denyButton: styles['deny-button']
        }
      });
    } else if (result.isDenied) {
      Swal.fire({
        title: 'Nội dung không được cập nhật',
        icon: 'error',
        customClass: {
          popup: styles['container-popup'],
          confirmButton: styles['confirm-button'],
          denyButton: styles['deny-button']
        }
      });
    }
  });
};
</script>
<template>
  <div :class="$style.container">
    <div :class="$style.container__modal">
      <div :class="$style['container__modal-heading']">
        <div class=""></div>
        <div :class="$style['container__modal-heading-title']">Cập nhật nội dung</div>
        <div :class="$style['container__modal-heading-exit']" @click="handleModalCancel">
          <svg viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" fill="none">
            <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
            <g id="SVGRepo_iconCarrier">
              <path
                fill="#000000"
                fill-rule="evenodd"
                d="M9 17a1 1 0 102 0v-6h6a1 1 0 100-2h-6V3a1 1 0 10-2 0v6H3a1 1 0 000 2h6v6z"
              ></path>
            </g>
          </svg>
        </div>
      </div>
      <div :class="$style['container__modal-update']">
        <div class=""></div>
        <div :class="$style['container__modal-update-btn']">
          <div
            :class="[
              $style['container__modal-update-btn-submit'],
              $style['container__modal-update-btn-cancel']
            ]"
            @click="handleModalCancel"
          >
            Hủy
          </div>
          <div
            :class="[
              $style['container__modal-update-btn-submit'],
              $style['container__modal-update-btn-update']
            ]"
            @click="handleModalUpdate"
          >
            Cập nhật
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './CategoryModalAdd.module.scss';
</style>

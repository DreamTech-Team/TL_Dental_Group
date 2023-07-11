<script setup lang="ts">
import { ref } from 'vue';
import Swal from 'sweetalert2';
import Camera from '@/assets/icons/Camera.png';

const context = defineProps({
  image: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['inFocus', 'close']);
const selectedImage = ref(context.image);
const fileInput = ref<HTMLInputElement | null>(null);

//Open file image
const openFileInput = () => {
  fileInput.value?.click();
};

//Choose image
const handleFileInputChange = (event: Event) => {
  const inputElement = event.target as HTMLInputElement;
  const file = inputElement.files?.[0];

  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      selectedImage.value = reader.result as string;
    };
    reader.readAsDataURL(file);
  }
};

const resetImg = () => {
  selectedImage.value = context.image;
  emit('close');
};

const submitForm = () => {
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
};
</script>

<template>
  <div :class="$style.logo__overlay">
    <div :class="$style.logo__modal">
      <div :class="$style.logo__modal__heading">
        <h4>Logo</h4>
      </div>
      <div :class="$style.logo__modal__body">
        <div :class="$style.logo__ctn">
          <img :src="selectedImage" alt="meeting" />
          <input
            type="file"
            ref="fileInput"
            style="display: none"
            @change="handleFileInputChange"
          />
          <div @click="openFileInput" :class="$style.logo_btn_img">
            <img :src="Camera" />
          </div>
        </div>
      </div>
      <div :class="$style['modal__buttons']">
        <button @click="resetImg">Hủy</button>
        <button @click="submitForm">Cập nhật</button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalLogo.module.scss';
</style>

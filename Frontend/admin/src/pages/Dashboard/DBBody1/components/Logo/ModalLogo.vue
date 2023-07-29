<script setup lang="ts">
import { ref, watch } from 'vue';
import Swal from 'sweetalert2';
import Camera from '@/assets/icons/Camera.png';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface MyFile extends File {
  name: string;
}

const context = defineProps({
  image: {
    type: String,
    required: true
  }
});

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', data: { image: string }): void;
}>();
const selectedImage = ref(context.image);
const fileInput = ref<HTMLInputElement | null>(null);
const file = ref<MyFile | null>(null);

//Open file image
const openFileInput = () => {
  fileInput.value?.click();
};

//Choose image
const handleFileInputChange = (event: Event) => {
  const inputElement = event.target as HTMLInputElement;
  file.value = inputElement.files?.[0] || null;

  if (file.value) {
    const reader = new FileReader();
    reader.onload = () => {
      selectedImage.value = reader.result as string;
    };
    reader.readAsDataURL(file.value);
  }
};

const resetImg = () => {
  emits('close');
};

const submitForm = () => {
  const deps = ref([]);
  const formData = new FormData();
  formData.append('logo', file.value as Blob);
  const { response } = useAxios<DataResponse>(
    'patch',
    '/information/logo',
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    },
    deps.value
  );

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
            image: selectedImage.value || ''
          });
          emits('close');
        }
      });
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

<script setup lang="ts">
import { Ref, ref, onMounted, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faRotate } from '@fortawesome/free-solid-svg-icons';
import CropImage from '@/components/CropImage/CropImage.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface AboutHeader {
  id: string;
  title: string;
  image: string;
}

const variableChange = ref([]);
const renderImage = ref<AboutHeader>({
  id: '',
  title: '',
  image: ''
});
const imageFile: Ref<string | null> = ref(null);
const isCrop = ref(false);
const isOpenInput = ref(false);

// Gọi hàm useAxios để lấy response, error, và isLoading
const { response, error, isLoading } = useAxios<DataResponse>(
  'get',
  '/introduce/header',
  {},
  {},
  variableChange.value
);

// Truy xuất giá trị response.value và gán vào responseData
watch(response, () => {
  imageFile.value = response?.value?.data?.image;
  renderImage.value = response?.value?.data;
});

// convert Base64 sang File
const base64ToBlob = (base64Data: string) => {
  const byteString = atob(base64Data.split(',')[1]);
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }
  return new Blob([ab], { type: 'image/png' });
};

const handleCroppedImage = (result: string) => {
  if (result) {
    imageFile.value = result;

    // Tạo một đối tượng File từ dữ liệu base64
    const fileData = base64ToBlob(result);
    const image = new File([fileData], 'image.png', { type: 'image/png' });

    const deps = ref([]);

    const object = {
      id: renderImage.value.id,
      title: renderImage.value.title,
      image: renderImage.value.image
    };

    const formData = new FormData();
    formData.append('data', JSON.stringify(object));
    formData.append('image', image as Blob);
    const { response } = useAxios<DataResponse>(
      'patch',
      '/introduce/header',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      },
      deps.value
    );
  }
};
</script>
<template>
  <div :class="$style.about__header">
    <div :class="$style['about__header-introduce']">
      <div v-if="imageFile" :class="$style['about__header-introduce-img']">
        <img :src="imageFile" alt="" />
      </div>

      <div :class="$style['about__header-introduce-bg']">
        <button
          :class="$style['about__header-introduce-bg-button']"
          @click="isOpenInput = !isOpenInput"
        >
          <font-awesome-icon
            :icon="faRotate"
            :class="$style['about__header-introduce-bg-button-ic']"
          />
          <span>Đổi hình ảnh</span>
        </button>
      </div>

      <p>Giới Thiệu</p>
    </div>
  </div>

  <crop-image
    :heightCrop="468"
    :widthCrop="1440"
    :heightWrap="468"
    :widthWrap="1440"
    :check="isOpenInput"
    v-show="isCrop"
    @close="isCrop = false"
    @open="isCrop = true"
    @crop="handleCroppedImage"
  />
</template>

<style module scoped lang="scss">
@import './AboutHeader.module.scss';
</style>

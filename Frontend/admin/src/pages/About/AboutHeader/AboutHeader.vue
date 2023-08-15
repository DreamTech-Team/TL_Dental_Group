<script setup lang="ts">
import { Ref, ref, onMounted, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faRotate } from '@fortawesome/free-solid-svg-icons';
import CropImage from '@/components/CropImage/CropImage.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import base64ToBlob from '@/utils/base64ToBlob';
import Swal from 'sweetalert2';
import styles from './AboutHeader.module.scss';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

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
const isLoadingBanner = ref(false);

const { response } = useAxios<DataResponse>(
  'get',
  '/introduce/header',
  {},
  {},
  variableChange.value
);

watch(response, () => {
  imageFile.value = response?.value?.data?.image;
  renderImage.value = response?.value?.data;
});

const handleCroppedImage = (result: string) => {
  if (result) {
    imageFile.value = result;

    // Tạo một đối tượng File từ dữ liệu base64
    const fileData = base64ToBlob.covertBase64ToBlob(result);
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
    const { response, isLoading } = useAxios<DataResponse>(
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
    isLoadingBanner.value = isLoading.value;
    watch(response, () => {
      if (response.value?.status === 'ok') {
        isLoadingBanner.value = isLoading.value;

        Swal.fire({
          title: 'Cập nhật thành công',
          icon: 'success',
          confirmButtonText: 'Hoàn tất',
          width: '50rem',
          padding: '0 2rem 2rem 2rem',
          timer: 2000,
          customClass: {
            confirmButton: styles['confirm-button'],
            cancelButton: styles['cancel-button'],
            title: styles['title']
          }
        }).then((result) => {
          if (result.isConfirmed) {
            Swal.close();
          }
        });
      } else {
        isLoadingBanner.value = isLoading.value;

        Swal.fire({
          title: 'Cập nhật thất bại',
          icon: 'success',
          confirmButtonText: 'Hoàn tất',
          width: '50rem',
          padding: '0 2rem 2rem 2rem',
          customClass: {
            confirmButton: styles['confirm-button'],
            cancelButton: styles['cancel-button'],
            title: styles['title']
          }
        }).then((result) => {
          if (result.isConfirmed) {
            Swal.close();
          }
        });
      }
    });
  }
};
</script>
<template>
  <div :class="$style.about__header">
    <div :class="$style['about__header-introduce']" v-if="!isLoadingBanner">
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
    <div v-else style="width: 100%; height: 100%">
      <loading-component />
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

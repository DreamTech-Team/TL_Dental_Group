<script setup lang="ts">
import { type Ref, ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCloudArrowUp, faUpload } from '@fortawesome/free-solid-svg-icons';
import CropImage from '@/components/CropImage/CropImage.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import base64ToBlob from '@/utils/base64ToBlob';
import Swal from 'sweetalert2';
import styles from './UpdateBanner.module.scss';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface UpdateBanner {
  id: string;
  title: string;
  content: string;
  image: string;
  slug: string;
  type: string;
}

const variableChange = ref([]);
const dataHeader = ref<UpdateBanner>({
  id: '',
  title: '',
  content: '',
  image: '',
  slug: '',
  type: ''
});
const dataHeaderPolicy = ref<UpdateBanner>({
  id: '',
  title: '',
  content: '',
  image: '',
  slug: '',
  type: ''
});
const dataHeaderNews = ref<UpdateBanner>({
  id: '',
  title: '',
  content: '',
  image: '',
  slug: '',
  type: ''
});
const imageFile: Ref<string | null> = ref(null);
const isCrop = ref(false);
const isOpenInput = ref(false);
const isUpdate = ref(false);
const isLoadingPolicy = ref(false);
const isLoadingNews = ref(false);
const isTab = ref('Policy');

const getImagePolicy = useAxios<DataResponse>(
  'get',
  '/policy/header',
  {},
  {},
  variableChange.value
);

const getImageNews = useAxios<DataResponse>('get', 'news/header', {}, {}, variableChange.value);

watch(getImagePolicy.isLoading, () => {
  isLoadingPolicy.value = getImagePolicy.isLoading.value;
});

watch(getImageNews.isLoading, () => {
  isLoadingNews.value = getImageNews.isLoading.value;
});

watch(getImagePolicy.response, () => {
  dataHeaderPolicy.value = getImagePolicy.response.value?.data;
  imageFile.value = getImagePolicy.response.value?.data?.image;
  dataHeader.value = dataHeaderPolicy.value;
});
watch(getImageNews.response, () => {
  dataHeaderNews.value = getImageNews.response.value?.data;
});

// Xử lí chuyển tab
const handleTab = (e: Event) => {
  const target = e.target as HTMLInputElement;

  if (target) {
    isTab.value = target.innerText;

    if (isTab.value === 'Policy') {
      isUpdate.value = false;

      imageFile.value = dataHeaderPolicy.value.image;
      dataHeader.value = dataHeaderPolicy.value;
    } else {
      isUpdate.value = false;

      imageFile.value = dataHeaderNews.value.image;
      dataHeader.value = dataHeaderNews.value;
    }
  }
};

// Lấy dữ liệu về khi Image đã được Crop
const handleCroppedImage = (result: string) => {
  if (result) {
    isUpdate.value = true;
    imageFile.value = result;
  }
};

// Xử lí upload ảnh lên dtb
const handleUpload = () => {
  if (imageFile.value) {
    // Tạo một đối tượng File từ dữ liệu base64
    const fileData = base64ToBlob.covertBase64ToBlob(imageFile.value);
    const image = new File([fileData], 'image.png', { type: 'image/png' });
    const deps = ref([]);
    const object = {
      id: dataHeader.value.id,
      title: dataHeader.value.title,
      content: dataHeader.value.content,
      image: dataHeader.value.image,
      slug: dataHeader.value.slug,
      type: dataHeader.value.type
    };

    const formData = new FormData();
    formData.append('data', JSON.stringify(object));
    formData.append('image', image as Blob);

    if (isTab.value === 'Policy') {
      const { response, isLoading } = useAxios<DataResponse>(
        'patch',
        '/policy/header',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        },
        deps.value
      );
      watch(isLoading, () => {
        isLoadingPolicy.value = isLoading.value;
      });
      watch(response, () => {
        if (response.value?.status === 'ok') {
          dataHeaderPolicy.value.image = response.value?.data?.image;

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
          isUpdate.value = false;
        } else {
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
    } else {
      const { response, isLoading } = useAxios<DataResponse>(
        'patch',
        '/news/header',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        },
        deps.value
      );
      watch(isLoading, () => {
        isLoadingNews.value = isLoading.value;
      });

      watch(response, () => {
        if (response.value?.status === 'ok') {
          dataHeaderNews.value.image = response.value?.data?.image;

          Swal.fire({
            title: 'Cập nhật thành công',
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
              isUpdate.value = false;

              Swal.close();
            }
          });
        } else {
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
  }
};
</script>
<template>
  <div :class="$style.banner">
    <div :class="$style.banner__item">
      <h1>QUẢN LÝ BANNER</h1>
      <span
        @click="handleTab"
        :style="{
          backgroundColor: isTab === 'Policy' ? '#1fbab8' : '',
          color: isTab === 'Policy' ? '#fff' : ''
        }"
        >Policy</span
      >
      <span
        @click="handleTab"
        :style="{
          backgroundColor: isTab === 'News' ? '#1fbab8' : '',
          color: isTab === 'News' ? '#fff' : ''
        }"
        >News</span
      >
      <div :class="$style['banner__item--policy']" v-if="!isLoadingNews && !isLoadingPolicy">
        <div
          :class="$style['banner__item--policy-img']"
          @click="isOpenInput = !isOpenInput"
          v-if="!imageFile"
        >
          <font-awesome-icon :icon="faCloudArrowUp" :class="$style['upload-ic']" />

          <p>Click để tải ảnh lên</p>
        </div>
        <div
          :class="$style['banner__item--policy-img']"
          :style="{ border: imageFile && 'none', cursor: imageFile && 'auto' }"
          v-else
        >
          <div v-if="imageFile">
            <img :src="imageFile" alt="" />
          </div>

          <button
            :class="$style['banner__item--policy-img-button']"
            @click="isOpenInput = !isOpenInput"
          >
            <font-awesome-icon
              :icon="faUpload"
              :class="$style['banner__item--policy-img-button-ic']"
            />
            Change
          </button>
        </div>

        <button
          :class="$style['banner__item--policy-button']"
          @click="handleUpload"
          v-if="isUpdate"
        >
          Update
        </button>
      </div>
    </div>
    <loading-component v-if="isLoadingNews || isLoadingPolicy" />
  </div>

  <crop-image
    :heightCrop="400"
    :widthCrop="1440"
    :heightWrap="400"
    :widthWrap="1440"
    :check="isOpenInput"
    v-show="isCrop"
    @close="isCrop = false"
    @open="isCrop = true"
    @crop="handleCroppedImage"
  />
</template>

<style module scoped lang="scss">
@import './UpdateBanner.module.scss';
</style>

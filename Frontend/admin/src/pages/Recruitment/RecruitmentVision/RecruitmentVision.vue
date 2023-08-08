<script setup lang="ts">
import { onMounted, ref, watch, type PropType } from 'vue';

import RecruitmentCard from '../RecruitmentCard/RecruitmentCard.vue';
import { ceo, imgHand } from '@/assets/imgs/Recruitment/RecruitmentImgs';
import styles from './RecruitmentVision.module.scss';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import Swal from 'sweetalert2';

interface CardElementItem {
  id: string;
  icon: { link: string; style: string };
  title: { content: string; style: string };
  content: { content: string; style: string };
  image: { link: string; style: string };
  type: string;
}

interface ListImage {
  id: string;
  image: string;
  type: string;
}

const props = defineProps({
  handleEditVision: { type: Function, required: true },
  contentVisionItems: {
    type: Object as unknown as PropType<CardElementItem[]>,
    required: true
  },
  imageVisionItems: { type: Object as unknown as PropType<ListImage[]>, required: true }
});

// const selectedImageVision = ref();
const selectedImage1 = ref();
const selectedImage2 = ref();
const visionItems = ref();
const showBtnUpdateImg = ref(false);
const paramAxios = ref();

const base64ToBlob = (base64Data: string) => {
  const byteString = atob(base64Data.split(',')[1]);
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }
  return new Blob([ab], { type: 'image/png' });
};

// Hàm chuyển đổi từ ArrayBuffer sang string
const arrayBufferToString = (buffer: ArrayBuffer) => {
  const uintArray = new Uint16Array(buffer);
  const charArray: string[] = [];
  for (let i = 0; i < uintArray.length; i++) {
    charArray.push(String.fromCharCode(uintArray[i]));
  }
  return charArray.join('');
};

// Hàm xử lí lấy ảnh từ máy lên
const handleFileInputChange = (event: Event, index: number) => {
  const target = event.target as HTMLInputElement;
  showBtnUpdateImg.value = true;

  if (target.files) {
    const file = target.files[0];
    const reader = new FileReader();

    reader.onload = (e: ProgressEvent<FileReader>) => {
      const result = e.target?.result;
      if (result instanceof ArrayBuffer) {
        if (index === 0) selectedImage1.value = arrayBufferToString(result);
        else selectedImage2.value = arrayBufferToString(result);
      } else if (typeof result === 'string') {
        if (index === 0) selectedImage1.value = result;
        else selectedImage2.value = result;
      }
    };

    reader.readAsDataURL(file);

    target.value = '';
  }
};

const handleUpdateImageVision = () => {
  const checkImage = [
    selectedImage1.value.split(':')[0] !== 'https',
    selectedImage2.value.split(':')[0] !== 'https'
  ];
  const imageBlodArr = [
    checkImage[0]
      ? (new File([base64ToBlob(selectedImage1.value)], 'image.png', {
          type: 'image/png'
        }) as Blob)
      : selectedImage1.value,
    checkImage[1]
      ? (new File([base64ToBlob(selectedImage2.value)], 'image.png', {
          type: 'image/png'
        }) as Blob)
      : selectedImage2.value
  ];
  const objData = {
    subItem1: {
      id: props.contentVisionItems[0].id,
      title: props.contentVisionItems[0].title.content,
      content: props.contentVisionItems[0].content.content,
      type: props.contentVisionItems[0].type
    },
    subItem2: {
      id: props.contentVisionItems[1].id,
      title: props.contentVisionItems[1].title.content,
      content: props.contentVisionItems[1].content.content,
      type: props.contentVisionItems[1].type
    },
    image1: props.imageVisionItems[0],
    image2: props.imageVisionItems[1]
  };

  const formData = new FormData();
  formData.append('data', JSON.stringify(objData));
  formData.append('image1', imageBlodArr[0] as Blob);
  formData.append('image2', imageBlodArr[1] as Blob);

  const patchContentVision = useAxios<DataResponse>(
    'patch',
    `/recruitment/section1`,
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    },
    paramAxios.value
  );

  watch(patchContentVision.response, () => {
    Swal.fire({
      title: 'Cập nhật ảnh thành công!',
      icon: 'success',
      customClass: {
        popup: styles['container-popup'],
        confirmButton: styles['confirm-button'],
        denyButton: styles['deny-button']
      }
    });
    showBtnUpdateImg.value = false;
  });

  watch(patchContentVision.error, (value) => console.log(value));
};

const handleCancelImage = () => {
  Swal.fire({
    title: 'Ảnh chưa được lưu!',
    icon: 'warning',
    showDenyButton: true,
    confirmButtonText: 'Trở lại',
    denyButtonText: 'Hủy',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    if (result.isDenied) {
      showBtnUpdateImg.value = false;
      selectedImage1.value = props.imageVisionItems[0].image;
      selectedImage2.value = props.imageVisionItems[1].image;
    }
  });
};

const handleUpdateImage = () => {
  Swal.fire({
    title: 'Bạn có chắc chắn muốn cập nhật ảnh không?',
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
      handleUpdateImageVision();
    } else if (result.isDenied) {
      Swal.fire({
        title: 'Ảnh chưa được cập nhật!',
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

watch(
  () => props.contentVisionItems,
  (value) => {
    visionItems.value = value;
    console.log(value);
  }
);

watch(
  () => props.imageVisionItems[0],
  (value) => {
    selectedImage1.value = props.imageVisionItems[0].image;
    selectedImage2.value = props.imageVisionItems[1].image;
  }
);

onMounted(() => {
  visionItems.value = props.contentVisionItems;
});
</script>
<template>
  <div :class="$style.container__vision">
    <div :class="[$style['btn-edit-2'], $style['btn-edit']]" @click="props.handleEditVision">
      <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
        <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
        <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
        <g id="SVGRepo_iconCarrier">
          <path
            fill-rule="evenodd"
            clip-rule="evenodd"
            d="m3.99 16.854-1.314 3.504a.75.75 0 0 0 .966.965l3.503-1.314a3 3 0 0 0 1.068-.687L18.36 9.175s-.354-1.061-1.414-2.122c-1.06-1.06-2.122-1.414-2.122-1.414L4.677 15.786a3 3 0 0 0-.687 1.068zm12.249-12.63 1.383-1.383c.248-.248.579-.406.925-.348.487.08 1.232.322 1.934 1.025.703.703.945 1.447 1.025 1.934.058.346-.1.677-.348.925L19.774 7.76s-.353-1.06-1.414-2.12c-1.06-1.062-2.121-1.415-2.121-1.415z"
            fill="#000000"
          ></path>
        </g>
      </svg>
    </div>
    <div v-if="showBtnUpdateImg" :class="$style['update-btn']">
      <div
        :class="[$style['update-btn-submit'], $style['update-btn-cancel']]"
        @click="handleCancelImage"
      >
        Hủy
      </div>
      <div
        :class="[$style['update-btn-submit'], $style['update-btn-update']]"
        @click="handleUpdateImage"
      >
        Cập nhật
      </div>
    </div>
    <div :class="$style['container__vision-block']">
      <div :class="$style['container__vision-block-img1']">
        <div :class="$style.block__img">
          <img :src="selectedImage1" alt="none" />
          <div :class="[$style['block__img-edit'], $style['block__img-edit-full1']]">
            <input type="file" @change="(e) => handleFileInputChange(e, 0)" />
            <svg
              viewBox="-368.64 -368.64 1761.28 1761.28"
              class="icon"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              fill="#000000"
            >
              <g id="SVGRepo_bgCarrier" stroke-width="0">
                <rect
                  x="-368.64"
                  y="-368.64"
                  width="1761.28"
                  height="1761.28"
                  rx="880.64"
                  fill="#7ed0ec"
                  strokewidth="0"
                ></rect>
              </g>
              <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
              <g id="SVGRepo_iconCarrier">
                <path
                  d="M723.2 258.133333H302.933333L375.466667 149.333333c8.533333-12.8 21.333333-19.2 36.266666-19.2h204.8c14.933333 0 27.733333 6.4 36.266667 19.2l70.4 108.8zM298.666667 234.666667H170.666667V196.266667C170.666667 181.333333 181.333333 170.666667 196.266667 170.666667h76.8c14.933333 0 25.6 10.666667 25.6 25.6V234.666667z"
                  fill="#5E35B1"
                ></path>
                <path
                  d="M853.333333 896H170.666667c-46.933333 0-85.333333-38.4-85.333334-85.333333V298.666667c0-46.933333 38.4-85.333333 85.333334-85.333334h682.666666c46.933333 0 85.333333 38.4 85.333334 85.333334v512c0 46.933333-38.4 85.333333-85.333334 85.333333z"
                  fill="#5E35B1"
                ></path>
                <path
                  d="M725.333333 533.333333c0-117.333333-96-213.333333-213.333333-213.333333-51.2 0-98.133333 17.066667-134.4 46.933333l25.6 34.133334C433.066667 377.6 469.333333 362.666667 512 362.666667c93.866667 0 170.666667 76.8 170.666667 170.666666h-74.666667l96 119.466667 96-119.466667H725.333333zM620.8 665.6C590.933333 689.066667 552.533333 704 512 704c-93.866667 0-170.666667-76.8-170.666667-170.666667h74.666667L320 413.866667 224 533.333333H298.666667c0 117.333333 96 213.333333 213.333333 213.333334 51.2 0 98.133333-17.066667 134.4-46.933334l-25.6-34.133333z"
                  fill="#E8EAF6"
                ></path>
              </g>
            </svg>
          </div>
        </div>
      </div>
      <div :class="$style['container__vision-block-img2']">
        <div :class="$style.block__img">
          <img :src="selectedImage2" alt="none" />
          <div :class="[$style['block__img-edit'], $style['block__img-edit-full2']]">
            <input type="file" @change="(e) => handleFileInputChange(e, 1)" />
            <svg
              viewBox="-368.64 -368.64 1761.28 1761.28"
              class="icon"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              fill="#000000"
            >
              <g id="SVGRepo_bgCarrier" stroke-width="0">
                <rect
                  x="-368.64"
                  y="-368.64"
                  width="1761.28"
                  height="1761.28"
                  rx="880.64"
                  fill="#7ed0ec"
                  strokewidth="0"
                ></rect>
              </g>
              <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
              <g id="SVGRepo_iconCarrier">
                <path
                  d="M723.2 258.133333H302.933333L375.466667 149.333333c8.533333-12.8 21.333333-19.2 36.266666-19.2h204.8c14.933333 0 27.733333 6.4 36.266667 19.2l70.4 108.8zM298.666667 234.666667H170.666667V196.266667C170.666667 181.333333 181.333333 170.666667 196.266667 170.666667h76.8c14.933333 0 25.6 10.666667 25.6 25.6V234.666667z"
                  fill="#5E35B1"
                ></path>
                <path
                  d="M853.333333 896H170.666667c-46.933333 0-85.333333-38.4-85.333334-85.333333V298.666667c0-46.933333 38.4-85.333333 85.333334-85.333334h682.666666c46.933333 0 85.333333 38.4 85.333334 85.333334v512c0 46.933333-38.4 85.333333-85.333334 85.333333z"
                  fill="#5E35B1"
                ></path>
                <path
                  d="M725.333333 533.333333c0-117.333333-96-213.333333-213.333333-213.333333-51.2 0-98.133333 17.066667-134.4 46.933333l25.6 34.133334C433.066667 377.6 469.333333 362.666667 512 362.666667c93.866667 0 170.666667 76.8 170.666667 170.666666h-74.666667l96 119.466667 96-119.466667H725.333333zM620.8 665.6C590.933333 689.066667 552.533333 704 512 704c-93.866667 0-170.666667-76.8-170.666667-170.666667h74.666667L320 413.866667 224 533.333333H298.666667c0 117.333333 96 213.333333 213.333333 213.333334 51.2 0 98.133333-17.066667 134.4-46.933334l-25.6-34.133333z"
                  fill="#E8EAF6"
                ></path>
              </g>
            </svg>
          </div>
        </div>
      </div>
    </div>
    <div :class="$style['container__vision-content']">
      <recruitment-card :items="visionItems" :style="'type2'" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentVision.module.scss';
</style>

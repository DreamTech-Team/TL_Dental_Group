<script setup lang="ts">
import { onMounted, ref, watch, type PropType } from 'vue';

import RecruitmentCard from '../RecruitmentCard/RecruitmentCard.vue';
import styles from './RecruitmentVision.module.scss';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import Swal from 'sweetalert2';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCameraRotate, faPencil } from '@fortawesome/free-solid-svg-icons';

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
  () => {
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
      <FontAwesomeIcon :icon="faPencil" />
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
            <font-awesome-icon :icon="faCameraRotate" />
          </div>
        </div>
      </div>
      <div :class="$style['container__vision-block-img2']">
        <div :class="$style.block__img">
          <img :src="selectedImage2" alt="none" />
          <div :class="[$style['block__img-edit'], $style['block__img-edit-full2']]">
            <input type="file" @change="(e) => handleFileInputChange(e, 1)" />
            <font-awesome-icon :icon="faCameraRotate" />
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

<script setup lang="ts">
import { ref, watch } from 'vue';
import Swal from 'sweetalert2';

import { ic_logo } from '@/assets/imgs/Recruitment/RecruitmentImgs';
import styles from './CategoryAddItem.module.scss';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { faCameraRotate } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import CropImage from '@/components/CropImage/CropImage.vue';
import base64ToBlob from '@/utils/base64ToBlob';

const props = defineProps({
  numCate: { type: Number, required: false },
  handleAddCate: { type: Function, required: false },
  data: { type: Object, required: false }
});

const selectedImage = ref(props.data?.img || ic_logo);
const nameCategory = ref(props.data?.title || '');
const emit = defineEmits(['close', 'add-cate']);
const paramAxios = ref([]);
const checkError = ref(false);

const isLoading = ref(false);

const isCrop = ref(false);
const isOpenInput = ref(false);

const handleCroppedImage = (result: string) => {
  if (result) {
    selectedImage.value = result;
  }
};

const handleAddCategory = () => {
  if (nameCategory.value === ' ' || selectedImage.value === '/src/assets/imgs/logo_nobg.png') {
    Swal.fire({
      title: 'Thông tin cung cấp không hợp lệ!',
      icon: 'error',
      customClass: {
        popup: styles['container-popup'],
        confirmButton: styles['confirm-button'],
        denyButton: styles['deny-button']
      }
    });
    return;
  }

  if (props.numCate === 1) {
    const dataCate = {
      title: nameCategory.value,
      highlight: 0
    };
    console.log(dataCate, selectedImage.value);

    const imgCate = new File([base64ToBlob.covertBase64ToBlob(selectedImage.value)], 'image.png', {
      type: 'image/png'
    });

    const formData = new FormData();
    formData.append('img', imgCate as Blob);
    formData.append('data', JSON.stringify(dataCate));

    const postNewCate = useAxios<DataResponse>(
      'post',
      '/cate1',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      },
      paramAxios.value
    );

    watch(postNewCate.isLoading, (value) => {
      isLoading.value = value;
    });

    watch(postNewCate.response, (getValue) => {
      // console.log(postNewCate.response.value?.data);
      if (props.handleAddCate) props.handleAddCate(getValue?.data);
    });

    watch(postNewCate.error, () => {
      checkError.value = true;
    });
  } else {
    const postNewCate = useAxios<DataResponse>(
      'post',
      '/cate2',
      { title: nameCategory.value },
      {},
      paramAxios.value
    );

    watch(postNewCate.response, (getValue) => {
      // console.log(postNewCate.response.value?.data);
      if (props.handleAddCate) props.handleAddCate(getValue?.data);
    });

    watch(postNewCate.isLoading, (value) => {
      isLoading.value = value;
    });

    watch(postNewCate.error, () => {
      checkError.value = true;
    });
  }

  Swal.fire({
    title: 'Đang cập nhật dữ liệu...',
    // timerProgressBar: true,
    // timer: 2000,
    customClass: {
      popup: styles['container-popup'],
      loader: styles['container-loader']
    },
    didOpen: () => {
      Swal.showLoading();
    }
  });

  watch(
    () => isLoading.value,
    (value) => {
      if (!value) {
        if (!checkError.value) {
          emit('close');
          Swal.fire({
            title: 'Thêm danh mục thành công!',
            icon: 'success',
            customClass: {
              popup: styles['container-popup'],
              confirmButton: styles['confirm-button'],
              denyButton: styles['deny-button']
            }
          });
        } else {
          Swal.fire({
            title: 'Danh mục đã tồn tại!',
            icon: 'error',
            customClass: {
              popup: styles['container-popup'],
              confirmButton: styles['confirm-button'],
              denyButton: styles['deny-button']
            }
          });
        }
      }
    }
  );
};

const handleUpdateCategory = () => {
  if (nameCategory.value === ' ' || selectedImage.value === '/src/assets/imgs/logo_nobg.png') {
    Swal.fire({
      title: 'Thông tin cung cấp không hợp lệ!',
      icon: 'error',
      customClass: {
        popup: styles['container-popup'],
        confirmButton: styles['confirm-button'],
        denyButton: styles['deny-button']
      }
    });
    return;
  }

  if (props.numCate === 1) {
    const dataCate = {
      id: props.data?.id,
      title: nameCategory.value,
      highlight: props.data?.highlight
    };

    const imgCate =
      selectedImage.value !== props.data?.img
        ? new File([base64ToBlob.covertBase64ToBlob(selectedImage.value)], 'image.png', {
            type: 'image/png'
          })
        : props.data?.img;

    const formData = new FormData();
    formData.append('img', imgCate as Blob);
    formData.append('data', JSON.stringify(dataCate));

    const updateCate1 = useAxios<DataResponse>(
      'patch',
      `/cate1/${props.data?.id}`,
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      },
      paramAxios.value
    );

    watch(updateCate1.isLoading, (value) => {
      isLoading.value = value;
    });

    watch(updateCate1.response, (value) => {
      console.log(value?.data);
      if (props.handleAddCate) props.handleAddCate(value?.data, 'edit');
    });

    watch(updateCate1.error, (value) => {
      checkError.value = false;
      console.log(value);
    });
  } else {
    const data = { id: props.data?.id, title: nameCategory.value };
    const updateCate2 = useAxios<DataResponse>(
      'patch',
      `/cate2/${props.data?.id}`,
      data,
      {},
      paramAxios.value
    );

    watch(updateCate2.isLoading, (value) => {
      isLoading.value = value;
    });

    watch(updateCate2.response, (value) => {
      console.log(value?.data);
      if (props.handleAddCate) props.handleAddCate(value?.data, 'edit');
    });

    watch(updateCate2.error, (value) => {
      console.log(value);
      checkError.value = false;
    });
  }

  Swal.fire({
    title: 'Đang cập nhật dữ liệu...',
    // timerProgressBar: true,
    timer: 2000,
    customClass: {
      popup: styles['container-popup'],
      loader: styles['container-loader']
    },
    didOpen: () => {
      Swal.showLoading();
    }
  });

  watch(
    () => isLoading.value,
    (value) => {
      if (!value) {
        if (!checkError.value) {
          emit('close');
          Swal.fire({
            title: 'Chỉnh sửa danh mục thành công!',
            icon: 'success',
            customClass: {
              popup: styles['container-popup'],
              confirmButton: styles['confirm-button'],
              denyButton: styles['deny-button']
            }
          });
        } else {
          Swal.fire({
            title: 'Tên danh mục đã tồn tại!',
            icon: 'error',
            customClass: {
              popup: styles['container-popup'],
              confirmButton: styles['confirm-button'],
              denyButton: styles['deny-button']
            }
          });
        }
      }
    }
  );
};

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
</script>
<template>
  <div :class="$style['container__content-right']">
    <h3 v-if="!props.data?.title" :class="$style['heading-style']">
      Tạo Danh Mục Cấp {{ numCate }}
    </h3>
    <h3 v-else :class="$style['heading-style']">Cập Nhật Danh Mục Cấp {{ numCate }}</h3>
    <div :class="$style['container__content-right-edit']">
      <p>Tên Danh Mục</p>
      <input v-model="nameCategory" type="text" placeholder="Nhập tên danh mục" />
    </div>
    <div v-if="numCate === 1" :class="$style['container__content-right-title']">
      <p>Ảnh</p>
    </div>
    <div v-if="numCate === 1" :class="$style['container__content-right-image']">
      <div :class="$style['container__content-right-image-block']">
        <img :src="selectedImage" />
        <div :class="$style['block__img-edit']">
          <button
            @click="
              () => {
                isOpenInput = true;
              }
            "
          >
            <FontAwesomeIcon :icon="faCameraRotate" />
          </button>
        </div>
      </div>
    </div>
    <div :class="$style['container__content-right-btn']">
      <div :class="$style['container__content-right-btn-block']">
        <button type="button" @click="handleModalCancel">Hủy</button>
        <button v-if="!props.data?.title" type="button" @click="handleAddCategory">Thêm</button>
        <button v-else type="button" @click="handleUpdateCategory">Cập nhật</button>
      </div>
    </div>
    <crop-image
      :heightCrop="500"
      :widthCrop="500"
      :heightWrap="600"
      :widthWrap="600"
      :check="isOpenInput"
      v-show="isCrop"
      @close="isCrop = false"
      @open="isCrop = true"
      @crop="handleCroppedImage"
    />
  </div>
</template>

<style module scoped lang="scss">
@import './CategoryAddItem.module.scss';
</style>

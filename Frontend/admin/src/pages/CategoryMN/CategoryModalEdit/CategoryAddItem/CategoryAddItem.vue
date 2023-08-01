<script setup lang="ts">
import { ref, watch, type PropType } from 'vue';
import Swal from 'sweetalert2';

import { ic_logo } from '@/assets/imgs/Recruitment/RecruitmentImgs';
import styles from './CategoryAddItem.module.scss';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface Cate1Object {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
  createAt: string;
}

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
const handleFileInputChange = (event: Event) => {
  const target = event.target as HTMLInputElement;

  if (target.files) {
    const file = target.files[0];
    const reader = new FileReader();

    reader.onload = (e: ProgressEvent<FileReader>) => {
      const result = e.target?.result;
      if (result instanceof ArrayBuffer) {
        selectedImage.value = arrayBufferToString(result);
      } else if (typeof result === 'string') {
        selectedImage.value = result;
      }
    };

    reader.readAsDataURL(file);

    target.value = '';
  }
};

const handleAddCategory = () => {
  if (props.numCate === 1) {
    const dataCate = {
      title: nameCategory.value,
      highlight: 0
    };
    const imgCate = new File([base64ToBlob(selectedImage.value)], 'image.png', {
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

    watch(postNewCate.response, (getValue) => {
      // console.log(postNewCate.response.value?.data);
      if (props.handleAddCate) props.handleAddCate(getValue?.data);
    });

    watch(postNewCate.error, (getValue) => {
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
  }).then((result) => {
    if (!checkError.value)
      if (result.dismiss === Swal.DismissReason.timer) {
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
  });
};

const handleUpdateCategory = () => {
  if (props.numCate === 1) {
    const dataCate = {
      id: props.data?.id,
      title: nameCategory.value,
      highlight: props.data?.highlight
    };
    const imgCate =
      selectedImage.value !== props.data?.img
        ? new File([base64ToBlob(selectedImage.value)], 'image.png', {
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

    watch(updateCate1.response, (value) => {
      console.log(value?.data);
      if (props.handleAddCate) props.handleAddCate(value?.data, 'edit');
    });

    watch(updateCate1.error, (value) => {
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

    watch(updateCate2.response, (value) => {
      console.log(value?.data);
      if (props.handleAddCate) props.handleAddCate(value?.data, 'edit');
    });

    watch(updateCate2.error, (value) => {
      console.log(value);
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
  }).then((result) => {
    if (result.dismiss === Swal.DismissReason.timer) {
      emit('close');
      Swal.fire({
        title: 'Cập nhật danh mục thành công!',
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
  });
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
          <input type="file" @change="(e) => handleFileInputChange(e)" />
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
    <div :class="$style['container__content-right-btn']">
      <div :class="$style['container__content-right-btn-block']">
        <button type="button" @click="handleModalCancel">Hủy</button>
        <button v-if="!props.data?.title" type="button" @click="handleAddCategory">Thêm</button>
        <button v-else type="button" @click="handleUpdateCategory">Cập nhật</button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './CategoryAddItem.module.scss';
</style>

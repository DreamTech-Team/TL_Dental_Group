<script setup lang="ts">
import { ref } from 'vue';
import Swal from 'sweetalert2';

import styles from './RecruitmentEnviroment.module.scss';
import { img1, img2, img3, img4, img5 } from '@/assets/imgs/Recruitment/RecruitmentImgs';

const editEvir = ref(false);
const selectedImageEnvir = ref([img1, img2, img3, img4, img5]);

let prevImageEnvir: string[] = [];

const handleEditEnvir = () => {
  prevImageEnvir = selectedImageEnvir.value.slice();
  editEvir.value = true;
};

//Choose image
const handleFileInputChange = (event: Event, index: number, topic: any) => {
  const inputElement = event.target as HTMLInputElement;
  const file = inputElement.files?.[0];

  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      selectedImageEnvir.value[index] = reader.result as string;
    };
    reader.readAsDataURL(file);
  }
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
      editEvir.value = false;
      selectedImageEnvir.value = [];
      selectedImageEnvir.value = prevImageEnvir.slice();
      prevImageEnvir = [];
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
      editEvir.value = false;
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
        title: 'Nội dung chưa được cập nhật',
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
  <div :class="$style.container__envir">
    <div :class="$style['block-btn-edit']">
      <div
        :class="[$style['btn-edit-2'], $style['btn-edit']]"
        v-if="!editEvir"
        @click="handleEditEnvir"
      >
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
      <div :class="$style['update-btn']" v-if="editEvir">
        <div
          :class="[$style['update-btn-submit'], $style['update-btn-cancel']]"
          @click="handleModalCancel"
        >
          Hủy
        </div>
        <div
          :class="[$style['update-btn-submit'], $style['update-btn-update']]"
          @click="handleModalUpdate"
        >
          Cập nhật
        </div>
      </div>
    </div>
    <div :class="$style['container__envir-title']">Môi Trường Làm Việc Chuyên Nghiệp</div>
    <div :class="$style['container__envir-block']">
      <div
        :class="[
          $style['container__envir-block-img1'],
          $style['container__envir-block-img1-bottom']
        ]"
      >
        <div :class="$style.block__img">
          <img :src="selectedImageEnvir[0]" alt="none" />
          <div :class="$style['block__img-edit']" v-if="editEvir">
            <input type="file" @change="(e) => handleFileInputChange(e, 0, '')" />
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
      <div :class="$style['container__envir-block-img2']">
        <div :class="$style.block__img">
          <img :src="selectedImageEnvir[1]" alt="none" />
          <div :class="$style['block__img-edit']" v-if="editEvir">
            <input type="file" @change="(e) => handleFileInputChange(e, 1, '')" />
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
        <div
          :class="[
            $style['container__envir-block-img2-circle'],
            $style['container__envir-block-img2-circle-1']
          ]"
        ></div>
      </div>
      <div
        :class="[
          $style['container__envir-block-img1'],
          $style['container__envir-block-img1-center']
        ]"
      >
        <div :class="$style.block__img">
          <img :src="selectedImageEnvir[2]" alt="none" />
          <div :class="$style['block__img-edit']" v-if="editEvir">
            <input type="file" @change="(e) => handleFileInputChange(e, 2, '')" />
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
      <div
        :class="[
          $style['container__envir-block-img2'],
          $style['container__envir-block-img2-bottom']
        ]"
      >
        <div>
          <div
            :class="[
              $style['container__envir-block-img2-circle'],
              $style['container__envir-block-img2-circle-2']
            ]"
          ></div>
          <div :class="$style.block__img">
            <img :src="selectedImageEnvir[3]" alt="none" />
            <div :class="$style['block__img-edit']" v-if="editEvir">
              <input type="file" @change="(e) => handleFileInputChange(e, 3, '')" />
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
      <div
        :class="[$style['container__envir-block-img1'], $style['container__envir-block-img1-top']]"
      >
        <div :class="$style.block__img">
          <img :src="selectedImageEnvir[4]" alt="none" />
          <div :class="$style['block__img-edit']" v-if="editEvir">
            <input type="file" @change="(e) => handleFileInputChange(e, 4, '')" />
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
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentEnviroment.module.scss';
</style>

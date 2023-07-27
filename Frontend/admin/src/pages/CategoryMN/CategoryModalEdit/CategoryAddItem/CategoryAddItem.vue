<script setup lang="ts">
import { ref } from 'vue';

import { ic_logo } from '@/assets/imgs/Recruitment/RecruitmentImgs';

const selectedImage = ref(ic_logo);

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
</script>
<template>
  <div :class="$style['container__content-right']">
    <h3 :class="$style['heading-style']">Tạo Danh Mục</h3>
    <div :class="$style['container__content-right-edit']">
      <p>Tên Danh Mục</p>
      <input type="text" placeholder="Nhập tên danh mục" />
    </div>
    <div :class="$style['container__content-right-title']">
      <p>Ảnh</p>
    </div>
    <div :class="$style['container__content-right-image']">
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
        <button type="button">Xóa</button>
        <button type="button">Cập nhật</button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './CategoryAddItem.module.scss';
</style>

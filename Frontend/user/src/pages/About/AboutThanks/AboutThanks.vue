<script setup lang="ts">
import { ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const variableChange = ref([]);
const contentLetter = ref('');

// Gọi hàm useAxios để lấy response, error, và isLoading
const { response, error, isLoading } = useAxios<DataResponse>(
  'get',
  '/introduce/letter',
  {},
  {},
  variableChange.value
);

// Truy xuất giá trị response.value và gán vào responseData
watch(response, () => {
  contentLetter.value = response?.value?.data?.content;
});

let countHover = 0; // biến đếm số lần hover

// hàm scroll theo lá thư khi hover vào bức thư
const handleOpen = () => {
  if (countHover == 0) {
    const mobileElement = document.getElementById('haha');

    if (mobileElement) {
      window.scrollTo({
        top: mobileElement.offsetHeight / 1.7,
        behavior: 'smooth'
      });
    }
  }
  countHover++;
};

const handleB = () => {
  countHover = 0;
  const mobileElement = document.getElementById('haha');

  if (mobileElement) {
    window.scrollTo({
      top: mobileElement.offsetHeight,
      behavior: 'smooth'
    });
  }
};
</script>
<template>
  <div :class="$style.about__thanks">
    <span>LỜI CẢM ƠN</span>

    <div :class="$style['about__thanks-wrapper']" @mouseover="handleOpen" @mouseleave="handleB">
      <div :class="$style['about__thanks-mail']">
        <div :class="$style['about__thanks-cover']"></div>
        <div :class="$style['about__thanks-letter']" id="haha">
          <p v-html="contentLetter"></p>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutThanks.module.scss';
</style>

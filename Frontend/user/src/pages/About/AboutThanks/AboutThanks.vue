<script setup lang="ts">
import { ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

const variableChange = ref([]);
const contentLetter = ref('');
const isLoadingLetter = ref(false);

const { response, isLoading } = useAxios<DataResponse>(
  'get',
  '/introduce/letter',
  {},
  {},
  variableChange.value
);

watch(response, () => {
  isLoadingLetter.value = isLoading.value;
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

// Xử lí scroll khi không hover nữa
const handleLeave = () => {
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

    <div :class="$style['about__thanks-wrapper']" @mouseover="handleOpen" @mouseleave="handleLeave">
      <div :class="$style['about__thanks-mail']">
        <div :class="$style['about__thanks-cover']"></div>
        <div :class="$style['about__thanks-letter']" id="haha" v-if="!isLoadingLetter">
          <h1>LỜI CẢM ƠN</h1>
          <p v-html="contentLetter"></p>
        </div>
        <loading-component v-else />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutThanks.module.scss';
</style>

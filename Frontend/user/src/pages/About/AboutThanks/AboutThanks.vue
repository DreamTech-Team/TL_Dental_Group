<script setup lang="ts">
import { ref, watch, onMounted } from 'vue';
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
  if (window.innerWidth > 1100) {
    if (countHover === 0) {
      const mobileElement = document.getElementById('letter');

      if (mobileElement) {
        window.scrollTo({
          top: mobileElement.offsetHeight / 1.7,
          behavior: 'smooth'
        });
      }
    }
    countHover++;
  }
};

// Xử lí scroll khi không hover nữa
const handleLeave = () => {
  if (window.innerWidth > 1100) {
    countHover = 0;
    const mobileElement = document.getElementById('letter');

    if (mobileElement) {
      window.scrollTo({
        top: mobileElement.offsetHeight,
        behavior: 'smooth'
      });
    }
  }
};

// Xử lí resize lại tấm ảnh để phù hợp với mobile
// onMounted(() => {
//   const parent = document.getElementById('content_letter');
//   // parent.getElementsByTagName('img');
//   if (parent) {
//     const a = ref<HTMLImageElement[] | null>(null);
//     const images = parent.getElementsByTagName('img');
//     const imageArray = Array.from(images);
//     a.value = imageArray;

//     a.value.forEach((item) => {
//       if (window.innerWidth < 736) {
//         const aspectRatio = item.height / item.width;
//         item.width = window.innerWidth - 30;
//         item.height = item.width * aspectRatio;
//       } else if (window.innerWidth < 1100) {
//         const aspectRatio = item.height / item.width;
//         item.width = window.innerWidth - 400;
//         item.height = item.width * aspectRatio;
//       }
//     });
//   }
// });
</script>
<template>
  <div :class="$style.about__thanks">
    <span>LỜI CẢM ƠN</span>

    <div :class="$style['about__thanks-wrapper']" @mouseover="handleOpen" @mouseleave="handleLeave">
      <div :class="$style['about__thanks-mail']">
        <div :class="$style['about__thanks-cover']"></div>
        <div :class="$style['about__thanks-letter']" id="letter" v-if="!isLoadingLetter">
          <h1>LỜI CẢM ƠN</h1>
          <p v-html="contentLetter" id="content_letter"></p>
        </div>
        <loading-component v-else />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutThanks.module.scss';
</style>

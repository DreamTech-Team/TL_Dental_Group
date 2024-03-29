<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

const variableChange = ref([]);
const contentLetter = ref('');
const isLoadingLetter = ref(false);
const canHover = ref(true);
const hoverSetHeight = ref(false);
const heightLetter = ref(0);

const { response, isLoading } = useAxios<DataResponse>(
  'get',
  '/introduce/letter',
  {},
  {},
  variableChange.value
);

// Hàm lấy thẻ p ra để chỉnh lại fontSize cho mobile
const fetchData = async () => {
  if (!isLoadingLetter.value) {
    const parent = document.getElementById('content_letter');

    if (parent) {
      const content = ref<HTMLElement[] | null>(null);
      const contents = parent.getElementsByTagName('p');
      const contentArray = Array.from(contents);

      content.value = contentArray;

      content.value.forEach((item) => {
        if (window.innerWidth < 736) {
          item.style.fontSize = '14px';
        }
      });
    }
  }
};

watch(isLoading, () => {
  isLoadingLetter.value = isLoading.value;
  setTimeout(fetchData, 100);
});

watch(response, () => {
  contentLetter.value = response?.value?.data?.content;
});

// hàm scroll theo lá thư khi hover vào bức thư
const handleOpen = () => {
  if (window.innerWidth > 1100) {
    canHover.value = true; // Vô hiệu hóa hover trong 2 giây

    const letter = document.getElementById('letter');
    if (letter) {
      heightLetter.value = letter.offsetHeight;
    }

    hoverSetHeight.value = true;

    const mobileElement = document.getElementById('letter');

    if (mobileElement) {
      window.scrollTo({
        top: mobileElement.offsetHeight / 1.7,
        behavior: 'smooth'
      });
    }
  }
};

// Xử lí scroll khi không hover nữa
const handleLeave = () => {
  canHover.value = false; // Đặt lại isHovering thành false

  hoverSetHeight.value = false;

  // Sau 3 giây, kích hoạt lại việc hover
  setTimeout(() => {
    canHover.value = true;
  }, 2000);

  if (window.innerWidth > 1100) {
    const mobileElement = document.getElementById('letter');

    if (mobileElement) {
      window.scrollTo({
        top: mobileElement.offsetHeight,
        behavior: 'smooth'
      });
    }
  }
};
</script>
<template>
  <div :class="$style.about__thanks" :style="{ pointerEvents: canHover ? 'auto' : 'none' }">
    <span>LỜI CẢM ƠN</span>

    <div
      :class="$style['about__thanks-wrapper']"
      @mouseenter="handleOpen"
      @mouseleave="handleLeave"
    >
      <div :class="$style['about__thanks-mail']">
        <div :class="$style['about__thanks-cover']"></div>
        <div
          :class="$style['about__thanks-letter']"
          id="letter"
          v-if="!isLoadingLetter"
          :style="{ top: hoverSetHeight ? -heightLetter - 200 + 'px' : 0 }"
        >
          <h1>LỜI CẢM ƠN</h1>
          <p v-html="contentLetter" id="content_letter"></p>
        </div>
        <loading-component v-else />
      </div>
    </div>
  </div>
  <div :class="$style['about__thanks-letter-MBTL']" v-if="!isLoadingLetter">
    <div :class="$style['background-image']">
      <h1>LỜI CẢM ƠN</h1>
      <div v-html="contentLetter" id="content_letter"></div>
    </div>
  </div>
  <loading-component v-else />
</template>

<style module scoped lang="scss">
@import './AboutThanks.module.scss';
</style>

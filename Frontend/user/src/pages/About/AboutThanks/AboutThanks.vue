<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

const variableChange = ref([]);
const contentLetter = ref('');
const isLoadingLetter = ref(false);
const canHover = ref(true);

const { response, isLoading } = useAxios<DataResponse>(
  'get',
  '/introduce/letter',
  {},
  {},
  variableChange.value
);

watch(isLoading, () => {
  isLoadingLetter.value = isLoading.value;
});

watch(response, () => {
  contentLetter.value = response?.value?.data?.content;
});

// hàm scroll theo lá thư khi hover vào bức thư
const handleOpen = () => {
  if (window.innerWidth > 1100) {
    canHover.value = true; // Vô hiệu hóa hover trong 2 giây

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

  // Sau 3 giây, kích hoạt lại việc hover
  setTimeout(() => {
    canHover.value = true;
  }, 3000);

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

// Hàm lấy thẻ p ra để chỉnh lại fontSize cho mobile
const fetchData = async () => {
  if (!isLoadingLetter.value) {
    const parent = document.getElementById('content_letter');

    if (parent) {
      console.log(parent.getElementsByTagName('p'));
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

// Sau 1s mới bắt đầu lấy dữ liệu
onMounted(() => {
  setTimeout(fetchData, 1000);
});
</script>
<template>
  <div :class="$style.about__thanks" :style="{ pointerEvents: canHover ? 'auto' : 'none' }">
    <span>LỜI CẢM ƠN</span>

    <div
      :class="$style['about__thanks-wrapper']"
      @mouseenter="handleOpen"
      @mouseleave="handleLeave"
    >
      <div
        :class="$style['about__thanks-mail']"
        :style="{ pointerEvents: canHover ? 'auto' : 'none' }"
      >
        <div :class="$style['about__thanks-cover']"></div>
        <div :class="$style['about__thanks-letter']" id="letter" v-if="!isLoadingLetter">
          <h1>LỜI CẢM ƠN</h1>
          <div v-html="contentLetter"></div>
        </div>
        <loading-component v-else />
      </div>
    </div>
  </div>
  <div :class="$style['about__thanks-letter-MBTL']" v-if="!isLoadingLetter">
    <h1>LỜI CẢM ƠN</h1>
    <div v-html="contentLetter" id="content_letter"></div>
  </div>
  <loading-component v-else />
</template>

<style module scoped lang="scss">
@import './AboutThanks.module.scss';
</style>

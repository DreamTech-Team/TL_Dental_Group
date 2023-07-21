<script setup lang="ts">
import { ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const variableChange = ref([]);
const imageIntro = ref('');

// Gọi hàm useAxios để lấy response, error, và isLoading
const { response, error, isLoading } = useAxios<DataResponse>(
  'get',
  '/introduce/header',
  {},
  {},
  variableChange.value
);

// Truy xuất giá trị response.value và gán vào responseData
watch(response, () => {
  imageIntro.value = response?.value?.data?.image;
});
</script>
<template>
  <div :class="$style.about__header">
    <div :class="$style['about__header-introduce']">
      <img :src="imageIntro" alt="" />

      <p>Giới Thiệu</p>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutHeader.module.scss';
</style>

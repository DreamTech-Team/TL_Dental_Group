<script setup lang="ts">
import { ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

const variableChange = ref([]);
const imageIntro = ref('');
const isLoadingHeader = ref(true);

// Gọi hàm useAxios để lấy response, error, và isLoading
const { response, isLoading } = useAxios<DataResponse>(
  'get',
  '/introduce/header',
  {},
  {},
  variableChange.value
);

// Truy xuất giá trị response.value và gán vào responseData
watch(response, () => {
  isLoadingHeader.value = isLoading.value;
  imageIntro.value = response?.value?.data?.image;
});
</script>
<template>
  <div :class="$style.about__header">
    <div :class="$style['about__header-introduce']" v-if="!isLoadingHeader">
      <img :src="imageIntro" alt="" />

      <p>Giới Thiệu</p>
    </div>
    <loading-component v-else />
  </div>
</template>

<style module scoped lang="scss">
@import './AboutHeader.module.scss';
</style>

<script setup lang="ts">
import { ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';
// import ImgageBanner from '@/assets/imgs/Product/imageBanner.png';

const variableChange = ref([]);
const imageBannerProduct = ref('');
const isLoadingBannerProduct = ref(false);

const { response, isLoading } = useAxios<DataResponse>(
  'get',
  '/products/header',
  {},
  {},
  variableChange.value
);

watch(isLoading, () => {
  isLoadingBannerProduct.value = isLoading.value;
});

watch(response, () => {
  imageBannerProduct.value = response?.value?.data?.image;
});
</script>
<template>
  <div :class="$style.banner" v-if="!isLoadingBannerProduct">
    <img :class="$style.banner__background" :src="imageBannerProduct" alt="banner" />
    <div :class="$style.banner__content">
      <div :class="$style['banner__content--rectangle']"></div>
      <div :class="$style['banner__content--txttitle']">DỤNG CỤ - THIẾT BỊ NHA KHOA</div>
      <div :class="$style['banner__content--txtnavigation']">TRANG CHỦ / SẢN PHẨM</div>
    </div>
  </div>
  <loading-component v-else />
</template>

<style module scoped lang="scss">
// @import '../ProductPage.module.scss';
@import '../ProductBanner/ProductBanner.module.scss';
</style>

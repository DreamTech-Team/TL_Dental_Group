<script setup lang="ts">
import ImgageBanner from '@/assets/imgs/Product/imageBanner.png';
</script>
<template>
  <div>
    <img :class="$style['product__banner-bg']" :src="ImgageBanner" alt="banner" />
    <div :class="$style['product__banner-content']">
      <div :class="$style['product__banner-bg-rectangle']"></div>
      <p :class="$style['product__banner-text--title']">DỤNG CỤ - THIẾT BỊ NHA KHOA</p>
      <p :class="$style['product__banner-text--navigation']">TRANG CHỦ / SẢN PHẨM</p>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../ProductPage.module.scss';
</style>

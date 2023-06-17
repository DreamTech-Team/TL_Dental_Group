<script setup lang="ts">
import HomeVector from '@/assets/imgs/Product/HomeNavigation.svg';
import NavIcon from '@/assets/imgs/Product/VectorNav.svg';
</script>
<template>
  <div>
    <div :class="$style['product__navigation--container']">
      <img :class="$style['product__navigation--icon-home']" :src="HomeVector" alt="Home" />
      <p :class="$style['product__navigation--nav']">Trang chủ&nbsp;</p>
      <img :class="$style['product__navigation--nav-icon']" :src="NavIcon" alt="nav" />
      <p :class="$style['product__navigation--nav']">&nbsp;Dụng cụ nha khoa</p>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../ProductPage.module.scss';
</style>

<script setup lang="ts">
import Banner from '@/assets/imgs/News/NewsBg.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faHome, faChevronRight } from '@fortawesome/free-solid-svg-icons';
</script>
<template>
  <div :class="$style.news__banner">
    <img :src="Banner" alt="banner" />
  </div>
  <div :class="$style.news__breadcrum">
    <div :class="$style['news__breadcrum-home']">
      <font-awesome-icon :icon="faHome" :class="$style['news__breadcrum-ic']" />
      <span>Home</span>
    </div>
    <div :class="$style['news__breadcrum-tag']">
      <font-awesome-icon :icon="faChevronRight" :class="$style['news__breadcrum-ic']" />
    </div>
    <div :class="$style['news__breadcrum-address']">
      <span>Tin tức</span>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../NewsPage.module.scss';
</style>

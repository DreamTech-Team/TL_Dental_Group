<script setup lang="ts">
import Banner from '@/assets/imgs/News/NewsBg.png';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
</script>
<template>
  <div :class="$style.news__banner">
    <img :src="Banner" alt="banner" />
  </div>
  <bread-crumb :tags="['Tin tức', 'Tag 2', 'Tag 3']" />
</template>

<style module scoped lang="scss">
@import '../NewsPage.module.scss';
</style>

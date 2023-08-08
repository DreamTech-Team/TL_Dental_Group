<script setup lang="ts">
import NewsDetailContent from './NewsDetailContent/NewsDetailContent.vue';
import Categories from '@/components/Category/BaseCategory.vue';
import Activity from '../Home/HomeActivity/HomeActivity.vue';
import DaiDien from '@/assets/imgs/NewsDetail/DaiDien.png';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import { useRoute } from 'vue-router';

const route = useRoute();
</script>
<template>
  <div :class="$style.newsdetail">
    <img :src="DaiDien" alt="" />

    <bread-crumb :tags="route.path" />

    <div :class="$style.newsdetail__wrapper">
      <NewsDetailContent />
      <Categories :class="$style['newsdetail__wrapper-category']" />
    </div>

    <Activity :class="$style.newsdetail__activity" />
  </div>
</template>

<style module scoped lang="scss">
@import './NewsDetail.module.scss';
</style>

<script setup lang="ts">
import { ref, watch } from 'vue';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const pathBC = 'tintuc';
const bannerImg = ref('');
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/news/header', {}, {}, deps.value);

watch(response, () => {
  bannerImg.value = response.value?.data.image;
});
</script>
<template>
  <div :class="$style.news__banner">
    <img :src="bannerImg" alt="banner" />
  </div>
  <bread-crumb :tags="pathBC" />
</template>

<style module scoped lang="scss">
@import './NewsBanner.module.scss';
</style>

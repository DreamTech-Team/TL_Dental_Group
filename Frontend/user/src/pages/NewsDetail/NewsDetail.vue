<script setup lang="ts">
import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import NewsDetailContent from './NewsDetailContent/NewsDetailContent.vue';
import Categories from '@/components/Category/BaseCategory.vue';
import Activity from '../Home/HomeActivity/HomeActivity.vue';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface NewsDetail {
  title: string;
  time: Date;
  summary: string;
  img: string;
  createAt: string;
  detail: string;
  slug: string;
}

const route = useRoute();

const path = ref(window.location.pathname.split('/').pop());
const variableChange = ref([]);
const dataRender = ref<NewsDetail>({
  title: '',
  time: new Date(),
  summary: '',
  img: '',
  createAt: '',
  detail: '',
  slug: ''
});
const isLoadingContent = ref(false);

const { response, isLoading } = useAxios<DataResponse>(
  'get',
  '/news/' + path.value,
  {},
  {},
  variableChange.value
);

watch(isLoading, () => {
  isLoadingContent.value = isLoading.value;
});

watch(response, () => {
  dataRender.value = response.value?.data;
});
</script>
<template>
  <loading-component v-if="isLoadingContent" />
  <div :class="$style.newsdetail" v-else>
    <div :class="$style.newsdetail__img">
      <img :src="dataRender.img" alt="" />
    </div>

    <bread-crumb :tags="route.path" />

    <div :class="$style.newsdetail__wrapper">
      <news-detail-content v-if="dataRender.slug" :data="dataRender" />
      <Categories :class="$style['newsdetail__wrapper-category']" />
    </div>

    <Activity :class="$style.newsdetail__activity" />
  </div>
</template>

<style module scoped lang="scss">
@import './NewsDetail.module.scss';
</style>

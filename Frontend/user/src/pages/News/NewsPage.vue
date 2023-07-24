<script setup lang="ts">
import { ref, watch } from 'vue';
import Banner from './NewsBanner/NewsBanner.vue';
import Control from './NewsControl/NewsControl.vue';
import Context from './NewsContext/NewsContext.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const deps = ref([]);
const dataContext = ref([]);
const currentPage = ref(0);
const { response } = useAxios<DataResponse>('get', '/news?sort=desc', {}, {}, deps.value);
watch(response, () => {
  console.log(response.value?.data);
  dataContext.value = response.value?.data?.data;
  currentPage.value = parseInt(response.value?.data?.page);
});
</script>
<template>
  <div :class="$style.news">
    <banner />
    <control />
    <context v-if="dataContext.length > 0" :list-item="dataContext" :prs-page="currentPage" />
  </div>
</template>

<style module scoped lang="scss">
@import './NewsPage.module.scss';
</style>

<script setup lang="ts">
import { ref, watch } from 'vue';
import Banner from './NewsBanner/NewsBanner.vue';
import Control from './NewsControl/NewsControl.vue';
import Context from './NewsContext/NewsContext.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface Item {
  id: string;
  name: string;
  slug: string;
  createAt: string;
}

const path = ref('');
const deps = ref([]);
const dataContext = ref([]);
const currentPage = ref(0);
const { response } = useAxios<DataResponse>('get', '/news?sort=desc', {}, {}, deps.value);

const onUpdateSort = (data: { sort: string }) => {
  console.log(data.sort);
};

const onUpdateContent = (data: { listrs: Item[] }) => {
  const slugs = data.listrs.map((item) => item.slug);
  path.value = slugs.map((slug) => `filterTags=${slug}`).join('&');
  // console.log(path.value);
};

watch(path, () => {
  const { response } = useAxios<DataResponse>(
    'get',
    `/news?${path.value}&sort=desc`,
    {},
    {},
    deps.value
  );

  watch(response, () => {
    dataContext.value = response.value?.data?.data;
  });
});

watch(response, () => {
  dataContext.value = response.value?.data?.data;
  currentPage.value = parseInt(response.value?.data?.page);
});
</script>
<template>
  <div :class="$style.news">
    <banner />
    <control @update-content="onUpdateContent" @update-sort="onUpdateSort" />
    <context v-if="dataContext.length > 0" :list-item="dataContext" :prs-page="currentPage" />
  </div>
</template>

<style module scoped lang="scss">
@import './NewsPage.module.scss';
</style>

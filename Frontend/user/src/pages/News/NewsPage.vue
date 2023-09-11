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
const popular = ref(false);
const deps = ref([]);
const dataContext = ref([]);
const currentPage = ref(0);
const totalPage = ref(0);
const loading = ref(false);

//Get total
const getTotal = useAxios<DataResponse>('get', '/news/total', {}, {}, deps.value);
watch(getTotal.response, () => {
  totalPage.value = getTotal.response.value?.data;
});

//Get default data
const { response, isLoading } = useAxios<DataResponse>(
  'get',
  `/news?sort=desc&page=${currentPage.value}&pageSize=8&popular=${popular.value}`,
  {},
  {},
  deps.value
);
watch(response, () => {
  dataContext.value = response.value?.data?.data;
});
watch(isLoading, () => {
  loading.value = isLoading.value;
});

//Sort
const onUpdateSort = (data: { sort: string }) => {
  if (data.sort === 'Phổ biến') {
    popular.value = true;
  } else {
    popular.value = false;
  }
};

//Update when change slug
const onUpdateSlug = (data: { listrs: Item[] }) => {
  const slugs = data.listrs.map((item) => item.slug);
  path.value = slugs.map((slug) => `filterTags=${slug}`).join('&');

  const updateSlug = useAxios<DataResponse>(
    'get',
    `/news?${path.value}&sort=desc&page=${currentPage.value}&pageSize=8&popular=${popular.value}`,
    {},
    {},
    deps.value
  );

  watch(updateSlug.response, () => {
    dataContext.value = updateSlug.response.value?.data?.data;
  });

  const getTotal = useAxios<DataResponse>('get', `/news/total?${path.value}`, {}, {}, deps.value);
  watch(getTotal.response, () => {
    totalPage.value = getTotal.response.value?.data;
  });
};

//Update current page
const updateCurrentPage = (currentIdx: number) => {
  currentPage.value = currentIdx;
};

watch([currentPage, popular], () => {
  const { response } = useAxios<DataResponse>(
    'get',
    `/news?${path.value}&sort=desc&page=${currentPage.value}&pageSize=8&popular=${popular.value}`,
    {},
    {},
    deps.value
  );

  watch(response, () => {
    dataContext.value = response.value?.data?.data;
  });
});
</script>
<template>
  <div :class="$style.news">
    <banner />
    <control @update-slug="onUpdateSlug" @update-sort="onUpdateSort" />
    <context
      :list-item="dataContext"
      :popular-status="popular"
      :path="path"
      :loading="loading"
      :prs-page="currentPage"
      :totalPage="totalPage"
      @current-page="updateCurrentPage"
    />
  </div>
</template>

<style module scoped lang="scss">
@import './NewsPage.module.scss';
</style>

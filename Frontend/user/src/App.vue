<script setup lang="ts">
import { ref, watch } from 'vue';
import { RouterView } from 'vue-router';
import BaseHeader from './components/BaseHeader/BaseHeader.vue';
import BaseFooter from './components/BaseFooter/BaseFooter.vue';
import ContactMessage from './components/ContactMessage/ContactMessage.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { useDataRenderStore } from '@/stores/counter';
import convertDataCate from '@/utils/covertDataCate';

interface ListCategory1 {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
}

interface ListCategory2 {
  title: string;
  slug: string;
}

interface ListCategories {
  id: string;
  cate1Id: ListCategory1;
  cate2Id: ListCategory2;
  companyId: {
    id: string;
    name: string;
    slug: string;
  };
}

interface DataRender {
  title: string;
  slug: string;
  company: { name: string; slug: string; cate2: ListCategory2[] }[];
}

const dataRenderStore = useDataRenderStore();

const valueChange = ref([]);
const listCategory1 = ref<ListCategory1[]>([]);
const listCategory2 = ref<ListCategory2[]>([]);
const dataRender = ref<DataRender[]>([]);
const isLoadingCategory = ref(false);

const fetchData = async () => {
  const { response, isLoading } = useAxios<DataResponse>('get', '/cate', {}, {}, valueChange.value);
  isLoadingCategory.value = isLoading.value;

  watch(response, () => {
    isLoadingCategory.value = isLoading.value;

    if (response.value?.data) {
      console.log(response.value?.data);

      response.value?.data.forEach((item: ListCategories) => {
        listCategory1.value.push(item.cate1Id);
        listCategory2.value.push(item.cate2Id);
      });

      dataRender.value = convertDataCate.handleDataRender(response.value?.data, dataRender.value);

      dataRenderStore.setDataRender(dataRender.value);
    }
  });
};

// Gọi fetchData sau 3 giây
setTimeout(fetchData, 3000);
</script>

<template>
  <BaseHeader />

  <RouterView />

  <contact-message />

  <BaseFooter />
</template>

<style scoped lang="scss"></style>

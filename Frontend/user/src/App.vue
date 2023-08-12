<script setup lang="ts">
import { ref, watch } from 'vue';
import { RouterView } from 'vue-router';
import BaseHeader from './components/BaseHeader/BaseHeader.vue';
import BaseFooter from './components/BaseFooter/BaseFooter.vue';
import ContactMessage from './components/ContactMessage/ContactMessage.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { useDataRenderStore } from '@/stores/counter';

interface ListCategory1 {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
}

interface ListCategory2 {
  id: string;
  title: string;
  slug: string;
}

interface ListCategories {
  id: string;
  cate1Id: ListCategory1;
  cate2Id: ListCategory2;
}

interface DataRender {
  title: string;
  slug: string;
  data: { name: string; slug: string }[];
}

const dataRenderStore = useDataRenderStore();

const valueChange = ref([]);
const listCategory1 = ref<ListCategory1[]>([]);
const listCategory2 = ref<ListCategory2[]>([]);
const dataRender = ref<DataRender[]>([]);
const isLoadingCategory = ref(false);

const { response, isLoading } = useAxios<DataResponse>('get', '/cate', {}, {}, valueChange.value);
isLoadingCategory.value = isLoading.value;

watch(response, () => {
  isLoadingCategory.value = isLoading.value;

  if (response.value?.data) {
    response.value?.data.forEach((item: ListCategories) => {
      listCategory1.value.push(item.cate1Id);
      listCategory2.value.push(item.cate2Id);
    });

    const uniqueElementsSet = new Set<string>();
    const duplicatesPositions: { [key: string]: number[] } = {};
    const duplicatesPositions1: { [key: string]: number[] } = {};

    listCategory1.value.forEach((item, index) => {
      const { id } = item;

      if (uniqueElementsSet.has(id)) {
        if (!duplicatesPositions[id]) {
          duplicatesPositions[id] = [index];
        } else {
          duplicatesPositions[id].push(index);
        }
      } else {
        uniqueElementsSet.add(id);
        duplicatesPositions1[id] = [index];
        duplicatesPositions1[id].push(index);
      }
    });

    Object.keys(duplicatesPositions).forEach((id) => {
      duplicatesPositions[id].push(duplicatesPositions1[id][0]);
      delete duplicatesPositions1[id];
    });

    const uniqueElementsArray = Array.from(uniqueElementsSet).map((id) => {
      return listCategory1.value.find((item) => item.id === id);
    });

    uniqueElementsArray.forEach((item) => {
      if (item) {
        const object = {
          title: '',
          slug: '',
          data: [
            {
              name: '',
              slug: ''
            }
          ]
        };
        const dataCate2: { name: string; slug: string }[] = [];

        if (duplicatesPositions1[item.id]) {
          object.title = item.title;
          object.slug = item.slug;

          if (listCategory2.value[duplicatesPositions1[item.id][0]] !== null) {
            dataCate2.push({
              name: listCategory2.value[duplicatesPositions1[item.id][0]].title,
              slug: listCategory2.value[duplicatesPositions1[item.id][0]].slug
            });
          }

          object.data = dataCate2;

          dataRender.value.push(object);
        }
        if (duplicatesPositions[item.id]) {
          object.title = item.title;
          object.slug = item.slug;

          duplicatesPositions[item.id].forEach((replace) => {
            if (listCategory2.value[replace] !== null) {
              dataCate2.push({
                name: listCategory2.value[replace].title,
                slug: listCategory2.value[replace].slug
              });
            }
          });

          object.data = dataCate2;

          dataRender.value.push(object);
        }
      }
    });
    console.log(dataRender.value);

    dataRenderStore.setDataRender(dataRender.value);
  }
});
</script>

<template>
  <BaseHeader />

  <RouterView />

  <contact-message />

  <BaseFooter />
</template>

<style scoped lang="scss"></style>

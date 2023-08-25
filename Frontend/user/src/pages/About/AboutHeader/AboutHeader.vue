<script setup lang="ts">
import { ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

const variableChange = ref([]);
const imageIntro = ref('');
const isLoadingHeader = ref(false);

const { response, isLoading } = useAxios<DataResponse>(
  'get',
  '/introduce/header',
  {},
  {},
  variableChange.value
);

watch(isLoading, () => {
  isLoadingHeader.value = isLoading.value;
});

watch(response, () => {
  imageIntro.value = response?.value?.data?.image;
});
</script>
<template>
  <div :class="$style.about__header">
    <div :class="$style['about__header-introduce']" v-if="!isLoadingHeader">
      <img :src="imageIntro" alt="" />

      <p>Giới Thiệu</p>
    </div>
    <loading-component v-else />
  </div>
</template>

<style module scoped lang="scss">
@import './AboutHeader.module.scss';
</style>

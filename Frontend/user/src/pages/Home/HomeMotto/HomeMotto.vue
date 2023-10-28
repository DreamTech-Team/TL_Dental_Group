<script setup lang="ts">
import { ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

//GET DATA
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/home/section2', {}, {}, deps.value);

const content = ref({
  title: '',
  context: ``,
  image: ''
});

watch(response, () => {
  content.value.title = response.value?.data?.title;
  content.value.context = response.value?.data?.content;
  content.value.image = response.value?.data?.image;
});
</script>
<template>
  <div :class="$style.home__motto">
    <div :class="$style['home__motto-left']">
      <h4>{{ content.title }}</h4>
      <span v-html="content.context"></span>
    </div>
    <div :class="$style['home__motto-right']">
      <h4 :class="$style['home__motto-title']">{{ content.title }}</h4>
      <img :src="content.image" alt="meeting" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './HomeMotto.module.scss';
</style>

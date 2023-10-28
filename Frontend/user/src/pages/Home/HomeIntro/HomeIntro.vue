<script setup lang="ts">
import { ref, watch } from 'vue';
import router from '@/router/index';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

//GET DATA
const deps = ref([]);
const nameComp = ref([]);
const { response } = useAxios<DataResponse>('get', '/home/section3', {}, {}, deps.value);
const getLogo = useAxios<DataResponse>('get', '/information?type=GENERAL', {}, {}, deps.value);

const content = ref({
  title: ``,
  context: ``,
  image: ''
});

watch(response, () => {
  content.value.image = response.value?.data?.image;
  content.value.title = response.value?.data?.title;
  content.value.context = response.value?.data?.content;
});

watch(getLogo.response, () => {
  nameComp.value = getLogo.response.value?.data?.name.content;
});
</script>
<template>
  <div :class="$style.home__intro">
    <div :class="$style['home__intro-left']">
      <h3 v-html="content.title"></h3>
      <button :class="$style['home__intro-btn']" @click="router.push('/gioithieu')">
        XEM CHI TIẾT
      </button>
    </div>
    <div :class="$style['home__intro-center']">
      <img :src="content.image" alt="ceo" />
      <div :class="$style['home__intro-description']">
        <div :class="$style['home__intro-logo']">
          <h3>{{ nameComp }}</h3>
        </div>
        <div :class="$style['home__intro-speach']" v-html="content.context"></div>
      </div>
    </div>
    <div :class="$style['home__intro-right']"></div>
  </div>
</template>

<style module scoped lang="scss">
@import './HomeIntro.module.scss';
</style>

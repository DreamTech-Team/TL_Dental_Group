<script setup lang="ts">
import { ref, watch } from 'vue';
import SVG from '@/assets/icons/HomeReason/white_check.png';

import useAxios, { type DataResponse } from '@/hooks/useAxios';

//GET DATA
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/home/section1', {}, {}, deps.value);

const content = ref({
  image: '',
  title: '',
  description: '',
  listrs: [
    {
      icon: SVG,
      title: '',
      description: ''
    },
    {
      icon: SVG,
      title: '',
      description: ''
    },
    {
      icon: SVG,
      title: '',
      description: ''
    }
  ]
});

watch(response, () => {
  content.value.image = response.value?.data?.heading?.image;
  content.value.title = response.value?.data?.heading?.title;
  content.value.description = response.value?.data?.heading?.content;
  content.value.listrs[0].title = response.value?.data?.subItem1?.title;
  content.value.listrs[0].icon = response.value?.data?.subItem1?.image;
  content.value.listrs[0].description = response.value?.data?.subItem1?.content;
  content.value.listrs[1].title = response.value?.data?.subItem2?.title;
  content.value.listrs[1].icon = response.value?.data?.subItem2?.image;
  content.value.listrs[1].description = response.value?.data?.subItem2?.content;
  content.value.listrs[2].title = response.value?.data?.subItem3?.title;
  content.value.listrs[2].icon = response.value?.data?.subItem3?.image;
  content.value.listrs[2].description = response.value?.data?.subItem3?.content;
});
</script>
<template>
  <div :class="$style.home__reason">
    <div :class="$style['home__reason-left']">
      <img :src="content.image" alt="company" />
    </div>
    <div :class="$style['home__reason-right']">
      <h4 :class="$style['home__reason-title']">{{ content.title }}</h4>
      <h4>{{ content.description }}</h4>
      <div :class="$style['home__reason-list']">
        <div
          :class="$style['home__reason-item']"
          v-for="(item, index) in content.listrs"
          :key="index"
        >
          <div :class="$style['home__reason-icon']">
            <img :src="item.icon" alt="icon1" />
          </div>
          <div :class="$style['home__reason-description']">
            <h5>{{ item.title }}</h5>
            <p>
              {{ item.description }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './HomeReason.module.scss';
</style>

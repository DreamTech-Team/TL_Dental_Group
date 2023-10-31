<script setup lang="ts">
import { type PropType } from 'vue';
import RecruitmentCard from '../RecruitmentCard/RecruitmentCard.vue';

interface CardElementItem {
  id: string;
  icon: { link: string; style: string };
  title: { content: string; style: string };
  content: { content: string; style: string };
  image: { link: string; style: string };
  type: string;
}

interface ListImage {
  id: string;
  image: string;
  type: string;
}

defineProps({
  contentVisionItems: {
    type: Object as unknown as PropType<CardElementItem[]>,
    required: true
  },
  imageVisionItems: { type: Object as unknown as PropType<ListImage[]>, required: true }
});
</script>
<template>
  <div :class="$style.container__vision">
    <div v-if="imageVisionItems.length > 0" :class="$style['container__vision-block']">
      <div :class="$style['container__vision-block-img1']">
        <img :src="imageVisionItems[0].image" alt="none" />
      </div>
      <div :class="$style['container__vision-block-img2']">
        <img :src="imageVisionItems[1].image" alt="none" />
      </div>
    </div>
    <div :class="$style['container__vision-content']">
      <recruitment-card :items="contentVisionItems" :style="'type2'" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentVision.module.scss';
</style>

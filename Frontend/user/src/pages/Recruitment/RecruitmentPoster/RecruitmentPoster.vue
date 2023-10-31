<script setup lang="ts">
import { ref, watch, onMounted, type PropType, type Ref } from 'vue';
import { girl1, girl2 } from '@/assets/imgs/Recruitment/RecruitmentImgs';
import RecruitmentCard from '../RecruitmentCard/RecruitmentCard.vue';
import TheAnimate from '@/components/TheAnimate/TheAnimate.vue';

interface CardElementItem {
  icon: { link: string; style: string };
  title: { content: string; style: string };
  content: { content: string; style: string };
  image: { link: string; style: string };
}

defineProps({
  hanldeScrollToVacancies: { type: Function, required: true },
  contentPosterItems: { type: Object as unknown as PropType<CardElementItem[]>, required: true }
});
</script>
<template>
  <div :class="$style.container__poster">
    <div :class="$style['container__poster-img']">
      <div :class="$style['container__poster-img-content']">
        <div class="">
          <img :src="girl1" alt="none" />
        </div>
        <div class="">
          <img :src="girl2" alt="none" />
        </div>
      </div>
    </div>
    <div :class="$style['container__poster-title']">
      <h4>Với bề dày hơn <span>2 năm</span> kinh doanh và phát triển</h4>
      <h3>Công ty sở hữu số lượng vật liệu và dụng cụ nha khoa</h3>
      <h2>LỚN NHẤT VIỆT NAM</h2>
      <h1>
        <span style="position: relative"
          >#TOP1
          <the-animate />
        </span>
      </h1>
    </div>
    <div :class="$style['container__poster-value']">
      <div :class="$style['container__poster-value-item']">
        Hơn <span>2000</span> vật liệu chỉnh nha
      </div>
    </div>
    <div :class="$style['container__poster-btn']">
      <div :class="$style['container__poster-btn-item']" @click="hanldeScrollToVacancies('smooth')">
        <p>Xem vị trí tuyển dụng</p>
      </div>
    </div>
    <div :class="$style['container__poster-block']">
      <div :class="$style['container__poster-block-card']">
        <recruitment-card :items="contentPosterItems" :style="'type1'" />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentPoster.module.scss';
</style>

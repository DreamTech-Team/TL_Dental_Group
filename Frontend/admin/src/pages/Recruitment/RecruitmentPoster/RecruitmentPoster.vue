<script setup lang="ts">
import { ref, watch, onMounted } from 'vue';
import { girl1, girl2 } from '@/assets/imgs/Recruitment/RecruitmentImgs';
import RecruitmentCard from '../RecruitmentCard/RecruitmentCard.vue';

const props = defineProps({
  hanldeScrollToVacancies: { type: Function, required: true },
  handleEditPoster: { type: Function, required: true },
  contentPosterItems: { type: Object, required: true }
});

const posterItems = ref();

watch(
  () => props.contentPosterItems,
  (value) => {
    posterItems.value = value;
    console.log(value);
  }
);

onMounted(() => {
  posterItems.value = props.contentPosterItems;
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
      <h1>#TOP1</h1>
    </div>
    <div :class="$style['container__poster-value']">
      <div :class="$style['container__poster-value-item']">
        Hơn <span>2000</span> vật liệu chỉnh nha
      </div>
    </div>
    <div :class="$style['container__poster-btn']">
      <div :class="$style['container__poster-btn-item']" @click="props.hanldeScrollToVacancies">
        <p>Xem vị trí tuyển dụng</p>
      </div>
    </div>
    <div :class="$style['container__poster-block']">
      <div :class="$style['container__poster-block-card']">
        <div :class="[$style['btn-edit-1'], $style['btn-edit']]" @click="props.handleEditPoster">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
            <g id="SVGRepo_iconCarrier">
              <path
                fill-rule="evenodd"
                clip-rule="evenodd"
                d="m3.99 16.854-1.314 3.504a.75.75 0 0 0 .966.965l3.503-1.314a3 3 0 0 0 1.068-.687L18.36 9.175s-.354-1.061-1.414-2.122c-1.06-1.06-2.122-1.414-2.122-1.414L4.677 15.786a3 3 0 0 0-.687 1.068zm12.249-12.63 1.383-1.383c.248-.248.579-.406.925-.348.487.08 1.232.322 1.934 1.025.703.703.945 1.447 1.025 1.934.058.346-.1.677-.348.925L19.774 7.76s-.353-1.06-1.414-2.12c-1.06-1.062-2.121-1.415-2.121-1.415z"
                fill="#000000"
              ></path>
            </g>
          </svg>
        </div>
        <recruitment-card :items="posterItems" :style="'type1'" />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentPoster.module.scss';
</style>

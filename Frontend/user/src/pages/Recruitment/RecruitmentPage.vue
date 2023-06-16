<script setup lang="ts">
import girl1 from '@/assets/imgs/girl1.png';
import girl2 from '@/assets/imgs/girl2.png';
import RecruitmentCard from './RecruitmentCard/RecruitmentCard.vue';
</script>
<template>
  <div :class="$style.container">
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
        <div :class="$style['container__poster-btn-item']">
          <p>Xem vị trí tuyển dụng</p>
        </div>
      </div>
      <div :class="$style['container__poster-block']">
        <div :class="$style['container__poster-block-card']">
          <recruitment-card />
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentPage.module.scss';
</style>

<script setup lang="ts">
import CEO from '@/assets/imgs/Home/CEO.png';
import Logo from '@/assets/imgs/logo_nobg.png';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
</script>
<template>
  <div :class="$style.home__intro">
    <div :class="$style['home__intro-left']">
      <h3>Lời ngỏ từ <strong>CEO NGUYEN HOANG</strong></h3>
      <button :class="$style['home__intro-btn']">XEM CHI TIẾT</button>
    </div>
    <div :class="$style['home__intro-center']">
      <img :src="CEO" alt="ceo" />
      <CamBtn />
      <div :class="$style['home__intro-description']">
        <div :class="$style['home__intro-logo']">
          <img :src="Logo" alt="logo" />
          <h3>Group</h3>
        </div>
        <div :class="$style['home__intro-speach']">
          <span
            >"I used to have a bunch of different tools I had to pay for, with Circle you get
            everything in one bundle."</span
          >
        </div>
        <div :class="$style['home__intro-signature']">
          <strong>Michel Dedrick</strong>
          <span>Senior Conversion Optimizer</span>
        </div>
      </div>
    </div>
    <div :class="$style['home__intro-right']"></div>
    <EditBtn style="top: 90px" />
  </div>
</template>

<style module scoped lang="scss">
@import './HomeIntro.module.scss';
</style>

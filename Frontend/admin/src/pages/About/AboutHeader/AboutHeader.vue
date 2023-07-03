<script setup lang="ts">
import Intro from '@/assets/imgs/About/Intro.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faRotate } from '@fortawesome/free-solid-svg-icons';
</script>
<template>
  <div :class="$style.about__header">
    <div :class="$style['about__header-introduce']">
      <img :src="Intro" alt="" />

      <div :class="$style['about__header-introduce-bg']">
        <button :class="$style['about__header-introduce-bg-button']">
          <font-awesome-icon
            :icon="faRotate"
            :class="$style['about__header-introduce-bg-button-ic']"
          />
          <span>Đổi hình ảnh</span>
        </button>
      </div>

      <p>Giới Thiệu</p>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutHeader.module.scss';
</style>

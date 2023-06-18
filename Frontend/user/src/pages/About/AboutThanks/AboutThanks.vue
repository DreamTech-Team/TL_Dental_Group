<script setup lang="ts">
// import Intro from '@/assets/imgs/About/Intro.png';
</script>
<template>
  <div :class="$style.about__thanks">
    <p>LỜI CẢM ƠN</p>

    <div :class="$style['about__thanks-wrapper']">
      <div :class="$style['about__thanks-mail']">
        <div :class="$style['about__thanks-cover']"></div>
        <div :class="$style['about__thanks-letter']"><h1>@</h1></div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutThanks.module.scss';
</style>

<script setup lang="ts">
import AboutHeader from './AboutHeader/AboutHeader.vue';
import AboutInfoCompany from './AboutInfoCompany/AboutInfoCompany.vue';
import AboutThanks from './AboutThanks/AboutThanks.vue';
import AboutMotto from './AboutMotto/AboutMotto.vue';
import AboutBusinessItems from '../Home/HomeCategory/HomeCategory.vue';
</script>
<template>
  <div :class="$style.about">
    <about-header />
    <about-thanks />
    <about-infoCompany />
    <about-motto />
    <about-businessItems />
  </div>
</template>

<style module scoped lang="scss">
@import './AboutPage.module.scss';
</style>

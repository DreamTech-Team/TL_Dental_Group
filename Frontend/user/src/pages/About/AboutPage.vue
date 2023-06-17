<script setup lang="ts">
import AboutHeader from './AboutHeader/AboutHeader.vue';
import AboutInfoCompany from './AboutInfoCompany/AboutInfoCompany.vue';
import AboutThanks from './AboutThanks/AboutThanks.vue';
import AboutMotto from './AboutMotto/AboutMotto.vue';
import AboutBusinessItems from './AboutBusinessItems/AboutBusinessItems.vue';
import AboutPolicy from './AboutPolicy/AboutPolicy.vue';
import AboutFacility from './AboutFacility/AboutFacility.vue';
</script>
<template>
  <div :class="$style.about">
    <AboutHeader />
    <AboutThanks />
    <AboutInfoCompany />
    <AboutMotto />
    <AboutBusinessItems />
    <AboutPolicy />
    <AboutFacility />
  </div>
</template>

<style module scoped lang="scss">
@import './AboutPage.module.scss';
</style>

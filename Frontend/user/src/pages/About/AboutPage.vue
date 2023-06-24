<script setup lang="ts">
import AboutHeader from './AboutHeader/AboutHeader.vue';
import AboutInfoCompany from './AboutInfoCompany/AboutInfoCompany.vue';
import AboutThanks from './AboutThanks/AboutThanks.vue';
import AboutMotto from './AboutMotto/AboutMotto.vue';
import AboutBusinessItems from './AboutBusinessItems/AboutBusinessItems.vue';
import AboutPolicy from './AboutPolicy/AboutPolicy.vue';
import AboutFacility from './AboutFacility/AboutFacility.vue';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';

const pathAbout = 'tintuc';
</script>
<template>
  <div :class="$style.about">
    <BreadCrumb :tags="pathAbout" />
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

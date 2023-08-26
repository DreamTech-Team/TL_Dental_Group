<script setup lang="ts">
import { useRoute } from 'vue-router';
import AboutHeader from './AboutHeader/AboutHeader.vue';
import AboutInfoCompany from './AboutInfoCompany/AboutInfoCompany.vue';
import AboutThanks from './AboutThanks/AboutThanks.vue';
import AboutMotto from './AboutMotto/AboutMotto.vue';
import AboutBusinessItems from '../Home/HomeCategory/HomeCategory.vue';
import AboutPolicy from './AboutPolicy/AboutPolicy.vue';
import AboutFacility from './AboutFacility/AboutFacility.vue';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';

const route = useRoute();
</script>
<template>
  <div :class="$style.about">
    <about-header />
    <bread-crumb :tags="route.path" />
    <about-thanks />
    <about-info-company />
    <about-motto />
    <about-business-items :style="{ marginTop: '6rem' }" />
    <about-policy />
    <about-facility />
  </div>
</template>

<style module scoped lang="scss">
@import './AboutPage.module.scss';
</style>

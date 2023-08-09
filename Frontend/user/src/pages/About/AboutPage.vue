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
    <bread-crumb :tags="route.path" />
    <about-header />
    <about-thanks />
    <about-infoCompany />
    <about-motto />
    <about-businessItems :style="{ marginTop: '6rem' }" />
    <about-policy />
    <about-facility />
  </div>
</template>

<style module scoped lang="scss">
@import './AboutPage.module.scss';
</style>

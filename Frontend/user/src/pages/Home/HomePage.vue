<script setup lang="ts">
import HomeBanner from './HomeBanner/HomeBanner.vue';
import HomeCategory from './HomeCategory/HomeCategory.vue';
import HomeCompany from './HomeCompany/HomeCompany.vue';
import HomeReason from './HomeReason/HomeReason.vue';
import HomeFBack from './HomeFBack/HomeFBack.vue';
import HomeActivity from './HomeActivity/HomeActivity.vue';
import HomeTrend from './HomeTrend/HomeTrend.vue';
import HomeMotto from './HomeMotto/HomeMotto.vue';
import HomeIntro from './HomeIntro/HomeIntro.vue';
</script>
<template>
  <div :class="$style.home">
    <home-banner />
    <home-category />
    <home-company />
    <home-trend />
    <home-reason />
    <home-motto />
    <home-intro />
    <home-f-back />
    <home-activity />
  </div>
</template>

<style module scoped lang="scss">
@import './HomePage.module.scss';
</style>

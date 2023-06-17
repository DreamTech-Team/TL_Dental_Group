<script setup lang="ts">
import HomeBanner from './HomeBanner/HomeBanner.vue';
import HomeCategory from './HomeCategory/HomeCategory.vue';
import HomeCompany from './HomeCompany/HomeCompany.vue';
import HomeReason from './HomeReason/HomeReason.vue';
import HomeFBack from './HomeFBack/HomeFBack.vue';
import HomeActivity from './HomeActivity/HomeActivity.vue';
</script>
<template>
  <div :class="$style.home">
    <home-banner />
    <home-category />
    <home-company />
    <div :class="$style.home__trend">
      <div :class="$style['home__trend-title']">
        <h3>SẢN PHẨM BÁN CHẠY</h3>
        <div :class="$style['home__trend-line']"></div>
      </div>
    </div>
    <home-reason />
    <home-f-back />
    <home-activity />
  </div>
</template>

<style module scoped lang="scss">
@import './HomePage.module.scss';
</style>

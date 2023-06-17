<script setup lang="ts">
import ProductBanner from './ProductBanner/ProductBanner.vue';
import ProductCard from './ProductCard/ProductCard.vue';
import ProductCategory from './ProductCategory/ProductCategory.vue';
import ProductNavigation from './ProductNavigation/ProductNavigation.vue';
</script>
<template>
  <div>
    <div :class="$style['product__header']">
      <product-banner :class="$style['product__header-banner']" />
      <product-navigation :class="$style['product__header-navigation']" />
    </div>
    <div :class="$style['product__content']">
      <product-category />
      <div :class="$style['product__content-wrap']">
        <div :class="$style['product__content-sort']"></div>
        <div :class="$style['product__content-container']">
          <product-card :class="$style['product__content-container--card']" />
          <product-card :class="$style['product__content-container--card']" />
          <product-card :class="$style['product__content-container--card']" />
          <product-card :class="$style['product__content-container--card']" />
          <product-card :class="$style['product__content-container--card']" />
        </div>
      </div>
    </div>
  </div>
</template>
s
<style scoped module lang="scss">
@import './ProductPage.module.scss';
</style>

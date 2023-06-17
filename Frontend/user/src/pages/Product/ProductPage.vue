<script setup lang="ts">
import ProductBanner from './ProductBanner/ProductBanner.vue';
import ProductCard from './ProductCard/ProductCard.vue';
import ProductCategory from './ProductCategory/ProductCategory.vue';
import ProductNavigation from './ProductNavigation/ProductNavigation.vue';
import { products } from '../Product/ProductHandle';
import IcSortDown from '@/assets/icons/IcSortDown.svg';
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
        <div :class="$style['product__content-sort']">
          <p :class="$style['product__content-sort--info']">
            Hiển thị <strong>1 - 16</strong> trên <strong>100</strong> kết quả
          </p>
          <div :class="$style['product__content-sort--type']">
            <p>Mới nhất</p>
            <img :src="IcSortDown" alt="sort down" />
          </div>
        </div>
        <div :class="$style['product__content-container']">
          <product-card
            v-for="(item, index) in products"
            :key="index"
            :product="item"
            :class="$style['product__content-container--card']"
          />
        </div>
      </div>
    </div>
  </div>
</template>
s
<style scoped module lang="scss">
@import './ProductPage.module.scss';
</style>

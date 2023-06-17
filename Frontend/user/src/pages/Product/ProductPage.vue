<script setup lang="ts">
import ProductBanner from './ProductBanner/ProductBanner.vue';
import ProductCard from './ProductCard/ProductCard.vue';
import ProductCategory from './ProductCategory/ProductCategory.vue';
import ProductNavigation from './ProductNavigation/ProductNavigation.vue';
const products = [
  {
    nameProduct:
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép) Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép)',
    price: '4.000.000đ',
    description:
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button. - Kẹp…',
    tag: 'Vật liệu chỉnh nha',
    company: 'TL Group'
  },
  {
    nameProduct: 'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép)',
    price: '4.000.000đ',
    description:
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button. - Kẹp…',
    tag: 'Vật liệu chỉnh nha',
    company: 'TL Group'
  },
  {
    nameProduct: 'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép)',
    price: '4.000.000đ',
    description:
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button. - Kẹp…',
    tag: 'Vật liệu chỉnh nha',
    company: 'TL Group'
  },
  {
    nameProduct: 'ABC Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO',
    price: '3.000.000đ',
    description:
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button. - Kẹp…',
    tag: 'Vật liệu chỉnh nha',
    company: 'TL Group'
  },
  {
    nameProduct: 'ADC Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO',
    price: '2.000.000đ',
    description:
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button. - Kẹp…',
    tag: 'Vật liệu chỉnh nha',
    company: 'TL Group'
  }
];
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

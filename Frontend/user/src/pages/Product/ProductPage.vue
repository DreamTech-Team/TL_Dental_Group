<script setup lang="ts">
import ProductBanner from './ProductBanner/ProductBanner.vue';
import ProductCard from './ProductCard/ProductCard.vue';
// import ProductCategory from './ProductCategory/ProductCategory.vue';
import ProductCategory from '@/components/Category/Category.vue';
import ProductNavigation from './ProductNavigation/ProductNavigation.vue';
import ServiceQuality from '@/components/ServiceQuality/ServiceQuality.vue';
import BasePagination from '@/components/Pagination/BasePagination.vue';
import { products } from '../Product/ProductHandle';
import { category } from './ProductCategory/ProductCategory';
import IcSortDown from '@/assets/icons/IcSortDown.svg';
import { ref } from 'vue';

const categories = [
  {
    title: 'Vật liệu chỉnh nha ABC',
    data: [
      {
        name: 'Lò xo chỉnh nha'
      },
      {
        name: 'Kềm chỉnh nha'
      },
      {
        name: 'Thun chỉnh chỉnh nha'
      },
      {
        name: 'Chỉ chỉnh nha'
      },
      {
        name: 'Kẹp gấp mắc cài'
      }
    ]
  },
  {
    title: 'Vật liệu chỉnh nha ADC',
    data: [
      {
        name: 'Lò xo chỉnh nha'
      },
      {
        name: 'Kềm chỉnh nha'
      },
      {
        name: 'Thun chỉnh chỉnh nha'
      },
      {
        name: 'Chỉ chỉnh nha'
      },
      {
        name: 'Kẹp gấp mắc cài'
      }
    ]
  },
  {
    title: 'Vật liệu chỉnh nha XYZ',
    data: [
      {
        name: 'Lò xo chỉnh nha'
      },
      {
        name: 'Kềm chỉnh nha'
      },
      {
        name: 'Thun chỉnh chỉnh nha'
      },
      {
        name: 'Chỉ chỉnh nha'
      },
      {
        name: 'Kẹp gấp mắc cài'
      }
    ]
  }
];

// Define reactive properties
const isDropdownOpen = ref(false);

// Define methods
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

const closeDropdown = () => {
  isDropdownOpen.value = false;
};

// Expose reactive properties and methods
defineExpose({
  isDropdownOpen,
  toggleDropdown,
  closeDropdown
});
</script>
<template>
  <div>
    <div :class="$style['product__header']">
      <product-banner :class="$style['product__header-banner']" />
      <product-navigation :class="$style['product__header-navigation']" />
    </div>
    <div :class="$style['product__content']">
      <product-category />
      <!-- <product-category /> -->
      <div :class="$style['product__content-wrap']">
        <div :class="$style['product__content-sort']">
          <p :class="$style['product__content-sort--info']">
            Hiển thị <strong>1 - 16</strong> trên <strong>100</strong> kết quả
          </p>

          <div :class="$style['product__content-sort--type']" @click="toggleDropdown">
            <p>Mới nhất</p>
            <img :src="IcSortDown" alt="sort down" />
          </div>

          <div
            v-if="isDropdownOpen"
            @click="closeDropdown"
            :class="$style['product__content-sort--content']"
          >
            <!-- Nội dung dropdown -->
            <ul :class="$style['dropdown-list']">
              <li :class="$style['dropdown-item']">Mới nhất</li>
              <li :class="$style['dropdown-item']">Giá tăng dần</li>
              <li :class="$style['dropdown-item']">Giá giảm dần</li>
            </ul>
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
        <div>
          <base-pagination />
        </div>
      </div>
    </div>

    <div>
      <ServiceQuality />
    </div>
  </div>
</template>
s
<style scoped module lang="scss">
@import './ProductPage.module.scss';
</style>

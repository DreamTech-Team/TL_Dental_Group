<script setup lang="ts">
import DCNK2 from '@/assets/imgs/Home/DCNK2.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
</script>
<template>
  <div :class="$style.home__category">
    <h3>DANH MỤC NỔI BẬT</h3>
    <div :class="$style['home__category-list']">
      <div :class="[$style['home__category-item'], $style['category-item1']]">
        <span>DỤNG CỤ CHỈNH NHA ABC</span>
        <div :class="$style['home__category-ctn']">
          <div :class="$style['home__category-img']">
            <img :src="DCNK2" alt="DCNK2" />
          </div>
        </div>
      </div>
      <div :class="[$style['home__category-item'], $style['category-item2']]">
        <span>DỤNG CỤ CHỈNH NHA XYZ</span>
        <div :class="$style['home__category-ctn']">
          <div :class="$style['home__category-img']">
            <img :src="DCNK2" alt="DCNK2" />
          </div>
        </div>
      </div>
      <div :class="[$style['home__category-item'], $style['category-item3']]">
        <span>VẬT LIỆU CHỈNH NHA ABC</span>
        <div :class="$style['home__category-ctn']">
          <div :class="$style['home__category-img']">
            <img :src="DCNK2" alt="DCNK2" />
          </div>
        </div>
      </div>
      <div :class="[$style['home__category-item'], $style['category-item4']]">
        <span>MẪU HÀM TƯ VẤN XYZ</span>
        <div :class="$style['home__category-ctn']">
          <div :class="$style['home__category-img']">
            <img :src="DCNK2" alt="DCNK2" />
          </div>
        </div>
      </div>
      <button :class="$style['home__category-left']">
        <font-awesome-icon :icon="faChevronLeft" :class="$style['home__category-ic']" />
      </button>
      <button :class="$style['home__category-right']">
        <font-awesome-icon :icon="faChevronRight" :class="$style['home__category-ic']" />
      </button>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../HomePage.module.scss';
</style>

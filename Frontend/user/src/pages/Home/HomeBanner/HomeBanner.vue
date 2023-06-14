<template>
  <div :class="$style.home__banner">
    <div :class="$style['home__banner-left']">
      <h2>TL Dental Group</h2>
      <p>Nhà cung cấp thiết bị y tế chính hãng, uy tín hàng đầu Việt Nam.</p>
      <div :class="$style['home__banner-list']">
        <div
          v-for="(item, index) in bannerItems"
          :key="index"
          :class="{ 'home__banner-item': true, active: activeIndex === index }"
          @click="moveLine(index)"
        >
          <img :src="item.src" :alt="item.alt" :width="item.width" :height="item.height" />
        </div>
        <div :class="$style['line']" :style="{ transform: lineTransform }"></div>
      </div>
    </div>
    <div :class="$style['home__banner-right']">
      <div :class="$style['home__banner-radial']">
        <div :class="$style['home__banner-circle']">
          <div :class="$style['home__banner-logo']">
            <img :src="Capgemini" alt="capgemini" width="127" height="30" />
          </div>
          <div :class="$style['home__banner-product']"></div>
        </div>
        <p>Trụ Implant Highness Hàn Quốc</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import Capgemini from '@/assets/imgs/Home/Capgemini.png';
import Yamaha from '@/assets/imgs/Home/Yamaha.png';
import DELL from '@/assets/imgs/Home/DELL.png';
import Biocon from '@/assets/imgs/Home/Biocon.png';

const bannerItems = [
  { src: Capgemini, alt: 'capgemini', width: '127', height: '30' },
  { src: Yamaha, alt: 'yamaha', width: '127', height: '26' },
  { src: Biocon, alt: 'biocon', width: '101', height: '40' },
  { src: DELL, alt: 'dell', width: '82.42', height: '25' }
];

const activeIndex = ref(0);

const lineTransform = computed(() => {
  let selectedItemLeft = 0;
  for (let i = 0; i < activeIndex.value; i++) {
    const itemWidth = Number(bannerItems[i].width) + 32;
    selectedItemLeft += itemWidth;
  }
  selectedItemLeft += (Number(bannerItems[activeIndex.value].width) - 73) / 2;
  return `translateX(${selectedItemLeft}px)`;
});

function moveLine(index: number): void {
  activeIndex.value = index;
}
</script>

<style module scoped lang="scss">
@import '../HomePage.module.scss';
</style>

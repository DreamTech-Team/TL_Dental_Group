<script setup lang="ts">
import IcSortDown from '@/assets/icons/IcSortDown.svg';
import { ref, onMounted, computed, onUnmounted } from 'vue';
import { products } from '../Product/ProductHandle';
import ProductCard from '../Product/ProductCard/ProductCard.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';

const wItem = ref(0);
const tranfX = ref(0);
let resizeListener: () => void;

const widthComputed = computed(() => {
  return wItem.value * products.length + 'px';
});

const widthItemComputed = computed(() => {
  return wItem.value + 'px';
});

const scrollLeft = () => {
  if (tranfX.value < 0) tranfX.value += wItem.value;
};

const scrollRight = () => {
  if (-tranfX.value + wItem.value * 4 < wItem.value * products.length) {
    tranfX.value -= wItem.value;
  } else {
    tranfX.value = 0;
  }
};

onMounted(() => {
  const container = document.getElementById('trend-wrapper');
  if (container) {
    wItem.value = container.offsetWidth / 4;
  }

  resizeListener = function () {
    const container = document.getElementById('trend-wrapper');
    if (container) {
      wItem.value = container.offsetWidth / 4;
      tranfX.value = 0;
    }
  };

  window.addEventListener('resize', resizeListener);
});

onUnmounted(() => {
  window.removeEventListener('resize', resizeListener);
});
</script>
<template>
  <div>
    <div :class="$style.sort">
      <p :class="$style['sort__info']">
        Tìm thấy <strong>100</strong> kết quả với từ khóa là <strong>“kềm”</strong>
      </p>
      <div :class="$style['sort__type']">
        <p>Mới nhất</p>
        <img :src="IcSortDown" alt="sort down" />
      </div>
    </div>
    <div :class="$style['product__container']">
      <product-card v-for="(item, index) in products" :key="index" :product="item" />
    </div>

    <div :class="$style.trend">
      <div :class="$style['trend__title']">
        <div :class="$style['trend__title--linef']"></div>
        <h3>SẢN PHẨM NỔI BẬT</h3>
        <div :class="$style['trend__title--line']"></div>
      </div>
      <div :class="$style['trend__title--ctn']">
        <div :class="$style['trend__title--ctn-wrapper']" id="trend-wrapper">
          <div
            :class="$style['home__trend-list']"
            :style="{ width: widthComputed, transform: 'translateX(' + tranfX + 'px)' }"
          >
            <product-card
              v-for="(item, index) in products"
              :key="index"
              :product="item"
              :class="$style['home__trend-item']"
              :style="{ width: widthItemComputed }"
            />
          </div>
        </div>
        <button :class="$style['trend__title--ctn-left']" @click="scrollLeft">
          <font-awesome-icon :icon="faChevronLeft" :class="$style['trend__title--ctn-ic']" />
        </button>
        <button :class="$style['trend__title--ctn-right']" @click="scrollRight">
          <font-awesome-icon :icon="faChevronRight" :class="$style['trend__title--ctn-ic']" />
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped module lang="scss">
@import '../Search/SearchPage.module.scss';
</style>

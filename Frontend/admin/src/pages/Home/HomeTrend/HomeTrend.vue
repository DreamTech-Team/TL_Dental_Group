<script setup lang="ts">
import { ref, onMounted, computed, onUnmounted } from 'vue';
import ProductCard from '@/components/Card/ProductCard.vue';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import ModalTrend from './ModalTrend.vue';

const isOpen = ref(false);

const products = [
  {
    nameProduct:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép) Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép)',
    price: '4.000.000đ',
    description:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button.',
    tag: 'Vật liệu chỉnh nha',
    company: 'TL Group'
  },
  {
    nameProduct:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép) Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép)',
    price: '4.000.000đ',
    description:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button.',
    tag: 'Vật liệu chỉnh nha',
    company: 'TL Group'
  },
  {
    nameProduct:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép) Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép)',
    price: '4.000.000đ',
    description:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button.',
    tag: 'Vật liệu chỉnh nha',
    company: 'TL Group'
  },
  {
    nameProduct:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép) Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép)',
    price: '4.000.000đ',
    description:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button.',
    tag: 'Vật liệu chỉnh nha',
    company: 'TL Group'
  },
  {
    nameProduct:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép) Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép)',
    price: '4.000.000đ',
    description:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button.',
    tag: 'Vật liệu chỉnh nha',
    company: 'TL Group'
  },
  {
    nameProduct:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép) Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép)',
    price: '4.000.000đ',
    description:
      // eslint-disable-next-line max-len
      'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button.',
    tag: 'Vật liệu chỉnh nha',
    company: 'TL Group'
  }
];

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
  <div :class="$style.home__trend">
    <div :class="$style['home__trend-title']">
      <h3>SẢN PHẨM BÁN CHẠY</h3>
      <div :class="$style['home__trend-line']"></div>
    </div>
    <div :class="$style['home__trend-ctn']">
      <div :class="$style['home__trend-wrapper']" id="trend-wrapper">
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
      <button :class="$style['home__trend-left']" @click="scrollLeft">
        <font-awesome-icon :icon="faChevronLeft" :class="$style['home__trend-ic']" />
      </button>
      <button :class="$style['home__trend-right']" @click="scrollRight">
        <font-awesome-icon :icon="faChevronRight" :class="$style['home__trend-ic']" />
      </button>
    </div>
    <EditBtn style="top: 30px" @click="isOpen = true" />
  </div>
  <ModalTrend v-if="isOpen" @close="isOpen = false" />
</template>

<style module scoped lang="scss">
@import './HomeTrend.module.scss';
</style>
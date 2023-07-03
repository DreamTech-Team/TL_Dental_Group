<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import Capgemini from '@/assets/imgs/Home/Capgemini.png';
import Yamaha from '@/assets/imgs/Home/Yamaha.png';
import DELL from '@/assets/imgs/Home/DELL.png';
import Biocon from '@/assets/imgs/Home/Biocon.png';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import ModalBanner from './ModalBanner.vue';

const isOpen = ref(false);

const content = {
  title: 'TL Dental Group',
  context: 'Nhà cung cấp thiết bị y tế chính hãng, uy tín hàng đầu Việt Nam.'
};

const bannerItems = [
  { src: Capgemini, alt: 'capgemini', width: '127', height: '30' },
  { src: Yamaha, alt: 'yamaha', width: '127', height: '26' },
  { src: Biocon, alt: 'biocon', width: '101', height: '40' },
  { src: DELL, alt: 'dell', width: '82.42', height: '25' }
];
//WEB
const activeIndex = ref(0);
const activeBanner = ref(bannerItems[0]);
const showBannerBg = ref(true);
const lineWidth = ref(0);
let resizeListener: () => void;

//Transform line active
const lineTransform = computed(() => {
  let selectedItemLeft = 0;
  for (let i = 0; i < activeIndex.value; i++) {
    const itemWidth = Number(bannerItems[i].width) + 30;
    selectedItemLeft += itemWidth;
  }
  selectedItemLeft += (Number(bannerItems[activeIndex.value].width) - lineWidth.value) / 2;
  return `translateX(${selectedItemLeft}px)`;
});

const selectedItem = computed(() => {
  return bannerItems[activeIndex.value];
});

//Set background for banner
const bannerBgColor = computed(() => {
  const colors = [
    `radial-gradient(50% 50% at 50% 50%, rgba(135, 255, 126, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`,
    `radial-gradient(50% 50% at 50% 50%, rgba(252, 126, 255, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`,
    `radial-gradient(50% 50% at 50% 50%, rgba(255, 126, 126, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`,
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`
  ];
  return colors[activeIndex.value];
});

//Set 4 ellipse color for web
const elipseColor = computed(() => {
  const elcolors = [
    [
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`
    ],
    [
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`
    ],
    [
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(252, 126, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(183, 255, 126, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`
    ],
    [
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(183, 255, 126, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(252, 126, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`
    ]
  ];
  return elcolors[activeIndex.value];
});

//To do when moveline
const moveLine = (index: number) => {
  activeIndex.value = index;
  activeBanner.value = bannerItems[index];
  showBannerBg.value = false;

  setTimeout(() => {
    showBannerBg.value = true;
  }, 100);
};

onMounted(() => {
  //Find active and set width for line
  const lineActive = document.getElementById('line_active');
  if (lineActive) {
    lineWidth.value = lineActive.offsetWidth;
  }

  //Resize Screen
  resizeListener = function () {
    const lineActive = document.getElementById('line_active');
    if (lineActive) {
      activeIndex.value = 0;
      lineWidth.value = lineActive.offsetWidth;
    }
  };

  window.addEventListener('resize', resizeListener);
});

onUnmounted(() => {
  window.removeEventListener('resize', resizeListener);
});
</script>

<template>
  <div :class="$style.home__banner">
    <div :class="$style['home__banner-left']">
      <h2>{{ content.title }}</h2>
      <p>{{ content.context }}</p>
      <div id="bannerList" :class="$style['home__banner-list']">
        <div
          v-for="(item, index) in bannerItems"
          :key="index"
          :class="[$style['home__banner-item'], { active: activeIndex === index }]"
          @click="moveLine(index)"
        >
          <img :src="item.src" :alt="item.alt" :width="item.width" :height="item.height" />
        </div>
        <div id="line_active" :class="$style['line']" :style="{ transform: lineTransform }"></div>
      </div>
    </div>
    <div :class="$style['home__banner-right']">
      <div
        v-if="showBannerBg"
        :class="$style['home__banner-bg']"
        :style="{ background: bannerBgColor }"
      >
        <div :class="$style['home__banner-radial']">
          <div :class="$style['home__banner-circle']">
            <div :class="$style['home__banner-logo']">
              <img :src="selectedItem.src" :alt="selectedItem.alt" width="127" height="30" />
            </div>
            <div :class="$style['home__banner-product']"></div>
          </div>
          <p>Trụ Implant Highness Hàn Quốc</p>
        </div>
      </div>
    </div>
    <div :class="$style['home__banner-elipse1']" :style="{ background: elipseColor[0] }"></div>
    <div :class="$style['home__banner-elipse2']" :style="{ background: elipseColor[1] }"></div>
    <div :class="$style['home__banner-elipse3']" :style="{ background: elipseColor[2] }"></div>
    <div :class="$style['home__banner-elipse4']" :style="{ background: elipseColor[3] }"></div>
    <EditBtn @click="isOpen = true" />
  </div>
  <ModalBanner
    v-if="isOpen"
    @close="isOpen = false"
    :title="content.title"
    :context="content.context"
  />
</template>

<style module scoped lang="scss">
@import './HomeBanner.module.scss';
</style>

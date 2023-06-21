<script setup lang="ts">
import { ref, nextTick } from 'vue';
// import { defineProps, defineExpose } from 'vue';
import IcDownCategory from '@/assets/icons/IcsortDown.svg';
import { category } from '../Category/Category';

const isAnimationVisible = ref(false);
const selectedItem = ref(-1);

const toggleAnimation = (index: number) => {
  selectedItem.value = index;
  isAnimationVisible.value = !isAnimationVisible.value;
  if (isAnimationVisible.value) {
    nextTick(() => {
      const animationContainer = document.getElementById(`id-${index}`);
      console.log(animationContainer);

      if (animationContainer) {
        animationContainer.scrollIntoView({ behavior: 'smooth' });
      }
    });
  }
};

const idDefine = (index: number) => {
  return `id-${index}`;
};
const isActive = ref(false);

const toggleActive = () => {
  isActive.value = !isActive.value;
};

// Xuất biến và hàm
defineExpose({
  isAnimationVisible,
  toggleAnimation
});
</script>
<template>
  <div :class="$style.category">
    <div :class="$style['category__title']">Danh mục</div>

    <div :class="[$style['category__firstX']]" v-for="(item, index) in category" :key="index">
      <div
        @click="toggleAnimation(index)"
        :class="[
          $style['category__firstX--choose'],
          { [$style['category__firstX--active']]: isAnimationVisible && selectedItem === index }
        ]"
      >
        <p>{{ item.title }}</p>
        <img
          :class="$style['category__firstX--choose-icon']"
          :src="IcDownCategory"
          alt="sort down"
        />
      </div>

      <div
        :id="idDefine(index)"
        :class="[
          $style['category__firstX--animation'],
          {
            [$style['category__firstX--show-animation']]:
              isAnimationVisible && selectedItem === index
          }
        ]"
        ref="animationContainer"
      >
        <div :class="$style['category__second']" v-for="(item1, idx) in item.data" :key="idx">
          {{ item1.name }}
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
// @import '../ProductPage.module.scss';
@import './Category.module.scss';
</style>

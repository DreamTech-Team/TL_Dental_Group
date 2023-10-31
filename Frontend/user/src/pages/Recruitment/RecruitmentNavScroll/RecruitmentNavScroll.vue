<script setup lang="ts">
import { onMounted, ref } from 'vue';

import { recStep, recStepItems } from '../RecruitmentHandle';
import RecruitmentCard from '../RecruitmentCard/RecruitmentCard.vue';

defineProps({
  screenWidth: { type: Boolean, required: true }
});

const itemSeleted = ref(0);
const hiddenElement = ref(false);

//Hàm set animation của element tuyển dụng
const handleScroll = () => {
  const element = document.getElementById('page');
  const rect = element?.getBoundingClientRect();
  const oneItemHeight = Number(element?.offsetHeight) / recStep.length;
  const topParent = Number(rect?.top);

  if (topParent < 0 && screen.width > 739) {
    const index = Math.abs(topParent / oneItemHeight);
    itemSeleted.value = Number(index.toFixed());

    const locationHidden = (recStep.length - 19 / 20) * oneItemHeight;

    hiddenElement.value = -topParent > locationHidden;
  }
};

const handleScrollToTopOfStepRec = () => {
  const element = document.getElementById(`page`);
  element?.scrollIntoView({ behavior: 'smooth', block: 'start', inline: 'nearest' });
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});
</script>

<template>
  <div :class="$style.container__recruit">
    <div
      :class="[
        $style['container__recruit-left'],
        $style['sticky-container'],
        $style[hiddenElement ? 'display-none' : '']
      ]"
    >
      <div :class="$style['container__recruit-left-title']">
        <span>Tuyển dụng TL Dental Group</span>
        <h2>Quy Trình Tuyển Dụng</h2>
      </div>
      <div :class="$style['container__recruit-left-nav']">
        <recruitment-card
          :items="recStep"
          :content="recStepItems"
          :style="'type4'"
          :on-selected="itemSeleted"
          :handleScrollToTopOfStepRec="handleScrollToTopOfStepRec"
        />
      </div>
    </div>
    <div v-if="screenWidth" :class="$style['container__recruit-right']" id="page">
      <recruitment-card :items="recStepItems" :style="'type5'" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentNavScroll.module.scss';
</style>

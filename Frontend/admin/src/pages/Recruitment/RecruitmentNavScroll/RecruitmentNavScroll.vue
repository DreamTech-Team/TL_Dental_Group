<script setup lang="ts">
import { onMounted, ref } from 'vue';

import { recStep, recStepItems } from '../RecruitmentHandle';
import RecruitmentCard from '../RecruitmentCard/RecruitmentCard.vue';

const hiddenElement = ref(false);
const itemSeleted = ref(0);

//Hàm set animation của element tuyển dụng
const handleScroll = () => {
  const element = document.getElementById('page');
  const rect = element?.getBoundingClientRect();
  const oneItemHeight = Number(element?.offsetHeight) / recStep.length;
  const topParent = Number(rect?.top);
  // console.log(rect?.top, element?.offsetHeight);

  if (topParent < 0 && screen.width > 739) {
    const index = Math.abs(topParent / oneItemHeight);
    itemSeleted.value = Number(index.toFixed());
    // console.log(itemSeleted.value);

    const locationHidden = (recStep.length - 19 / 20) * oneItemHeight;

    if (-topParent > locationHidden) hiddenElement.value = true;
    else hiddenElement.value = false;
    // console.log(locationHidden, hiddenElement.value);
  }
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
        />
      </div>
    </div>
    <div :class="$style['container__recruit-right']" id="page">
      <recruitment-card :items="recStepItems" :style="'type5'" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentNavScroll.module.scss';
</style>

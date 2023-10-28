<script setup lang="ts">
import { onMounted, ref } from 'vue';

import { recruitWorkItems } from '../RecruitmentHandle';
import RecruitmentCardWork from '../RecruitmentCardWork/RecruitmentCardWork.vue';

const props = defineProps({ getElementRec: { type: Function, required: true } });

const moreRecruitWorkItems = ref();
const showMore = ref(false);

//Hàm cập nhật item sau khi loading
const showPageCompleted = () => {
  showMore.value = false;

  if (!moreRecruitWorkItems.value) {
    moreRecruitWorkItems.value = [...recruitWorkItems];
  } else recruitWorkItems.forEach((item) => moreRecruitWorkItems.value.push(item));

  // console.log(moreRecruitWorkItems.value, recruitWorkItems);
};

//Hàm loading
const openLoading = () => {
  showMore.value = true;
  setTimeout(showPageCompleted, 3000);
};

onMounted(() => {
  const element = document.getElementById('position-rec');
  props.getElementRec(element);
});
</script>
<template>
  <div :class="$style.container__work" id="position-rec">
    <div :class="$style['container__work-heading']">
      <div :class="$style['container__work-heading-title']">Các Vị Trí Tuyển Dụng</div>
      <div :class="$style['container__work-heading-filter']">
        <input type="text" name="filter" id="" placeholder="Tìm kiếm" />
      </div>
    </div>
    <div :class="$style['container__work-staff']">
      <div
        :class="$style['container__work-staff-item']"
        v-for="(item, index) in recruitWorkItems"
        :key="index"
      >
        <recruitment-card-work :infor="item" />
      </div>
    </div>
    <div :class="[$style['container__work-staff'], $style['animate-bottom']]">
      <div
        :class="$style['container__work-staff-item']"
        v-for="(item, index) in moreRecruitWorkItems"
        :key="index"
      >
        <recruitment-card-work :infor="item" />
      </div>
    </div>
    <div :id="$style.loader" v-if="showMore === true"></div>
    <div :class="$style['container__work-btn']" v-else @click="openLoading">
      <p>Xem thêm</p>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentWork.module.scss';
</style>

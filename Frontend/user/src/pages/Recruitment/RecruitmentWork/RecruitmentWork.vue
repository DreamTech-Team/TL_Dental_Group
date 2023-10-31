<script setup lang="ts">
import { type PropType, watch, ref } from 'vue';

import RecruitmentCardWork from '../RecruitmentCardWork/RecruitmentCardWork.vue';

interface WorkItem {
  id: string;
  title: string;
  typeWork: string;
  time: string;
  location: string;
}

const props = defineProps({
  handleEventGetTextInput: { type: Function, required: true },
  recruitWorkItems: { type: Object as PropType<WorkItem[]>, required: true },
  openLoading: { type: Function, required: true },
  showMore: { type: Boolean, required: true }
});

const searchWork = ref('');

watch(searchWork, (value) => {
  props.handleEventGetTextInput(value);
});
</script>
<template>
  <div :class="$style.container__work" id="position-rec">
    <div :class="$style['container__work-heading']">
      <div :class="$style['container__work-heading-title']">Các Vị Trí Tuyển Dụng</div>
      <div :class="$style['container__work-heading-filter']">
        <input v-model="searchWork" type="text" name="filter" id="" placeholder="Tìm kiếm" />
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
    <div>
      <div :id="$style.loader" v-if="showMore"></div>
      <div :class="$style['container__work-btn']" v-else @click="props.openLoading">
        <p>Xem thêm</p>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentWork.module.scss';
</style>

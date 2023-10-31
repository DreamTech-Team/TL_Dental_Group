<script setup lang="ts">
import { type PropType } from 'vue';
import RecruitmentCard from '../RecruitmentCard/RecruitmentCard.vue';

interface CardElementItem {
  id: string;
  icon: { link: string; style: string };
  title: { content: string; style: string };
  content: { content: string; style: string };
  image: { link: string; style: string };
  type: string;
}

interface MainItem {
  id: string;
  content: string;
  type: string;
}

defineProps({
  contentValueItems: { type: Object as unknown as PropType<CardElementItem[]>, required: true },
  contentValueMainItem: { type: Object as PropType<MainItem>, required: true }
});
</script>
<template>
  <div style="padding: 5rem">
    <div :class="$style.container__value">
      <div :class="$style['container__value-heading']">
        <div :class="$style['container__value-heading-title']">
          <div :class="$style['container__value-heading-title-main']">
            <div :class="$style['container__value-heading-title-main-1']">Giá Trị</div>
            <div :class="$style['container__value-heading-title-main-2']">Cốt Lõi</div>
          </div>
          <div :class="$style['container__value-heading-title-3']">Từ TL Dental Group</div>
        </div>
        <div :class="$style['container__value-heading-content']">
          <p>
            {{ contentValueMainItem }}
          </p>
        </div>
      </div>
      <div :class="$style['container__value-list']">
        <recruitment-card :items="contentValueItems" :style="'type3'" />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentValue.module.scss';
</style>

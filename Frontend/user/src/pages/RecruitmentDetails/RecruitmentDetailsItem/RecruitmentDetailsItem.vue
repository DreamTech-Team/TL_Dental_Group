<script setup lang="ts">
import { type PropType } from 'vue';

type FormartItemDetails = {
  title: string;
  content: string;
};

const props = defineProps({
  items: { type: Object as PropType<FormartItemDetails>, required: true }
});
</script>
<template>
  <div :class="$style['container-children']">
    <div :class="$style['container-children__title']">{{ props.items.title }}</div>
    <div :class="$style['container-children__content']" v-html="props.items.content"></div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentDetailsItem.module.scss';
</style>

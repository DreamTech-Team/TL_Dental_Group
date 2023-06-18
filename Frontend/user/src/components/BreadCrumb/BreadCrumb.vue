<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faHome, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { defineProps } from 'vue';

defineProps({
  tags: {
    type: Array as () => string[],
    required: true
  }
});
</script>
<template>
  <div :class="$style.news__breadcrum">
    <div :class="$style['news__breadcrum-home']">
      <font-awesome-icon :icon="faHome" :class="$style['news__breadcrum-ic']" />
      <span>Home</span>
    </div>
    <div :class="$style['news__breadcrum-tag']" v-for="tag in tags" :key="tag">
      <font-awesome-icon :icon="faChevronRight" :class="$style['news__breadcrum-ic']" />
      <span>{{ tag }}</span>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './BreadCrumb.module.scss';
</style>

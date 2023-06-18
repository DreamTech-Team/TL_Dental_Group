<script setup lang="ts">
import { ref, computed } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';

const isFocus = ref(false);
const focusComputed = computed(() => {
  return isFocus.value ? '#e1f5fd' : '';
});
</script>
<template>
  <form :class="$style['header-search']" :style="{ backgroundColor: focusComputed }">
    <font-awesome-icon :icon="faSearch" :class="$style['header-search__icon']" />
    <input
      type="text"
      name="search"
      placeholder="Tìm kiếm"
      :class="$style['header-search__input']"
      v-on:focus="isFocus = true"
      v-on:blur="isFocus = false"
    />
  </form>
</template>

<style scoped module lang="scss">
@import './HeaderSearch.module.scss';
</style>

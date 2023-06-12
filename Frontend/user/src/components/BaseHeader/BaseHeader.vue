<script setup lang="ts">
import HeaderContact from './HeaderContact.vue';
import HeaderNav from './HeaderNav.vue';
</script>
<template>
  <div :class="$style.header">
    <HeaderContact />
    <HeaderNav />
  </div>
</template>

<style module scoped lang="scss">
@import './BaseHeader.module.scss';
</style>

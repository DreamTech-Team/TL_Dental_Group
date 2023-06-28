<script setup lang="ts">
import { RouterView } from 'vue-router';
import BaseHeader from './components/BaseHeader/BaseHeader.vue';
import BaseSidebar from './components/BaseSidebar/BaseSidebar.vue';
</script>

<template>
  <BaseHeader />

  <div style="width: calc(100% - 70px); margin-left: auto">
    <RouterView />
  </div>

  <BaseSidebar />
</template>

<style scoped></style>

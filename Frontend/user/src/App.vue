<script setup lang="ts">
import { RouterView } from 'vue-router';
import BaseHeader from './components/BaseHeader/BaseHeader.vue';
import BaseFooter from './components/BaseFooter/BaseFooter.vue';
</script>

<template>
  <header>
    <BaseHeader />
  </header>

  <RouterView />

  <footer>
    <BaseFooter />
  </footer>
</template>

<style scoped></style>

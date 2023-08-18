<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router';
import BaseHeader from './components/BaseHeader/BaseHeader.vue';
import BaseSidebar from './components/BaseSidebar/BaseSidebar.vue';
import { onMounted, ref, watch } from 'vue';

const checkLoginPage = ref(false);

watch(useRoute(), (value) => {
  if (value.path.includes('/login')) checkLoginPage.value = true;
  else checkLoginPage.value = false;
});
</script>

<template>
  <BaseHeader v-if="!checkLoginPage" />

  <div v-if="checkLoginPage">
    <RouterView />
  </div>
  <div v-else style="width: calc(100% - 70px); margin-left: auto">
    <RouterView />
  </div>

  <BaseSidebar v-if="!checkLoginPage" />
</template>

<style scoped></style>

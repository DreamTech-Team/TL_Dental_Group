<script setup lang="ts">
import { ref, watch } from 'vue';
import { RouterView } from 'vue-router';
import BaseHeader from './components/BaseHeader/BaseHeader.vue';
import BaseFooter from './components/BaseFooter/BaseFooter.vue';
import ContactMessage from './components/ContactMessage/ContactMessage.vue';
</script>

<template>
  <BaseHeader />

  <RouterView />

  <contact-message />

  <BaseFooter />
</template>

<style scoped lang="scss"></style>

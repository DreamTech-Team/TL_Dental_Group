<script setup lang="ts">
import IconCam from '@/assets/icons/Camera.png';
</script>
<template>
  <button :class="$style.imagebtn">
    <img :src="IconCam" alt="icon" />
  </button>
</template>

<style module scoped lang="scss">
@import './ImageBtn.module.scss';
</style>

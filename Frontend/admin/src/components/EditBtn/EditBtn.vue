<script setup lang="ts">
import IconEdit from '@/assets/icons/Edit.png';
</script>
<template>
  <button :class="$style.editbtn">
    <img :src="IconEdit" alt="icon" />
  </button>
</template>

<style module scoped lang="scss">
@import './EditBtn.module.scss';
</style>

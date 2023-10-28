<script setup lang="ts"></script>
<template>
  <div :class="$style.wrapper">
    <ul :class="$style['loader-list']">
      <li>
        <div :class="[$style['loader-5'], $style['center']]"><span></span></div>
      </li>
    </ul>
  </div>
</template>

<style module scoped lang="scss">
@import './LoadingComponent.module.scss';
</style>

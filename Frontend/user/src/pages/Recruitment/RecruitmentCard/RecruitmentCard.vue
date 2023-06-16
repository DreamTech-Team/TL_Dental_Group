<script setup lang="ts">
import { items } from './RecruitmentHandle';
</script>

<template>
  <div :class="$style['container__poster-block-card-item']" v-for="item in items" :key="item.title">
    <div :class="$style['container__poster-block-card-item-icon']" v-if="item.icon.link !== ''">
      <div
        :class="[
          $style['container__poster-block-card-item-icon-block'],
          $style[`${item.icon.color}`]
        ]"
      >
        <img :src="`${item.icon.link}`" alt="none" />
      </div>
    </div>
    <div :class="$style['container__poster-block-card-item-title']">
      <h3>{{ item.title }}</h3>
    </div>
    <div :class="$style['container__poster-block-card-item-content']">
      <p v-html="item.content"></p>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentCard.module.scss';
</style>

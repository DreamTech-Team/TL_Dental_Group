<script setup lang="ts">
import background from '@/assets/imgs/Policy/bg.png';
import { listPolicy } from './PolicyHandle';

const selectedItem = 0;
</script>
<template>
  <div :class="$style.container">
    <div :class="$style.container__bg">
      <img :src="background" alt="none" />
    </div>
    <div :class="$style.container__content">
      <div :class="$style['container__content-nav']">
        <div :class="$style['container__content-nav-title']"><h3>Chính sách</h3></div>
        <ul>
          <li v-for="(item, index) in listPolicy" :key="index">
            <div
              v-if="index === selectedItem"
              :class="$style[`container__content-nav-item-active`]"
            >
              {{ item.title }}
            </div>
            <div v-else :class="$style[`container__content-nav-item`]">{{ item.title }}</div>
          </li>
        </ul>
      </div>
      <div :class="$style['container__content-section']">
        <div :class="$style['container__content-section-title']">
          {{ listPolicy[selectedItem].title }}
        </div>
        <div
          :class="$style['container__content-section-details']"
          v-html="listPolicy[selectedItem].content"
        ></div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './PolicyPage.module.scss';
</style>

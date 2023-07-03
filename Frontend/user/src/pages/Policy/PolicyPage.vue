<script setup lang="ts">
import background from '@/assets/imgs/Policy/bg.png';
import { listPolicy } from './PolicyHandle';
import { ref } from 'vue';

const selectedItem = ref(0);

const handleSelected = (index: any) => {
  selectedItem.value = Number(index);
  console.log(index);
};
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
          <li v-for="(item, index) in listPolicy" :key="index" @click="handleSelected(index)">
            <div
              :class="[
                $style[index === selectedItem ? `container__content-nav-item-active` : ''],
                $style[`container__content-nav-item`]
              ]"
            >
              {{ item.title }}
            </div>
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

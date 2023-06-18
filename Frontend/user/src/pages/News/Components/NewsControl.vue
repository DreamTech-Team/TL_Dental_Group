<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faSortDown } from '@fortawesome/free-solid-svg-icons';

let isCustomSelectListOpen = false;

const clickHandle = () => {
  isCustomSelectListOpen = !isCustomSelectListOpen;
  console.log('customSelectListDisplay');
};

const closeCustomSelectList = () => {
  isCustomSelectListOpen = false;
};
</script>

<template>
  <div :class="$style.news__control">
    <div :class="$style['news__control-left']">
      <div :class="$style['news__control-heading']">
        <h4>Tin tức nổi bật</h4>
        <div :class="$style['news__control-border']"></div>
        <div :class="$style['news__control-tabs']">
          <div :class="$style['news__tabs-item--active']">Mới nhất</div>
          <div :class="$style['news__tabs-item']">Tên tin tức từ A đến Z</div>
          <div :class="$style['news__tabs-item']">Tên tin tức từ Z đến A</div>
          <div :class="$style['news__tabs-line']"></div>
        </div>
      </div>
    </div>
    <div :class="$style['news__control-right']">
      <div :class="$style['news__control-tag']">
        <h4>TAGS</h4>
        <div :class="$style['custom-select']" v-on:click="clickHandle">
          <div :class="$style['custom-select-item']">Chọn</div>
          <font-awesome-icon :icon="faSortDown" :class="$style['select_ic']" />
          <div :class="$style['custom-select-list']" v-show="isCustomSelectListOpen">
            <ul>
              <li @click="closeCustomSelectList">Tin tức</li>
              <li @click="closeCustomSelectList">Hoạt động</li>
              <li @click="closeCustomSelectList">Hội thảo</li>
              <li @click="closeCustomSelectList">Phỏng vấn</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../NewsPage.module.scss';
</style>

<script setup lang="ts"></script>
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
        <div :class="$style['custom-select']">
          <select>
            <option>Chọn</option>
            <option value="option1">Option 1</option>
            <option value="option2">Option 2</option>
            <option value="option3">Option 3</option>
          </select>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../NewsPage.module.scss';
</style>

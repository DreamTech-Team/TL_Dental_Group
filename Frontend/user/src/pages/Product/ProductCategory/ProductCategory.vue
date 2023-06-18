<script setup lang="ts">
import IcDownCategory from '@/assets/icons/IcSortDown.svg';
</script>
<template>
  <div :class="$style['product__content-category']">
    <div :class="$style['product__content-category--title']">Danh mục</div>
    <div :class="$style['product__content-category--first']">
      <p>Vật liệu chỉnh nha ABC</p>
      <img :src="IcDownCategory" alt="sort down" />
    </div>
    <div>
      <div :class="$style['product__content-category--firstX']">
        <p>Vật liệu chỉnh nha ABC</p>
        <img :src="IcDownCategory" alt="sort down" />
      </div>
      <div :class="$style['product__content-category--second']">Lò xo chỉnh nha</div>
      <div :class="$style['product__content-category--second']">Thun chỉnh nha</div>
      <div :class="$style['product__content-category--second']">Chỉ nha khoa</div>
      <div :class="$style['product__content-category--secondX']">Kẹp gấp mắc cài</div>
      <div :class="$style['product__content-category--second']">Răng sứ các loại</div>
      <div :class="$style['product__content-category--second']">Máy cứng silicon</div>
    </div>
    <div :class="$style['product__content-category--first']">
      <p>Vật liệu chỉnh nha ABC</p>
      <img :src="IcDownCategory" alt="sort down" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../ProductPage.module.scss';
</style>

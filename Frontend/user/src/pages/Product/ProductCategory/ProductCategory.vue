<script setup lang="ts">
import IcDownCategory from '@/assets/icons/IcSortDown.svg';
</script>
<template>
  <div :class="$style.category">
    <!-- <div :class="$style['category__sticky']"></div> -->
    <div :class="$style['category__title']">Danh mục</div>
    <div :class="$style['category__first']">
      <p>Vật liệu chỉnh nha ABC</p>
      <img :src="IcDownCategory" alt="sort down" />
    </div>
    <div>
      <div :class="$style['category__firstX']">
        <p>Vật liệu chỉnh nha ABC</p>
        <img :src="IcDownCategory" alt="sort down" />
      </div>
      <div>
        <div :class="$style['category__second']">Lò xo chỉnh nha</div>
        <div :class="$style['category__second']">Thun chỉnh nha</div>
        <div :class="$style['category__second']">Chỉ nha khoa</div>
        <div :class="$style['category__secondX']">Kẹp gấp mắc cài</div>
        <div :class="$style['category__second']">Răng sứ các loại</div>
        <div :class="$style['category__second']">Máy cứng silicon</div>
      </div>
    </div>
    <div :class="$style['category__first']">
      <p>Vật liệu chỉnh nha ABC</p>
      <img :src="IcDownCategory" alt="sort down" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
// @import '../ProductPage.module.scss';
@import '../ProductCategory/ProductCategory.module.scss';
</style>

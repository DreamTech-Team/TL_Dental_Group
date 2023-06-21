<script setup lang="ts">
import BGItem from '@/assets/imgs/News/NewsItem.png';
</script>
<template>
  <div :class="$style.news__context">
    <div :class="$style['news__context-left']">
      <div :class="$style['news__context-item']">
        <div :class="$style['news__item-left']">
          <img :src="BGItem" alt="BGItem" />
        </div>
        <div :class="$style['news__item-right']">
          <div :class="$style['news__item-date']">
            <p>26/05/2023, 11:00</p>
            <span></span>
          </div>
          <h4>Tác Dụng Và Quy Trình Của Kĩ Thuật Đánh Lún Răng</h4>
          <span
            >Đánh lún răng là kỹ thuật hỗ trợ chỉnh nha nhằm dịch chuyển răng về đúng vị trí mong
            muốn bằng cách sử dụng dây cung hoặc minivis. Hai khí cụ này sẽ tạo ra lực kéo giúp răng
            dịch chuyển đúng hướng. Vậy quy trình đánh lún như thế nào và có tác dụng gì? Bài viết
            dưới đây…</span
          >
          <div :class="$style['news__item-footage']">
            <div :class="$style['news__footage-line']"></div>
            <span>HOẠT ĐỘNG</span>
            <div :class="$style['news__footage-line--mb']"></div>
          </div>
        </div>
      </div>
      <div :class="$style['news__context-item']">
        <div :class="$style['news__item-left']">
          <img :src="BGItem" alt="BGItem" />
        </div>
        <div :class="$style['news__item-right']">
          <div :class="$style['news__item-date']">
            <p>26/05/2023, 11:00</p>
            <span></span>
          </div>
          <h4>Tác Dụng Và Quy Trình Của Kĩ Thuật Đánh Lún Răng</h4>
          <span
            >Đánh lún răng là kỹ thuật hỗ trợ chỉnh nha nhằm dịch chuyển răng về đúng vị trí mong
            muốn bằng cách sử dụng dây cung hoặc minivis. Hai khí cụ này sẽ tạo ra lực kéo giúp răng
            dịch chuyển đúng hướng. Vậy quy trình đánh lún như thế nào và có tác dụng gì? Bài viết
            dưới đây…</span
          >
          <div :class="$style['news__item-footage']">
            <div :class="$style['news__footage-line']"></div>
            <span>HOẠT ĐỘNG</span>
            <div :class="$style['news__footage-line--mb']"></div>
          </div>
        </div>
      </div>
      <div :class="$style['news__context-item']">
        <div :class="$style['news__item-left']">
          <img :src="BGItem" alt="BGItem" />
        </div>
        <div :class="$style['news__item-right']">
          <div :class="$style['news__item-date']">
            <p>26/05/2023, 11:00</p>
            <span></span>
          </div>
          <h4>Tác Dụng Và Quy Trình Của Kĩ Thuật Đánh Lún Răng</h4>
          <span
            >Đánh lún răng là kỹ thuật hỗ trợ chỉnh nha nhằm dịch chuyển răng về đúng vị trí mong
            muốn bằng cách sử dụng dây cung hoặc minivis. Hai khí cụ này sẽ tạo ra lực kéo giúp răng
            dịch chuyển đúng hướng. Vậy quy trình đánh lún như thế nào và có tác dụng gì? Bài viết
            dưới đây…</span
          >
          <div :class="$style['news__item-footage']">
            <div :class="$style['news__footage-line']"></div>
            <span>HOẠT ĐỘNG</span>
            <div :class="$style['news__footage-line--mb']"></div>
          </div>
        </div>
      </div>
    </div>
    <div :class="$style['news__context-right']"></div>
  </div>
</template>

<style module scoped lang="scss">
@import './NewsContext.module.scss';
</style>

<script setup lang="ts">
import { ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import DCNK2 from '@/assets/imgs/Home/DCNK2.png';

const items = ref([
  {
    title: 'DỤNG CỤ CHỈNH NHA ABC',
    src: DCNK2
  },
  {
    title: 'DỤNG CỤ CHỈNH NHA XYZ',
    src: DCNK2
  },
  {
    title: 'VẬT LIỆU CHỈNH NHA ABC',
    src: DCNK2
  },
  {
    title: 'MẪU HÀM TƯ VẤN XYZ1',
    src: DCNK2
  },
  {
    title: 'MẪU HÀM TƯ VẤN XYZ2',
    src: DCNK2
  },
  {
    title: 'MẪU HÀM TƯ VẤN XYZ3',
    src: DCNK2
  },
  {
    title: 'MẪU HÀM TƯ VẤN XYZ4',
    src: DCNK2
  }
]);

const colors = [
  // eslint-disable-next-line max-len
  `linear-gradient(143.33deg, #A300F0 24.48%, rgba(246, 76, 218, 0.547363) 78.16%, rgba(173, 0, 255, 0.74) 103.49%)`,
  // eslint-disable-next-line max-len
  `linear-gradient(119.43deg, #0168C8 28.13%, rgba(69, 108, 245, 0.547363) 80.05%, rgba(0, 194, 255, 0.74) 104.55%)`,
  // eslint-disable-next-line max-len
  `linear-gradient(148.87deg, #0085FF 26.28%, rgba(0, 200, 188, 0.547363) 78.26%, rgba(5, 223, 92, 0.74) 126.07%)`,
  // eslint-disable-next-line max-len
  `linear-gradient(143.33deg, #0168C8 24.48%, rgba(246, 76, 218, 0.547363) 78.16%, rgba(173, 0, 255, 0.74) 103.49%)`
];

function getCategoryColor(index: number): string {
  return colors[index % colors.length];
}
</script>

<template>
  <div :class="$style.about__businessitems">
    <h3>MẶT HÀNG KINH DOANH</h3>

    <div :class="$style['about__businessitems-ctn']">
      <div :class="$style['about__businessitems-wrapper']" id="category-wrapper">
        <div :class="$style['about__businessitems-list']" id="category-list">
          <div
            v-for="(item, index) in items.slice(0, 4)"
            :key="index"
            :class="$style['about__businessitems-item']"
            :style="{ background: getCategoryColor(index), width: widthItemComputed }"
          >
            <span>{{ item.title }}</span>
            <div :class="$style['about__businessitems-ctn']">
              <div :class="$style['about__businessitems-img']">
                <img :src="item.src" :alt="item.title" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutBusinessItems.module.scss';
</style>

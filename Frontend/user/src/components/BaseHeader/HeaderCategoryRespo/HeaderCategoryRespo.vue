<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { RouterLink } from 'vue-router';
import { ref } from 'vue';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { categories } from '../HeaderHandle';

const cate1Hover = ref('');
</script>
<template>
  <div>
    <ul :class="$style['hder-category']">
      <li :class="$style['hder-category__line']"></li>
      <li
        v-for="item1 in categories.slice(0, 6)"
        v-on:mouseenter="cate1Hover = item1.slug"
        v-on:mouseleave="cate1Hover = ''"
        :key="item1.slug"
        :class="$style['hder-category__item']"
      >
        <router-link to="" :class="$style['hder-category__item-link']">
          <p>{{ item1.name }}</p>
          <font-awesome-icon :icon="faChevronRight" size="xs" />
        </router-link>

        <!-- <ul
          :class="[
            $style['hder-category'],
            $style['hder-category-sub'],
            $style[cate1Hover === item1.slug ? 'hder-category-show' : 'hder-category--hidden']
          ]"
          :style="{ maxHeight: heightCate2(item1?.slug) + 'vh' }"
        >
          <li :class="$style['hder-category__line']"></li>
          <li
            v-for="item2 in item1.list.slice(0, 8)"
            :key="item2.slug"
            :class="$style['hder-category__item']"
          >
            <router-link to="" :class="$style['hder-category__item-link']">
              <p>{{ item2.name }}</p>
            </router-link>
          </li>

          <li :class="$style['hder-category__more']" key="xemtatca">
            <router-link to="/sanpham" :class="$style['hder-category__more-link']">
              Xem tất cả
            </router-link>
          </li>
        </ul> -->
      </li>

      <li :class="$style['hder-category__more']" key="xemtatca">
        <router-link to="/sanpham" :class="$style['hder-category__more-link']">
          Xem tất cả
        </router-link>
      </li>
    </ul>
  </div>
</template>

<style module scoped lang="scss">
@import './HeaderCategoryRespo.module.scss';
</style>

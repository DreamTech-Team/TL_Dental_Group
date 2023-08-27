<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { RouterLink } from 'vue-router';
import { computed, ref, toRefs } from 'vue';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { useDataRenderStore, saveActive } from '@/stores/counter';

const { dataRender } = toRefs(useDataRenderStore());
const saveIndexState = saveActive();

const props = defineProps({
  pageHover: String
});

const cate1Hover = ref('');

const heightCate1 = computed(() => {
  return props.pageHover === 'sanpham' ? 100 : 0;
});

const heightCate2 = (slug: string) => {
  return cate1Hover.value === slug ? 100 : 0;
};

const handleGetIndexCate1 = (categoryIndex: number) => {
  saveIndexState.setActiveCategory({ categoryIndex, itemIndex: -1 });
};

const handleGetIndexCate2 = (categoryIndex: number, itemIndex: number) => {
  saveIndexState.setActiveCategory({ categoryIndex: categoryIndex, itemIndex: itemIndex });
};
</script>
<template>
  <div>
    <ul
      :class="[
        $style['header-category'],
        $style[props.pageHover === 'sanpham' ? 'header-category-show' : 'header-category--hidden']
      ]"
      :style="{ maxHeight: heightCate1 + 'vh' }"
    >
      <li :class="$style['header-category__line']"></li>
      <li
        v-for="(item1, idx) in dataRender.slice(0, 6)"
        v-on:mouseenter="cate1Hover = item1.slug"
        v-on:mouseleave="cate1Hover = ''"
        :key="item1.slug"
        :class="$style['header-category__item']"
        @click.stop="handleGetIndexCate1(idx)"
      >
        <router-link
          :to="`/sanpham?slug1=${item1.slug}`"
          :class="$style['header-category__item-link']"
        >
          <p>{{ item1.title }}</p>
          <font-awesome-icon :icon="faChevronRight" size="xs" />
        </router-link>

        <ul
          :class="[
            $style['header-category'],
            $style['header-category-sub'],
            $style[cate1Hover === item1.slug ? 'header-category-show' : 'header-category--hidden']
          ]"
          :style="{ maxHeight: heightCate2(item1?.slug) + 'vh' }"
        >
          <li :class="$style['header-category__line']"></li>
          <li
            v-for="(item2, index) in item1.data.slice(0, 8)"
            :key="item2.slug"
            :class="$style['header-category__item']"
            @click.prevent.stop="handleGetIndexCate2(idx, index)"
          >
            <!-- ?slug1=vat-lieu-chinh-nha11&slug2=kem-chinh-nha-23 -->
            <router-link
              :to="`/sanpham?slug1=${item1.slug}&slug2=${item2.slug}`"
              :class="$style['header-category__item-link']"
            >
              <p>{{ item2.name }}</p>
            </router-link>
          </li>

          <li :class="$style['header-category__more']" key="xemtatca">
            <router-link to="/sanpham" :class="$style['header-category__more-link']">
              Xem tất cả
            </router-link>
          </li>
        </ul>
      </li>

      <li :class="$style['header-category__more']" key="xemtatca">
        <router-link to="/sanpham" :class="$style['header-category__more-link']">
          Xem tất cả
        </router-link>
      </li>
    </ul>
  </div>
</template>

<style module scoped lang="scss">
@import './HeaderCategory.module.scss';
</style>

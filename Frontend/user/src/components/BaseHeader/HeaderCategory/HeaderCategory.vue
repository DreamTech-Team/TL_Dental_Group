<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { RouterLink } from 'vue-router';
import { computed, ref, toRefs, watch } from 'vue';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { useDataRenderStore, saveActive } from '@/stores/counter';

const { dataRender } = toRefs(useDataRenderStore());
const saveIndexState = saveActive();

const props = defineProps({
  pageHover: String
});

const cate1Hover = ref('');
const companyHover = ref('');

const heightCate1 = computed(() => {
  return props.pageHover === 'sanpham' ? 100 : 0;
});

const heightCompany = (slug: string) => {
  return cate1Hover.value === slug ? 100 : 0;
};

const heightCate2 = (slug: string) => {
  return companyHover.value === slug ? 100 : 0;
};

const handleGetIndexCate1 = (categoryIndex: number) => {
  console.log('1');

  saveIndexState.setTypeCategory('cate1Header' + categoryIndex);
  saveIndexState.setActiveCategory(categoryIndex);
  saveIndexState.setActiveCategory2(-1);
  saveIndexState.setActiveCategory3(-1);
};

const handleGetIndexCate2 = (categoryIndex: number, companyIndex: number) => {
  console.log('2');

  saveIndexState.setTypeCategory('cate2Header' + categoryIndex + '' + companyIndex);
  saveIndexState.setActiveCategory(categoryIndex);
  saveIndexState.setActiveCategory2(companyIndex);
  saveIndexState.setActiveCategory3(-1);
};

const handleGetIndexCate3 = (categoryIndex: number, companyIndex: number, indexCate2: number) => {
  console.log('3');

  saveIndexState.setTypeCategory(
    'cate3Header' + categoryIndex + '' + companyIndex + '' + indexCate2
  );
  saveIndexState.setActiveCategory(categoryIndex);
  saveIndexState.setActiveCategory2(companyIndex);
  saveIndexState.setActiveCategory3(indexCate2);
};

const handleResetCate = () => {
  console.log('reset');

  saveIndexState.setTypeCategory('reset');
  saveIndexState.setActiveCategory(-1);
  saveIndexState.setActiveCategory2(-1);
  saveIndexState.setActiveCategory3(-1);
};
</script>
<template>
  <div>
    <!-- Category 1 -->
    <ul
      :class="[
        $style['header-category'],
        $style[props.pageHover === 'sanpham' ? 'header-category-show' : 'header-category--hidden']
      ]"
      :style="{ maxHeight: heightCate1 + 'vh' }"
    >
      <li :class="$style['header-category__line']"></li>
      <li
        v-for="(cate1, idx) in dataRender.slice(0, 6)"
        v-on:mouseenter="cate1Hover = cate1.slug"
        v-on:mouseleave="cate1Hover = ''"
        :key="cate1.slug"
        :class="$style['header-category__item']"
        @click.stop="handleGetIndexCate1(idx)"
      >
        <router-link
          :to="`/sanpham?slug1=${cate1.slug}`"
          :class="$style['header-category__item-link']"
        >
          <p>{{ cate1.title }}</p>
          <font-awesome-icon :icon="faChevronRight" size="xs" />
        </router-link>

        <!-- Company -->
        <ul
          :class="[
            $style['header-category'],
            $style['header-category-sub'],
            $style[cate1Hover === cate1.slug ? 'header-category-show' : 'header-category--hidden']
          ]"
          :style="{ maxHeight: heightCompany(cate1?.slug) + 'vh' }"
        >
          <li :class="$style['header-category__line']"></li>
          <li
            v-for="(company, index) in cate1.company.slice(0, 8)"
            v-on:mouseenter="companyHover = company.slug"
            v-on:mouseleave="companyHover = ''"
            :key="company.slug"
            :class="$style['header-category__item']"
            @click.prevent.stop="handleGetIndexCate2(idx, index)"
          >
            <router-link
              :to="`/sanpham?slug1=${cate1.slug}&slug2=${company.slug}`"
              :class="$style['header-category__item-link']"
            >
              <p>{{ company.name }}</p>
              <font-awesome-icon :icon="faChevronRight" size="xs" />
            </router-link>

            <!-- Category 2 -->
            <ul
              :if="company.cate2.length"
              :class="[
                $style['header-category'],
                $style['header-category-sub'],
                $style[
                  companyHover === company.slug ? 'header-category-show' : 'header-category--hidden'
                ]
              ]"
              :style="{ maxHeight: heightCate2(company?.slug) + 'vh' }"
            >
              <li :class="$style['header-category__line']"></li>
              <li
                v-for="(cate2, index1) in company.cate2.slice(0, 4).filter((e) => e.title !== '')"
                :key="cate2.slug"
                :class="$style['header-category__item']"
                @click.prevent.stop="handleGetIndexCate3(idx, index, index1)"
              >
                <router-link
                  :to="`/sanpham?slug1=${cate1.slug}&slug2=${company.slug}&slug3=${cate2.slug}`"
                  :class="$style['header-category__item-link']"
                >
                  <p>{{ cate2.title }}</p>
                </router-link>
              </li>

              <li
                :class="$style['header-category__more']"
                key="xemtatca"
                @click="handleGetIndexCate2(idx, index)"
              >
                <router-link
                  :to="`/sanpham?slug1=${cate1.slug}&slug2=${company.slug}`"
                  :class="$style['header-category__more-link']"
                >
                  Xem tất cả
                </router-link>
              </li>
            </ul>
          </li>

          <li :class="$style['header-category__more']" key="xemtatca">
            <router-link
              :to="`/sanpham?slug1=${cate1.slug}`"
              :class="$style['header-category__more-link']"
              @click="handleGetIndexCate1(idx)"
            >
              Xem tất cả
            </router-link>
          </li>
        </ul>
      </li>

      <li :class="$style['header-category__more']" key="xemtatca">
        <router-link
          to="/sanpham"
          :class="$style['header-category__more-link']"
          @click.stop="handleResetCate"
        >
          Xem tất cả
        </router-link>
      </li>
    </ul>
  </div>
</template>

<style module scoped lang="scss">
@import './HeaderCategory.module.scss';
</style>

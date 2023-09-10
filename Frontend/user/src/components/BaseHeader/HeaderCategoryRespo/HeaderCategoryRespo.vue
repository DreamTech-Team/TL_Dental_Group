<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { RouterLink } from 'vue-router';
import { ref, toRefs } from 'vue';
import { faChevronDown } from '@fortawesome/free-solid-svg-icons';
import { useDataRenderStore } from '@/stores/counter';

const props = defineProps({
  pageMobile: String,
  currentItem: String,
  hiddenNav: Function
});

const { dataRender } = toRefs(useDataRenderStore());

const cate1Active = ref('none');
const companyActive = ref('none');
</script>
<template>
  <div>
    <!-- Category 1 -->
    <ul
      :class="[
        $style['hder-category'],
        $style[
          props.pageMobile === 'sanpham' && props.currentItem === 'sanpham'
            ? 'hder-category--show'
            : 'hder-category--hidden'
        ]
      ]"
    >
      <li v-for="cate1 in dataRender" :key="cate1.slug" :class="$style['hder-category__item']">
        <router-link
          to=""
          :class="$style['hder-category__item-link']"
          v-on:click="
            (cate1Active = cate1Active === cate1.slug ? 'none' : cate1.slug),
              (companyActive = 'none')
          "
        >
          <p>{{ cate1.title }}</p>
          <font-awesome-icon :icon="faChevronDown" size="xs" />
        </router-link>

        <!-- Company -->
        <ul
          :class="[
            $style['hder-category'],
            $style['hder-category__sub'],
            $style[cate1Active === cate1.slug ? 'hder-category--show' : 'hder-category--hidden']
          ]"
        >
          <li
            v-for="company in cate1.company.slice(0, 4)"
            :key="company.slug"
            :class="$style['hder-category__item']"
            v-on:click="companyActive = companyActive === company.slug ? 'none' : company.slug"
          >
            <router-link to="" :class="$style['hder-category__item-link']">
              <p>{{ company.name }}</p>
              <font-awesome-icon :icon="faChevronDown" size="xs" />
            </router-link>

            <!-- Category 2 -->
            <ul
              :class="[
                $style['hder-category'],
                $style['hder-category__sub2'],
                $style[
                  companyActive === company.slug ? 'hder-category--show' : 'hder-category--hidden'
                ]
              ]"
            >
              <li
                v-for="cate2 in company.cate2.slice(0, 4).filter((e) => e.title !== '')"
                :key="cate2.slug"
                :class="$style['hder-category__item']"
              >
                <router-link
                  :to="`/sanpham?slug1=${cate1.slug}&slug2=${cate2.slug}`"
                  :class="$style['hder-category__item-link']"
                  @click="hiddenNav"
                >
                  <p>{{ cate2.title }}</p>
                </router-link>
              </li>

              <li :class="$style['hder-category__more']" key="xemtatca">
                <router-link
                  to="/sanpham"
                  :class="$style['hder-category__more-link']"
                  @click="hiddenNav"
                >
                  Xem tất cả ...
                </router-link>
              </li>
            </ul>
          </li>

          <li :class="$style['hder-category__more']" key="xemtatca">
            <router-link
              to="/sanpham"
              :class="$style['hder-category__more-link']"
              @click="hiddenNav"
            >
              Xem tất cả ...
            </router-link>
          </li>
        </ul>
      </li>
      <li :class="$style['hder-category__item']">
        <router-link to="/sanpham" :class="$style['hder-category__item-link']" @click="hiddenNav">
          <p>Xem tất cả...</p>
        </router-link>
      </li>
    </ul>
  </div>
</template>

<style module scoped lang="scss">
@import './HeaderCategoryRespo.module.scss';
</style>

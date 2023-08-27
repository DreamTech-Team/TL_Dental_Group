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

const subActive = ref('none');
</script>
<template>
  <div>
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
      <li v-for="item in dataRender" :key="item.slug" :class="$style['hder-category__item']">
        <router-link
          to=""
          :class="$style['hder-category__item-link']"
          v-on:click="subActive = subActive === item.slug ? 'none' : item.slug"
        >
          <p>{{ item.title }}</p>
          <font-awesome-icon :icon="faChevronDown" size="xs" />
        </router-link>

        <ul
          :class="[
            $style['hder-category'],
            $style['hder-category__sub'],
            $style[subActive === item.slug ? 'hder-category--show' : 'hder-category--hidden']
          ]"
        >
          <li
            v-for="subItem in item.data.slice(0, 4)"
            :key="subItem.slug"
            :class="$style['hder-category__item']"
          >
            <router-link
              :to="`/sanpham?slug1=${item.slug}&slug2=${subItem.slug}`"
              :class="$style['hder-category__item-link']"
              @click="hiddenNav"
            >
              <p>{{ subItem.name }}</p>
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

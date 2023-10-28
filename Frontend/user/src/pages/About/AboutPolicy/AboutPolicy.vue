<script setup lang="ts">
import { ref, watch } from 'vue';
import { RouterLink } from 'vue-router';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface AboutPolicy {
  name: string;
  detail: string;
  symbol: string;
  slug: string;
}

const variableChange = ref([]);
const listPolicy = ref<AboutPolicy[]>([]);
const isLoadingPolicy = ref(false);

const { response, isLoading } = useAxios<DataResponse>(
  'get',
  '/policy',
  {},
  {},
  variableChange.value
);

watch(isLoading, () => {
  isLoadingPolicy.value = isLoading.value;
});

watch(response, () => {
  listPolicy.value = response?.value?.data;
});
</script>
<template>
  <div :class="$style.about__policy">
    <h3>CHÍNH SÁCH CÔNG TY</h3>

    <div :class="$style['about__policy-list']" v-if="!isLoadingPolicy">
      <div
        :class="$style['about__policy-items']"
        v-for="(itemPolicy, index) in listPolicy"
        :key="index"
      >
        <router-link :to="'/chinhsach/'" :class="$style['about__policy-items-links']">
          <div :class="$style['about__policy-ic']">
            <img :src="itemPolicy.symbol" alt="" />
          </div>

          <p :class="$style['about__policy-items-links-title']">{{ itemPolicy.name }}</p>

          <div>
            <p :class="$style['about__policy-items-links-more']">Xem chi tiết</p>
            <font-awesome-icon :icon="faChevronRight" :class="$style['about__policy-ic-right']" />
          </div>
        </router-link>
      </div>
    </div>
    <loading-component v-else />
  </div>
</template>

<style module scoped lang="scss">
@import './AboutPolicy.module.scss';
</style>

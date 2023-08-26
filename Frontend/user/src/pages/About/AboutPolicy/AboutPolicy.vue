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
const oneItem = ref(false);

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
  if (listPolicy.value.length === 1) oneItem.value = true;
});
</script>
<template>
  <div :class="$style.about__policy">
    <h3>CHÍNH SÁCH CÔNG TY</h3>

    <div
      :class="$style['about__policy-list']"
      v-if="!isLoadingPolicy"
      :style="{ justifyContent: oneItem ? 'center' : 'space-between' }"
    >
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
          <p :class="$style['about__policy-items-links-content']" v-html="itemPolicy.detail"></p>

          <div>
            <p :class="$style['about__policy-items-links-more']">
              Xem thêm về {{ itemPolicy.name }}
            </p>
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

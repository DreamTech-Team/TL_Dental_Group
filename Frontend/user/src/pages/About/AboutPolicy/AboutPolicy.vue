<script setup lang="ts">
import { ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface AboutPolicy {
  name: string;
  detail: string;
  symbol: string;
}

const variableChange = ref([]);
const listPolicy = ref<AboutPolicy[]>([]);

// Gọi hàm useAxios để lấy response, error, và isLoading
const { response, error, isLoading } = useAxios<DataResponse>(
  'get',
  '/policy',
  {},
  {},
  variableChange.value
);

// Truy xuất giá trị response.value và gán vào responseData
watch(response, () => {
  listPolicy.value = response?.value?.data;
});
</script>
<template>
  <div :class="$style.about__policy">
    <h3>CHÍNH SÁCH CÔNG TY</h3>

    <div :class="$style['about__policy-list']">
      <div
        :class="$style['about__policy-items']"
        v-for="(itemPolicy, index) in listPolicy"
        :key="index"
      >
        <div :class="$style['about__policy-ic']">
          <img :src="itemPolicy.symbol" alt="" />
        </div>

        <p :class="$style['about__policy-items-title']">{{ itemPolicy.name }}</p>
        <p :class="$style['about__policy-items-content']">
          {{ itemPolicy.detail }}
        </p>

        <div>
          <p :class="$style['about__policy-items-more']">Xem thêm về {{ itemPolicy.name }}</p>
          <font-awesome-icon :icon="faChevronRight" :class="$style['about__policy-ic-right']" />
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutPolicy.module.scss';
</style>

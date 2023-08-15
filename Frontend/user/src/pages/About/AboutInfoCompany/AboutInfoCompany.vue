<script setup lang="ts">
import { ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface CompanyInfo {
  content: string;
  image: string;
}

const variableChange = ref([]);
const contentInfoComapny = ref<CompanyInfo[]>([]);
const isLoadingInfo = ref(false);

// Gọi hàm useAxios để lấy response, error, và isLoading
const { response, isLoading } = useAxios<DataResponse>(
  'get',
  '/introduce/company-info',
  {},
  {},
  variableChange.value
);

// Truy xuất giá trị response.value và gán vào responseData
watch(response, () => {
  isLoadingInfo.value = isLoading.value;
  contentInfoComapny.value = response?.value?.data;
});
</script>
<template>
  <div :class="$style.about__infocompany">
    <p :class="$style['about__infocompany-title']">THÔNG TIN CÔNG TY</p>

    <div :class="$style['about__infocompany-content']" v-if="!isLoadingInfo">
      <div
        :class="$style['about__infocompany-content-item']"
        v-for="(company, idx) in contentInfoComapny"
        :key="idx"
      >
        <p v-html="company.content"></p>
        <div>
          <img :src="company.image" alt="" />
        </div>
      </div>
    </div>
    <loading-component v-else />
  </div>
</template>

<style module scoped lang="scss">
@import './AboutInfoCompany.module.scss';
</style>

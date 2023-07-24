<script setup lang="ts">
import { ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface Company {
  id: string;
  name: string;
  logo: string;
  description: string;
  highlight: number;
  slug: string;
  createAt: string;
}

const companies = ref<Company[]>([]);
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/company', {}, {}, deps.value);

watch(response, () => {
  companies.value = response.value?.data;

  if (companies.value) {
    const sortedCompanies = [...companies.value];
    sortedCompanies.sort((a, b) => {
      if (a.highlight === b.highlight) return 0;
      if (a.highlight === 0) return 1;
      if (b.highlight === 0) return -1;
      return a.highlight - b.highlight;
    });

    companies.value = sortedCompanies;
  }
});
</script>

<template>
  <div :class="$style.home__company">
    <h3>NHÃN HÀNG HỢP TÁC</h3>
    <div :class="[$style['home__company-ctn'], 'logos']">
      <div :class="[$style['home__company-list'], 'logos-slide']" ref="companyList">
        <div
          :class="$style['home__company-item']"
          v-for="(company, index) in companies"
          :key="index"
        >
          <img :src="company.logo" :alt="company.name" />
        </div>
      </div>
      <div :class="[$style['home__company-list'], 'logos-slide']" ref="companyList">
        <div
          :class="$style['home__company-item']"
          v-for="(company, index) in companies"
          :key="index"
        >
          <img :src="company.logo" :alt="company.name" />
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './HomeCompany.module.scss';
</style>

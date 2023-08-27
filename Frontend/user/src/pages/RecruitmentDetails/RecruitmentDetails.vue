<script setup lang="ts">
import { useRoute } from 'vue-router';

import logo from '@/assets/imgs/logo.png';
import RecruitmentDetailsItem from './RecruitmentDetailsItem/RecruitmentDetailsItem.vue';
import RecruitmentCard from '../Recruitment/RecruitmentCard/RecruitmentCard.vue';
import { data } from './RecruitmentDetailsHandle';
import { ic_bag, ic_hourglass, ic_location } from '@/assets/imgs/Recruitment/RecruitmentImgs';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { ref, watch } from 'vue';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

const typeRecuit = ref([
  {
    icon: { link: ic_location, style: 'type6' },
    title: { content: '', style: 'type6' },
    content: { content: '', style: '' },
    image: { link: '', style: '' }
  },
  {
    icon: { link: ic_bag, style: 'type6' },
    title: { content: '', style: 'type6' },
    content: { content: '', style: '' },
    image: { link: '', style: '' }
  },
  {
    icon: { link: ic_hourglass, style: 'type6' },
    title: { content: '', style: 'type6' },
    content: { content: '', style: '' },
    image: { link: '', style: '' }
  }
]);

const contentOrganite = [
  'Cơ hội thăng tiến dựa trên năng lực của bạn',
  'Phần thưởng dựa trên năng suất lao động',
  'Các chuyến du lịch hàng năm cùng công ty',
  'Kiểm tra sức khoẻ hàng năm',
  'Hỗ trợ bảo hiểm sức khỏe'
];

const linkCurrent = useRoute();
const descriptionRec = ref();
const paramAxios = ref();
const isLoading = ref(false);

const getRecruitmentDetails = useAxios<DataResponse>(
  'get',
  `/recruitment/${linkCurrent.path.split('/')[2]}`,
  {},
  {},
  paramAxios.value
);

watch(getRecruitmentDetails.isLoading, (value) => {
  isLoading.value = value;
  console.log(value);
});

watch(getRecruitmentDetails.error, (value) => {
  console.log(value);
});

watch(getRecruitmentDetails.response, (value) => {
  const tmp = value?.data;

  typeRecuit.value[0].title = { content: tmp.location, style: 'type6' };
  typeRecuit.value[1].title = { content: tmp.position, style: 'type6' };
  typeRecuit.value[2].title = { content: tmp.working_form, style: 'type6' };

  // console.log(tmp, typeRecuit.value);

  descriptionRec.value = tmp.description;
});
</script>
<template>
  <LoadingComponent v-if="isLoading" />
  <div v-else :class="$style.container">
    <div :class="$style.container__heading">
      <div :class="$style['container__heading-logo']"><img :src="logo" alt="none" /></div>
      <div :class="$style['container__heading-title']">
        <div :class="$style['container__heading-title-rec']">
          <p>TUYỂN DỤNG</p>
        </div>
        <div :class="$style['container__heading-title-name']"><p>DENTAL GROUP</p></div>
      </div>
    </div>
    <div :class="$style.container__content">
      <div :class="$style['container__content-left']">
        <div :class="$style['container__content-left-title']">Đãi ngộ hấp dẫn</div>
        <ul>
          <li v-for="(item, index) in contentOrganite" :key="index">{{ item }}</li>
        </ul>
      </div>
      <div :class="$style['container__content-right']">
        <div :class="$style['container__content-right-title']">{{ data.title }}</div>
        <div :class="$style['container__content-right-type']">
          <recruitment-card :items="typeRecuit" :style="'type6'" />
        </div>
        <div :class="$style['container__content-right-content']" v-html="descriptionRec"></div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentDetails.module.scss';
</style>

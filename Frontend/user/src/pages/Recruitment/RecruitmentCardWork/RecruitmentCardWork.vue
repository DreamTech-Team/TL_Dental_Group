<script setup lang="ts">
import RecruitmentCard from '.././RecruitmentCard/RecruitmentCard.vue';
import { ic_bag, ic_hourglass, ic_location } from '@/assets/imgs/Recruitment/RecruitmentImgs';
const props = defineProps({
  infor: Object
});

// const locationItem = [
//   {
//     icon: { link: ic_location, style: 'type6' },
//     title: { content: props.infor.location, style: 'type6' },
//     content: { content: '', style: '' },
//     image: { link: '', style: '' }
//   }
// ];

// const contentRecruit = [
//   {
//     icon: { link: ic_hourglass, style: 'type6' },
//     title: { content: '', style: 'type6' },
//     content: { content: '', style: '' },
//     image: { link: '', style: '' }
//   },
//   {
//     icon: { link: ic_bag, style: 'type6' },
//     title: { content: '', style: 'type6' },
//     content: { content: '', style: '' },
//     image: { link: '', style: '' }
//   }
// ];
</script>

<template>
  <div>
    <div class="">
      <div class="">VHG – Tuyển Thực Tập Sinh Marketing</div>
      <div class="">
        <recruitment-card
          :items="[
            {
              icon: { link: ic_location, style: 'type6' },
              title: { content: props.infor.location, style: 'type6' },
              content: { content: '', style: '' },
              image: { link: '', style: '' }
            }
          ]"
          :style="'type6'"
        />
      </div>
    </div>
    <div class="">
      <div class=""><recruitment-card :items="locationItem" :style="'type6'" /></div>
      <div class="">
        <p>Ứng tuyển ngay</p>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentCardWork.module.scss';
</style>

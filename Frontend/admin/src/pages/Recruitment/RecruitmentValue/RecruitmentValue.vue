<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import RecruitmentCard from '../RecruitmentCard/RecruitmentCard.vue';

const props = defineProps({
  handleEditValue: { type: Function, required: true },
  contentValueItems: { type: Object, required: true }
});

const valueItems = ref();

watch(
  () => props.contentValueItems,
  (value) => {
    valueItems.value = value;
    console.log(value);
  }
);

onMounted(() => {
  valueItems.value = props.contentValueItems;
  console.log(props.contentValueItems);
});
</script>
<template>
  <div :class="$style.container__value">
    <div :class="$style['block-btn-edit']">
      <div :class="[$style['btn-edit-2'], $style['btn-edit']]" @click="props.handleEditValue">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
          <g id="SVGRepo_iconCarrier">
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="m3.99 16.854-1.314 3.504a.75.75 0 0 0 .966.965l3.503-1.314a3 3 0 0 0 1.068-.687L18.36 9.175s-.354-1.061-1.414-2.122c-1.06-1.06-2.122-1.414-2.122-1.414L4.677 15.786a3 3 0 0 0-.687 1.068zm12.249-12.63 1.383-1.383c.248-.248.579-.406.925-.348.487.08 1.232.322 1.934 1.025.703.703.945 1.447 1.025 1.934.058.346-.1.677-.348.925L19.774 7.76s-.353-1.06-1.414-2.12c-1.06-1.062-2.121-1.415-2.121-1.415z"
              fill="#000000"
            ></path>
          </g>
        </svg>
      </div>
    </div>
    <div :class="$style['container__value-heading']">
      <div :class="$style['container__value-heading-title']">
        <div :class="$style['container__value-heading-title-main']">
          <div :class="$style['container__value-heading-title-main-1']">Giá Trị</div>
          <div :class="$style['container__value-heading-title-main-2']">Cốt Lõi</div>
        </div>
        <div :class="$style['container__value-heading-title-3']">Từ TL Dental Group</div>
      </div>
      <div :class="$style['container__value-heading-content']">
        <p>
          Tài sản đầu tiên là Sức khỏe. Chúng ta không chỉ xây dựng thương hiệu cá nhân, mà chúng ta
          xây dựng đội ngũ và đội ngũ xây dựng Thương hiệu
        </p>
      </div>
    </div>
    <div :class="$style['container__value-list']">
      <recruitment-card :items="contentValueItems" :style="'type3'" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentValue.module.scss';
</style>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface AboutMotto {
  title: string;
  content: string;
  image: string;
}

const variableChange = ref([]);
const mottoItems = ref<AboutMotto[]>([]);
const move = ref(0);
const isOneItem = ref(false);
const isDisableLeft = ref(false);
const isDisableRight = ref(false);
const isLoadingMotto = ref(false);

const { response, isLoading } = useAxios<DataResponse>(
  'get',
  '/introduce/section1',
  {},
  {},
  variableChange.value
);

watch(isLoading, () => {
  isLoadingMotto.value = isLoading.value;
});

watch(response, () => {
  mottoItems.value = response?.value?.data;

  // Xử lí trường hợp có 1 hoặc 2 item
  if (mottoItems.value.length === 1) {
    isOneItem.value = true;
    isDisableRight.value = true;
    isDisableLeft.value = true;
  } else if (mottoItems.value.length === 2) {
    isDisableRight.value = true;
  }
});

// Xử lí Click thì các items di chuyển sang trái
const handleClickLeft = () => {
  const widthItem = document.getElementById('1');

  if (widthItem) {
    isDisableRight.value = false;
    if (move.value === 0) (move.value = widthItem.offsetWidth + 150), (isDisableLeft.value = true);
    else {
      move.value += widthItem.offsetWidth + 150;
    }
  }
};

// Xử lí Click thì các items di chuyển sang phải
const handleClickRight = () => {
  isDisableLeft.value = false;
  const widthItem = document.getElementById('1');

  if (widthItem) {
    if (move.value === (3 - mottoItems.value.length) * (widthItem.offsetWidth + 150))
      (move.value -= widthItem.offsetWidth + 150), (isDisableRight.value = true);
    else {
      move.value -= widthItem.offsetWidth + 150;
    }
  }
};
</script>

<template>
  <div :class="$style.about__motto">
    <p :class="$style['about__motto-title']">PHƯƠNG CHÂM - GIÁ TRỊ</p>

    <div>
      <div
        :class="$style['about__motto-slider']"
        id="motto-list"
        :style="{
          transform: 'translateX' + '(' + move + 'px' + ')',
          justifyContent: isOneItem ? 'center' : ''
        }"
        v-if="!isLoadingMotto"
      >
        <div
          :class="$style['about__motto-slider-item']"
          v-for="(item, index) in mottoItems"
          :key="index"
          :id="index.toString()"
        >
          <img :src="item.image" alt="" />

          <p :class="$style['about__motto-slider-item-title']">{{ item.title }}</p>

          <p :class="$style['about__motto-slider-item-content']">
            {{ item.content }}
          </p>
        </div>
      </div>
      <loading-component v-else />

      <div :class="$style['about__motto-curtain']">
        <div :class="$style['about__motto-curtain-left']"></div>

        <div :class="$style['about__motto-curtain-button']">
          <button
            :class="$style['about__motto-curtain-button-left']"
            @click="handleClickLeft"
            :disabled="isDisableLeft"
          >
            <font-awesome-icon
              :icon="faChevronLeft"
              :class="$style['about__motto-curtain-button-ic']"
            />
          </button>
          <button
            :class="$style['about__motto-curtain-button-right']"
            @click="handleClickRight"
            :disabled="isDisableRight"
          >
            <font-awesome-icon
              :icon="faChevronRight"
              :class="$style['about__motto-curtain-button-ic']"
            />
          </button>
        </div>

        <div :class="$style['about__motto-curtain-right']"></div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutMotto.module.scss';
</style>

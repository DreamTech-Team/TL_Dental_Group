<script setup lang="ts">
import Intro from '@/assets/imgs/About/Intro.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faRotate } from '@fortawesome/free-solid-svg-icons';
import { Ref, ref, onMounted, watch } from 'vue';
import Croppie from 'croppie';
import 'croppie/croppie.css';
import CropImage from '@/components/CropImage/CropImage.vue';

const imageFile: Ref<string | null> = ref(null);
const isCrop = ref(false);
const isOpenInput = ref(false);

const handleCroppedImage = (result: string) => {
  imageFile.value = result;
};
</script>
<template>
  <div :class="$style.about__header">
    <div :class="$style['about__header-introduce']">
      <div v-if="imageFile" :class="$style['about__header-introduce-img']">
        <img :src="imageFile" alt="" />
      </div>
      <div :class="$style['about__header-introduce-img']" v-else>
        <img :src="Intro" alt="" />
      </div>

      <div :class="$style['about__header-introduce-bg']">
        <button
          :class="$style['about__header-introduce-bg-button']"
          @click="isOpenInput = !isOpenInput"
        >
          <font-awesome-icon
            :icon="faRotate"
            :class="$style['about__header-introduce-bg-button-ic']"
          />
          <span>Đổi hình ảnh</span>
        </button>
      </div>

      <p>Giới Thiệu</p>
    </div>
  </div>

  <crop-image
    :height="468"
    :width="1440"
    :check="isOpenInput"
    v-show="isCrop"
    @close="isCrop = false"
    @open="isCrop = true"
    @crop="handleCroppedImage"
  />
</template>

<style module scoped lang="scss">
@import './AboutHeader.module.scss';
</style>

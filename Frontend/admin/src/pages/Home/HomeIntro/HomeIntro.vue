<script setup lang="ts">
import { ref } from 'vue';
import CEO from '@/assets/imgs/Home/CEO.png';
import Logo from '@/assets/imgs/logo_nobg.png';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
import ModalIntro from './ModalIntro.vue';
import ModalCam from './CamIntro.vue';

const content = ref({
  title: `Lời ngỏ từ <strong>CEO NGUYEN HOANG</strong>`,
  context: `<span
            >"I used to have a bunch of different tools I had to pay for, with Circle you get
            everything in one bundle."</span
          >
          <br />
          <br />
          <br />
          <strong>Michel Dedrick</strong>
          <br />
          <span>Senior Conversion Optimizer</span>`,
  image: CEO
});

const isOpen = ref(false);
const isOpenCam = ref(false);
</script>
<template>
  <div :class="$style.home__intro">
    <div :class="$style['home__intro-left']">
      <h3 v-html="content.title"></h3>
      <button :class="$style['home__intro-btn']">XEM CHI TIẾT</button>
    </div>
    <div :class="$style['home__intro-center']">
      <img :src="content.image" alt="ceo" />
      <CamBtn @click="isOpenCam = true" />
      <div :class="$style['home__intro-description']">
        <div :class="$style['home__intro-logo']">
          <img :src="Logo" alt="logo" />
          <h3>Group</h3>
        </div>
        <div :class="$style['home__intro-speach']" v-html="content.context"></div>
      </div>
    </div>
    <div :class="$style['home__intro-right']"></div>
    <EditBtn style="top: 90px" @click="isOpen = true" />
  </div>
  <ModalIntro
    v-if="isOpen"
    @close="isOpen = false"
    :title="content.title"
    :tags="content.context"
  />
  <ModalCam v-if="isOpenCam" :image="content.image" @close="isOpenCam = false" />
</template>

<style module scoped lang="scss">
@import './HomeIntro.module.scss';
</style>

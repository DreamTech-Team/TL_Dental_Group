<script setup lang="ts">
import { ref } from 'vue';
import Meeting from '@/assets/imgs/Home/Meeting.png';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
import ModalMotto from './ModalMotto.vue';
import ModalCam from './CamMotto.vue';

const isOpen = ref(false);
const isOpenCam = ref(false);

const content = ref({
  title: 'SỨC KHỎE LÀ CHÂN ÁI',
  context: `Lấy uy tín khách hàng là hàng đầu. <br />
        <br />
        Sản phẩm cho mọi người. An toàn và chất lượng. Trao sản phẩm và bảo hành trọn đời. <br />
        Sản phẩm cho mọi người. An toàn và chất lượng. Trao sản phẩm và bảo hành trọn đời. <br />
        Sản phẩm cho mọi người. An toàn và chất lượng. Trao sản phẩm và bảo hành trọn đời. Sản
        người. An toàn và chất lượng. <br />
        <br />
        Trao sản phẩm và bảo hành trọn đời. Lấy uy tín khách hàng <br />
        hàng đầu. Sản phẩm cho mọi người. An toàn và chất lượng. 
        Trao sản phẩm và bảo hành trọn đời.`,
  image: Meeting
});
</script>
<template>
  <div :class="$style.home__motto">
    <div :class="$style['home__motto-left']">
      <h4>SỨC KHỎE LÀ CHÂN ÁI</h4>
      <span v-html="content.context"></span>
      <EditBtn style="right: 50px" @click="isOpen = true" />
    </div>
    <div :class="$style['home__motto-right']">
      <h4 :class="$style['home__motto-title']">PHƯƠNG CHÂM</h4>
      <img :src="content.image" alt="meeting" />
      <CamBtn @click="isOpenCam = true" />
    </div>
  </div>
  <ModalMotto
    v-if="isOpen"
    @close="isOpen = false"
    :title="content.title"
    :tags="content.context"
  />
  <ModalCam v-if="isOpenCam" :image="content.image" @close="isOpenCam = false" />
</template>

<style module scoped lang="scss">
@import './HomeMotto.module.scss';
</style>

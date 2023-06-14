<script setup lang="ts">
import Capgemini from '@/assets/imgs/Home/Capgemini.png';
import Yamaha from '@/assets/imgs/Home/Yamaha.png';
import DELL from '@/assets/imgs/Home/DELL.png';
import Biocon from '@/assets/imgs/Home/Biocon.png';
</script>
<template>
  <div :class="$style.home__company">
    <h3>NHÃN HÀNG HỢP TÁC</h3>
    <div :class="$style['home__company-list']">
      <div :class="$style['home__company-item']">
        <img :src="Capgemini" alt="capgemini" />
      </div>
      <div :class="$style['home__company-item']">
        <img :src="Yamaha" alt="yamaha" />
      </div>
      <div :class="$style['home__company-item']">
        <img :src="Biocon" alt="biocon" />
      </div>
      <div :class="$style['home__company-item']">
        <img :src="DELL" alt="dell" />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../HomePage.module.scss';
</style>

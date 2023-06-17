<script setup lang="ts">
import Facility from '@/assets/imgs/About/Facility.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
</script>
<template>
  <div :class="$style.about__facility">
    <h3>CƠ SỞ CỦA CÔNG TY</h3>

    <div :class="$style['about__facility-wrap']">
      <div :class="$style['about__facility-left']">
        <h4>Cơ sở TP HCM</h4>

        <div>
          <font-awesome-icon :icon="faChevronRight" :class="$style['about__facility-ic']" />
          <p>bootdey Technologies Inc. 2694 Queen City Rainbow Drive Florida 99161</p>
        </div>

        <div>
          <font-awesome-icon :icon="faChevronRight" :class="$style['about__facility-ic']" />
          <p>+01 234 567 89</p>
        </div>

        <div>
          <font-awesome-icon :icon="faChevronRight" :class="$style['about__facility-ic']" />
          <p>abc@gmail.com</p>
        </div>

        <div>
          <font-awesome-icon :icon="faChevronRight" :class="$style['about__facility-ic']" />
          <p>https://www.facebook.com/BiomeawTLG</p>
        </div>

        <button :class="$style['about__facility-button']">
          <font-awesome-icon :icon="faChevronRight" :class="$style['about__facility-button-ic']" />
          <p>Đường đi</p>
        </button>
      </div>
      <img :src="Facility" alt="" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../AboutPage.module.scss';
</style>

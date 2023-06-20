<script setup lang="ts">
import Facility from '@/assets/imgs/About/Facility.png';
import Location from '@/assets/imgs/About/Location.png';
import Telephone from '@/assets/imgs/About/Telephone.png';
import Message from '@/assets/imgs/About/Message.png';
import Facebook from '@/assets/imgs/About/Facebook.png';
import Signpost from '@/assets/imgs/About/Signpost.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
</script>
<template>
  <div :class="$style.about__facility">
    <div :class="$style['about__facility-wrap']">
      <div :class="$style['about__facility-left']">
        <h4>Cơ sở tại TP Hồ Chí Minh</h4>

        <div>
          <img :src="Location" :class="$style['about__facility-ic']" />
          <p>bootdey Technologies Inc. 2694 Queen City Rainbow Drive Florida 99161</p>
        </div>

        <div>
          <img :src="Telephone" :class="$style['about__facility-ic']" />
          <p>+01 234 567 89</p>
        </div>

        <div>
          <img :src="Message" :class="$style['about__facility-ic']" />
          <p>abc@gmail.com</p>
        </div>

        <div>
          <img :src="Facebook" :class="$style['about__facility-ic']" />
          <a href="https://www.facebook.com/BiomeawTLG"> https://www.facebook.com/BiomeawTLG </a>
        </div>

        <button :class="$style['about__facility-button']">
          <img :src="Signpost" :class="$style['about__facility-button-ic']" />
          <p>Đường đi</p>
        </button>
      </div>
      <img :src="Facility" alt="" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ContactFacility.module.scss';
</style>

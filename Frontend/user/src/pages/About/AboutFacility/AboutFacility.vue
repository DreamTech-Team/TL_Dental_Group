<script setup lang="ts">
import { toRefs } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faDiamondTurnRight } from '@fortawesome/free-solid-svg-icons';
import Location from '@/assets/imgs/About/Location.png';
import Telephone from '@/assets/imgs/About/Telephone.png';
import Message from '@/assets/imgs/About/Message.png';
import Facebook from '@/assets/imgs/About/Facebook.png';
import { saveDataContact } from '@/stores/counter';

const { dataFacility, dataContact } = toRefs(saveDataContact());
</script>
<template>
  <div :class="$style.about__facility">
    <h3>CƠ SỞ CỦA CÔNG TY</h3>

    <div :class="$style['about__facility-wrap']">
      <div :class="$style['about__facility-left']">
        <h4>Cơ sở TP HCM</h4>

        <div>
          <img :src="Location" :class="$style['about__facility-ic']" />
          <p>{{ dataFacility.address }}</p>
        </div>

        <div>
          <img :src="Telephone" :class="$style['about__facility-ic']" />
          <p>{{ dataFacility.hotline }}</p>
        </div>

        <div>
          <img :src="Message" :class="$style['about__facility-ic']" />
          <p>{{ dataContact.email.content }}</p>
        </div>

        <div>
          <img :src="Facebook" :class="$style['about__facility-ic']" />
          <a :href="dataContact.facebook.content" target="_blank">{{
            dataContact.facebook.content
          }}</a>
        </div>

        <button :class="$style['about__facility-button']">
          <a :href="dataFacility.mapLink" target="_blank">
            <font-awesome-icon
              :icon="faDiamondTurnRight"
              :class="$style['about__facility-button-ic']"
            />
            <p>Đường đi</p>
          </a>
        </button>
      </div>
      <img :src="dataFacility.image" alt="" :class="$style['about__facility-img']" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutFacility.module.scss';
</style>

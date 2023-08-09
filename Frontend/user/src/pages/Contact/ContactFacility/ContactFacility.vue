<script setup lang="ts">
import { ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faDiamondTurnRight } from '@fortawesome/free-solid-svg-icons';
import Facility from '@/assets/imgs/About/Facility.png';
import Location from '@/assets/imgs/About/Location.png';
import Telephone from '@/assets/imgs/About/Telephone.png';
import Message from '@/assets/imgs/About/Message.png';
import Facebook from '@/assets/imgs/About/Facebook.png';

interface Info {
  address: string;
  hotline: string;
  mapLink: string;
  image: string;
}

interface Contact {
  email: {
    content: string;
  };
  facebook: {
    content: string;
  };
  zalo: {
    content: string;
  };
}

const props = defineProps({
  dataFacility: {
    type: Object,
    required: true
  },
  dataContact: {
    type: Object,
    required: true
  }
});

const dataContactRender = ref<Contact>({ ...(props.dataContact as Contact) });
const dataFacilityRender = ref<Info>({ ...(props.dataFacility as Info) });
</script>
<template>
  <div :class="$style.contact__facility">
    <h3>CƠ SỞ CỦA CÔNG TY</h3>

    <div :class="$style['contact__facility-wrap']">
      <div :class="$style['contact__facility-left']">
        <h4>Cơ sở TP HCM</h4>

        <div>
          <img :src="Location" :class="$style['contact__facility-ic']" />
          <p>{{ dataFacilityRender.address }}</p>
        </div>

        <div>
          <img :src="Telephone" :class="$style['contact__facility-ic']" />
          <p>{{ dataFacilityRender.hotline }}</p>
        </div>

        <div>
          <img :src="Message" :class="$style['contact__facility-ic']" />
          <p>{{ dataContactRender.email.content }}</p>
        </div>

        <div>
          <img :src="Facebook" :class="$style['contact__facility-ic']" />
          <a :href="dataContactRender.facebook.content" target="_blank">{{
            dataContactRender.facebook.content
          }}</a>
        </div>

        <button :class="$style['contact__facility-button']">
          <a :href="dataFacilityRender.mapLink" target="_blank">
            <font-awesome-icon
              :icon="faDiamondTurnRight"
              :class="$style['contact__facility-button-ic']"
            />
            <p>Đường đi</p>
          </a>
        </button>
      </div>
      <img :src="dataFacilityRender.image" alt="" :class="$style['contact__facility-img']" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ContactFacility.module.scss';
</style>

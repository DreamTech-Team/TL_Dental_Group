<script setup lang="ts">
import { ref } from 'vue';

interface Info {
  address: string;
  hotline: string;
  mapLink: string;
  image: string;
}

const props = defineProps({
  dataFacility: {
    type: Object,
    required: true
  }
});

const dataFacilityRender = ref<Info>({ ...(props.dataFacility as Info) });
</script>

<template>
  <div :class="$style.contact__maps">
    <iframe
      :class="$style['contact__maps-frame']"
      src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.2723424634596!2d106.69234569999999!3d10.790441099999997!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x317528ccc64cc6bd%3A0x83657910a19e5812!2zNDUgVGjhuqFjaCBUaOG7iyBUaGFuaCwgUGjGsOG7nW5nIFTDom4gxJDhu4tuaCwgUXXhuq1uIDEsIFRow6BuaCBwaOG7kSBI4buTIENow60gTWluaA!5e0!3m2!1svi!2s!4v1691487943765!5m2!1svi!2s"
      allowfullscreen="false"
      loading="lazy"
      referrerpolicy="no-referrer-when-downgrade"
    ></iframe>
  </div>
</template>

<style module scoped lang="scss">
@import './ContactMaps.module.scss';
</style>

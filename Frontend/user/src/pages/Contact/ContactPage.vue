<script setup lang="ts">
import ContactForm from './ContactForm/ContactForm.vue';
import ContactMaps from './ContactMaps/ContactMaps.vue';
import ContactFacility from './ContactFacility/ContactFacility.vue';
import ContactActivity from './ContactActivity/ContactActivity.vue';
import ContactMessage from './ContactMessage/ContactMessage.vue';

import MessageV2 from '@/assets/imgs/Contact/MessageV2.png';
import Close from '@/assets/imgs/Contact/Close.png';
import { ref } from 'vue';

import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';

const pathContact = 'lienhe';

const isOpen = ref(false);
</script>
<template>
  <div :class="$style.contact">
    <BreadCrumb :tags="pathContact" :class="$style.contact__breadcrumb" />
    <ContactForm />
    <ContactMaps />
    <ContactFacility />
    <ContactActivity />
    <ContactMessage />
  </div>
</template>

<style module scoped lang="scss">
@import './ContactPage.module.scss';
</style>

<script setup lang="ts">
import ContactForm from './ContactForm/ContactForm.vue';
import ContactMaps from './ContactMaps/ContactMaps.vue';
import ContactFacility from './ContactFacility/ContactFacility.vue';
import ContactActivity from '../Home/HomeActivity/HomeActivity.vue';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import { useRoute } from 'vue-router';

const route = useRoute();
</script>
<template>
  <div :class="$style.contact">
    <bread-crumb :tags="route.path" :class="$style.contact__breadcrumb" />
    <contact-form />
    <contact-maps />
    <contact-facility />
    <contact-activity />
  </div>
</template>

<style module scoped lang="scss">
@import './ContactPage.module.scss';
</style>

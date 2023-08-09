<script setup lang="ts">
import { ref, watch } from 'vue';
import ContactForm from './ContactForm/ContactForm.vue';
import ContactMaps from './ContactMaps/ContactMaps.vue';
import ContactFacility from './ContactFacility/ContactFacility.vue';
import ContactActivity from '../Home/HomeActivity/HomeActivity.vue';
// import ContactMessage from './ContactMessage/ContactMessage.vue';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import { useRoute } from 'vue-router';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

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

const route = useRoute();

const variableChange = ref([]);
const variableChangeContact = ref([]);
const dataFacility = ref<Info>({
  address: '',
  hotline: '',
  mapLink: '',
  image: ''
});
const dataContact = ref<Contact>({
  email: {
    content: ''
  },
  facebook: {
    content: ''
  },
  zalo: {
    content: ''
  }
});
const move = ref(false);

// Gọi hàm useAxios để lấy response, error, và isLoading
const getInfo = useAxios<DataResponse>('get', '/facility/', {}, {}, variableChange.value);
const getContact = useAxios<DataResponse>(
  'get',
  '/information?type=CONTACT',
  {},
  {},
  variableChangeContact.value
);

// Truy xuất giá trị response.value và gán vào responseData
watch(getInfo.response, () => {
  dataFacility.value = getInfo.response?.value?.data;
});

watch(getContact.response, () => {
  dataContact.value = getContact.response?.value?.data;

  move.value = true;
});
</script>
<template>
  <div :class="$style.contact" v-if="move">
    <bread-crumb :tags="route.path" :class="$style.contact__breadcrumb" />
    <contact-form :data-contact="dataContact" :data-facility="dataFacility" />
    <contact-maps :data-facility="dataFacility" />
    <contact-facility :data-contact="dataContact" :data-facility="dataFacility" />
    <contact-activity />
  </div>
  <loading-component v-else />
</template>

<style module scoped lang="scss">
@import './ContactPage.module.scss';
</style>

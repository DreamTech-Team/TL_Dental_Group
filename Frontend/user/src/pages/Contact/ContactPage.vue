<script setup lang="ts">
import { ref, watch } from 'vue';
import ContactForm from './ContactForm/ContactForm.vue';
import ContactMaps from './ContactMaps/ContactMaps.vue';
import ContactFacility from './ContactFacility/ContactFacility.vue';
import ContactActivity from '../Home/HomeActivity/HomeActivity.vue';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import { useRoute } from 'vue-router';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';
import { saveDataContact } from '@/stores/counter';

interface Info {
  address: string;
  phoneNumber: string;
  hotline: string;
  mapLink: string;
  image: string;
  mapIframe: string;
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

// Xử lí store
const dataContactStore = saveDataContact();

const dataFacility = ref<Info>({
  address: '',
  phoneNumber: '',
  hotline: '',
  mapLink: '',
  image: '',
  mapIframe: ''
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
const isLoadingContact = ref(false);
const isLoadingFacility = ref(false);

// Kiểm tra store có dữ liệu chưa
if (
  dataContactStore.dataContact.email.content === '' &&
  dataContactStore.dataFacility.address === ''
) {
  const variableChange = ref([]);
  const variableChangeContact = ref([]);

  const getInfo = useAxios<DataResponse>('get', '/facility/', {}, {}, variableChange.value);

  const getContact = useAxios<DataResponse>(
    'get',
    '/information?type=CONTACT',
    {},
    {},
    variableChangeContact.value
  );

  watch(getInfo.isLoading, () => {
    isLoadingFacility.value = getInfo.isLoading.value;
  });

  watch(getContact.isLoading, () => {
    isLoadingContact.value = getContact.isLoading.value;
  });

  watch(getInfo.response, () => {
    dataFacility.value = getInfo.response?.value?.data;
    dataContactStore.setDataFacility(dataFacility.value);
  });

  watch(getContact.response, () => {
    dataContact.value = getContact.response?.value?.data;
    dataContactStore.setDataContact(dataContact.value);
  });
} else {
  dataContact.value = dataContactStore.dataContact;
  dataFacility.value = dataContactStore.dataFacility;
  isLoadingContact.value = false;
  isLoadingFacility.value = false;
}
</script>
<template>
  <div :class="$style.contact" v-if="!isLoadingContact && !isLoadingFacility">
    <bread-crumb :tags="route.path" :class="$style.contact__breadcrumb" />
    <contact-form />
    <contact-maps />
    <contact-facility />
    <contact-activity />
  </div>
  <loading-component v-else />
</template>

<style module scoped lang="scss">
@import './ContactPage.module.scss';
</style>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPhone, faEnvelope, faLocationDot } from '@fortawesome/free-solid-svg-icons';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { saveDataContact } from '@/stores/counter';
import { RouterLink } from 'vue-router';

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

const saveData = saveDataContact();

const variableChange = ref([]);
const variableChangeContact = ref([]);
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

const fetchData = async () => {
  const getInfo = useAxios<DataResponse>('get', '/facility/', {}, {}, variableChange.value);

  const getContact = useAxios<DataResponse>(
    'get',
    '/information?type=CONTACT',
    {},
    {},
    variableChangeContact.value
  );

  watch(getInfo.response, () => {
    dataFacility.value = getInfo.response?.value?.data;
    saveData.setDataFacility(dataFacility.value);
  });

  watch(getContact.response, () => {
    dataContact.value = getContact.response?.value?.data;
    saveData.setDataContact(dataContact.value);
  });
};

// Gọi fetchData sau 3 giây
setTimeout(fetchData, 3000);
</script>
<template>
  <div :class="$style.header__contact">
    <div
      :style="{
        maxWidth: '1560px',
        margin: '0 auto',
        height: '100%',
        display: 'flex',
        justifyContent: 'flex-end',
        alignItems: 'center'
      }"
    >
      <div :class="$style['header__contact-item']">
        <font-awesome-icon :icon="faPhone" :class="$style['header__contact-icon']" />
        <a :href="'tel:' + dataFacility.phoneNumber">{{ dataFacility.phoneNumber }}</a>
      </div>
      |
      <div :class="$style['header__contact-item']">
        <font-awesome-icon :icon="faEnvelope" :class="$style['header__contact-icon']" />
        <a :href="'mailto:' + dataContact.email.content">{{ dataContact.email.content }}</a>
      </div>
      |
      <div :class="$style['header__contact-item']">
        <font-awesome-icon :icon="faLocationDot" :class="$style['header__contact-icon']" />
        <router-link to="/lienhe">{{ dataFacility.address }}</router-link>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './BaseHeader.module.scss';
</style>

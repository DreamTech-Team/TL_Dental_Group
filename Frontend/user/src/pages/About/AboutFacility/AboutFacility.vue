<script setup lang="ts">
import { ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faDiamondTurnRight } from '@fortawesome/free-solid-svg-icons';
import Location from '@/assets/imgs/About/Location.png';
import Telephone from '@/assets/imgs/About/Telephone.png';
import Message from '@/assets/imgs/About/Message.png';
import Facebook from '@/assets/imgs/About/Facebook.png';
import { saveDataContact } from '@/stores/counter';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

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

// Gọi hàm xử lí store
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

// Kiểm tra xem ở store có dữ liệu chưa, nếu chưa có thì call API về
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
    dataContactStore.setDataFacility(dataFacility.value); // Lưu vào store
  });

  watch(getContact.response, () => {
    dataContact.value = getContact.response?.value?.data;
    dataContactStore.setDataContact(dataContact.value); // Lưu vào store
  });
} else {
  dataContact.value = dataContactStore.dataContact;
  dataFacility.value = dataContactStore.dataFacility;
  isLoadingContact.value = false;
  isLoadingFacility.value = false;
}
</script>
<template>
  <div :class="$style.about__facility" v-if="!isLoadingContact && !isLoadingFacility">
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
          <p>{{ dataFacility.phoneNumber }}</p>
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
  <loading-component v-else />
</template>

<style module scoped lang="scss">
@import './AboutFacility.module.scss';
</style>

<script setup lang="ts">
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import ModalLogo from './components/Logo/ModalLogo.vue';
import ModalPart1 from './components/Part1/ModalPart1.vue';
import ModalPart2 from './components/Part2/ModalPart2.vue';
import ModalPart3 from './components/Part3/ModalPart3.vue';
import { ref, onMounted, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

//GET DATA
const deps = ref([]);
const getPart1 = useAxios<DataResponse>('get', '/information?type=GENERAL', {}, {}, deps.value);
const getPart2 = useAxios<DataResponse>('get', '/information?type=CEO', {}, {}, deps.value);
const getPart3 = useAxios<DataResponse>('get', '/information?type=CONTACT', {}, {}, deps.value);
const getPart4 = useAxios<DataResponse>('get', '/information?type=LOGO', {}, {}, deps.value);

//Properties
const sourceElement = ref<HTMLElement | null>(null);
const targetElement = ref<HTMLElement | null>(null); //For 4 parts same height

//Open status
const isOpen1 = ref(false);
const isOpen2 = ref(false);
const isOpen3 = ref(false);
const isOpen4 = ref(false);

//Init data structure
const part1 = ref({
  namecomp: '',
  slogan: '',
  years: ''
});

const part2 = ref({
  ceo: '',
  address: '',
  day: ''
});

const part3 = ref({
  phone: '',
  hotline: '',
  email: '',
  facebook: '',
  zalo: ''
});

const part4 = ref({
  image: ''
});

//Functions handle emits from modal
const onUpdateContent1 = (data: object) => {
  part1.value = { ...part1.value, ...data };
};

const onUpdateContent2 = (data: object) => {
  part2.value = { ...part2.value, ...data };
};

const onUpdateContent3 = (data: object) => {
  part3.value = { ...part3.value, ...data };
};

const onUpdateContent4 = (data: object) => {
  part4.value = { ...part4.value, ...data };
};

//Set information from axios
watch(getPart1.response, () => {
  part1.value.namecomp = getPart1.response.value?.data?.name.content ?? '';
  part1.value.years = getPart1.response.value?.data?.duringTime.content ?? '';
  part1.value.slogan = getPart1.response.value?.data?.slogan.content ?? '';
});

watch(getPart2.response, () => {
  part2.value.ceo = getPart2.response.value?.data?.fullname_ceo.content ?? '';
  part2.value.address = getPart2.response.value?.data?.address_ceo.content ?? '';
  part2.value.day = getPart2.response.value?.data?.inauguration_ceo.content ?? '';
});

watch(getPart3.response, () => {
  part3.value.phone = getPart3.response.value?.data?.phone.content ?? '';
  part3.value.hotline = getPart3.response.value?.data?.hotline.content ?? '';
  part3.value.email = getPart3.response.value?.data?.email.content ?? '';
  part3.value.facebook = getPart3.response.value?.data?.facebook.content ?? '';
  part3.value.zalo = getPart3.response.value?.data?.zalo.content ?? '';
});

watch(getPart4.response, () => {
  part4.value.image = getPart4.response.value?.data?.logo.content ?? '';
});

onMounted(() => {
  const height = sourceElement.value?.clientHeight;
  if (height !== undefined && targetElement.value !== null) {
    targetElement.value.style.height = `${height}px`;
  }
});
</script>
<template>
  <div :class="$style.dashboard_body1">
    <div :class="$style.dashboard_body1_upper">
      <div :class="$style.dashboard_item1">
        <h4>Thông tin chung</h4>
        <div :class="$style.dashboard_item1_infor">
          <h5>Tên công ty</h5>
          <span>{{ part1.namecomp }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Slogan</h5>
          <span>{{ part1.slogan }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Khoảng thời gian hoạt động</h5>
          <span>{{ part1.years }}</span>
        </div>
        <EditBtn @click="isOpen1 = true" style="width: 5rem; height: 5rem" />
      </div>
      <div :class="$style.dashboard_item1">
        <h4>Thông tin CEO</h4>
        <div :class="$style.dashboard_item1_infor">
          <h5>Họ tên</h5>
          <span>{{ part2.ceo }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Địa chỉ</h5>
          <span>{{ part2.address }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Ngày nhậm chức</h5>
          <span>{{ part2.day }}</span>
        </div>
        <EditBtn @click="isOpen2 = true" style="width: 5rem; height: 5rem" />
      </div>
      <div :class="$style.dashboard_item1" ref="sourceElement">
        <h4>Thông tin liên hệ</h4>
        <div :class="$style.dashboard_item1_infor">
          <h5>Số điện thoại</h5>
          <span>{{ part3.phone }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Hotline</h5>
          <span>{{ part3.hotline }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Email</h5>
          <span>{{ part3.email }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Link Facebook</h5>
          <span>{{ part3.facebook }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Số điện thoại Zalo</h5>
          <span>{{ part3.zalo }}</span>
        </div>
        <EditBtn @click="isOpen3 = true" style="width: 5rem; height: 5rem" />
      </div>
      <div :class="$style.dashboard_item1" ref="targetElement">
        <h4>Logo</h4>
        <div :class="$style.dashboard_item1_body">
          <div :class="$style.dashboard_item1_img">
            <img :src="part4.image" />
          </div>
          <button @click="isOpen4 = true">Thay đổi</button>
        </div>
      </div>
    </div>
  </div>
  <ModalLogo
    v-if="isOpen4"
    :image="part4.image"
    @close="isOpen4 = false"
    @update-content="onUpdateContent4"
  />
  <ModalPart1
    v-if="isOpen1"
    @close="isOpen1 = false"
    :namecomp="part1.namecomp"
    :slogan="part1.slogan"
    :years="part1.years"
    @update-content="onUpdateContent1"
  />
  <ModalPart2
    v-if="isOpen2"
    @close="isOpen2 = false"
    :ceo="part2.ceo"
    :address="part2.address"
    :day="part2.day"
    @update-content="onUpdateContent2"
  />
  <ModalPart3
    v-if="isOpen3"
    @close="isOpen3 = false"
    :phone="part3.phone"
    :hotline="part3.hotline"
    :email="part3.email"
    :facebook="part3.facebook"
    :zalo="part3.zalo"
    @update-content="onUpdateContent3"
  />
</template>

<style module scoped lang="scss">
@import './DBComBody1.module.scss';
</style>

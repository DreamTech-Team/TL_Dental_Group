<script setup lang="ts">
import Logo from '@/assets/imgs/logo_nobg.png';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import ModalLogo from './components/Logo/ModalLogo.vue';
import ModalPart1 from './components/Part1/ModalPart1.vue';
import ModalPart2 from './components/Part2/ModalPart2.vue';
import ModalPart3 from './components/Part3/ModalPart3.vue';
import { ref } from 'vue';

const content = ref({
  image: Logo,
  namecomp: 'TL Dental Group',
  slogan: 'Dental distributors',
  years: '12 YEARS',
  ceo: 'Trần Duy Khương',
  address: 'Bà Rịa - Vũng Tàu',
  day: '23/07/2022',
  phone: '0898919265',
  hotline: '44325421',
  email: 'TLDental@gmail.com',
  facebook: 'https://www.facebook.com/tranduykhuong.it',
  zalo: '038967123'
});
const isOpen1 = ref(false);
const isOpen2 = ref(false);
const isOpen3 = ref(false);
const isOpen4 = ref(false);
</script>
<template>
  <div :class="$style.dashboard_body1">
    <div :class="$style.dashboard_body1_upper">
      <div :class="$style.dashboard_item1">
        <h4>Thông tin chung</h4>
        <div :class="$style.dashboard_item1_infor">
          <h5>Tên công ty</h5>
          <span>{{ content.namecomp }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Slogan</h5>
          <span>{{ content.slogan }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Khoảng thời gian hoạt động</h5>
          <span>{{ content.years }}</span>
        </div>
        <EditBtn @click="isOpen1 = true" style="width: 5rem; height: 5rem" />
      </div>
      <div :class="$style.dashboard_item1">
        <h4>Thông tin CEO</h4>
        <div :class="$style.dashboard_item1_infor">
          <h5>Họ tên</h5>
          <span>{{ content.ceo }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Địa chỉ</h5>
          <span>{{ content.address }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Ngày nhậm chức</h5>
          <span>{{ content.day }}</span>
        </div>
        <EditBtn @click="isOpen2 = true" style="width: 5rem; height: 5rem" />
      </div>
      <div :class="$style.dashboard_item1">
        <h4>Thông tin liên hệ</h4>
        <div :class="$style.dashboard_item1_infor">
          <h5>Số điện thoại</h5>
          <span>{{ content.phone }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Hotline</h5>
          <span>{{ content.hotline }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Email</h5>
          <span>{{ content.email }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Link Facebook</h5>
          <span>{{ content.facebook }}</span>
        </div>
        <div :class="$style.dashboard_item1_infor">
          <h5>Số điện thoại Zalo</h5>
          <span>{{ content.zalo }}</span>
        </div>
        <EditBtn @click="isOpen3 = true" style="width: 5rem; height: 5rem" />
      </div>
      <div :class="$style.dashboard_item1">
        <h4>Logo</h4>
        <div :class="$style.dashboard_item1_body">
          <div :class="$style.dashboard_item1_img">
            <img :src="Logo" />
          </div>
          <button @click="isOpen4 = true">Thay đổi</button>
        </div>
      </div>
    </div>
  </div>
  <ModalLogo v-if="isOpen4" :image="content.image" @close="isOpen4 = false" />
  <ModalPart1
    v-if="isOpen1"
    @close="isOpen1 = false"
    :namecomp="content.namecomp"
    :slogan="content.slogan"
    :years="content.years"
  />
  <ModalPart2
    v-if="isOpen2"
    @close="isOpen2 = false"
    :ceo="content.ceo"
    :address="content.address"
    :day="content.day"
  />
  <ModalPart3
    v-if="isOpen3"
    @close="isOpen3 = false"
    :phone="content.phone"
    :hotline="content.hotline"
    :email="content.email"
    :facebook="content.facebook"
    :zalo="content.zalo"
  />
</template>

<style module scoped lang="scss">
@import './DBComBody1.module.scss';
</style>

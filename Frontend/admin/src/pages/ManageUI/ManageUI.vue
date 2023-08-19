<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faHouse,
  faCircleInfo,
  faArrowCircleRight,
  faBoxArchive,
  faPersonCirclePlus,
  faNewspaper
} from '@fortawesome/free-solid-svg-icons';
import { faImages } from '@fortawesome/free-regular-svg-icons';
import { RouterLink } from 'vue-router';

const componentUI = [
  {
    title: 'Trang chủ',
    icon: faHouse,
    color:
      'linear-gradient(220deg, #0094E8 12%, rgba(102, 209, 255, 0.65) 88%, rgba(0, 162, 232, 0.00) 100%)',
    path: 'mnhome'
  },
  {
    title: 'Giới thiệu',
    icon: faCircleInfo,
    color:
      'linear-gradient(220deg, #0033E8 12%, rgba(102, 182, 255, 0.65) 88%, rgba(232, 0, 0, 0.00) 100%, rgba(0, 120, 232, 0.00) 100%)',
    path: 'mnabout'
  },
  {
    title: 'Banner',
    icon: faImages,
    color:
      'linear-gradient(220deg, #00E878 12%, rgba(102, 255, 182, 0.65) 88%, rgba(0, 232, 134, 0.00) 100%)',
    path: 'mnbanner'
  },
  {
    title: 'Sản phẩm',
    icon: faBoxArchive,
    color:
      'linear-gradient(220deg, #E80E00 12%, rgba(255, 130, 102, 0.65) 88%, rgba(232, 42, 0, 0.00) 100%)',
    path: 'mnproduct'
  },
  {
    title: 'Tuyển dụng',
    icon: faPersonCirclePlus,
    color:
      'linear-gradient(220deg, #B900E8 12%, rgba(255, 102, 221, 0.65) 88%, rgba(185, 0, 232, 0.00) 100%)',
    path: 'tuyendung'
  },
  {
    title: 'Tin tức',
    icon: faNewspaper,
    color:
      'linear-gradient(220deg, #E8A700 12%, rgba(255, 221, 102, 0.65) 88%, rgba(199, 232, 0, 0.00) 100%)',
    path: 'activity'
  }
];
</script>
<template>
  <div :class="$style.mn_ui">
    <h2>QUẢN LÝ GIAO DIỆN VÀ NỘI DUNG</h2>

    <div :class="$style['mn_ui--list']">
      <router-link
        :class="$style['mn_ui--item']"
        v-for="(ui, index) in componentUI"
        :key="index"
        :style="{ background: ui.color }"
        :to="'/' + ui.path"
      >
        <h3>{{ ui.title }}</h3>

        <font-awesome-icon :icon="ui.icon" :class="$style['mn_ui--item-ic']" />

        <div :class="$style['mn_ui--item-footer']">
          <p>Chỉnh sửa</p>

          <font-awesome-icon :icon="faArrowCircleRight" :class="$style['mn_ui--item-footer-ic']" />
        </div>
      </router-link>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ManageUI.module.scss';
</style>

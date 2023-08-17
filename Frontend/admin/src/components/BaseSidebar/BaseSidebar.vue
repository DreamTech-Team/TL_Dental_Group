<script setup lang="ts">
import ic_logo from '@/assets/imgs/logo_nobg.png';
import BaseSidebarBtn from './BaseSidebarBtn/BaseSidebarBtn.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faDashboard,
  faPeopleGroup,
  faBuilding,
  faAngleRight
} from '@fortawesome/free-solid-svg-icons';
import { ref, reactive } from 'vue';

const btnItems = [
  { icon: faDashboard, label: 'Quản lý giao diện', link: '/mnui' },
  { icon: faPeopleGroup, label: 'Quản lý nhân viên', link: '/mnstaff' },
  { icon: faBuilding, label: 'Quản lý công ty', link: '/mncompany' }
];

const showSidebar = ref(false);
const showBtnSidebar = ref(false);
const activatedSidebar = reactive({ value: '/' + window.location.href.split('/').pop() });

const hanldeConvertPage = (link: string) => {
  activatedSidebar.value = link;
};
</script>
<template>
  <div :class="$style.container__sidebar">
    <div
      :class="[$style.sidebar, $style[showSidebar ? 'show' : '']]"
      v-on:mouseover="showBtnSidebar = true"
      v-on:mouseout="showBtnSidebar = false"
    >
      <div :class="$style['sidebar__logo']">
        <img :src="ic_logo" alt="none" />
        <div :class="$style['sidebar__logo-title']">
          <h3>TL Dental Group</h3>
          <p>Dental Distributors</p>
        </div>
      </div>
      <div :class="$style['sidebar__btn']">
        <div v-for="(item, index) in btnItems" :key="index">
          <base-sidebar-btn
            :obj="item"
            :show="showSidebar"
            :active="activatedSidebar.value === item.link"
            :hanldeConvertPage="hanldeConvertPage"
          />
        </div>
      </div>
    </div>
    <div
      :class="[
        $style['container__sidebar-btn'],
        $style[showSidebar ? 'container__sidebar-btn-active' : '']
      ]"
    >
      <div
        :class="[
          $style['container__sidebar-btn-block'],
          $style[showBtnSidebar ? 'container__sidebar-btn-block-show' : '']
        ]"
        @click="showSidebar = !showSidebar"
        v-on:mouseover="showBtnSidebar = true"
        v-on:mouseout="showBtnSidebar = false"
      >
        <font-awesome-icon
          :class="$style['container__sidebar-btn-block-icon']"
          :icon="faAngleRight"
        />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './BaseSidebar.module.scss';
</style>

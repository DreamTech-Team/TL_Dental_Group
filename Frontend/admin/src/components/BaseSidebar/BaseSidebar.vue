<script setup lang="ts">
import ic_logo from '@/assets/imgs/logo_nobg.png';
import BaseSidebarBtn from './BaseSidebarBtn/BaseSidebarBtn.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faDashboard,
  faPeopleGroup,
  faBuilding,
  faAngleRight,
  faChartLine,
  faUsers,
  faNewspaper,
  faLayerGroup,
  faUniversalAccess,
  faBuildingShield,
  faBars
} from '@fortawesome/free-solid-svg-icons';
import { ref, reactive } from 'vue';

let btnItems = [
  { icon: faChartLine, label: 'Dashboard', link: '/' },
  { icon: faDashboard, label: 'Quản lý giao diện', link: '/mnui' },
  { icon: faLayerGroup, label: 'Quản lý sản phẩm', link: '/mnproduct' },
  { icon: faBars, label: 'Quản lý danh mục', link: '/mncategory' },
  { icon: faNewspaper, label: 'Quản lý tin tức', link: 'mnactivity' },
  { icon: faPeopleGroup, label: 'Quản lý nhân viên', link: '/mnstaff' },
  { icon: faUniversalAccess, label: 'Quản lý tuyển dụng', link: '/mnrecruitment' },
  { icon: faUsers, label: 'Quản lý khách hàng', link: '/mncustomer' },
  { icon: faBuilding, label: 'Quản lý công ty', link: '/mncompany' },
  { icon: faBuildingShield, label: 'Quản lý chính sách', link: '/mnpolicy' }
];

const inforAdminString = localStorage.getItem('infor_admin');

if (inforAdminString !== null) {
  const inforAdmin = JSON.parse(inforAdminString);

  if (inforAdmin.roles !== 'ROLE_ADMIN') {
    const filteredBtnItems = btnItems.filter(
      (item) => item.label !== 'Dashboard' && item.label !== 'Quản lý nhân viên'
    );
    btnItems = filteredBtnItems;
  }
} else {
  // Xử lý khi giá trị không có trong localStorage
}

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

<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faPhone,
  faEnvelope,
  faArrowRightFromBracket,
  faCircleUser
} from '@fortawesome/free-solid-svg-icons';
import Avatar from '@/assets/imgs/adminavt.png';
import { ref, onMounted, onUnmounted } from 'vue';

const isOpen = ref(false);

//Close Modal when click outside
const handleClickOutside = (event: MouseEvent) => {
  if (!event.target || !(event.target instanceof Element)) return;

  const modalElement = document.getElementById('personal');
  if (!modalElement || !modalElement.contains(event.target)) {
    isOpen.value = false;
  }
};

onMounted(() => {
  window.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  window.removeEventListener('click', handleClickOutside);
});
</script>
<template>
  <div :class="$style.header">
    <div :class="$style['header__infor']">
      <div :class="$style['header__infor-item']">
        <font-awesome-icon :icon="faPhone" :class="$style['header__ic']" />
        <p>(+84) 898923230</p>
      </div>
      <div :class="$style['header__infor-item']">
        <font-awesome-icon :icon="faEnvelope" :class="$style['header__ic']" />
        <p>admin@gmail.com</p>
      </div>
      <div
        id="personal"
        :class="$style['header__infor-item']"
        @click="isOpen = !isOpen"
        :style="isOpen ? 'background: rgba(227, 225, 225, 0.91);' : ''"
      >
        <img :src="Avatar" alt="avatar" />
        <div :class="$style['header__infor-item--active']"></div>
        <ul :class="$style['header__infor-list']" v-show="isOpen">
          <li :class="$style['header__list-item']">
            <img :src="Avatar" alt="avatar" />
            <div>
              <h4>Anh Khoi</h4>
              <p>Admin</p>
            </div>
          </li>
          <li :class="$style['header__list-item']">
            <font-awesome-icon :icon="faCircleUser" :class="$style['person__ic']" />
            <p>Cá nhân</p>
          </li>
          <li :class="$style['header__list-item']">
            <font-awesome-icon :icon="faArrowRightFromBracket" :class="$style['person__ic']" />
            <p>Đăng xuất</p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './BaseHeader.module.scss';
</style>

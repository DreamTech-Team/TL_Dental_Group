<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPhone, faEnvelope, faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';
import Avatar from '@/assets/imgs/adminavt.png';
import { ref, onMounted, onUnmounted } from 'vue';
import router from '@/router';

interface Infor {
  id: string;
  email: string;
  fullName: string;
  phoneNumber: string;
  address: string;
  password: string;
  roles: 'ROLE_ADMIN';
  token: string;
}

const isOpen = ref(false);
const storedInfor = window.localStorage.getItem('infor_admin');

let inforAdmin: Infor | null = null;

if (storedInfor !== null) {
  try {
    inforAdmin = JSON.parse(storedInfor);
  } catch (error) {
    console.error('Error parsing stored information:', error);
  }
}

const handleLogout = () => {
  router.push('/login');
  localStorage.clear();
};

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
        <p>{{ inforAdmin?.phoneNumber }}</p>
      </div>
      <div :class="$style['header__infor-item']">
        <font-awesome-icon :icon="faEnvelope" :class="$style['header__ic']" />
        <p>{{ inforAdmin?.email }}</p>
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
              <h4>{{ inforAdmin?.fullName }}</h4>
              <p>Admin</p>
            </div>
          </li>
          <li :class="$style['header__list-item']" @click="handleLogout">
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

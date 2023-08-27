<script setup lang="ts">
import { RouterView, useRoute, useRouter } from 'vue-router';
import BaseHeader from './components/BaseHeader/BaseHeader.vue';
import BaseSidebar from './components/BaseSidebar/BaseSidebar.vue';
import { ref, watch } from 'vue';

const checkLoginPage = ref(false);
const router = useRouter();
watch(useRoute(), (value) => {
  const getInforAdmin = localStorage.getItem('infor_admin');
  const inforAdmin = getInforAdmin ? JSON.parse(getInforAdmin) : null;

  if (value.path.includes('/login')) checkLoginPage.value = true;
  else checkLoginPage.value = false;

  if (!getInforAdmin && !value.path.includes('/login')) router.push('/login');

  if (getInforAdmin && value.path.includes('/login')) router.push('/');

  // Kiểm tra nếu roles là ROLE_ADMIN và path là dashboard hoặc "/"
  if (
    inforAdmin &&
    inforAdmin.roles !== 'ROLE_ADMIN' &&
    (value.path === '/' || value.path.includes('/dashboard') || value.path.includes('/mnstaff'))
  ) {
    router.push('/mnui');
  }
});
</script>

<template>
  <BaseHeader v-if="!checkLoginPage" />

  <div v-if="checkLoginPage">
    <RouterView />
  </div>
  <div v-else style="width: calc(100% - 70px); margin-left: auto">
    <RouterView />
  </div>

  <BaseSidebar v-if="!checkLoginPage" />
</template>

<style scoped></style>

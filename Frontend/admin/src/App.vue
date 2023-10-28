<script setup lang="ts">
import { RouterView, useRoute, useRouter } from 'vue-router';
import BaseHeader from './components/BaseHeader/BaseHeader.vue';
import BaseSidebar from './components/BaseSidebar/BaseSidebar.vue';
import { ref, watch } from 'vue';
import LoadingComponent from './components/LoadingComponent/LoadingComponent.vue';

const checkLoginPage = ref(false);
const isLoading = ref(true);
const router = useRouter();

watch(useRoute(), (value) => {
  const getInforAdmin = localStorage.getItem('infor_admin');
  const inforAdmin = getInforAdmin ? JSON.parse(getInforAdmin) : null;

  isLoading.value = true;

  if (value.path.includes('/login')) checkLoginPage.value = true;
  else checkLoginPage.value = false;

  // Kiểm tra nếu roles là ROLE_ADMIN và path là dashboard hoặc "/"
  if (
    inforAdmin &&
    inforAdmin.roles !== 'ROLE_ADMIN' &&
    (value.path === '/' || value.path.includes('/dashboard') || value.path.includes('/mnstaff'))
  ) {
    router.push('/mnui');
  }

  isLoading.value = false;
});
</script>

<template>
  <LoadingComponent v-if="isLoading" />
  <div v-else>
    <BaseHeader v-if="!checkLoginPage" />

    <div v-if="checkLoginPage">
      <RouterView />
    </div>
    <div v-else style="width: calc(100% - 70px); margin-left: auto">
      <RouterView />
    </div>

    <BaseSidebar v-if="!checkLoginPage" />
  </div>
</template>

<style scoped></style>

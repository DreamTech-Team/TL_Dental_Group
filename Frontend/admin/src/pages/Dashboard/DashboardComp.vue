<script setup lang="ts">
import DBControl from './DBControl/DBComControl.vue';
import DBBody1 from './DBBody1/DBComBody1.vue';
import DBBody2 from './DBBody2/DBComBody2.vue';
import { ref } from 'vue';

const updateStatus = ref('1');

//Switch Tab
const handleUpdateStatus = (newStatus: string) => {
  updateStatus.value = newStatus;
};
</script>
<template>
  <div :class="$style.dashboard">
    <h3>DASHBOARD</h3>
    <DBControl @update-status="handleUpdateStatus" />
    <DBBody1 v-if="updateStatus === '1'" />
    <DBBody2 v-if="updateStatus === '2'" />
  </div>
</template>

<style module scoped lang="scss">
@import './DashboardComp.module.scss';
</style>

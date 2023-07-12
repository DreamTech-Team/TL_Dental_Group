<script setup lang="ts">
import { ref } from 'vue';
import { staffs } from './Staff';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import StaffTable from './StaffTable/StaffTable.vue';
import StaffModal from './StaffTable/StaffModal/StaffModal.vue';

const isModalOpen = ref(false);
const activeTab = ref('activity');

const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};
</script>
<template>
  <div>
    <div :class="$style.mn_activity">
      <h3>DANH SÁCH NHÂN SỰ</h3>

      <div v-if="activeTab === 'activity'" :class="$style['mn_activity--total']">
        <p>
          Có tất cả <span :class="$style.highlight">{{ staffs.length }}</span> nhân viên
        </p>

        <button @click="openModal" :class="$style.mn_activity_control">
          <font-awesome-icon :icon="faPlus" :class="$style['mn_activity--total-ic1']" />
          THÊM NHÂN NHÂN SỰ
        </button>
      </div>
      <staff-table />
    </div>
    <div :class="$style.activity_overlay" v-if="isModalOpen">
      <staff-modal :closeModal="closeModal" @click.stop @close="closeModal" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ManageStaff.module.scss';
</style>

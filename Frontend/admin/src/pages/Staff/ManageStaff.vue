<script setup lang="ts">
import { ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import StaffTable from './StaffTable/StaffTable.vue';
import StaffModal from './StaffTable/StaffModal/StaffModal.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface StaffItem {
  id: string;
  email: string;
  fullname: string;
  phonenumber: string;
  address: string;
  password: string;
  changed: boolean;
  createAt: string;
  roles: string;
}
const staffArray = ref<StaffItem[]>([]);
const isModalOpen = ref(false);
const activeTab = ref('activity');
const isLoadingState = ref(false);

const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

const deps = ref([]);

// //GET DATA
const { response, isLoading } = useAxios<DataResponse>(
  'get',
  `/employees?page=0&pageSize=1000000`,
  {},
  {},
  deps.value
);
watch(isLoading, () => {
  isLoadingState.value = isLoading.value;
});
watch([response, isLoading], () => {
  if (response.value?.data?.data != undefined) {
    staffArray.value = response.value?.data?.data;
  }
});

const handleChangeAdd = (dataAdded: StaffItem) => {
  staffArray.value.unshift(dataAdded);
};

const handleStaffDeleted = (deletedNewsId: string) => {
  staffArray.value = staffArray.value.filter((item) => item.id !== deletedNewsId);
};
</script>
<template>
  <div>
    <div :class="$style.mn_activity">
      <h3>DANH SÁCH NHÂN SỰ</h3>

      <div v-if="activeTab === 'activity'" :class="$style['mn_activity--total']">
        <p>
          Có tất cả <span :class="$style.highlight">{{ staffArray.length }}</span> nhân viên
        </p>

        <button @click="openModal" :class="$style.mn_activity_control">
          <font-awesome-icon :icon="faPlus" :class="$style['mn_activity--total-ic1']" />
          THÊM NHÂN SỰ
        </button>
      </div>
      <loading-component v-if="isLoadingState" />
      <staff-table v-else :staffArray="staffArray" :handleStaffDeleted="handleStaffDeleted" />
    </div>
    <div :class="$style.activity_overlay" v-if="isModalOpen">
      <staff-modal
        :closeModal="closeModal"
        @click.stop
        @close="closeModal"
        :change="handleChangeAdd"
      />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ManageStaff.module.scss';
</style>

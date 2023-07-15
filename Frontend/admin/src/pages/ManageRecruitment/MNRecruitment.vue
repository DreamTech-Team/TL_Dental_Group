<script setup lang="ts">
import { ref } from 'vue';
import { recruitments } from './Recruitment';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import ManageRecruitment from './RecruitmentTable.vue';
import ModalRecruitment from './ModalRecruitment/ModalRecruitment.vue';

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
    <div>
      <div :class="$style.mn_activity">
        <h3>QUẢN LÝ TUYỂN DỤNG</h3>

        <div v-if="activeTab === 'activity'" :class="$style['mn_activity--total']">
          <p>
            Có tất cả <span :class="$style.highlight">{{ recruitments.length }}</span> nhân viên
          </p>

          <button @click="openModal" :class="$style.mn_activity_control">
            <font-awesome-icon :icon="faPlus" :class="$style['mn_activity--total-ic1']" />
            THÊM BÀI TUYỂN DỤNG
          </button>
        </div>
        <manage-recruitment />
      </div>
      <div :class="$style.activity_overlay" v-if="isModalOpen">
        <modal-recruitment :closeModal="closeModal" @click.stop @close="closeModal" />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './MNRecruitment.mudole.scss';
</style>

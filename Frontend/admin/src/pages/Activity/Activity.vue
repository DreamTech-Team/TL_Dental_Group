<script setup lang="ts">
import { ref } from 'vue';
import { activities, tags } from './Activity';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus, faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import ActivityTable from '@/pages/Activity/ActivityTable/ActivityTable.vue';
import ActivityTag from '@/pages/Activity/ActivityTag/ActivityTag.vue';
import ActivityModal from './ActivityTable/ModalActivity/ActivityModal.vue';

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
      <h3>DANH SÁCH HOẠT ĐỘNG</h3>

      <div v-if="activeTab === 'activity'" :class="$style['mn_activity--total']">
        <p>
          Có tất cả <span :class="$style.highlight">{{ activities.length }}</span> hoạt động
        </p>

        <button @click="openModal" :class="$style.mn_activity_control">
          <font-awesome-icon :icon="faPlus" :class="$style['mn_activity--total-ic1']" />
          THÊM HOẠT ĐỘNG
        </button>
      </div>

      <div v-else-if="activeTab === 'tags'" :class="$style['mn_activity--total']">
        <p>
          Có tất cả <span :class="$style.highlight">{{ tags.length }}</span> tags
        </p>
        <button @click="openModal" :class="$style.mn_activity_control">
          <font-awesome-icon :icon="faPlus" :class="$style['mn_activity--total-ic1']" />
          THÊM TAG
        </button>
      </div>
      <div :class="$style['mn_activity__wrapTab']">
        <p
          :class="[
            $style['mn_activity__wrapTab--text'],
            activeTab === 'activity' ? $style.active : ''
          ]"
          @click="activeTab = 'activity'"
        >
          Danh sách hoạt động
        </p>
        <p
          :class="[$style['mn_activity__wrapTab--text'], activeTab === 'tags' ? $style.active : '']"
          @click="activeTab = 'tags'"
        >
          Danh sách tags
        </p>
      </div>
      <hr />
      <!-- Hoạt động -->
      <div v-if="activeTab === 'activity'">
        <activity-table />
      </div>
      <!-- Tags -->
      <div v-else-if="activeTab === 'tags'">
        <activity-tag />
      </div>
    </div>
    <div :class="$style.activity_overlay" v-if="isModalOpen" @click="closeModal">
      <activity-modal @click.stop @close="closeModal" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './Activity.module.scss';
</style>

<script setup lang="ts">
import { ref, watchEffect, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { format } from 'date-fns';
import { faPlus, faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import ActivityTable from '@/pages/Activity/ActivityTable/ActivityTable.vue';
import ActivityTag from '@/pages/Activity/ActivityTag/ActivityTag.vue';
import ActivityModal from './ActivityTable/ModalActivity/ActivityModal.vue';
import ModalAddTag from './ActivityTag/ModalTag/ModalTag.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const deps = ref([]);
const {
  response: totalNewsResponse,
  error: errorNews,
  isLoading: loadingNews
} = useAxios<DataResponse>('get', `/news/total`, {}, {}, deps.value);

const {
  response: totalTagsResponsive,
  error: errorTags,
  isLoading: loadingTags
} = useAxios<DataResponse>('get', `/tags`, {}, {}, deps.value);

const isModalOpen = ref(false);
const activeTab = ref('activity');
const totalActivity = ref(0);
const sampleTag = {
  id: '',
  name: '',
  slug: '',
  createAt: ''
};
const listTags = ref<Array<typeof sampleTag>>([]);

const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

const handleTagDeleted = (deletedTagId: string) => {
  listTags.value = listTags.value.filter((tag) => tag.id !== deletedTagId);
  console.log('cập nhật' + listTags.value);
};

watchEffect(() => {
  totalActivity.value = totalNewsResponse?.value?.data;
  listTags.value = totalTagsResponsive?.value?.data;
});
</script>
<template>
  <div>
    <div :class="$style.mn_activity">
      <h3>DANH SÁCH HOẠT ĐỘNG</h3>

      <div v-if="activeTab === 'activity'" :class="$style['mn_activity--total']">
        <p>
          Có tất cả <span :class="$style.highlight">{{ totalActivity }}</span> hoạt động
        </p>

        <button @click="openModal" :class="$style.mn_activity_control">
          <font-awesome-icon :icon="faPlus" :class="$style['mn_activity--total-ic1']" />
          THÊM HOẠT ĐỘNG
        </button>
      </div>

      <div v-else-if="activeTab === 'tags'" :class="$style['mn_activity--total']">
        <p>
          Có tất cả <span :class="$style.highlight">{{ listTags?.length }}</span> tags
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
        <activity-tag :listTags="listTags" :handleTagDeleted="handleTagDeleted" />
      </div>
    </div>
    <div :class="$style.activity_overlay" v-if="isModalOpen">
      <activity-modal
        :closeModal="closeModal"
        v-if="activeTab === 'activity'"
        @click.stop
        @close="closeModal"
      />

      <modal-add-tag
        :closeModal="closeModal"
        v-if="activeTab === 'tags'"
        @click.stop
        @close="closeModal"
      />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './Activity.module.scss';
</style>

<script setup lang="ts">
import { ref, watchEffect, watch, PropType } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { format } from 'date-fns';
import { faPlus, faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import ActivityTable from '@/pages/Activity/ActivityTable/ActivityTable.vue';
import ActivityTag from '@/pages/Activity/ActivityTag/ActivityTag.vue';
import ActivityModal from './ActivityTable/ModalActivity/ActivityModal.vue';
import ModalAddTag from './ActivityTag/ModalTag/ModalTag.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface Tags {
  id: string;
  name: string;
  slug: string;
  createAt: string;
}

interface News {
  id: string;
  title: string;
  img: string;
  slug: string;
  summary: string;
  detail: string;
  detailMobile: string;
  highlight: number;
  createAt: string;
  tags: [
    {
      id: string;
      name: string;
      slug: string;
      createAt: string;
    }
  ];
}
//Call API News
const currentPage = ref(0);
const pageSize = ref(10);
const deps = ref([]);

const listNews = ref<News[]>([]);
const api = ref(`/news?pageSize=${pageSize.value}&page=${currentPage.value}`);
const {
  response: dataNews,
  error,
  isLoading
} = useAxios<DataResponse>('get', api.value, {}, {}, deps.value);

watch(dataNews, () => {
  listNews.value = dataNews?.value?.data?.data;
  console.log(listNews);
});

//Call API tổng news
const {
  response: totalNewsResponse,
  error: errorNews,
  isLoading: loadingNews
} = useAxios<DataResponse>('get', `/news/total`, {}, {}, deps.value);

watch(
  currentPage,
  () => {
    // console.log(12345);
    const {
      response: responseChanged,
      error,
      isLoading
    } = useAxios<DataResponse>(
      'get',
      `/news?pageSize=${pageSize.value}&page=${currentPage.value}`,
      {},
      {},
      deps.value
    );
    watch(responseChanged, () => {
      dataNews.value = responseChanged.value;
    });
  },
  { immediate: false }
);

//Call API tags
const {
  response: totalTagsResponsive,
  error: errorTags,
  isLoading: loadingTags
} = useAxios<DataResponse>('get', '/tags', {}, {}, deps.value);

const isModalOpen = ref(false);
const activeTab = ref('activity');
const totalActivity = ref(0);

const listTags = ref<Array<Tags>>([]);

const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

const updateCurrentPage = (currentIdx: number) => {
  currentPage.value = currentIdx;
};

const handleTagDeleted = (deletedTagId: string) => {
  listTags.value = listTags.value.filter((tag) => tag.id !== deletedTagId);
  // console.log('cập nhật' + listTags.value);
};

const handleNewsDeleted = (deletedNewsId: string) => {
  totalActivity.value = totalActivity.value - 1;
  listNews.value = listNews.value.filter((item) => item.id !== deletedNewsId);
};

const handleChangeAdd = (dataAdded: Tags) => {
  listTags.value.unshift(dataAdded);
};

const handleUpdateData = (newsAdd: News) => {
  listNews.value.push(newsAdd);
  location.reload();
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
        <activity-table
          :change="handleNewsDeleted"
          :dataNews="listNews"
          @current-page="updateCurrentPage"
          :totalPage="totalActivity"
        />
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
        @update-content="handleUpdateData"
      />

      <modal-add-tag
        :closeModal="closeModal"
        v-if="activeTab === 'tags'"
        @click.stop
        @close="closeModal"
        :change="handleChangeAdd"
        :listTags="listTags"
      />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './Activity.module.scss';
</style>

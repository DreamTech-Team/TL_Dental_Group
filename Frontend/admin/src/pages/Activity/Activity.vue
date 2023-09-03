<script setup lang="ts">
import { ref, watchEffect, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import ActivityTable from '@/pages/Activity/ActivityTable/ActivityTable.vue';
import ActivityTag from '@/pages/Activity/ActivityTag/ActivityTag.vue';
import ActivityModal from './ActivityTable/ModalActivity/ActivityModal.vue';
import ModalAddTag from './ActivityTag/ModalTag/ModalTag.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';
import Pagination from '@/components/Pagination/BasePagination.vue';

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
const isLoadingState = ref(false);

const listNews = ref<News[]>([]);
const api = ref(`/news?pageSize=${pageSize.value}&page=${currentPage.value}`);
const { response: dataNews, isLoading } = useAxios<DataResponse>(
  'get',
  api.value,
  {},
  {},
  deps.value
);

watch(isLoading, () => {
  isLoadingState.value = isLoading.value;
});

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
  [currentPage, isLoading],
  () => {
    // console.log(12345);
    const { response: responseChanged, isLoading } = useAxios<DataResponse>(
      'get',
      `/news?pageSize=${pageSize.value}&page=${currentPage.value}`,
      {},
      {},
      deps.value
    );

    watch(isLoading, () => {
      isLoadingState.value = isLoading.value;
    });
    watch(responseChanged, () => {
      dataNews.value = responseChanged.value;
    });
  },
  { immediate: false }
);

//Call API tags
const { response: totalTagsResponsive } = useAxios<DataResponse>(
  'get',
  '/tags',
  {},
  {},
  deps.value
);

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

//Pagination Handle
const scrollToTop = (top: number) => {
  window.scrollTo({
    top: top,
    behavior: 'smooth'
  });
};

const handlePageChange = (page: number) => {
  currentPage.value = page - 1;
  scrollToTop(0);
  // console.log(currentPage.value);
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
        <loading-component v-if="isLoadingState" style="height: calc(100vh - 200px)" />
        <activity-table
          v-else
          :change="handleNewsDeleted"
          :dataNews="listNews"
          @current-page="updateCurrentPage"
          :totalPage="totalActivity"
        />
        <div :class="$style['mn_activity_pagination']">
          <pagination
            :total="totalActivity ? totalActivity : 0"
            :current-page="currentPage"
            :page-size="pageSize"
            @current-change="handlePageChange"
          />
        </div>
      </div>
      <!-- Tags -->
      <div v-else-if="activeTab === 'tags'">
        <loading-component v-if="isLoadingState" style="height: calc(100vh - 200px)" />
        <activity-tag v-else :listTags="listTags" :handleTagDeleted="handleTagDeleted" />
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

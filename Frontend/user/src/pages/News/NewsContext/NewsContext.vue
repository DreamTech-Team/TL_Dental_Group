<script setup lang="ts">
import Category from '@/components/Category/BaseCategory.vue';
import Pagination from '@/components/Pagination/BasePagination.vue';
import router from '@/router/index';
import { ref, onMounted, onUnmounted, type PropType } from 'vue';
import { news } from './ContextHandle';

//GET DATA
interface ItemRS {
  news: {
    id: string;
    title: string;
    img: string;
    slug: string;
    summary: string;
    detail: string;
    detailMobile: string;
    highlight: 0;
    createAt: string;
  };
  tags: [
    {
      id: string;
      name: string;
      slug: string;
      createAt: string;
    }
  ];
}

const content = defineProps({
  listItem: {
    type: Array as PropType<ItemRS[]>,
    required: false
  },
  prsPage: {
    type: Number,
    require: false
  }
});

const currentPage = ref(content && content.prsPage ? content.prsPage + 1 : 1);
const pageSize = ref(12);
const isDesktop = ref(true);

const checkScreenSize = () => {
  if (window.innerWidth < 739) {
    isDesktop.value = false;
  } else {
    isDesktop.value = true;
  }
};

const scrollToTop = (top: number) => {
  window.scrollTo({
    top: top,
    behavior: 'smooth' // Tạo hiệu ứng cuộn mượt
  });
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  if (window.innerWidth < 739) {
    isDesktop.value = false;
    scrollToTop(0);
  } else {
    scrollToTop(400);
  }
};

const formatDate = (timestamp: string) => {
  const date = new Date(timestamp);
  const day = date.getDate().toString().padStart(2, '0');
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const year = date.getFullYear().toString();
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');

  return `${day}/${month}/${year}, ${hours}:${minutes}`;
};

const displayNews = ref(content.listItem);

const linkDetail = () => {
  router.push('/tintuc/a');
};

onMounted(() => {
  checkScreenSize();
  window.addEventListener('resize', checkScreenSize);
});

onUnmounted(() => {
  window.removeEventListener('resize', checkScreenSize);
});
</script>
<template>
  <div :class="$style.news__context">
    <div :class="$style['news__context-left']">
      <div
        :class="$style['news__context-item']"
        v-for="(item, index) in displayNews"
        :key="index"
        @click="linkDetail()"
      >
        <div :class="$style['news__item-left']">
          <img :src="item.news.img" alt="BGItem" />
        </div>
        <div :class="$style['news__item-right']">
          <div :class="$style['news__item-date']">
            <p>{{ formatDate(item.news.createAt) }}</p>
            <span></span>
          </div>
          <h4>{{ item.news.title }}</h4>
          <div :class="$style['news__item-summary']">
            <span>{{ item.news.summary }}</span>
          </div>
          <div :class="$style['news__item-footage']">
            <div :class="$style['news__footage-line']"></div>
            <span
              style="text-transform: uppercase; margin-right: 2rem"
              v-for="(subitem, index) in item.tags"
              :key="index"
              >{{ subitem.name }}</span
            >
            <div :class="$style['news__footage-line--mb']"></div>
          </div>
        </div>
      </div>
      <pagination
        :class="$style['news__context-left-pagination']"
        style="margin-top: 50px"
        :total="Math.ceil(news.length / pageSize)"
        :current-page="currentPage"
        :page-size="pageSize"
        @current-change="handlePageChange"
      />
    </div>
    <div :class="$style['news__context-right']">
      <category :class="$style['news__context-right-category']" style="margin-top: 0" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './NewsContext.module.scss';
</style>

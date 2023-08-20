<script setup lang="ts">
import { ref, watch, onMounted, onUnmounted } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { RouterLink } from 'vue-router';

//GET DATA
interface Item {
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

//Init data structure
const activities = ref([
  {
    id: '',
    title: '',
    img: '',
    slug: '',
    summary: '',
    detail: '',
    detailMobile: '',
    highlight: 1,
    createAt: ''
  }
]);
const uniqueTags = ref([
  {
    id: '',
    name: '',
    slug: '',
    createAt: ''
  }
]);

//Properties
let resizeListener: () => void;
const isPhone = ref(false);
const selectedItem = ref(-1);
const sourceElement = ref<HTMLElement | null>(null);
const itemWidth = ref(0);
const tags = ref('all');
const feedbacks = ref<Item[]>([]);

//Get highlight news
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/news/highlight', {}, {}, deps.value);

watch(response, () => {
  feedbacks.value = response.value?.data;
  if (window.innerWidth < 739) {
    activities.value = feedbacks.value.map((item) => item).slice(0, 4);
  } else if (window.innerWidth > 740 && window.innerWidth < 1024) {
    activities.value = feedbacks.value.map((item) => item).slice(0, 6);
  } else {
    activities.value = feedbacks.value.map((item) => item).slice(0, 8);
  }

  const allTags = feedbacks.value.flatMap((item) => item.tags);
  const uniqueTagsMap = new Map<string, (typeof allTags)[0]>();

  allTags.forEach((tag) => {
    uniqueTagsMap.set(tag.name, tag);
  });

  uniqueTags.value = Array.from(uniqueTagsMap.values());
});

//Function filter tags by news
const filterTags = (selectedTag: string) => {
  if (selectedTag === 'all') {
    if (window.innerWidth < 739) {
      activities.value = feedbacks.value.map((item) => item).slice(0, 4);
    } else if (window.innerWidth > 740 && window.innerWidth < 1024) {
      activities.value = feedbacks.value.map((item) => item).slice(0, 6);
    } else {
      activities.value = feedbacks.value.map((item) => item).slice(0, 8);
    }
  } else {
    if (window.innerWidth < 739) {
      activities.value = feedbacks.value
        .filter((item) => item.tags.some((tag) => tag.name === selectedTag))
        .slice(0, 4);
    } else if (window.innerWidth > 740 && window.innerWidth < 1024) {
      activities.value = feedbacks.value
        .filter((item) => item.tags.some((tag) => tag.name === selectedTag))
        .slice(0, 6);
    } else {
      activities.value = feedbacks.value
        .filter((item) => item.tags.some((tag) => tag.name === selectedTag))
        .slice(0, 8);
    }
  }
};

const setTags = (temp: string) => {
  tags.value = temp;
  filterTags(temp);
};

const startHold = (index: number) => {
  selectedItem.value = index;
};
const endHold = () => {
  selectedItem.value = -1;
};

onMounted(() => {
  const width = sourceElement.value?.clientWidth;

  if (width !== undefined) {
    if (window.innerWidth >= 1024) {
      itemWidth.value = (width - 200) / 4;
    } else if (window.innerWidth > 740 && window.innerWidth < 1024) {
      itemWidth.value = (width - 145) / 3;
    } else {
      isPhone.value = true;
      itemWidth.value = (width - 35) / 2;
    }
  }

  resizeListener = function () {
    if (width !== undefined) {
      if (window.innerWidth >= 1024) {
        itemWidth.value = (width - 200) / 4;
      } else if (window.innerWidth > 740 && window.innerWidth < 1024) {
        itemWidth.value = (width - 145) / 3;
      } else {
        isPhone.value = true;
        itemWidth.value = (width - 35) / 2;
      }
    }
  };
});

onUnmounted(() => {
  if (window.innerWidth < 739) {
    window.removeEventListener('resize', resizeListener);
  }
});
</script>
<template>
  <div :class="$style.home__activities">
    <h3>CÁC HOẠT ĐỘNG CỦA CÔNG TY</h3>
    <div :class="$style['home__activities-button']">
      <router-link :class="$style['home__activities-link']" to="/tintuc"> Xem tất cả </router-link>
    </div>
    <div :class="$style['home__activities-list']">
      <button
        @click="setTags('all')"
        :style="{
          'background-color': tags === 'all' ? 'white' : '',
          color: tags === 'all' ? '#005796' : ''
        }"
      >
        Tất cả
      </button>
      <button
        v-for="(item, index) in uniqueTags"
        :key="index"
        @click="setTags(item.name)"
        :style="{
          'background-color': tags === item.name ? 'white' : '',
          color: tags === item.name ? '#005796' : ''
        }"
      >
        {{ item.name }}
      </button>
    </div>
    <div :class="$style['home__activities-grid']" ref="sourceElement">
      <router-link
        v-for="(activity, index) in activities"
        :key="index"
        :class="$style['home__activities-item']"
        :style="{ width: itemWidth + 'px' }"
        @touchstart="isPhone ? startHold(index) : ''"
        @touchend="isPhone ? endHold() : ''"
        :to="'/tintuc/' + activity.slug"
      >
        <img :src="activity.img" alt="activity" />
        <div
          :class="$style['home__activities-hover']"
          :style="selectedItem === index ? 'display: flex' : ''"
        >
          <div :class="$style['home__activities-text']">
            <h4>{{ activity.title }}</h4>
            <span>{{ activity.summary }}</span>
          </div>
        </div>
      </router-link>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './HomeActivity.module.scss';
</style>

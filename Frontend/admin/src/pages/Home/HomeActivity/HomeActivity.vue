<script setup lang="ts">
import { ref, watch, onMounted, onUnmounted } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

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

//Properties
let resizeListener: () => void;

//Get hightlights news
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/news/highlight', {}, {}, deps.value);

//Properties
const sourceElement = ref<HTMLElement | null>(null);
const itemWidth = ref(0);
const tags = ref('all');
const feedbacks = ref<Item[]>([]);
const selectedItem = ref(-1);

//Init structure data
const activities = ref<Item[]>([]);
const uniqueTags = ref([
  {
    id: '',
    name: '',
    slug: '',
    createAt: ''
  }
]);

//Truncate text
const truncateText = (str: string, maxLength: number) => {
  if (str.length <= maxLength) {
    return str;
  } else {
    return str.substring(0, maxLength - 3) + '...';
  }
};

//Filter news by Tag
const filterTags = (selectedTag: string) => {
  if (selectedTag === 'all') {
    if (window.innerWidth < 739) {
      activities.value = feedbacks.value.map((item) => item).slice(0, 4);
    } else {
      activities.value = feedbacks.value.map((item) => item).slice(0, 8);
    }
  } else {
    if (window.innerWidth < 739) {
      activities.value = feedbacks.value
        .filter((item) => item.tags.some((tag) => tag.name === selectedTag))
        .slice(0, 4);
    } else {
      activities.value = feedbacks.value
        .filter((item) => item.tags.some((tag) => tag.name === selectedTag))
        .slice(0, 8);
    }
  }
};

//Change Tags
const setTags = (temp: string) => {
  tags.value = temp;
  filterTags(temp);
};

const handleClick = (index: number) => {
  selectedItem.value = index;
};

watch(response, () => {
  feedbacks.value = response.value?.data;
  if (window.innerWidth < 739) {
    activities.value = feedbacks.value.map((item) => item).slice(0, 4);
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

onMounted(() => {
  const width = sourceElement.value?.clientWidth;

  if (width !== undefined) {
    itemWidth.value = (width - 200) / 4;
  }

  resizeListener = function () {
    if (width !== undefined) {
      itemWidth.value = (width - 200) / 4;
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
    <button :class="$style['home__activities-button']">Xem tất cả</button>
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
      <div
        v-for="(activity, index) in activities"
        :key="index"
        :class="$style['home__activities-item']"
        :style="{ width: itemWidth + 'px' }"
        @click="handleClick(index)"
      >
        <img :src="activity.img" alt="activity" />
        <div
          :class="$style['home__activities-hover']"
          :style="selectedItem === index ? 'display: flex' : ''"
        >
          <div :class="$style['home__activities-text']">
            <h4>{{ activity.title }}</h4>
            <span v-html="truncateText(activity.summary, 195)"></span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './HomeActivity.module.scss';
</style>

<script setup lang="ts">
import { ref, watch } from 'vue';
import router from '@/router/index';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

//GET DATA
interface Item {
  news: {
    id: string;
    title: string;
    img: string;
    slug: string;
    summary: string;
    detail: string;
    detailMobile: string;
    highlight: number;
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
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/news/highlight', {}, {}, deps.value);

const feedbacks = ref<Item[]>([]);
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

watch(response, () => {
  feedbacks.value = response.value?.data;
  if (window.innerWidth < 739) {
    activities.value = feedbacks.value.map((item) => item.news).slice(0, 4);
  } else {
    activities.value = feedbacks.value.map((item) => item.news).slice(0, 8);
  }

  const allTags = feedbacks.value.flatMap((item) => item.tags);
  const uniqueTagsMap = new Map<string, (typeof allTags)[0]>();

  allTags.forEach((tag) => {
    uniqueTagsMap.set(tag.name, tag);
  });

  uniqueTags.value = Array.from(uniqueTagsMap.values()); //HỘI NGHỊ THEO THÁNG
});
const selectedItem = ref(-1);

const HandleClick = (index: number) => {
  selectedItem.value = index;
};

const SeeAll = () => {
  router.push('/tintuc');
};
</script>
<template>
  <div :class="$style.home__activities">
    <h3>CÁC HOẠT ĐỘNG CỦA CÔNG TY</h3>
    <button :class="$style['home__activities-button']" @click="SeeAll">Xem tất cả</button>
    <div :class="$style['home__activities-list']">
      <button>Tất cả</button>
      <button v-for="(item, index) in uniqueTags" :key="index">{{ item.name }}</button>
    </div>
    <div :class="$style['home__activities-grid']">
      <div
        v-for="(activity, index) in activities"
        :key="index"
        :class="$style['home__activities-item']"
        @click="HandleClick(index)"
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
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './HomeActivity.module.scss';
</style>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import dayjs from 'dayjs';
import 'dayjs/locale/vi';
import Time from '@/assets/imgs/NewsDetail/Time.png';

interface NewsDetail {
  title: string;
  time: Date;
  summary: string;
  img: string;
  createAt: string;
  detail: string;
  slug: string;
}

interface News {
  slug: string;
  title: string;
}

const props = defineProps({
  data: {
    type: Object,
    required: true
  }
});

const variableChangeNews = ref([]);
const dataRender = ref<NewsDetail>({ ...props.data } as NewsDetail);
const dataNews = ref<News[]>([]);
const indexNews = ref(-1);
const showButtonLeft = ref(true);
const showButtonRight = ref(true);
const contentButtonLeft = ref('');
const contentButtonRight = ref('');

const getNews = useAxios<DataResponse>('get', '/news', {}, {}, variableChangeNews.value);

watch(getNews.response, () => {
  dataNews.value = getNews.response.value?.data?.data;

  dataNews.value.forEach((item, idx) => {
    if (item.slug === dataRender.value.slug) indexNews.value = idx;
  });

  // Xử lí nút button chuyển tin tức
  if (indexNews.value === 0) {
    showButtonLeft.value = false;
    showButtonRight.value = true;
    contentButtonRight.value = dataNews.value[indexNews.value + 1].title;
  } else if (indexNews.value === dataNews.value.length - 1) {
    showButtonLeft.value = true;
    showButtonRight.value = false;
    contentButtonLeft.value = dataNews.value[indexNews.value - 1].title;
  } else {
    showButtonLeft.value = true;
    showButtonRight.value = true;
    contentButtonLeft.value = dataNews.value[indexNews.value - 1].title;
    contentButtonRight.value = dataNews.value[indexNews.value + 1].title;
  }
});

// Format lại thời gian
const handleFormatTime = (time: string) => {
  const inputDate = dayjs(time).locale('vi'); // Đặt ngôn ngữ

  const daysOfWeek = ['Chủ Nhật', 'Thứ Hai', 'Thứ Ba', 'Thứ Tư', 'Thứ Năm', 'Thứ Sáu', 'Thứ Bảy'];

  return `${daysOfWeek[inputDate.day()]}, ${inputDate.format('DD/MM/YYYY, HH:mm [GMT]Z')}`;
};

// Xử lí chuyển bài viết bên trái
const handleClickLeft = () => {
  if (indexNews.value >= 0) {
    window.location.href = `/tintuc/${dataNews.value[indexNews.value - 1].slug}`;
  }
};

// Xử lí chuyển bài viết bên phải
const handleClickRight = () => {
  if (indexNews.value >= 0) {
    window.location.href = `/tintuc/${dataNews.value[indexNews.value + 1].slug}`;
  }
};

// Xử lí resize lại tấm ảnh để phù hợp với mobile
onMounted(() => {
  const parent = document.getElementById('content_body');
  if (parent) {
    const image = ref<HTMLImageElement[] | null>(null);
    const content = ref<HTMLElement[] | null>(null);
    const tagli = ref<HTMLElement[] | null>(null);
    const images = parent.getElementsByTagName('img');
    const contents = parent.getElementsByTagName('p');
    const listli = parent.getElementsByTagName('li');
    const imageArray = Array.from(images);
    const contentArray = Array.from(contents);
    const liArray = Array.from(listli);
    image.value = imageArray;
    content.value = contentArray;
    tagli.value = liArray;

    image.value.forEach((item) => {
      if (window.innerWidth < 736) {
        const aspectRatio = item.height / item.width;
        item.width = window.innerWidth - 30;
        item.height = item.width * aspectRatio;
      } else if (window.innerWidth < 1100) {
        const aspectRatio = item.height / item.width;
        item.width = window.innerWidth - 400;
        item.height = item.width * aspectRatio;
      }
    });

    content.value.forEach((item) => {
      if (window.innerWidth > 1100) {
        item.style.lineHeight = '1.6';
      }
    });

    tagli.value.forEach((item) => {
      if (window.innerWidth > 1100) {
        item.style.lineHeight = '1.6';
        item.style.paddingLeft = '15px';
      }
    });
  }
});
</script>
<template>
  <div :class="$style.newsdetail__content">
    <h1>{{ dataRender.title }}</h1>

    <div :class="$style['newsdetail__content-time']">
      <img :src="Time" alt="" />

      <p>{{ handleFormatTime(dataRender.createAt) }}</p>
    </div>

    <div :class="$style['newsdetail__content-main']">
      <div
        id="content_body"
        :class="$style['newsdetail__content-main-wrap']"
        v-html="dataRender.detail"
      ></div>

      <div
        :class="$style['newsdetail__content-main-footer']"
        :style="{ justifyContent: !showButtonLeft ? 'right' : 'none' }"
      >
        <button
          :class="$style['newsdetail__content-main-footer-button-left']"
          @click="handleClickLeft"
          v-if="showButtonLeft"
        >
          <font-awesome-icon
            :icon="faChevronLeft"
            :class="$style['newsdetail__content-main-footer-button-left-ic']"
          />
          <p :class="$style['newsdetail__content-main-footer-button-left-content']">
            {{ contentButtonLeft }}
          </p>
        </button>
        <button
          :class="$style['newsdetail__content-main-footer-button-right']"
          @click="handleClickRight"
          v-if="showButtonRight"
        >
          <p :class="$style['newsdetail__content-main-footer-button-right-conten']">
            {{ contentButtonRight }}
          </p>
          <font-awesome-icon
            :icon="faChevronRight"
            :class="$style['newsdetail__content-main-footer-button-right-ic']"
          />
        </button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './NewsDetailContent.module.scss';
</style>

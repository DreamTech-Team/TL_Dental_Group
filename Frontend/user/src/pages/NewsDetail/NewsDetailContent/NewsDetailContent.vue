<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import dayjs from 'dayjs';
import 'dayjs/locale/vi';
import Time from '@/assets/imgs/NewsDetail/Time.png';
import { useRouter } from 'vue-router';

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

// Gọi hàm useAxios để lấy response, error, và isLoading
const getNews = useAxios<DataResponse>('get', '/news', {}, {}, variableChangeNews.value);

// Truy xuất giá trị response.value và gán vào responseData
watch(getNews.response, () => {
  dataNews.value = getNews.response.value?.data?.data;

  dataNews.value.forEach((item, idx) => {
    if (item.slug === dataRender.value.slug) indexNews.value = idx;
  });

  if (indexNews.value === 0) {
    showButtonLeft.value = false;
    showButtonRight.value = true;
    contentButtonRight.value = dataNews.value[indexNews.value + 1].title;
  } else if (indexNews.value === dataNews.value.length - 1) {
    showButtonLeft.value = false;
    showButtonRight.value = true;
    contentButtonLeft.value = dataNews.value[indexNews.value - 1].title;
  } else {
    showButtonLeft.value = true;
    showButtonRight.value = true;
    contentButtonLeft.value = dataNews.value[indexNews.value - 1].title;
    contentButtonRight.value = dataNews.value[indexNews.value + 1].title;
  }
});

const handleFormatTime = (time: string) => {
  const inputDate = dayjs(time).locale('vi'); // Đặt ngôn ngữ

  const daysOfWeek = ['Chủ Nhật', 'Thứ Hai', 'Thứ Ba', 'Thứ Tư', 'Thứ Năm', 'Thứ Sáu', 'Thứ Bảy'];

  return `${daysOfWeek[inputDate.day()]}, ${inputDate.format('DD/MM/YYYY, HH:mm [GMT]Z')}`;
};

const handleClickLeft = () => {
  if (indexNews.value >= 0) {
    window.location.href = `/tintuc/${dataNews.value[indexNews.value - 1].slug}`;
  }
};

const handleClickRight = () => {
  if (indexNews.value >= 0) {
    window.location.href = `/tintuc/${dataNews.value[indexNews.value + 1].slug}`;
  }
};

onMounted(() => {
  const parent = document.getElementById('haha');
  // parent.getElementsByTagName('img');
  if (parent) {
    const a = ref<HTMLImageElement[] | null>(null); // Initialize as null or an empty array based on your use case
    const images = parent.getElementsByTagName('img');
    const imageArray = Array.from(images); // Convert NodeList to an array
    a.value = imageArray;

    a.value.forEach((item) => {
      if (window.innerWidth < 736) {
        const aspectRatio = item.height / item.width;
        item.width = window.innerWidth - 30;
        item.height = item.width * aspectRatio;
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
      <!-- <div :class="$style['newsdetail__content-main-header']">
        <p>{{ dataRender.summary }}</p>
        <img :src="dataRender.img" alt="" />
      </div> -->

      <div
        id="haha"
        :class="$style['newsdetail__content-main-wrap']"
        v-html="dataRender.detail"
      ></div>

      <!-- <div :class="$style['newsdetail__content-main-body']">
        <h3>Lẫy mẫu test Covid - 19</h3>
        <div>
          <p>
            Phát huy tinh thần “Đâu cần thanh niên có, đâu khó có thanh niên”, chúng tôi thực hiện
            lấy mẫu cho người dân ở địa phương mỗi ngày, giúp đỡ những người dân cần lấy mẫu để đi
            xa. Những ngày lâm bệnh, anh chia sẻ rằng rất nhớ những ngày cùng mọi người đi hỗ trợ
            lấy mẫu xét nghiệm, phát gạo, chuyển thức ăn đến các hộ dân, rồi lại lo lắng anh em sẽ
            phải gánh thêm phần việc của mình... Chỉ sau 7 ngày tự cách ly, điều trị tại nhà, anh
            hầu như khỏi bệnh. Và ngay sau khi hoàn thành đợt cách ly, anh Hải đã lập tức quay trở
            lại và tiếp tục công việc của mình tại địa phương.
          </p>
          <img :src="Test" alt="" />
        </div>
      </div>

      <div :class="$style['newsdetail__content-main-body']">
        <h3>Lẫy mẫu test Covid - 19</h3>
        <div>
          <p>
            Phát huy tinh thần “Đâu cần thanh niên có, đâu khó có thanh niên”, chúng tôi thực hiện
            lấy mẫu cho người dân ở địa phương mỗi ngày, giúp đỡ những người dân cần lấy mẫu để đi
            xa. Những ngày lâm bệnh, anh chia sẻ rằng rất nhớ những ngày cùng mọi người đi hỗ trợ
            lấy mẫu xét nghiệm, phát gạo, chuyển thức ăn đến các hộ dân, rồi lại lo lắng anh em sẽ
            phải gánh thêm phần việc của mình... Chỉ sau 7 ngày tự cách ly, điều trị tại nhà, anh
            hầu như khỏi bệnh. Và ngay sau khi hoàn thành đợt cách ly, anh Hải đã lập tức quay trở
            lại và tiếp tục công việc của mình tại địa phương.
          </p>
          <img :src="Test" alt="" />
        </div>
      </div> -->

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

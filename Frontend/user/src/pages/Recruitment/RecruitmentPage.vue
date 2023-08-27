<!-- eslint-disable @typescript-eslint/no-explicit-any -->
<script setup lang="ts">
import {
  girl1,
  girl2,
  img1,
  img2,
  img3,
  img4,
  img5,
  ceo,
  imgHand,
  ic_clock,
  ic_DaiNgo,
  ic_light
} from '@/assets/imgs/Recruitment/RecruitmentImgs';
import {
  recStep,
  recStepItems
  // recruitWorkItems
} from './RecruitmentHandle';
import RecruitmentCard from './RecruitmentCard/RecruitmentCard.vue';
import RecruitmentCardWork from './RecruitmentCardWork/RecruitmentCardWork.vue';
import { ref, onMounted, type Ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

// import RecruitmentPoster from './RecruitmentPoster/RecruitmentPoster.vue';
// import RecruitmentVision from './RecruitmentVision/RecruitmentVision.vue';
// import RecruitmentValue from './RecruitmentValue/RecruitmentValue.vue';
// import RecruitmentEnviroment from './RecruitmentEnviroment/RecruitmentEnviroment.vue';
// import RecruitmentNavScroll from './RecruitmentNavScroll/RecruitmentNavScroll.vue';
// import RecruitmentWork from './RecruitmentWork/RecruitmentWork.vue';

import TheAnimate from '@/components/TheAnimate/TheAnimate.vue';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface CardElementItem {
  id: string;
  icon: { link: string; style: string };
  title: { content: string; style: string };
  content: { content: string; style: string };
  image: { link: string; style: string };
  type: string;
}

interface ListImage {
  id: string;
  image: string;
  type: string;
}

interface WorkItem {
  id: string;
  title: string;
  typeWork: string;
  time: string;
  location: string;
}
const itemSeleted = ref(0);
const hiddenElement = ref(false);
const showMore = ref(false);
const hiddenShowMore = ref(false);
const paramAxios = ref();

const iconCard = [ic_clock, ic_DaiNgo, ic_light];
const contentPosterItems: Ref<CardElementItem[]> = ref([]);
const contentVisionItems: Ref<CardElementItem[]> = ref([]);
const imageVisionItems: Ref<ListImage[]> = ref([]);
const contentValueItems: Ref<CardElementItem[]> = ref([]);
const contentValueMainItem = ref();
const recruitWorkItems: Ref<WorkItem[]> = ref([]);
const screenWidth = ref(true);
const isLoading = ref([false, false, false, false]);

const callApiContentPoster = () => {
  //Lấy nội dung của poster
  const getContentPoster = useAxios<DataResponse>(
    'get',
    '/recruitment/header',
    {},
    {},
    paramAxios.value
  );

  watch(getContentPoster.isLoading, (value) => {
    isLoading.value[0] = value;
  });

  watch(getContentPoster.response, (value) => {
    const tmp = value?.data;

    tmp.forEach((value: { [x: string]: any }, index: number) => {
      contentPosterItems.value.push({
        id: value.id,
        icon: {
          link: iconCard[index],
          style: `icon${index + 1}`
        },
        title: { content: value.title, style: 'type1' },
        content: { content: value.content, style: 'type1' },
        image: { link: '', style: '' },
        type: value.type
      });
    });
  });
};

const callApiContentVision = () => {
  //Lấy nội dung của Vision
  const getContentVision = useAxios<DataResponse>(
    'get',
    '/recruitment/section1',
    {},
    {},
    paramAxios.value
  );

  watch(getContentVision.isLoading, (value) => {
    isLoading.value[1] = value;
  });

  watch(getContentVision.error, (value) => console.log(value));

  watch(getContentVision.response, (value) => {
    const dataArr = value?.data;

    imageVisionItems.value[0] = {
      id: dataArr.image1.id,
      image: dataArr.image1.image,
      type: dataArr.image1.type
    };
    imageVisionItems.value[1] = {
      id: dataArr.image2.id,
      image: dataArr.image2.image,
      type: dataArr.image2.type
    };

    contentVisionItems.value.push({
      id: dataArr.subItem1.id,
      icon: {
        link: '',
        style: ''
      },
      title: { content: dataArr.subItem1.title, style: 'type2' },
      content: { content: dataArr.subItem1.content, style: 'type2' },
      image: { link: '', style: '' },
      type: dataArr.subItem1.type
    });
    contentVisionItems.value.push({
      id: dataArr.subItem2.id,
      icon: {
        link: '',
        style: ''
      },
      title: { content: dataArr.subItem2.title, style: 'type2' },
      content: { content: dataArr.subItem2.content, style: 'type2' },
      image: { link: '', style: '' },
      type: dataArr.subItem2.type
    });
  });
};

const callApiContentValue = () => {
  //Lấy nội dung của Value
  const getContentValue = useAxios<DataResponse>(
    'get',
    '/recruitment/section2',
    {},
    {},
    paramAxios.value
  );

  watch(getContentValue.isLoading, (value) => {
    isLoading.value[2] = value;
  });

  watch(getContentValue.error, (value) => console.log(value));

  watch(getContentValue.response, (value) => {
    const tmp = value?.data;
    contentValueItems.value = [];

    tmp.subItem.forEach((value: { [x: string]: any }) => {
      contentValueItems.value.push({
        id: value.id,
        icon: {
          link: '',
          style: ''
        },
        title: { content: value.title, style: 'type3' },
        content: { content: value.content, style: 'type3' },
        image: { link: '', style: '' },
        type: value.type
      });
    });

    contentValueMainItem.value = tmp.mainItem.content;
  });
};

const callApiPositionRecruitment = () => {
  const getPositionRecruitment = useAxios<DataResponse>(
    'get',
    '/recruitment/',
    {},
    {},
    paramAxios.value
  );

  watch(getPositionRecruitment.isLoading, (value) => {
    isLoading.value[3] = value;
  });

  watch(getPositionRecruitment.error, (value) => {
    console.log(value);
  });

  watch(getPositionRecruitment.response, (value) => {
    // console.log(value?.data);
    const tmp = value?.data.data;
    recruitWorkItems.value = [];

    tmp.forEach(
      (value: { id: any; title: any; position: any; working_form: any; location: any }) => {
        recruitWorkItems.value.push({
          id: value.id,
          title: value.title,
          typeWork: value.position,
          time: value.working_form,
          location: value.location
        });
      }
    );

    if (tmp.length > 6) {
      hiddenShowMore.value = true;
    }
  });
};

callApiContentPoster();
callApiContentVision();
callApiContentValue();
callApiPositionRecruitment();

//Hàm set animation của element tuyển dụng
const handleScroll = () => {
  const element = document.getElementById('page');
  const rect = element?.getBoundingClientRect();
  const oneItemHeight = Number(element?.offsetHeight) / recStep.length;
  const topParent = Number(rect?.top);
  // console.log(rect?.top, element?.offsetHeight);

  if (topParent < 0 && screen.width > 739) {
    const index = Math.abs(topParent / oneItemHeight);
    itemSeleted.value = Number(index.toFixed());
    // console.log(itemSeleted.value);

    const locationHidden = (recStep.length - 19 / 20) * oneItemHeight;

    if (-topParent > locationHidden) hiddenElement.value = true;
    else hiddenElement.value = false;
    // console.log(locationHidden, hiddenElement.value);
  }
};

const hanldeScrollToVacancies = () => {
  const element = document.getElementById('position-rec');
  element?.scrollIntoView({ behavior: 'smooth', block: 'nearest', inline: 'nearest' });
};

//Hàm cập nhật item sau khi loading
const showPageCompleted = () => {
  showMore.value = false;
  // if (!recruitWorkItems.value) {
  //   recruitWorkItems.value = [...recruitWorkItems];
  // } else recruitWorkItems.value.forEach((item) => recruitWorkItems.value.push(item));
  // console.log(recruitWorkItems.value, recruitWorkItems);
};

//Hàm loading
const openLoading = () => {
  showMore.value = true;
  setTimeout(showPageCompleted, 3000);
};

const checkScreenWidth = () => {
  const currentWidth = window.innerWidth;
  screenWidth.value = currentWidth > 739;
};

const handleScrollToTopOfStepRec = () => {
  const element = document.getElementById(`page`);
  element?.scrollIntoView({ behavior: 'smooth', block: 'start', inline: 'nearest' });
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
  window.addEventListener('resize', checkScreenWidth);
});
</script>
<template>
  <LoadingComponent v-if="isLoading[0] || isLoading[1] || isLoading[2] || isLoading[3]" />
  <div v-else :class="$style.container">
    <div :class="$style.container__poster">
      <div :class="$style['container__poster-img']">
        <div :class="$style['container__poster-img-content']">
          <div class="">
            <img :src="girl1" alt="none" />
          </div>
          <div class="">
            <img :src="girl2" alt="none" />
          </div>
        </div>
      </div>
      <div :class="$style['container__poster-title']">
        <h4>Với bề dày hơn <span>2 năm</span> kinh doanh và phát triển</h4>
        <h3>Công ty sở hữu số lượng vật liệu và dụng cụ nha khoa</h3>
        <h2>LỚN NHẤT VIỆT NAM</h2>
        <h1>
          <span style="position: relative"
            >#TOP1
            <the-animate />
          </span>
        </h1>
      </div>
      <div :class="$style['container__poster-value']">
        <div :class="$style['container__poster-value-item']">
          Hơn <span>2000</span> vật liệu chỉnh nha
        </div>
      </div>
      <div :class="$style['container__poster-btn']">
        <div :class="$style['container__poster-btn-item']" @click="hanldeScrollToVacancies">
          <p>Xem vị trí tuyển dụng</p>
        </div>
      </div>
      <div :class="$style['container__poster-block']">
        <div :class="$style['container__poster-block-card']">
          <recruitment-card :items="contentPosterItems" :style="'type1'" />
        </div>
      </div>
    </div>
    <div :class="$style.container__vision">
      <div :class="$style['container__vision-block']">
        <div :class="$style['container__vision-block-img1']">
          <img :src="imgHand" alt="none" />
        </div>
        <div :class="$style['container__vision-block-img2']">
          <img :src="ceo" alt="none" />
        </div>
      </div>
      <div :class="$style['container__vision-content']">
        <recruitment-card :items="contentVisionItems" :style="'type2'" />
      </div>
    </div>
    <div style="padding: 5rem">
      <div :class="$style.container__value">
        <div :class="$style['container__value-heading']">
          <div :class="$style['container__value-heading-title']">
            <div :class="$style['container__value-heading-title-main']">
              <div :class="$style['container__value-heading-title-main-1']">Giá Trị</div>
              <div :class="$style['container__value-heading-title-main-2']">Cốt Lõi</div>
            </div>
            <div :class="$style['container__value-heading-title-3']">Từ TL Dental Group</div>
          </div>
          <div :class="$style['container__value-heading-content']">
            <p>
              {{ contentValueMainItem }}
            </p>
          </div>
        </div>
        <div :class="$style['container__value-list']">
          <recruitment-card :items="contentValueItems" :style="'type3'" />
        </div>
      </div>
    </div>
    <div :class="$style.container__envir">
      <div :class="$style['container__envir-title']">Môi Trường Làm Việc Chuyên Nghiệp</div>
      <div :class="$style['container__envir-block']">
        <div
          :class="[
            $style['container__envir-block-img1'],
            $style['container__envir-block-img1-bottom']
          ]"
        >
          <img :src="img1" alt="none" />
        </div>
        <div :class="$style['container__envir-block-img2']">
          <img :src="img2" alt="none" />
          <div
            :class="[
              $style['container__envir-block-img2-circle'],
              $style['container__envir-block-img2-circle-1']
            ]"
          ></div>
        </div>
        <div
          :class="[
            $style['container__envir-block-img1'],
            $style['container__envir-block-img1-center']
          ]"
        >
          <img :src="img3" alt="none" />
        </div>
        <div
          :class="[
            $style['container__envir-block-img2'],
            $style['container__envir-block-img2-bottom']
          ]"
        >
          <div>
            <div
              :class="[
                $style['container__envir-block-img2-circle'],
                $style['container__envir-block-img2-circle-2']
              ]"
            ></div>
            <img :src="img4" alt="none" />
          </div>
        </div>
        <div
          :class="[
            $style['container__envir-block-img1'],
            $style['container__envir-block-img1-top']
          ]"
        >
          <img :src="img5" alt="none" />
        </div>
      </div>
    </div>
    <div :class="$style.container__recruit">
      <div
        :class="[
          $style['container__recruit-left'],
          $style['sticky-container'],
          $style[hiddenElement ? 'display-none' : '']
        ]"
      >
        <div :class="$style['container__recruit-left-title']">
          <span>Tuyển dụng TL Dental Group</span>
          <h2>Quy Trình Tuyển Dụng</h2>
        </div>
        <div :class="$style['container__recruit-left-nav']">
          <recruitment-card
            :items="recStep"
            :content="recStepItems"
            :style="'type4'"
            :on-selected="itemSeleted"
            :handleScrollToTopOfStepRec="handleScrollToTopOfStepRec"
          />
        </div>
      </div>
      <div v-if="screenWidth" :class="$style['container__recruit-right']" id="page">
        <recruitment-card :items="recStepItems" :style="'type5'" />
      </div>
    </div>
    <div :class="$style.container__work" id="position-rec">
      <div :class="$style['container__work-heading']">
        <div :class="$style['container__work-heading-title']">Các Vị Trí Tuyển Dụng</div>
        <div :class="$style['container__work-heading-filter']">
          <input type="text" name="filter" id="" placeholder="Tìm kiếm" />
        </div>
      </div>
      <div :class="$style['container__work-staff']">
        <div
          :class="$style['container__work-staff-item']"
          v-for="(item, index) in recruitWorkItems"
          :key="index"
        >
          <recruitment-card-work :infor="item" />
        </div>
      </div>
      <div v-if="hiddenShowMore">
        <div :id="$style.loader" v-if="showMore"></div>
        <div :class="$style['container__work-btn']" v-else @click="openLoading">
          <p>Xem thêm</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentPage.module.scss';
</style>

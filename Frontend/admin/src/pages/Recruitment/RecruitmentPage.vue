<!-- eslint-disable @typescript-eslint/no-explicit-any -->
<script setup lang="ts">
import { ref, watch, type Ref, onMounted } from 'vue';

import { ic_clock, ic_DaiNgo, ic_light } from '@/assets/imgs/Recruitment/RecruitmentImgs';

import ModalUpdateContent from './ModalUpdateContent/ModalUpdateContent.vue';
import RecruitmentPoster from './RecruitmentPoster/RecruitmentPoster.vue';
import RecruitmentVision from './RecruitmentVision/RecruitmentVision.vue';
import RecruitmentValue from './RecruitmentValue/RecruitmentValue.vue';
import RecruitmentEnviroment from './RecruitmentEnviroment/RecruitmentEnviroment.vue';
import RecruitmentNavScroll from './RecruitmentNavScroll/RecruitmentNavScroll.vue';
// import RecruitmentWork from './RecruitmentWork/RecruitmentWork.vue';
import type { DataResponse } from '@/hooks/useAxios';
import useAxios from '@/hooks/useAxios';
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

const topics = {
  poster: 'poster',
  vision: 'vision',
  value: 'value'
};

const iconCard = [ic_clock, ic_DaiNgo, ic_light];

const elementEventRec = ref();

const contentEdit = ref();
const topicUpdate = ref();

const isOpen = ref(false);

const contentPosterItems: Ref<CardElementItem[]> = ref([]);
const contentVisionItems: Ref<CardElementItem[]> = ref([]);
const imageVisionItems: Ref<ListImage[]> = ref([]);
const contentValueItems: Ref<CardElementItem[]> = ref([]);
const contentValueMainItem = ref();
const limitContent = ref(-1);
const hiddenCustomizeModal = ref(false);
const paramAxios = ref();
const cntIsLoading = ref(0);

const callApiContentPoster = () => {
  //Lấy nội dung của poster
  const getContentPoster = useAxios<DataResponse>(
    'get',
    '/recruitment/header',
    {},
    {},
    paramAxios.value
  );

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

    // console.log('poster: ', contentPosterItems.value);
  });

  watch(getContentPoster.isLoading, (value) => {
    if (!value) cntIsLoading.value++;
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

  watch(getContentVision.error, (value) => console.log(value));
  watch(getContentVision.isLoading, (value) => {
    if (!value) cntIsLoading.value++;
  });

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

  watch(getContentValue.error, (value) => console.log(value));

  watch(getContentValue.isLoading, (value) => {
    if (!value) cntIsLoading.value++;
  });

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

    contentValueMainItem.value = {
      id: tmp.mainItem.id,
      content: tmp.mainItem.content,
      type: tmp.mainItem.type
    };
  });
};

const handleEditPoster = () => {
  topicUpdate.value = topics.poster;
  hiddenCustomizeModal.value = true;
  contentEdit.value = [];

  contentPosterItems.value?.forEach((value) =>
    contentEdit.value.push({
      key: value.title.content.replace(/[\s~`!@#$%^&*()_+={}[\]:;<>,.?/\\|'"-]/g, ''),
      title: value.title.content,
      content: value.content.content
    })
  );

  isOpen.value = true;
  disableScroll();
};

const handleUpdatePoster = (newContent: any) => {
  topicUpdate.value = '';
  hiddenCustomizeModal.value = false;
  const newContentArray: CardElementItem[] = [];
  const dataPoster: any = [];

  newContent.forEach((value: { title: string; content: any }, index: number) => {
    dataPoster.push({
      id: contentPosterItems.value[index].id,
      title: value.title,
      content: value.content,
      type: contentPosterItems.value[index].type
    });

    newContentArray.push({
      id: contentPosterItems.value[index].id,
      icon: {
        link: iconCard[index],
        style: `icon${index + 1}`
      },
      title: { content: value.title, style: 'type1' },
      content: { content: value.content, style: 'type1' },
      image: { link: '', style: '' },
      type: contentPosterItems.value[0].type
    });
  });

  const patchContentPoster = useAxios<DataResponse>(
    'patch',
    `/recruitment/header`,
    dataPoster,
    {},
    paramAxios.value
  );

  watch(patchContentPoster.response, () => {
    contentPosterItems.value = [];
    contentPosterItems.value = newContentArray;
    // console.log(value);
  });
};

const handleEditVision = () => {
  topicUpdate.value = topics.vision;
  hiddenCustomizeModal.value = true;
  contentEdit.value = [];

  contentVisionItems.value.forEach((value) =>
    contentEdit.value.push({
      key: value.title.content.replace(/[\s~`!@#$%^&*()_+={}[\]:;<>,.?/\\|'"-]/g, ''),
      title: value.title.content,
      content: value.content.content
    })
  );

  isOpen.value = true;
  disableScroll();
};

const handleUpdateVision = (newContent: any) => {
  topicUpdate.value = '';
  hiddenCustomizeModal.value = false;
  const newContentArray: CardElementItem[] = [];
  const dataVision: any = [];

  newContent.forEach((value: any, index: number) => {
    dataVision.push({
      id: contentVisionItems.value[index].id,
      title: value.title,
      content: value.content,
      type: contentVisionItems.value[index].type
    });

    newContentArray.push({
      id: contentVisionItems.value[index].id,
      icon: { link: '', style: '' },
      title: { content: value.title, style: 'type2' },
      content: { content: value.content, style: 'type2' },
      image: { link: '', style: '' },
      type: contentVisionItems.value[index].type
    });
  });

  const objData = {
    subItem1: dataVision[0],
    subItem2: dataVision[1],
    image1: imageVisionItems.value[0],
    image2: imageVisionItems.value[1]
  };
  const data = new FormData();
  data.append('data', JSON.stringify(objData));

  const patchContentVision = useAxios<DataResponse>(
    'patch',
    `/recruitment/section1`,
    data,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    },
    paramAxios.value
  );

  watch(patchContentVision.response, () => {
    contentVisionItems.value = [];
    contentVisionItems.value = newContentArray;
    // console.log(value);
  });
};

const handleEditValue = () => {
  topicUpdate.value = topics.value;
  contentEdit.value = [];
  // limitContent.value = 2;

  contentValueItems.value.forEach((value) =>
    contentEdit.value.push({
      key: value.title.content.replace(/[\s~`!@#$%^&*()_+={}[\]:;<>,.?/\\|'"-]/g, ''),
      title: value.title.content,
      content: value.content.content
    })
  );

  isOpen.value = true;
  disableScroll();
};

const handleUpdateValue = (newContent: any) => {
  topicUpdate.value = '';
  const subItems: { id: string; title: string; content: string; type: string }[] = [];
  const listDelete = [];

  newContent.forEach((value: { title: any; content: any }, index: number) => {
    const idItem = contentValueItems.value.length > index ? contentValueItems.value[index].id : '';
    subItems.push({
      id: idItem,
      title: value.title,
      content: value.content,
      type: contentValueItems.value[0].type
    });
  });

  if (newContent.length < contentValueItems.value.length) {
    for (let i = newContent.length - 1; i < contentValueItems.value.length; i++)
      listDelete.push(contentValueItems.value[i].id);
  }

  const dataUpdate = {
    mainItem: contentValueMainItem.value,
    subItem: subItems,
    deletedSubItem: listDelete
  };
  // console.log(dataUpdate);

  const patchContentValue = useAxios<DataResponse>(
    'patch',
    '/recruitment/section2',
    dataUpdate,
    {},
    paramAxios.value
  );

  watch(patchContentValue.error, (error) => console.log(error));

  watch(patchContentValue.response, () => {
    callApiContentValue();
    cntIsLoading.value = 3;
    // console.log(value);
  });
};

const handleUpdated = (newContent: any) => {
  if (topicUpdate.value === topics.poster) handleUpdatePoster(newContent);
  else if (topicUpdate.value === topics.vision) handleUpdateVision(newContent);
  else handleUpdateValue(newContent);
};

const handleModalClose = () => {
  isOpen.value = false;
  hiddenCustomizeModal.value = false;
  enableScroll();
};

// Ngăn chặn cuộn chuột
function disableScroll() {
  // Lưu trữ vị trí cuộn hiện tại
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
  const scrollLeft = window.pageXOffset || document.documentElement.scrollLeft;

  // Lưu trữ vị trí cuộn hiện tại dưới dạng CSS
  document.body.style.top = `-${scrollTop}px`;
  document.body.style.left = `-${scrollLeft}px`;
  document.body.style.position = 'fixed';
  document.body.style.overflow = 'hidden';
}

// Cho phép cuộn chuột
function enableScroll() {
  // Khôi phục vị trí cuộn ban đầu
  const scrollTop = parseInt(document.body.style.top, 10);
  const scrollLeft = parseInt(document.body.style.left, 10);

  document.body.style.position = '';
  document.body.style.overflow = '';
  document.body.style.top = '';
  document.body.style.left = '';

  // Cuộn lại đến vị trí ban đầu
  window.scrollTo(-scrollLeft, -scrollTop);
}

const hanldeScrollToVacancies = () => {
  const element = elementEventRec.value;
  element?.scrollIntoView({ behavior: 'smooth', block: 'end', inline: 'nearest' });
};

onMounted(() => {
  callApiContentPoster();
  callApiContentVision();
  callApiContentValue();
});
</script>
<template>
  <loading-component v-if="cntIsLoading < 3" />
  <div v-else :class="$style.container">
    <modal-update-content
      v-if="isOpen"
      @close="handleModalClose"
      :contentEdit="contentEdit"
      :handleUpdated="handleUpdated"
      :limit-content="limitContent"
      :hidden-customize="hiddenCustomizeModal"
    />
    <recruitment-poster
      :hanldeScrollToVacancies="hanldeScrollToVacancies"
      :handleEditPoster="handleEditPoster"
      :contentPosterItems="contentPosterItems"
    />
    <recruitment-vision
      :handleEditVision="handleEditVision"
      :contentVisionItems="contentVisionItems"
      :imageVisionItems="imageVisionItems"
    />
    <recruitment-value
      :handleEditValue="handleEditValue"
      :contentValueItems="contentValueItems"
      :contentValueMainItem="contentValueMainItem"
    />
    <recruitment-enviroment />
    <recruitment-nav-scroll />
    <!-- <recruitment-work :getElementRec="getElementRec" /> -->
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentPage.module.scss';
</style>

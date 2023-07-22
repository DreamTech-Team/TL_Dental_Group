<!-- eslint-disable @typescript-eslint/no-explicit-any -->
<script setup lang="ts">
import { ref } from 'vue';

import { posterItems, visionItems, valueItems } from './RecruitmentHandle';

import ModalUpdateContent from './ModalUpdateContent/ModalUpdateContent.vue';
import RecruitmentPoster from './RecruitmentPoster/RecruitmentPoster.vue';
import RecruitmentVision from './RecruitmentVision/RecruitmentVision.vue';
import RecruitmentValue from './RecruitmentValue/RecruitmentValue.vue';
import RecruitmentEnviroment from './RecruitmentEnviroment/RecruitmentEnviroment.vue';
import RecruitmentNavScroll from './RecruitmentNavScroll/RecruitmentNavScroll.vue';
import RecruitmentWork from './RecruitmentWork/RecruitmentWork.vue';

const topics = {
  poster: 'poster',
  vision: 'vision',
  value: 'value'
};

const elementEventRec = ref();

const contentEdit = ref();
const topicUpdate = ref();

const isOpen = ref(false);

const contentPosterItems = ref(posterItems);
const contentVisionItems = ref(visionItems);
const contentValueItems = ref(valueItems);

const handleEditPoster = () => {
  topicUpdate.value = topics.poster;
  contentEdit.value = [];

  contentPosterItems.value.forEach((value) =>
    contentEdit.value.push({
      title: value.title.content,
      content: value.content.content
    })
  );

  isOpen.value = true;
  disableScroll();
};

const handleUpdatePoster = (newContent: any) => {
  topicUpdate.value = '';
  contentPosterItems.value = [];

  newContent.forEach((value: any, index: number) => {
    contentPosterItems.value.push({
      icon: {
        link: posterItems[index].icon.link || posterItems[0].icon.link,
        style: posterItems[index].icon.style || posterItems[0].icon.style
      },
      title: { content: value.title, style: 'type1' },
      content: { content: value.content, style: 'type1' },
      image: { link: '', style: '' }
    });
  });
};

const handleEditVision = () => {
  topicUpdate.value = topics.vision;
  contentEdit.value = [];

  contentVisionItems.value.forEach((value) =>
    contentEdit.value.push({
      title: value.title.content,
      content: value.content.content
    })
  );

  isOpen.value = true;
  disableScroll();
};

const handleUpdateVision = (newContent: any) => {
  topicUpdate.value = '';
  contentVisionItems.value = [];

  newContent.forEach((value: any) => {
    contentVisionItems.value.push({
      icon: '',
      title: { content: value.title, style: 'type2' },
      content: { content: value.content, style: 'type2' },
      image: { link: '', style: '' }
    });
  });
};

const handleEditValue = () => {
  topicUpdate.value = topics.value;
  contentEdit.value = [];

  contentValueItems.value.forEach((value) =>
    contentEdit.value.push({
      title: value.title.content,
      content: value.content.content
    })
  );

  isOpen.value = true;
  disableScroll();
};

const handleUpdateValue = (newContent: any) => {
  topicUpdate.value = '';
  contentValueItems.value = [];

  newContent.forEach((value: any) => {
    contentValueItems.value.push({
      icon: '',
      title: { content: value.title, style: 'type3' },
      content: { content: value.content, style: 'type3' },
      image: { link: '', style: '' }
    });
  });
};

const handleUpdated = (newContent: any) => {
  if (topicUpdate.value === topics.poster) handleUpdatePoster(newContent);
  else if (topicUpdate.value === topics.vision) handleUpdateVision(newContent);
  else handleUpdateValue(newContent);
};

const handleModalClose = () => {
  isOpen.value = false;
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

const getElementRec = (e: Event) => (elementEventRec.value = e);

const hanldeScrollToVacancies = () => {
  const element = elementEventRec.value;
  element?.scrollIntoView({ behavior: 'smooth', block: 'end', inline: 'nearest' });
};
</script>
<template>
  <div :class="$style.container">
    <modal-update-content
      v-if="isOpen"
      @close="handleModalClose"
      :contentEdit="contentEdit"
      :handleUpdated="handleUpdated"
    />
    <recruitment-poster
      :hanldeScrollToVacancies="hanldeScrollToVacancies"
      :handleEditPoster="handleEditPoster"
      :contentPosterItems="contentPosterItems"
    />
    <recruitment-vision
      :handleEditVision="handleEditVision"
      :contentVisionItems="contentVisionItems"
    />
    <recruitment-value :handleEditValue="handleEditValue" :contentValueItems="contentValueItems" />
    <recruitment-enviroment />
    <recruitment-nav-scroll />
    <recruitment-work :getElementRec="getElementRec" />
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentPage.module.scss';
</style>

<script setup lang="ts">
import { ref, watch } from 'vue';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
import ModalIntro from './ModalIntro.vue';
import ModalCam from './CamIntro.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

//Status modal
const isOpen = ref(false);
const isOpenCam = ref(false);

//GET information
const deps = ref([]);
const nameComp = ref([]);
const { response } = useAxios<DataResponse>('get', '/home/section3', {}, {}, deps.value);
const getLogo = useAxios<DataResponse>('get', '/information?type=GENERAL', {}, {}, deps.value);

const content = ref({
  id: '',
  title: ``,
  context: ``,
  image: ''
});

const onUpdateContent = (data: object) => {
  content.value = { ...content.value, ...data };
};

watch(response, () => {
  content.value.id = response.value?.data?.id;
  content.value.image = response.value?.data?.image;
  content.value.title = response.value?.data?.title;
  content.value.context = response.value?.data?.content;
});

watch(getLogo.response, () => {
  nameComp.value = getLogo.response.value?.data?.name.content;
});
</script>
<template>
  <div :class="$style.home__intro">
    <div :class="$style['home__intro-left']">
      <h3 v-html="content.title"></h3>
      <button :class="$style['home__intro-btn']">XEM CHI TIẾT</button>
    </div>
    <div :class="$style['home__intro-center']">
      <img :src="content.image" alt="CEO" />
      <CamBtn @click="isOpenCam = true" />
      <div :class="$style['home__intro-description']">
        <div :class="$style['home__intro-logo']">
          <h3>{{ nameComp }}</h3>
        </div>
        <div :class="$style['home__intro-speach']" v-html="content.context"></div>
      </div>
    </div>
    <div :class="$style['home__intro-right']"></div>
    <EditBtn style="top: 90px" @click="isOpen = true" />
  </div>
  <ModalIntro
    v-if="isOpen"
    @close="isOpen = false"
    @update-content="onUpdateContent"
    :uuid="content.id"
    :title="content.title"
    :tags="content.context"
    :image="content.image"
  />
  <ModalCam
    v-if="isOpenCam"
    @update-content="onUpdateContent"
    :uuid="content.id"
    :title="content.title"
    :tags="content.context"
    :image="content.image"
    @close="isOpenCam = false"
  />
</template>

<style module scoped lang="scss">
@import './HomeIntro.module.scss';
</style>

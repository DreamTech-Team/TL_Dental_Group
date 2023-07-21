<script setup lang="ts">
import { ref, watch } from 'vue';
import ICEdit from '@/assets/icons/Edit.png';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
import ModalCam from './CamReason.vue';
import ModalReason from './ModalReason.vue';
import ModalSVG from './SVGReason.vue';
import SVG from '@/assets/icons/white_check.png';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

//GET DATA
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/home/section1', {}, {}, deps.value);

const content = ref({
  id: '',
  image: '',
  title: '',
  description: '',
  listrs: [
    {
      id: '',
      image: SVG,
      title: '',
      description: ''
    },
    {
      id: '',
      image: SVG,
      title: '',
      description: ''
    },
    {
      id: '',
      image: SVG,
      title: '',
      description: ''
    }
  ]
});

const onUpdateContent = (data: object) => {
  content.value = { ...content.value, ...data };
};

watch(response, () => {
  content.value.id = response.value?.data?.heading?.id;
  content.value.image = response.value?.data?.heading?.image;
  content.value.title = response.value?.data?.heading?.title;
  content.value.description = response.value?.data?.heading?.content;
  content.value.listrs[0].id = response.value?.data?.subItem1?.id;
  content.value.listrs[0].title = response.value?.data?.subItem1?.title;
  content.value.listrs[0].description = response.value?.data?.subItem1?.content;
  content.value.listrs[1].id = response.value?.data?.subItem2?.id;
  content.value.listrs[1].title = response.value?.data?.subItem2?.title;
  content.value.listrs[1].description = response.value?.data?.subItem2?.content;
  content.value.listrs[2].id = response.value?.data?.subItem3?.id;
  content.value.listrs[2].title = response.value?.data?.subItem3?.title;
  content.value.listrs[2].description = response.value?.data?.subItem3?.content;
});

const selectedSVG = ref(SVG);
const isOpen = ref(false);
const isOpenCam = ref(false);
const isOpenSVG = ref(false);

const updateSVG = (path: string) => {
  selectedSVG.value = path;
  isOpenSVG.value = true;
};
</script>
<template>
  <div :class="$style.home__reason">
    <div :class="$style['home__reason-left']">
      <img :src="content.image" alt="company" />
      <CamBtn @click="isOpenCam = true" />
    </div>
    <div :class="$style['home__reason-right']">
      <h4 :class="$style['home__reason-title']">{{ content.title }}</h4>
      <h4>{{ content.description }}</h4>
      <div :class="$style['home__reason-list']">
        <div
          :class="$style['home__reason-item']"
          v-for="(item, index) in content.listrs"
          :key="index"
        >
          <div :class="$style['home__reason-icon']">
            <img :src="item.image" alt="icon1" />
            <button @click="updateSVG(item.image)">
              <img :src="ICEdit" alt="iconed" />
            </button>
          </div>
          <div :class="$style['home__reason-description']">
            <h5>{{ item.title }}</h5>
            <p>
              {{ item.description }}
            </p>
          </div>
        </div>
      </div>
    </div>
    <EditBtn @click="isOpen = true" />
  </div>
  <ModalCam v-if="isOpenCam" :image="content.image" @close="isOpenCam = false" />
  <ModalSVG v-if="isOpenSVG" :image="selectedSVG" @close="isOpenSVG = false" />
  <ModalReason
    v-if="isOpen"
    @close="isOpen = false"
    @update-content="onUpdateContent"
    :uuid="content.id"
    :title="content.title"
    :description="content.description"
    :list-item="content.listrs"
    :image="content.image"
  />
</template>

<style module scoped lang="scss">
@import './HomeReason.module.scss';
</style>

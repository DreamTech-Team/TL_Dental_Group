<script setup lang="ts">
import { ref, watch } from 'vue';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
import ModalMotto from './ModalMotto.vue';
import ModalCam from './CamMotto.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

//Status modal
const isOpen = ref(false);
const isOpenCam = ref(false);

//Get information
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/home/section2', {}, {}, deps.value);

const content = ref({
  id: '',
  title: '',
  context: ``,
  image: ''
});

const onUpdateContent = (data: object) => {
  content.value = { ...content.value, ...data };
};

watch(response, () => {
  content.value.id = response.value?.data?.id;
  content.value.title = response.value?.data?.title;
  content.value.context = response.value?.data?.content;
  content.value.image = response.value?.data?.image;
});
</script>
<template>
  <div :class="$style.home__motto">
    <div :class="$style['home__motto-left']">
      <h4>{{ content.title }}</h4>
      <span v-html="content.context"></span>
      <EditBtn style="right: 50px" @click="isOpen = true" />
    </div>
    <div :class="$style['home__motto-right']">
      <img :src="content.image" alt="meeting" />
      <CamBtn @click="isOpenCam = true" />
    </div>
  </div>
  <ModalMotto
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
@import './HomeMotto.module.scss';
</style>

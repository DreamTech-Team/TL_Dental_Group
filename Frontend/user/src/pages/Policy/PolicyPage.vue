<script setup lang="ts">
import { ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { useRoute } from 'vue-router';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';

interface Policy {
  title: string;
  content: string;
}

interface ItemPolicy {
  id: string;
  name: string;
  symbol: string;
  slug: string;
  detail: string;
  createAt: string;
}

//Properties
const banner = ref('');
const listPolicy = ref<Policy[]>([]);
const route = useRoute();

//GET BANNER
const deps = ref([]);
const getBanner = useAxios<DataResponse>('get', '/policy/header', {}, {}, deps.value);
watch(getBanner.response, () => {
  banner.value = getBanner.response.value?.data?.image;
});

//GET POLICY
const getPolicies = useAxios<DataResponse>('get', '/policy', {}, {}, deps.value);
watch(getPolicies.response, () => {
  listPolicy.value = getPolicies.response.value?.data.map((item: ItemPolicy) => {
    return {
      title: item.name,
      content: item.detail
    };
  });
});

const selectedItem = ref(0);
const showNav = ref(false);

const handleResizeData = () => {
  const parent = document.getElementById('content_body');
  if (parent) {
    console.log(parent.getElementsByTagName('p'));
    const content = ref<HTMLElement[] | null>(null);
    const tagli = ref<HTMLElement[] | null>(null);
    const contents = parent.getElementsByTagName('p');
    const listli = parent.getElementsByTagName('li');
    const contentArray = Array.from(contents);
    const liArray = Array.from(listli);
    content.value = contentArray;
    tagli.value = liArray;

    content.value.forEach((item) => {
      item.style.lineHeight = '1.8';
    });

    tagli.value.forEach((item) => {
      item.style.lineHeight = '1.8';
      item.style.marginLeft = '15px';
      item.style.paddingLeft = '5px';
    });
  }
};

watch(getPolicies.isLoading, handleResizeData);

const handleSelected = (index: number) => {
  selectedItem.value = Number(index);
  setTimeout(handleResizeData, 1000);
};

const handleActiveNav = () => {
  showNav.value = !showNav.value;
};
</script>
<template>
  <div :class="$style.container">
    <div :class="$style.container__bg">
      <img :src="banner" alt="banner" />
    </div>
    <bread-crumb :tags="route.path" />
    <div :class="$style.container__content">
      <div :class="$style['container__content-nav']">
        <div
          :class="[
            $style['container__content-nav-title'],
            $style[showNav ? 'container__content-nav-title-active' : '']
          ]"
          @click="handleActiveNav"
        >
          <font-awesome-icon :icon="faBars" :class="$style['container__content-nav-title-icon']" />
          <h3>Chính sách</h3>
        </div>
        <ul>
          <li
            v-for="(item, index) in listPolicy"
            :key="index"
            @click="handleSelected(index)"
            :class="$style[showNav ? 'active' : 'hidden']"
          >
            <div
              :class="[
                $style[index === selectedItem ? `container__content-nav-item-active` : ''],
                $style[`container__content-nav-item`]
              ]"
            >
              {{ item?.title }}
            </div>
          </li>
        </ul>
      </div>
      <div :class="$style['container__content-section']">
        <div :class="$style['container__content-section-title']">
          {{ listPolicy[selectedItem]?.title }}
        </div>
        <div
          id="content_body"
          :class="$style['container__content-section-details']"
          v-html="listPolicy[selectedItem]?.content"
        ></div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './PolicyPage.module.scss';
</style>

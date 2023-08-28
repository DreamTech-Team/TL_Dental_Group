<script setup lang="ts">
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import ServiceQuality from '@/components/ServiceQuality/ServiceQuality.vue';
import HomeTrend from '../Home/HomeTrend/HomeTrend.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faRegistered } from '@fortawesome/free-solid-svg-icons';
import { faFacebook } from '@fortawesome/free-brands-svg-icons';
import {
  faChevronLeft,
  faChevronRight,
  faMagnifyingGlassPlus,
  faTimes
} from '@fortawesome/free-solid-svg-icons';
import ZaloImg from '@/assets/imgs/Contact/Zalo.png';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';
import { ref, watch, onMounted, onUnmounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

export interface Product {
  id: string;
  name: string;
  slug: string;
  price: number;
  priceSale: number;
  summary: string;
  description: string;
  mainImg: string;
  imgs: string;
  highlight: number;
  createAt: string;
  fkCategory: {
    id: string;
    companyId: {
      id: string;
      name: string;
      logo: string;
      description: string;
      highlight: number;
      slug: string;
      createAt: string;
      outstandingProductId: string;
    };
    cate1Id: {
      id: string;
      title: string;
      img: string;
      highlight: 3;
      slug: string;
      createAt: string;
    };
    cate2Id: {
      id: string;
      title: string;
      slug: string;
      createAt: string;
    };
  };
}

const route = useRoute();
const inforProduct = ref<Product>();
const pathBC = ref();

// const images = [DetailImage, Product1, Product2, Product3, Product4];

const isDesktop = ref(false);

const wItem = ref(0);
const tranfX = ref(0);
let resizeListener: () => void;
const isDialogOpen = ref(false);
const currentIndex = ref(0);
const startIndex = ref(0);
const displayedImagesCount = ref();
const currentImage = ref();
const isLoadingDetail = ref(false);

const deps = ref([]);
const { response, isLoading } = useAxios<DataResponse>(
  'get',
  `/products/${route.params.catchAll[0]}`, //Params slug product
  {},
  {},
  deps.value
);

const images = ref<string[]>([]);

watch(response, () => {
  inforProduct.value = response.value?.data;
  isLoadingDetail.value = isLoading.value;
  if (inforProduct.value) {
    const apiResponseImg = inforProduct.value.imgs;
    if (apiResponseImg) {
      const cleanedResponse = apiResponseImg.substring(1, apiResponseImg.length - 1);
      const imageUrls = cleanedResponse.split(',').map((url: string) => url.trim());

      // Tạo một mảng mới chứa inforProduct.value?.mainImg và imageUrls
      images.value.splice(0, images.value.length, ...imageUrls);
      displayedImagesCount.value = 3;
    }

    // eslint-disable-next-line max-len
    pathBC.value =
      'sanpham' +
      '/' +
      inforProduct.value.fkCategory.cate1Id.slug +
      '/' +
      inforProduct.value.fkCategory.cate2Id.slug +
      '/' +
      inforProduct.value.slug;
  }

  currentImage.value = images.value[0];
});

const setCurrentImage = (index: number) => {
  currentIndex.value = index;
};

const displayedImages = computed(() =>
  images.value.slice(startIndex.value, startIndex.value + displayedImagesCount.value)
);

const nextImage = () => {
  currentIndex.value = (currentIndex.value + 1) % images.value.length;
};

const prevImage = () => {
  currentIndex.value = (currentIndex.value - 1 + images.value.length) % images.value.length;
};

// const currentImage = computed(() => images[currentIndex.value]);

const showDialog = () => {
  isDialogOpen.value = true;
};

const closeDialog = () => {
  isDialogOpen.value = false;
};

watch(currentIndex, () => {
  currentImage.value = images.value[currentIndex.value];
});

onMounted(() => {
  const container = document.getElementById('trend-wrapper');

  if (container) {
    if (window.innerWidth < 739) {
      wItem.value = container.offsetWidth / 2;
    } else {
      wItem.value = container.offsetWidth / 4;
    }
  }

  resizeListener = function () {
    if (window.innerWidth < 739) {
      isDesktop.value = false;
    } else {
      isDesktop.value = true;
    }
    const container = document.getElementById('trend-wrapper');
    if (container) {
      if (window.innerWidth < 739) {
        wItem.value = container.offsetWidth / 2;
      } else {
        wItem.value = container.offsetWidth / 4;
      }

      tranfX.value = 0;
    }
  };

  window.addEventListener('resize', resizeListener);
});

//Function 1000 to 1.000
const formatNumberWithCommas = (num: number) => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
};

onUnmounted(() => {
  window.removeEventListener('resize', resizeListener);
});
</script>
<template>
  <div v-if="!isLoadingDetail && displayedImages">
    <bread-crumb :tags="pathBC" />
    <div :class="$style.detail">
      <div :class="$style.detail__image">
        <div :class="$style['detail__image--ctn']">
          <img
            :class="$style['detail__image--ctn-main']"
            :src="currentImage"
            alt="Detail Product"
          />
          <button :class="$style['detail__image--ctn-left']" @click="prevImage">
            <font-awesome-icon :icon="faChevronLeft" :class="$style['home__trend-ic']" />
          </button>
          <button :class="$style['detail__image--ctn-right']" @click="nextImage">
            <font-awesome-icon :icon="faChevronRight" :class="$style['home__trend-ic']" />
          </button>
          <button :class="$style['detail__image--ctn-zoom']" @click="showDialog">
            <font-awesome-icon :icon="faMagnifyingGlassPlus" :class="$style['home__trend-ic']" />
          </button>
        </div>

        <div v-if="isDialogOpen" :class="$style.dialog" @click="closeDialog">
          <img
            @click.stop
            :src="currentImage"
            alt="Detail Product"
            :class="$style['dialog-image']"
          />
          <button @click="closeDialog" :class="$style['dialog-close']">
            <font-awesome-icon :icon="faTimes" />
          </button>
        </div>

        <div :class="$style['detail__image--multi']">
          <img
            v-for="(image, index) in displayedImages"
            :key="index"
            :class="[
              $style['detail__image--multi-product'],
              {
                [$style['detail__image--multi-product-active']]: index === currentIndex
              }
            ]"
            :src="image"
            alt="product1"
            @click="setCurrentImage(index)"
          />
          <div :class="$style['detail__image--multi-products']">
            <img
              v-if="images.length > 3"
              :class="[
                $style['detail__image--multi-product'],
                {
                  [$style['detail__image--multi-product-active']]: currentIndex === 3
                }
              ]"
              :src="images[3]"
              alt="product4"
            />
            <div
              v-if="images.length > 3"
              :class="[
                $style['detail__image--multi-products-count'],
                {
                  [$style['detail__image--multi-product-active']]: currentIndex === 3
                }
              ]"
            >
              <p>+ {{ images.length - 3 }}</p>
            </div>
            <p v-else></p>
          </div>
        </div>
        <div v-if="!isDesktop" :class="$style['detail__image--counts']">
          <p>{{ currentIndex + 1 }}/{{ images.length }}</p>
        </div>
      </div>
      <div :class="$style.detail__content">
        <div>
          <div :class="$style['detail__content--name']">
            {{ inforProduct?.name }}
          </div>
          <div :class="$style['detail__content--price']">
            {{
              inforProduct?.price !== undefined
                ? formatNumberWithCommas(inforProduct.price) + 'đ'
                : 'N/A'
            }}
          </div>

          <div :class="$style['detail__content--object']" v-html="inforProduct?.summary"></div>

          <div :class="$style['detail__content--brand']">
            <font-awesome-icon
              :icon="faRegistered"
              :class="$style['detail__content--brand-icon']"
            />
            <p :class="$style['detail__content--brand-text']">Thương hiệu:</p>
            <p :class="$style['detail__content--brand-company']">
              {{ inforProduct?.fkCategory.companyId.name }}
            </p>
          </div>
        </div>
        <div :class="$style['detail__content--contact']">
          <div :class="$style['detail__content--contact-btn1']">
            <font-awesome-icon :class="$style['fb-logo']" :icon="faFacebook" />
            <p :class="$style['detail__content--contact-btn1-text']">Tư vấn qua Facebook</p>
          </div>
          <div :class="$style['detail__content--contact-btn2']">
            <img :class="$style['zalo-logo']" :src="ZaloImg" alt="Zalo logo" />
            <p :class="$style['detail__content--contact-btn2-text']">Tư vấn qua Zalo</p>
          </div>
        </div>
      </div>
    </div>
    <div :class="$style.description">
      <div :class="$style.description__title">
        <div :class="$style['description__title--line']"></div>
        <p :class="$style['description__title--text']">MÔ TẢ CHI TIẾT SẢN PHẨM</p>
      </div>
      <div :class="$style.description__name">{{ inforProduct?.name }}</div>
      <div :class="$style.list">
        <div v-html="inforProduct?.description"></div>
      </div>
    </div>
    <home-trend />
    <service-quality />
  </div>
  <div v-else>
    <loading-component />
  </div>
</template>

<style module scoped lang="scss">
@import './DetailPage.module.scss';
</style>

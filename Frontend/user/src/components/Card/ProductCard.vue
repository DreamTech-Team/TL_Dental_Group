<script setup lang="ts">
import OkSticker from '@/assets/imgs/Product/GroupOk.svg';
import Insurance from '@/assets/imgs/Product/GroupInsurance.svg';
import SPSticker from '@/assets/imgs/Product/GroupSupport.svg';
import { type PropType, ref, watch, onMounted } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import router from '@/router/index';
import { event } from 'vue-gtag';

export interface Product {
  nameProduct: string;
  price: number;
  summary: string;
  tag: string;
  company: string;
  image: string;
  brand: string;
  slug: string;
}

defineProps({
  product: {
    type: Object as PropType<Product>,
    required: true
  }
});

//Get Logo Company
const isPhone = ref(false);
const logoValue = ref('');
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/information?type=LOGO', {}, {}, deps.value);
watch(response, () => {
  logoValue.value = response.value?.data?.logo.content;
});

//Function 1000 to 1.000
const formatNumberWithCommas = (num: number) => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
};

//Go to detail Page
const linkDetail = (slug: string, productName: string) => {
  event('view_item', {
    event_label: 'Xem sản phẩm',
    value: productName
  });

  router.push(`/chitiet/${slug}`);
};

onMounted(() => {
  if (window.innerWidth < 739) {
    isPhone.value = true;
  } else {
    isPhone.value = false;
  }
});
</script>
<template>
  <div :class="$style.card" @click="linkDetail(product.slug, product.nameProduct)">
    <div :class="$style.card__show">
      <p :class="$style['card__show--info']" ref="info" v-html="product.summary"></p>
      <div
        :class="$style['card__show--button']"
        @click="linkDetail(product.slug, product.nameProduct)"
      >
        Xem chi tiết
      </div>
    </div>
    <div :class="$style.card__header">
      <div :class="$style['card__header--factors']">
        <div :class="$style['card__header--factors-item']">
          <img
            :class="$style['card__header--factors-genuine-sticker']"
            :src="OkSticker"
            alt="sticker"
          />
          <p :class="$style['card__header--factors-genuine-text']">100% chính hãng</p>
        </div>
        <div :class="$style['card__header--factors-item']">
          <img
            :class="$style['card__header--factors-insurance-sticker']"
            :src="Insurance"
            alt="sticker"
          />
          <p :class="$style['card__header--factors-insurance-text']">Bảo hành 12 tháng</p>
        </div>
        <div :class="$style['card__header--factors-item']">
          <img
            :class="$style['card__header--factors-support-sticker']"
            :src="SPSticker"
            alt="sticker"
          />
          <p :class="$style['card__header--factors-support-text']">Hỗ trợ đổi trả</p>
        </div>
      </div>
      <div :class="$style['card__header--container']">
        <div :class="$style['card__header--wgenuine']">
          <div :class="$style['card__header--wgenuine-trigle']"></div>
          <div :class="$style['card__header--wgenuine-confirm']">Chính hãng</div>
        </div>

        <div :class="$style['card__header--title']">
          <img :class="$style['card__header--title-logo']" :src="logoValue" alt="logononbg" />
          <div v-if="!isPhone" :class="$style['card__header--title-content']">
            {{ product.tag }} {{ product.company }}
          </div>
          <div v-if="isPhone" :class="$style['card__header--title-content']">
            {{ product.company }}
          </div>
        </div>
        <div :class="$style['card__header--image']">
          <img :class="$style['card__header--picture']" :src="product.image" alt="product" />
        </div>
      </div>
    </div>
    <div :class="$style.card__body">
      <p :class="$style['card__body--name']">
        {{ product.nameProduct }}
      </p>
      <div :class="$style['card__body--info']">
        <img :class="$style['card__body--info-company']" :src="product.brand" alt="Logo company" />
        <p :class="$style['card__body--info-price']">
          {{ formatNumberWithCommas(product.price) }}đ
        </p>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
// @import '../ProductPage.module.scss';
@import './ProductCard.module.scss';
</style>

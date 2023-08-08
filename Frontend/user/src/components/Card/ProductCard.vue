<script setup lang="ts">
import OkSticker from '@/assets/imgs/Product/GroupOk.svg';
import Insurance from '@/assets/imgs/Product/GroupInsurance.svg';
import SPSticker from '@/assets/imgs/Product/GroupSupport.svg';
import { type PropType, ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import router from '@/router/index';

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
const linkDetail = (slug: string) => {
  router.push(`/chitiet/${slug}`);
};
</script>
<template>
  <div :class="$style.card">
    <div :class="$style.card__show">
      <p :class="$style['card__show--info']" v-html="product.summary"></p>
      <div :class="$style['card__show--button']" @click="linkDetail(product.slug)">
        Xem chi tiết
      </div>
    </div>
    <div :class="$style.card__header">
      <div :class="$style['card__header--container']">
        <div :class="$style['card__header--genuine']">
          <img :class="$style['card__header--genuine-sticker']" :src="OkSticker" alt="sticker" />
          <p :class="$style['card__header--genuine-text']">100% chính hãng</p>
        </div>
        <div :class="$style['card__header--insurance']">
          <img :class="$style['card__header--insurance-sticker']" :src="Insurance" alt="sticker" />
          <p :class="$style['card__header--insurance-text']">Bảo hành 12 tháng</p>
        </div>
        <div :class="$style['card__header--support']">
          <img :class="$style['card__header--support-sticker']" :src="SPSticker" alt="sticker" />
          <p :class="$style['card__header--support-text']">Hỗ trợ đổi trả</p>
        </div>
        <div :class="$style['card__header--wgenuine']">
          <div :class="$style['card__header--wgenuine-confirm']"></div>
          <div :class="$style['card__header--txtconfirm']">Chính hãng</div>
        </div>

        <div :class="$style['card__header--title']">
          <img :class="$style['card__header--title-logo']" :src="logoValue" alt="logononbg" />
          <div :class="$style['card__header--title-content']">
            {{ product.tag }} {{ product.company }}
          </div>
        </div>
        <div
          style="
            widows: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
          "
        >
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

<script setup lang="ts">
import LogoNoBg from '@/assets/imgs/logo_nobg.png';
import OkSticker from '@/assets/imgs/Product/GroupOk.svg';
import Insurance from '@/assets/imgs/Product/GroupInsurance.svg';
import SupportSticker from '@/assets/imgs/Product/GroupSupport.svg';
import { useRouter } from 'vue-router';
import { type PropType } from 'vue';

interface Product {
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

interface Item {
  nameProduct: string;
  price: number;
  summary: string;
  tag: string;
  company: string;
  image: string;
  brand: string;
  slug: string;
}
const router = useRouter();

const goToDetailPage = (slug: string) => {
  router.push(`/chitiet/${slug}`);
};

//Function 1000 to 1.000
const formatNumberWithCommas = (num: number) => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
};

defineProps({
  product: {
    type: Object as PropType<Item>,
    required: true
  }
});
</script>
<template>
  <div :class="$style.mbcard" @click="goToDetailPage(product.slug)">
    <div :class="$style['mbcard__left']">
      <div :class="$style['mbcard__left-header']">
        <img :src="product.brand" alt="logo" />
      </div>
      <div :class="$style['mbcard__left-cpn']">
        <img :src="product.image" alt="product" />
      </div>

      <div :class="$style['mbcard__left-infor']">
        <div :class="$style['mbcard__left-logo']">
          <img :class="$style['mbcard__left-logo']" :src="LogoNoBg" alt="logo" />
        </div>
        <h3>{{ product.tag }} {{ product.company }}</h3>
      </div>
    </div>
    <div :class="$style['mbcard__right']">
      <h4 :class="$style['mbcard__right-title']">
        {{ product.nameProduct }}
      </h4>
      <p v-html="product.summary"></p>
      <div :class="$style['mbcard__right-list']">
        <div :class="$style['mbcard__right--genuine']">
          <img :class="$style['mbcard__right-sticker']" :src="OkSticker" alt="sticker" />
          <p :class="$style['mbcard__right-text']">100% chính hãng</p>
        </div>
        <div :class="$style['mbcard__right--genuine']">
          <img :class="$style['mbcard__right-sticker']" :src="Insurance" alt="sticker" />
          <p :class="$style['mbcard__right-text']">Bảo hành 12 tháng</p>
        </div>
        <div :class="$style['mbcard__right--genuine']">
          <img :class="$style['mbcard__right-sticker']" :src="SupportSticker" alt="sticker" />
          <p :class="$style['mbcard__right-text']">Hỗ trợ đổi trả</p>
        </div>
      </div>
      <h4 :class="$style['mbcard__right-price']">
        {{ formatNumberWithCommas(product.price) + 'đ' }}
      </h4>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './MobileCard.module.scss';
</style>

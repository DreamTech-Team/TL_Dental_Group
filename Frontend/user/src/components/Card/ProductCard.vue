<script setup lang="ts">
import LogoNoBg from '@/assets/imgs/logo_nobg.png';
import ProductPic from '@/assets/imgs/Product/product.png';
import LogoCompany from '@/assets/imgs/Product/logoCompany.png';
import OkSticker from '@/assets/imgs/Product/GroupOk.svg';
import Insurance from '@/assets/imgs/Product/GroupInsurance.svg';
import SPSticker from '@/assets/imgs/Product/GroupSupport.svg';
import { useRouter } from 'vue-router';
import { watch, watchEffect, type PropType } from 'vue';
import { products } from '../../pages/Product/ProductHandle';

interface ProductItem {
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
  id: string;
  name: string;
  src: string;
  company: string;
  price: string;
}

const router = useRouter();

const goToDetailPage = () => {
  router.push('/chitiet');
};

const props = defineProps({
  product: {
    type: Object as PropType<ProductItem>,
    required: true
  }
});

// Hàm định dạng giá tiền
const formatPrice = (price: number) => {
  if (typeof price !== 'number') {
    return ''; // Nếu price không phải là số, trả về chuỗi rỗng hoặc thông báo lỗi tùy bạn
  }
  // Định dạng giá tiền thành chuỗi với phân cách hàng nghìn và ký tự đ
  return price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
};

watch(props.product, () => {
  console.log(props.product.name);
});
</script>
<template>
  <div :class="$style.card" @click="goToDetailPage">
    <div :class="$style.card__show">
      <p :class="$style['card__show--info']">{{ product.description }}</p>
      <div :class="$style['card__show--button']">Xem chi tiết</div>
    </div>
    <div :class="$style.card__header">
      <div :class="$style['card__header--container']">
        <div :class="$style['card__header--container-wrapcfc']">
          <div :class="$style['card__header--genuine']">
            <img :class="$style['card__header--genuine-sticker']" :src="OkSticker" alt="sticker" />
            <p :class="$style['card__header--genuine-text']">100% chính hãng</p>
          </div>
          <div :class="$style['card__header--genuine']">
            <img :class="$style['card__header--genuine-sticker']" :src="Insurance" alt="sticker" />
            <p :class="$style['card__header--genuine-text']">Bảo hành 12 tháng</p>
          </div>
          <div :class="$style['card__header--genuine']">
            <img :class="$style['card__header--genuine-sticker']" :src="SPSticker" alt="sticker" />
            <p :class="$style['card__header--genuine-text']">Hỗ trợ đổi trả</p>
          </div>
        </div>
        <div :class="$style['card__header--wgenuine']">
          <div :class="$style['card__header--wgenuine-confirm']"></div>
          <div :class="$style['card__header--txtconfirm']">Chính hãng</div>
        </div>

        <div :class="$style['card__header--title']">
          <img :class="$style['card__header--title-logo']" :src="LogoNoBg" alt="logononbg" />
          <div :class="$style['card__header--title-content']">
            <div v-html="product.summary"></div>
            {{ product.fkCategory.companyId.name }}
          </div>
        </div>
        <div :class="$style['card__header--image']">
          <img
            :class="$style['card__header--image-picture']"
            :src="product.mainImg"
            alt="product"
          />
        </div>
      </div>
    </div>
    <div :class="$style.card__body">
      <p :class="$style['card__body--name']">
        {{ product.name }}
      </p>
      <div :class="$style['card__body--info']">
        <img
          :class="$style['card__body--info-company']"
          :src="product.fkCategory.companyId.logo"
          alt="Logo company"
        />
        <p :class="$style['card__body--info-price']">{{ formatPrice(product.price) }}</p>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
// @import '../ProductPage.module.scss';
@import './ProductCard.module.scss';
</style>

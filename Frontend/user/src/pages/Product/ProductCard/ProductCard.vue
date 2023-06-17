<script setup lang="ts">
import LogoNoBg from '@/assets/imgs/logo_nobg.png';
import ProductPic from '@/assets/imgs/Product/product.png';
import LogoCompany from '@/assets/imgs/Product/logoCompany.png';
import OkSticker from '@/assets/imgs/Product/GroupOk.svg';
import Insurance from '@/assets/imgs/Product/GroupInsurance.svg';
import SupportSticker from '@/assets/imgs/Product/GroupSupport.svg';
</script>
<template>
  <div :class="$style['product__content-container--card']">
    <div :class="$style['cart__show--info']">
      <p :class="$style['cart__show--info-text']">
        Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO THÔNG TIN SẢN PHẨM - Kẹp gắp đầu cong để gắp
        mắc cài R6,7 và button. - Kẹp gắp đầu cong để gắp mắc cài R6,7 và button. - Kẹp…
      </p>
      <div :class="$style['cart__show--info-button']">Xem chi tiết</div>
    </div>
    <div :class="$style['product__card--header']">
      <div :class="$style['product__card--container']">
        <div>
          <div :class="$style['product__sticker--cf-ok']">
            <img :class="$style['product__sticker--ok']" :src="OkSticker" alt="sticker" />
            <p :class="$style['product__sticker--cf-text']">100% chính hãng</p>
          </div>
          <div :class="$style['product__sticker--cf-insurance']">
            <img :class="$style['product__sticker--insurance']" :src="Insurance" alt="sticker" />
            <p :class="$style['product__sticker--insurance-text']">Bảo hành 12 tháng</p>
          </div>
          <div :class="$style['product__sticker--cf-support']">
            <img :class="$style['product__sticker--support']" :src="SupportSticker" alt="sticker" />
            <p :class="$style['product__sticker--support-text']">Hỗ trợ đổi trả</p>
          </div>
          <div :class="$style['product__card--genuine-wrap']">
            <div :class="$style['product__card--genuine']"></div>
            <!-- <div :class="$style['product__card--triangle']"></div> -->
          </div>
          <p :class="$style['product__card--text-genuine']">Chính hãng</p>
        </div>
        <div :class="$style['product__card--title']">
          <img :class="$style['product__card--logo']" :src="LogoNoBg" alt="logononbg" />
          <div :class="$style['product__cart--content']">Vật liệu chỉnh nha TL Group</div>
        </div>
        <div>
          <img :class="$style['product__card--picture']" :src="ProductPic" alt="product" />
        </div>
      </div>
    </div>
    <div :class="$style['product__card--body']">
      <p :class="$style['product__card--name']">
        Kẹp gấp mắc cài R6,7/ kẹp gấp Tube – PMC ORTHO (Sao chép)
      </p>
      <div :class="$style['product__card--info']">
        <img :class="$style['product__card--info-company']" :src="LogoCompany" alt="Logo company" />
        <p :class="$style['product__card--info-price']">4.000.000đ</p>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../ProductPage.module.scss';
</style>

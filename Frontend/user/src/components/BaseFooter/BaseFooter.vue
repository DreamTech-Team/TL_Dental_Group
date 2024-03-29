<script setup lang="ts">
import { ref, toRefs, watch } from 'vue';
import { RouterLink } from 'vue-router';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faLocationDot,
  faEnvelope,
  faPhoneVolume,
  faCircleCheck
} from '@fortawesome/free-solid-svg-icons';
import QrcodeVue, { type Level, type RenderAs } from 'qrcode.vue';
import { useDataRenderStore, saveDataContact, saveActive } from '@/stores/counter';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface AboutPolicy {
  name: string;
  detail: string;
  symbol: string;
  slug: string;
}

// lấy dữ liệu category từ store để render
const { dataRender } = toRefs(useDataRenderStore());
// Lấy dữ liệu facility và contact từ store
const { dataFacility, dataContact } = toRefs(saveDataContact());
const saveIndexState = saveActive();

const variableChange = ref([]);
const listPolicy = ref<AboutPolicy[]>([]);

const level = ref<Level>('M');
const renderAs = ref<RenderAs>('svg');
const currentYear = new Date().getFullYear();

const { response } = useAxios<DataResponse>('get', '/policy', {}, {}, variableChange.value);

watch(response, () => {
  listPolicy.value = response?.value?.data;
});

const handleGetIndexCate1 = (categoryIndex: number) => {
  saveIndexState.setTypeCategory('cate1Footer' + categoryIndex);
  saveIndexState.setActiveCategory(categoryIndex);
  saveIndexState.setActiveCategory2(-1);
  saveIndexState.setActiveCategory3(-1);
};
</script>
<template>
  <div :class="$style.footer">
    <div :class="$style.footer__info">
      <div :class="$style['footer__info-maxwidth']">
        <div :class="$style['footer__info-maxwidth-item']">
          <p>TL DENTAL GROUP</p>
          <div style="padding: 10px; background-color: white; display: inline-block">
            <qrcode-vue
              :value="dataContact.facebook.content"
              :level="level"
              :render-as="renderAs"
            />
          </div>
        </div>
        <div :class="$style['footer__info-maxwidth-item']">
          <h4 :class="$style['footer__info-maxwidth-item--title']">Sản phẩm</h4>
          <ul
            :class="$style['footer__info-maxwidth-item--list']"
            v-for="(item, idx) in dataRender"
            :key="idx"
          >
            <li @click="handleGetIndexCate1(idx)">
              <router-link :to="`/sanpham?slug1=${item.slug}`">{{ item.title }}</router-link>
            </li>
          </ul>
        </div>
        <div :class="$style['footer__info-maxwidth-item']">
          <h4 :class="$style['footer__info-maxwidth-item--title']">Công ty</h4>
          <ul :class="$style['footer__info-maxwidth-item--list']">
            <li><router-link to="/gioithieu">Giới thiệu công ty</router-link></li>
            <li><router-link to="/tuyendung">Tuyển dụng</router-link></li>
          </ul>
        </div>
        <div :class="$style['footer__info-maxwidth-item']">
          <h4 :class="$style['footer__info-maxwidth-item--title']">Chính sách</h4>
          <ul
            :class="$style['footer__info-maxwidth-item--list']"
            v-for="(item, idx) in listPolicy"
            :key="idx"
          >
            <li>
              <router-link to="/chinhsach">{{ item.name }}</router-link>
            </li>
          </ul>
        </div>
        <div :class="$style['footer__info-maxwidth-item']">
          <h4 :class="$style['footer__info-maxwidth-item--title']">Liên hệ</h4>
          <ul :class="$style['footer__info-maxwidth-item--list']">
            <li>
              <font-awesome-icon :icon="faLocationDot" />
              <p>
                <router-link to="/lienhe">{{ dataFacility.address }}</router-link>
              </p>
            </li>
            <li>
              <font-awesome-icon :icon="faEnvelope" />
              <p>
                <router-link to="/lienhe">{{ dataContact.email.content }}</router-link>
              </p>
            </li>
            <li>
              <font-awesome-icon :icon="faPhoneVolume" />
              <p>
                <a :href="'tel:' + dataFacility.phoneNumber">{{ dataFacility.phoneNumber }}</a>
              </p>
            </li>
          </ul>
        </div>

        <div :class="$style['footer__info-maxwidth-contact']">
          <div :class="$style['footer__info-maxwidth-contact--item']">
            <p>Nhà cung cấp thiết bị y tế</p>
            <ul>
              <li><font-awesome-icon :icon="faCircleCheck" /> Uy tín</li>
              <li><font-awesome-icon :icon="faCircleCheck" /> Chính hãng</li>
              <li><font-awesome-icon :icon="faCircleCheck" /> Chất lượng</li>
            </ul>
          </div>
          <div :class="$style['footer__info-maxwidth-contact--item']">
            <router-link to="/gioithieu">Giới thiệu chi tiết</router-link>
          </div>
        </div>
      </div>
    </div>
    <div :class="$style.footer__license">
      {{ 'Copyright © ' + currentYear + ' | Powered by TL Dental Group' }}
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './BaseFooter.module.scss';
</style>

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
import { useDataRenderStore, saveDataContact } from '@/stores/counter';
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

const variableChange = ref([]);
const listPolicy = ref<AboutPolicy[]>([]);

const level = ref<Level>('M');
const renderAs = ref<RenderAs>('svg');

const { response } = useAxios<DataResponse>('get', '/policy', {}, {}, variableChange.value);

watch(response, () => {
  listPolicy.value = response?.value?.data;
});
</script>
<template>
  <div :class="$style.footer">
    <div :class="$style.footer__info">
      <div :class="$style['footer__info-item']">
        <p>TL DENTAL GROUP</p>
        <div style="padding: 10px; background-color: white; display: inline-block">
          <qrcode-vue :value="dataContact.facebook.content" :level="level" :render-as="renderAs" />
        </div>
      </div>
      <div :class="$style['footer__info-item']">
        <h4 :class="$style['footer__info-item--title']">Sản phẩm</h4>
        <ul :class="$style['footer__info-item--list']" v-for="(item, idx) in dataRender" :key="idx">
          <li>
            <router-link to="">{{ item.title }}</router-link>
          </li>
        </ul>
      </div>
      <div :class="$style['footer__info-item']">
        <h4 :class="$style['footer__info-item--title']">Công ty</h4>
        <ul :class="$style['footer__info-item--list']">
          <li><router-link to="/gioithieu">Giới thiệu công ty</router-link></li>
          <li><router-link to="/tuyendung">Tuyển dụng</router-link></li>
        </ul>
      </div>
      <div :class="$style['footer__info-item']">
        <h4 :class="$style['footer__info-item--title']">Chính sách</h4>
        <ul :class="$style['footer__info-item--list']" v-for="(item, idx) in listPolicy" :key="idx">
          <li>
            <router-link to="/chinhsach">{{ item.name }}</router-link>
          </li>
        </ul>
      </div>
      <div :class="$style['footer__info-item']">
        <h4 :class="$style['footer__info-item--title']">Liên hệ</h4>
        <ul :class="$style['footer__info-item--list']">
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

      <div :class="$style['footer__info-contact']">
        <div :class="$style['footer__info-contact--item']">
          <p>Nhà cung cấp thiết bị y tế</p>
          <ul>
            <li><font-awesome-icon :icon="faCircleCheck" /> Uy tín</li>
            <li><font-awesome-icon :icon="faCircleCheck" /> Chính hãng</li>
            <li><font-awesome-icon :icon="faCircleCheck" /> Chất lượng</li>
          </ul>
        </div>
        <div :class="$style['footer__info-contact--item']">
          <router-link to="/gioithieu">Giới thiệu chi tiết</router-link>
        </div>
      </div>
    </div>
    <div :class="$style.footer__license">© 2023 All Rights Reserved</div>
  </div>
</template>

<style module scoped lang="scss">
@import './BaseFooter.module.scss';
</style>

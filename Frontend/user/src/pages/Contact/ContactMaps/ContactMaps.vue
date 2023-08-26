<script setup lang="ts">
import { toRefs } from 'vue';
import { saveDataContact } from '@/stores/counter';

// Lấy dữ liệu từ store
const { dataFacility } = toRefs(saveDataContact());

// Hàm lấy src từ iframe được call về
const getSrc = () => {
  // Sử dụng biểu thức chính quy để tìm phần src
  const srcPattern = /src="([^"]*)"/;
  const matches = dataFacility.value.mapIframe.match(srcPattern);

  if (matches && matches.length > 1) {
    const srcValue = matches[1];

    return srcValue;
  }
};
</script>

<template>
  <div :class="$style.contact__maps">
    <iframe
      :class="$style['contact__maps-iframe']"
      :src="getSrc()"
      allowfullscreen="false"
      loading="lazy"
      referrerpolicy="no-referrer-when-downgrade"
    ></iframe>
  </div>
</template>

<style module scoped lang="scss">
@import './ContactMaps.module.scss';
</style>

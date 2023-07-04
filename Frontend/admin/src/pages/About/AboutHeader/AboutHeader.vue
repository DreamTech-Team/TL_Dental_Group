<script setup lang="ts">
import Intro from '@/assets/imgs/About/Intro.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faRotate } from '@fortawesome/free-solid-svg-icons';
import { ref } from 'vue';

const selectedImage = ref(null);

const handleChangeImage = () => {
  document.getElementById('input_file').click();
};

const handleFileInputChange = (event) => {
  const file = event.target.files[0];
  const reader = new FileReader();

  reader.onload = (e) => {
    selectedImage.value = e.target.result;
  };

  reader.readAsDataURL(file);
};
</script>
<template>
  <div :class="$style.about__header">
    <div :class="$style['about__header-introduce']">
      <div v-if="selectedImage" :class="$style['about__header-introduce-img']">
        <img :src="selectedImage" alt="" />
      </div>
      <div :class="$style['about__header-introduce-img']" v-else>
        <img :src="Intro" alt="" />
      </div>

      <div :class="$style['about__header-introduce-bg']">
        <button :class="$style['about__header-introduce-bg-button']" @click="handleChangeImage">
          <font-awesome-icon
            :icon="faRotate"
            :class="$style['about__header-introduce-bg-button-ic']"
          />
          <span>Đổi hình ảnh</span>
        </button>
      </div>

      <input
        type="file"
        style="display: none"
        id="input_file"
        accept="image/*"
        @change="handleFileInputChange"
      />

      <p>Giới Thiệu</p>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutHeader.module.scss';
</style>

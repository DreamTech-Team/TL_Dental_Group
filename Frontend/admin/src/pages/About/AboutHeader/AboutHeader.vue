<script setup lang="ts">
import Intro from '@/assets/imgs/About/Intro.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faRotate } from '@fortawesome/free-solid-svg-icons';
import { Ref, ref, onMounted, watch } from 'vue';
import Croppie from 'croppie';
import 'croppie/croppie.css';

const selectedImage: Ref<string | null> = ref(null);
const croppieInstance: Ref<Croppie | null> = ref(null);
const imageFile: Ref<string | null> = ref(null);
const isCrop = ref(false);

const handleChangeImage = () => {
  const inputElement = document.getElementById('input_file');
  if (inputElement) {
    inputElement.click();
  }
};
// Hàm chuyển đổi từ ArrayBuffer sang string
const arrayBufferToString = (buffer: ArrayBuffer) => {
  const uintArray = new Uint16Array(buffer);
  const charArray: string[] = [];
  for (let i = 0; i < uintArray.length; i++) {
    charArray.push(String.fromCharCode(uintArray[i]));
  }
  return charArray.join('');
};

const handleFileInputChange = (event: Event) => {
  const target = event.target as HTMLInputElement;

  if (target.files) {
    const file = target.files[0];
    const reader = new FileReader();

    reader.onload = (e: ProgressEvent<FileReader>) => {
      const result = e.target?.result;
      if (result instanceof ArrayBuffer) {
        selectedImage.value = arrayBufferToString(result);
      } else if (typeof result === 'string') {
        selectedImage.value = result;
      }

      croppieInstance.value.bind({
        url: reader.result as string
      });
    };

    reader.readAsDataURL(file);
  }
};

const initCroppie = () => {
  const container = document.getElementById('croppieContainer');
  if (container) {
    croppieInstance.value = new Croppie(container, {
      viewport: { width: 1440, height: 486 },
      boundary: { width: 1440, height: 600 },
      showZoomer: false
    });
  }
};

onMounted(initCroppie);

const crop = () => {
  if (croppieInstance.value) {
    isCrop.value = false;
    croppieInstance.value.result('base64').then((result: string) => {
      // Xử lý kết quả crop ở đây (ví dụ: lưu vào biến selectedImage)
      imageFile.value = result;
    });
  }
};

const handleCheckCrop = () => {
  if (selectedImage.value) {
    isCrop.value = true;
  } else {
    isCrop.value = false;
  }
};

watch(() => selectedImage.value, handleCheckCrop);
</script>
<template>
  <div :class="$style.about__header">
    <div :class="$style['about__header-introduce']">
      <div v-if="imageFile" :class="$style['about__header-introduce-img']">
        <img :src="imageFile" alt="" />
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

  <div :class="$style.crop" v-show="isCrop">
    <div id="croppieContainer" ref="croppieContainer"></div>
    <div>
      <button @click="crop">Crop</button>
      <button @click="isCrop = false">Hủy</button>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutHeader.module.scss';
</style>

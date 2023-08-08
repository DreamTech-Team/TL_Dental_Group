<script setup lang="ts">
import { ref, watch, type Ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
import CropImage from '@/components/CropImage/CropImage.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const context = defineProps({
  uuid: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  tags: {
    type: String,
    required: true
  },
  image: {
    type: String,
    required: true
  }
});

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', data: { image: string }): void;
}>();

const urlFile: Ref<string | null> = ref(context.image);
const isCrop = ref(false);
const isOpenInput = ref(false);
const fileData = ref();
const finalImage = ref();

//Open file image
const openFileInput = () => {
  isOpenInput.value = !isOpenInput.value;
};

const base64ToBlob = (base64Data: string) => {
  const byteString = atob(base64Data.split(',')[1]);
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }
  return new Blob([ab], { type: 'image/png' });
};

const handleCroppedImage = (result: string) => {
  if (result) {
    urlFile.value = result;

    fileData.value = base64ToBlob(result);
    finalImage.value = new File([fileData.value], 'image.png', { type: 'image/png' });
  }
};

const submitForm = () => {
  const deps = ref([]);

  const object = {
    id: context.uuid,
    title: context.title,
    content: context.tags
  };

  const formData = new FormData();
  formData.append('data', JSON.stringify(object));
  formData.append('image', finalImage.value as Blob);
  const { response } = useAxios<DataResponse>(
    'patch',
    '/home/section3',
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    },
    deps.value
  );

  watch(response, () => {
    if (response.value?.status === 'ok') {
      Swal.fire({
        title: 'Cập nhật thành công',
        icon: 'success',
        confirmButtonText: 'Hoàn tất',
        width: '30rem'
      }).then((result) => {
        if (result.isConfirmed) {
          Swal.close();
          emits('update-content', {
            image: urlFile.value || ''
          });
          emits('close');
        }
      });
    }
  });
};
</script>

<template>
  <div :class="$style.camintro__overlay">
    <div :class="$style.camintro__modal">
      <div :class="$style.camintro__modal__heading">
        THAY ĐỔI ẢNH
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['camintro__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.camintro__modal__body">
        <div :class="$style.camintro__ctn">
          <img v-if="urlFile" :src="urlFile" alt="CEO" />
          <div :class="$style.camintro__image_overlay">
            <CamBtn @click="openFileInput" />
          </div>
        </div>
        <div :class="$style['modal__buttons']">
          <button @click="$emit('close')">Hủy</button>
          <button @click="submitForm">Cập nhật</button>
        </div>
      </div>
    </div>
  </div>
  <crop-image
    :heightCrop="570"
    :widthCrop="420"
    :heightWrap="570"
    :widthWrap="420"
    :check="isOpenInput"
    v-show="isCrop"
    @close="isCrop = false"
    @open="isCrop = true"
    @crop="handleCroppedImage"
  />
</template>

<style module scoped lang="scss">
@import './CamIntro.module.scss';
</style>
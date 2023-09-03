<script setup lang="ts">
import { ref, watch, type PropType } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
import CropImage from '@/components/CropImage/CropImage.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import Loading from '@/components/LoadingComponent/LoadingComponent.vue';

interface ItemRS {
  id: string;
  image: string;
  title: string;
  description: string;
}

const context = defineProps({
  uuid: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: false
  },
  description: {
    type: String,
    required: false
  },
  listItem: {
    type: Array as PropType<ItemRS[]>,
    required: false
  },
  image: {
    type: String,
    required: false
  }
});

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', data: { image: string }): void;
}>();
const urlFile = ref(context.image);
const isCrop = ref(false);
const isOpenInput = ref(false);
const fileData = ref();
const finalImage = ref();
const isLoading = ref(false);

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

//Choose image
const handleCroppedImage = (result: string) => {
  if (result) {
    urlFile.value = result;

    fileData.value = base64ToBlob(result);
    finalImage.value = new File([fileData.value], 'image.png', { type: 'image/png' });
  }
};

const submitForm = () => {
  isLoading.value = true;
  const deps = ref([]);

  const object = {
    heading: {
      id: context.uuid,
      title: context.title,
      content: context.description,
      image: context.image
    },
    subItem1: context.listItem?.[0]
      ? {
          id: context.listItem[0].id,
          title: context.listItem[0].title,
          content: context.listItem[0].description,
          image: context.listItem[0].image
        }
      : null,
    subItem2: context.listItem?.[1]
      ? {
          id: context.listItem[1].id,
          title: context.listItem[1].title,
          content: context.listItem[1].description,
          image: context.listItem[1].image
        }
      : null,
    subItem3: context.listItem?.[2]
      ? {
          id: context.listItem[2].id,
          title: context.listItem[1].title,
          content: context.listItem[1].description,
          image: context.listItem[2].image
        }
      : null
  };

  const formData = new FormData();
  formData.append('data', JSON.stringify(object));
  formData.append('image', finalImage.value as Blob);
  const { response } = useAxios<DataResponse>(
    'patch',
    '/home/section1',
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
      isLoading.value = false;
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
  <div :class="$style.camreason__overlay">
    <div :class="$style.camreason__modal">
      <div :class="$style.camreason__modal__heading">
        THAY ĐỔI ẢNH
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['camreason__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.camreason__modal__body">
        <div :class="$style.camreason__ctn">
          <img v-if="urlFile" :src="urlFile" alt="company" />
          <div :class="$style.camreason__image_overlay">
            <CamBtn @click="openFileInput" />
          </div>
        </div>
        <div :class="$style['modal__buttons']">
          <button @click="$emit('close')">Hủy</button>
          <button @click="submitForm">Cập nhật</button>
        </div>
      </div>
      <div v-show="isLoading" :class="$style.loading__overlay">
        <Loading />
      </div>
    </div>
  </div>
  <crop-image
    :heightCrop="447"
    :widthCrop="517"
    :heightWrap="447"
    :widthWrap="517"
    :check="isOpenInput"
    v-show="isCrop"
    @close="isCrop = false"
    @open="isCrop = true"
    @crop="handleCroppedImage"
  />
</template>

<style module scoped lang="scss">
@import './CamReason.module.scss';
</style>

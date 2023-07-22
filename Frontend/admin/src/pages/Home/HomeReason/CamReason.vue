<script setup lang="ts">
import { ref, watch, type PropType } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface MyFile extends File {
  name: string;
}

interface ItemRS {
  id: string;
  image: string;
  title: string;
  description: string;
}

const content = defineProps({
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
const selectedImage = ref(content.image);
const fileInput = ref<HTMLInputElement | null>(null);
const file = ref<MyFile | null>(null);

//Open file image
const openFileInput = () => {
  fileInput.value?.click();
};

//Choose image
const handleFileInputChange = (event: Event) => {
  const inputElement = event.target as HTMLInputElement;
  file.value = inputElement.files?.[0] || null;

  if (file.value) {
    const reader = new FileReader();
    reader.onload = () => {
      selectedImage.value = reader.result as string;
    };
    reader.readAsDataURL(file.value);
  }
};

const submitForm = () => {
  const deps = ref([]);

  const object = {
    heading: {
      id: content.uuid,
      title: content.title,
      content: content.description,
      image: content.image
    },
    subItem1: content.listItem?.[0]
      ? {
          id: content.listItem[0].id,
          title: content.listItem[0].title,
          content: content.listItem[0].description,
          image: content.listItem[0].image
        }
      : null,
    subItem2: content.listItem?.[1]
      ? {
          id: content.listItem[1].id,
          title: content.listItem[1].title,
          content: content.listItem[1].description,
          image: content.listItem[1].image
        }
      : null,
    subItem3: content.listItem?.[2]
      ? {
          id: content.listItem[2].id,
          title: content.listItem[1].title,
          content: content.listItem[1].description,
          image: content.listItem[2].image
        }
      : null
  };

  const formData = new FormData();
  formData.append('data', JSON.stringify(object));
  formData.append('image', file.value as Blob);
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
      Swal.fire({
        title: 'Cập nhật thành công',
        icon: 'success',
        confirmButtonText: 'Hoàn tất',
        width: '30rem'
      }).then((result) => {
        if (result.isConfirmed) {
          Swal.close();
          emits('update-content', {
            image: selectedImage.value || ''
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
          <img :src="selectedImage" alt="meeting" />
          <div :class="$style.camreason__image_overlay">
            <input
              type="file"
              ref="fileInput"
              style="display: none"
              @change="handleFileInputChange"
            />
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
</template>

<style module scoped lang="scss">
@import './CamReason.module.scss';
</style>

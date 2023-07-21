<script setup lang="ts">
import { ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';

interface MyFile extends File {
  name: string;
}

const context = defineProps({
  image: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['inFocus', 'close']);
const selectedImage = ref(context.image);
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
  Swal.fire({
    title: 'Cập nhật thành công',
    icon: 'success',
    confirmButtonText: 'Hoàn tất',
    width: '30rem'
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.close();
      emit('close');
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
          <img :src="selectedImage" alt="meeting" />
          <div :class="$style.camintro__image_overlay">
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
@import './CamIntro.module.scss';
</style>

<script setup lang="ts">
import { ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faPen } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';

const context = defineProps({
  image: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['inFocus', 'close']);
const selectedImage = ref(context.image);
const fileInput = ref<HTMLInputElement | null>(null);

//Open file image
const openFileInput = () => {
  fileInput.value?.click();
};

//Choose image
const handleFileInputChange = (event: Event) => {
  const inputElement = event.target as HTMLInputElement;
  const file = inputElement.files?.[0];

  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      selectedImage.value = reader.result as string;
    };
    reader.readAsDataURL(file);
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
  <div :class="$style.SVGReason__overlay">
    <div :class="$style.SVGReason__modal">
      <div :class="$style.SVGReason__modal__heading">
        Thay đổi Icon
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['SVGReason__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.SVGReason__modal__body">
        <div :class="$style.SVGReason__ctn">
          <img :src="selectedImage" alt="svg" />
          <div :class="$style.SVGReason__image_overlay">
            <input
              type="file"
              ref="fileInput"
              style="display: none"
              @change="handleFileInputChange"
            />
            <button @click="openFileInput">
              <font-awesome-icon :icon="faPen" :class="$style['SVGReason__img-edit']" />
            </button>
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
@import './SVGReason.module.scss';
</style>

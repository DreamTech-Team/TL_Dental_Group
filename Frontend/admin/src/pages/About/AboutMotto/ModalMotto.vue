<script setup lang="ts">
import { ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faCloudArrowUp, faRotate } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';

const context = defineProps({
  title: {
    type: String,
    required: true
  },
  content: {
    type: String,
    required: true
  },
  check: {
    type: String,
    required: true
  },
  image: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['close']);

const titleInput = ref(context.title);
const contentInput = ref(context.content);
const isModal = ref(context.check);
const img = ref(context.image);
const selectedImage = ref(null);
const fileImg = ref('');

//Validate form
const updateTitle = (e: Event) => {
  const target = e.target as HTMLInputElement;
  titleInput.value = target.value;
};
const updateContent = (e: Event) => {
  const target = e.target as HTMLInputElement;
  contentInput.value = target.value;
};

const submitForm = () => {
  if (titleInput.value.length < 4 || contentInput.value.length < 4) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '30rem'
    });
  } else {
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
  }
};

const handleFileInputChange = (event) => {
  const file = event.target.files[0];
  const reader = new FileReader();

  reader.onload = (e) => {
    selectedImage.value = e.target.result;
  };

  reader.readAsDataURL(file);
};

const handleChangeImage = () => {
  document.getElementById('input_file_modal').click();
};

const addFile = (e) => {
  const { files } = e.dataTransfer;
  if (files.length > 0) {
    const imageFile = files[0];
    fileImg.value = URL.createObjectURL(imageFile);
  }
};
</script>

<template>
  <div :class="$style.motto__overlay">
    <div :class="$style.motto__modal">
      <div :class="$style.motto__modal__heading">
        CẬP NHẬT PHƯƠNG CHÂM
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['motto__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.motto__modal__body">
        <h4>Tiêu đề chính</h4>
        <input type="text" placeholder="Nhập tiêu đề..." :value="titleInput" @input="updateTitle" />

        <h4>Mô tả</h4>
        <input
          type="text"
          placeholder="Nhập mô tả..."
          :value="contentInput"
          @input="updateContent"
        />

        <h4>Ảnh minh họa</h4>
        <div
          :class="$style['wrapper']"
          v-if="isModal === 'add' && !selectedImage && fileImg === ''"
          @click="handleChangeImage"
          v-cloak
          @drop.prevent="addFile"
          @dragover.prevent
        >
          <font-awesome-icon :icon="faCloudArrowUp" :class="$style['upload-ic']" />

          <span>Thả ảnh hoặc click để tải ảnh lên</span>

          <input
            type="file"
            style="display: none"
            id="input_file_modal"
            accept="image/*"
            @change="handleFileInputChange"
          />
        </div>
        <div :class="$style['wrapper1']" v-else>
          <div v-if="selectedImage || fileImg">
            <img :src="selectedImage || fileImg" />
          </div>

          <div :class="$style['about__mottomodal-button-wrapper']">
            <button :class="$style['about__mottomodal-button']" @click="handleChangeImage">
              <font-awesome-icon :icon="faRotate" :class="$style['about__mottomodal-button-ic']" />
              <span>Đổi ảnh</span>
            </button>

            <input
              type="file"
              style="display: none"
              id="input_file_modal"
              accept="image/*"
              @change="handleFileInputChange"
            />
          </div>
        </div>
        <div :class="$style['wrapper1']" v-if="isModal === 'update'">
          <div>
            <img :src="img" />
          </div>

          <div :class="$style['about__mottomodal-button-wrapper']">
            <button :class="$style['about__mottomodal-button']">
              <font-awesome-icon :icon="faRotate" :class="$style['about__mottomodal-button-ic']" />
              <span>Đổi ảnh</span>
            </button>

            <!-- <input
              type="file"
              style="display: none"
              id="input_file2"
              accept="image/*"
              @change="handleFileInputChange"
            /> -->
          </div>
        </div>

        <div :class="$style['modal__buttons']">
          <button @click="$emit('close')">Hủy</button>
          <button @click="submitForm">Lưu thay đổi</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalMotto.module.scss';
</style>

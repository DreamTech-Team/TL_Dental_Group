<script setup lang="ts">
import no_icon from '@/assets/imgs/Policy/icon/ic_noimage.svg';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faUpload } from '@fortawesome/free-solid-svg-icons';
import Editor from '@tinymce/tinymce-vue';
import { ref } from 'vue';

const selectedImage = ref(no_icon);
const fileName = ref('');

//Choose image
const handleFileInputChange = (event: Event) => {
  const inputElement = event.target as HTMLInputElement;
  const file = inputElement.files?.[0];

  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      selectedImage.value = reader.result as string;
      fileName.value = file.name as string;
    };
    reader.readAsDataURL(file);
  }
};
</script>
<template>
  <div :class="$style.container">
    <div :class="$style.container__back">
      <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
        <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
        <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
        <g id="SVGRepo_iconCarrier">
          <path
            d="M15 7L10 12L15 17"
            stroke="#000000"
            stroke-width="1.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          ></path>
        </g>
      </svg>
      <p>Trở về</p>
    </div>
    <div :class="$style.container__body">
      <div :class="$style['container__body-icon']">
        <h3 :class="$style['title-policy']">Biểu tượng</h3>
        <div :class="$style['container__body-icon-block']">
          <div :class="$style['container__body-icon-block-edit']">
            <img :src="selectedImage" alt="none" />
          </div>
          <input type="file" id="upload-btn" accept="image/*" @change="handleFileInputChange" />
          <label for="upload-btn">
            <div :class="$style['btn-title']">
              <div v-if="fileName === ''">Chọn Biểu Tượng</div>
              <div v-else>{{ fileName }}</div>
            </div>
            <div :class="$style['btn-icon']">
              <font-awesome-icon :class="$style['btn-icon-item']" :icon="faUpload" />
              <p>Upload</p>
            </div>
          </label>
        </div>
      </div>
      <div :class="$style['container__body-title']">
        <h3 :class="$style['title-policy']">Tên chính sách</h3>
        <input type="text" />
      </div>
      <div :class="$style['container__body-content']">
        <h3 :class="$style['title-policy']">Nội dung chính sách</h3>
        <!-- <editor
          allowedEvents="onChange"
          :onchange="updateTags"
          api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
          :initial-value="textareaInput.level.content"
          :value="textareaInput.level.content"
          :init="{
            selector: 'textarea',
            placeholder: 'Nhập phương châm',
            height: 350,
            menubar: false,
            plugins: [
              'advlist autolink lists link image charmap print preview anchor',
              'searchreplace visualblocks code fullscreen',
              'insertdatetime media table paste code help wordcount'
            ],
            toolbar:
              'undo redo | formatselect | bold italic backcolor | \
           alignleft aligncenter alignright alignjustify | \
           bullist numlist outdent indent | removeformat | help'
          }"
        /> -->

        <editor
          allowedEvents="onChange"
          api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
          :init="{
            selector: 'textarea',
            placeholder: 'Nội dung chính sách',
            height: 350,
            menubar: false,
            plugins: [
              'advlist autolink lists link image charmap print preview anchor',
              'searchreplace visualblocks code fullscreen',
              'insertdatetime media table paste code help wordcount'
            ],
            toolbar:
              'undo redo | formatselect | bold italic backcolor | \
           alignleft aligncenter alignright alignjustify | \
           bullist numlist outdent indent | removeformat | help'
          }"
        />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ManagePolicyEdit.module.scss';
</style>

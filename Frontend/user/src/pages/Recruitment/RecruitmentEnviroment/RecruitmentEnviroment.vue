<script setup lang="ts">
import { ref } from 'vue';
import Swal from 'sweetalert2';

import styles from './RecruitmentEnviroment.module.scss';
import { img1, img2, img3, img4, img5 } from '@/assets/imgs/Recruitment/RecruitmentImgs';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCameraRotate, faPencil } from '@fortawesome/free-solid-svg-icons';

const editEvir = ref(false);
const selectedImageEnvir = ref([img1, img2, img3, img4, img5]);

let prevImageEnvir: string[] = [];

const handleEditEnvir = () => {
  prevImageEnvir = selectedImageEnvir.value.slice();
  editEvir.value = true;
};

//Choose image
const handleFileInputChange = (event: Event, index: number) => {
  const inputElement = event.target as HTMLInputElement;
  const file = inputElement.files?.[0];

  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      selectedImageEnvir.value[index] = reader.result as string;
    };
    reader.readAsDataURL(file);
  }
};

const handleModalCancel = () => {
  Swal.fire({
    title: 'Nội dung của bạn chưa được lưu!',
    icon: 'warning',
    showDenyButton: true,
    confirmButtonText: 'Trở lại',
    denyButtonText: 'Thoát',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    if (result.isDenied) {
      editEvir.value = false;
      selectedImageEnvir.value = [];
      selectedImageEnvir.value = prevImageEnvir.slice();
      prevImageEnvir = [];
    }
  });
};

const handleModalUpdate = () => {
  Swal.fire({
    title: 'Bạn có chắc chắn muốn cập nhật nội dung không?',
    icon: 'question',
    showDenyButton: true,
    confirmButtonText: 'Cập nhật',
    denyButtonText: 'Hủy',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      editEvir.value = false;
      Swal.fire({
        title: 'Cập nhật nội dung thành công!',
        icon: 'success',
        customClass: {
          popup: styles['container-popup'],
          confirmButton: styles['confirm-button'],
          denyButton: styles['deny-button']
        }
      });
    } else if (result.isDenied) {
      Swal.fire({
        title: 'Nội dung chưa được cập nhật',
        icon: 'error',
        customClass: {
          popup: styles['container-popup'],
          confirmButton: styles['confirm-button'],
          denyButton: styles['deny-button']
        }
      });
    }
  });
};
</script>
<template>
  <div :class="$style.container__envir">
    <div :class="$style['block-btn-edit']">
      <div
        :class="[$style['btn-edit-2'], $style['btn-edit']]"
        v-if="!editEvir"
        @click="handleEditEnvir"
      >
        <FontAwesomeIcon :icon="faPencil" />
      </div>
      <div :class="$style['update-btn']" v-if="editEvir">
        <div
          :class="[$style['update-btn-submit'], $style['update-btn-cancel']]"
          @click="handleModalCancel"
        >
          Hủy
        </div>
        <div
          :class="[$style['update-btn-submit'], $style['update-btn-update']]"
          @click="handleModalUpdate"
        >
          Cập nhật
        </div>
      </div>
    </div>
    <div :class="$style['container__envir-title']">Môi Trường Làm Việc Chuyên Nghiệp</div>
    <div :class="$style['container__envir-block']">
      <div
        :class="[
          $style['container__envir-block-img1'],
          $style['container__envir-block-img1-bottom']
        ]"
      >
        <div :class="$style.block__img">
          <img :src="selectedImageEnvir[0]" alt="none" />
          <div :class="$style['block__img-edit']" v-if="editEvir">
            <input type="file" @change="(e) => handleFileInputChange(e, 0)" />
            <FontAwesomeIcon :icon="faCameraRotate" />
          </div>
        </div>
      </div>
      <div :class="$style['container__envir-block-img2']">
        <div :class="$style.block__img">
          <img :src="selectedImageEnvir[1]" alt="none" />
          <div :class="$style['block__img-edit']" v-if="editEvir">
            <input type="file" @change="(e) => handleFileInputChange(e, 1)" />
            <FontAwesomeIcon :icon="faCameraRotate" />
          </div>
        </div>
        <div
          :class="[
            $style['container__envir-block-img2-circle'],
            $style['container__envir-block-img2-circle-1']
          ]"
        ></div>
      </div>
      <div
        :class="[
          $style['container__envir-block-img1'],
          $style['container__envir-block-img1-center']
        ]"
      >
        <div :class="$style.block__img">
          <img :src="selectedImageEnvir[2]" alt="none" />
          <div :class="$style['block__img-edit']" v-if="editEvir">
            <input type="file" @change="(e) => handleFileInputChange(e, 2)" />
            <FontAwesomeIcon :icon="faCameraRotate" />
          </div>
        </div>
      </div>
      <div
        :class="[
          $style['container__envir-block-img2'],
          $style['container__envir-block-img2-bottom']
        ]"
      >
        <div>
          <div
            :class="[
              $style['container__envir-block-img2-circle'],
              $style['container__envir-block-img2-circle-2']
            ]"
          ></div>
          <div :class="$style.block__img">
            <img :src="selectedImageEnvir[3]" alt="none" />
            <div :class="$style['block__img-edit']" v-if="editEvir">
              <input type="file" @change="(e) => handleFileInputChange(e, 3)" />
              <FontAwesomeIcon :icon="faCameraRotate" />
            </div>
          </div>
        </div>
      </div>
      <div
        :class="[$style['container__envir-block-img1'], $style['container__envir-block-img1-top']]"
      >
        <div :class="$style.block__img">
          <img :src="selectedImageEnvir[4]" alt="none" />
          <div :class="$style['block__img-edit']" v-if="editEvir">
            <input type="file" @change="(e) => handleFileInputChange(e, 4)" />
            <FontAwesomeIcon :icon="faCameraRotate" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentEnviroment.module.scss';
</style>

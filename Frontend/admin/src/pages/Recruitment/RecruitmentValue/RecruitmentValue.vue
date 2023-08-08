<script setup lang="ts">
import { onMounted, ref, watch, type PropType } from 'vue';
import RecruitmentCard from '../RecruitmentCard/RecruitmentCard.vue';
import styles from './RecruitmentValue.module.scss';
import Swal from 'sweetalert2';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface CardElementItem {
  id: string;
  icon: { link: string; style: string };
  title: { content: string; style: string };
  content: { content: string; style: string };
  image: { link: string; style: string };
  type: string;
}

interface MainItem {
  id: string;
  content: string;
  type: string;
}

const props = defineProps({
  handleEditValue: { type: Function, required: true },
  contentValueItems: { type: Object as unknown as PropType<CardElementItem[]>, required: true },
  contentValueMainItem: { type: Object as PropType<MainItem>, required: true }
});

const valueItems = ref();
const valueMainItem = ref();
const showBtnUpdateImg = ref(false);
const paramAxios = ref();

const updateContent = (e: Event) => {
  const target = e.target as HTMLInputElement;
  valueMainItem.value = target.value;
};

const handleCancelImage = () => {
  Swal.fire({
    title: 'Nội chưa được lưu!',
    icon: 'warning',
    showDenyButton: true,
    confirmButtonText: 'Trở lại',
    denyButtonText: 'Hủy',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    if (result.isDenied) {
      showBtnUpdateImg.value = false;
      valueItems.value = props.contentValueItems;
    }
  });
};

const handleUpdateImage = () => {
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
      const dataUpdate = {
        mainItem: {
          id: props.contentValueMainItem.id,
          content: valueMainItem.value,
          type: props.contentValueMainItem.type
        }
      };

      const patchMainItem = useAxios<DataResponse>(
        'patch',
        '/recruitment/section2',
        dataUpdate,
        {},
        paramAxios.value
      );

      watch(patchMainItem.error, (value) => console.log(value));

      watch(patchMainItem.response, () => {
        Swal.fire({
          title: 'Cập nhật nội dung thành công!',
          icon: 'success',
          customClass: {
            popup: styles['container-popup'],
            confirmButton: styles['confirm-button'],
            denyButton: styles['deny-button']
          }
        });
        showBtnUpdateImg.value = false;
      });
    } else if (result.isDenied) {
      Swal.fire({
        title: 'Nội chưa được cập nhật!',
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

watch(
  () => props.contentValueItems,
  (value) => {
    valueItems.value = value;
    console.log(value);
  }
);

watch(
  () => props.contentValueMainItem,
  (value) => {
    valueMainItem.value = value.content;
  }
);

onMounted(() => {
  valueItems.value = props.contentValueItems;
  console.log(props.contentValueItems);
});
</script>
<template>
  <div :class="$style.container__value">
    <div :class="$style['block-btn-edit']">
      <div :class="[$style['btn-edit-2'], $style['btn-edit']]" @click="props.handleEditValue">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
          <g id="SVGRepo_iconCarrier">
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="m3.99 16.854-1.314 3.504a.75.75 0 0 0 .966.965l3.503-1.314a3 3 0 0 0 1.068-.687L18.36 9.175s-.354-1.061-1.414-2.122c-1.06-1.06-2.122-1.414-2.122-1.414L4.677 15.786a3 3 0 0 0-.687 1.068zm12.249-12.63 1.383-1.383c.248-.248.579-.406.925-.348.487.08 1.232.322 1.934 1.025.703.703.945 1.447 1.025 1.934.058.346-.1.677-.348.925L19.774 7.76s-.353-1.06-1.414-2.12c-1.06-1.062-2.121-1.415-2.121-1.415z"
              fill="#000000"
            ></path>
          </g>
        </svg>
      </div>
    </div>
    <div :class="$style['container__value-heading']">
      <div :class="$style['container__value-heading-title']">
        <div :class="$style['container__value-heading-title-main']">
          <div :class="$style['container__value-heading-title-main-1']">Giá Trị</div>
          <div :class="$style['container__value-heading-title-main-2']">Cốt Lõi</div>
        </div>
        <div :class="$style['container__value-heading-title-3']">Từ TL Dental Group</div>
      </div>
      <div :class="$style['container__value-heading-content']">
        <div
          v-if="!showBtnUpdateImg"
          :class="[$style['btn-edit-3'], $style['btn-edit']]"
          @click="showBtnUpdateImg = true"
        >
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
            <g id="SVGRepo_iconCarrier">
              <path
                fill-rule="evenodd"
                clip-rule="evenodd"
                d="m3.99 16.854-1.314 3.504a.75.75 0 0 0 .966.965l3.503-1.314a3 3 0 0 0 1.068-.687L18.36 9.175s-.354-1.061-1.414-2.122c-1.06-1.06-2.122-1.414-2.122-1.414L4.677 15.786a3 3 0 0 0-.687 1.068zm12.249-12.63 1.383-1.383c.248-.248.579-.406.925-.348.487.08 1.232.322 1.934 1.025.703.703.945 1.447 1.025 1.934.058.346-.1.677-.348.925L19.774 7.76s-.353-1.06-1.414-2.12c-1.06-1.062-2.121-1.415-2.121-1.415z"
                fill="#000000"
              ></path>
            </g>
          </svg>
        </div>
        <div v-if="showBtnUpdateImg" :class="$style['update-btn']">
          <div
            :class="[$style['update-btn-submit'], $style['update-btn-cancel']]"
            @click="handleCancelImage"
          >
            Hủy
          </div>
          <div
            :class="[$style['update-btn-submit'], $style['update-btn-update']]"
            @click="handleUpdateImage"
          >
            Cập nhật
          </div>
        </div>
        <p v-if="!showBtnUpdateImg">
          {{ valueMainItem }}
        </p>
        <textarea
          v-else
          placeholder="Nhập nội dung mô tả"
          v-model="valueMainItem"
          @input="updateContent"
        />
      </div>
    </div>
    <div :class="$style['container__value-list']">
      <recruitment-card :items="contentValueItems" :style="'type3'" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentValue.module.scss';
</style>

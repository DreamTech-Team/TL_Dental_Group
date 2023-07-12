<script setup lang="ts">
import Editor from '@tinymce/tinymce-vue';
import { ref, computed } from 'vue';
import type { CSSProperties } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus, faMagnifyingGlass, faMinus, faXmark } from '@fortawesome/free-solid-svg-icons';
import { type PropType } from 'vue';
import { staffs } from '../../Staff';

// interface Tags {
//   id: number;
//   name: string;
//   createDate: string;
// }


export interface CloseModalFn {
  (...payload: any[]): void;
}

defineProps({
  closeModal: {
    type: Function as PropType<CloseModalFn>,
    required: true
  }
});

const activityContent = ref({
  content: ``
});

const _content = ref(``);
const activityTitle = ref('');

const handleChangeContent = (e: { target: { getContent: () => string } }) => {
  _content.value = e.target.getContent();
  console.log(_content.value);
};

// const handleUpdateContent = () => {
//   isEdit.value = false;
//   activityContent.value.content = _content.value;
// };
</script>
<template>
  <div :class="$style.activity_container">
    <div :class="$style['activity_container--title']">
      <p>THÊM NHÂN VIÊN</p>
      <font-awesome-icon @click="closeModal" :icon="faXmark" :class="$style.activity_cancel" />
    </div>
    <div :class="$style['activity_container--wrap']">
      <div :class="$style['activity_container--wrap-left']">
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-name">Họ và tên</label>
          <input
            id="staff-name"
            v-model="activityTitle"
            placeholder="Nhập họ và tên"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-position">Vị trí</label>
          <input
            id="staff-position"
            v-model="activityTitle"
            placeholder="Nhập vị trí"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-phone">Số điện thoại</label>
          <input
            id="staff-phone"
            v-model="activityTitle"
            placeholder="Nhập số điện thoại"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-address">Địa chỉ</label>
          <input
            id="staff-address"
            v-model="activityTitle"
            placeholder="Nhập địa chỉ"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-email">Email</label>
          <input
            id="staff-email"
            v-model="activityTitle"
            placeholder="Nhập email"
            :class="$style['custom-input']"
          />
        </div>
      </div>
      <div :class="$style['activity_container--btn']">
        <button :class="$style.button_cancel">Hủy</button>
        <button :class="$style.button_add">Thêm</button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './StaffModal.module.scss';
</style>

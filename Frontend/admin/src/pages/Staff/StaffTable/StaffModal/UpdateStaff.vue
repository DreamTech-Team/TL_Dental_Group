<script setup lang="ts">
import Editor from '@tinymce/tinymce-vue';
import { ref, computed } from 'vue';
import type { CSSProperties } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus, faMagnifyingGlass, faMinus, faXmark } from '@fortawesome/free-solid-svg-icons';

import { type PropType } from 'vue';
// interface Tags {
//   id: number;
//   name: string;
//   createDate: string;
// }

interface Staff {
  name: string;
  position: string;
  phone: string;
  address: string;
  email: string;
}

export interface CloseModalFn {
  (...payload: any[]): void;
}

const props = defineProps({
  selectedStaff: {
    type: Object as () => Staff,
    required: true
  },
  closeModal: {
    type: Function as PropType<CloseModalFn>,
    required: true
  }
});

const staffName = ref(props.selectedStaff.name);
const staffPosition = ref(props.selectedStaff.position);
const staffPhone = ref(props.selectedStaff.phone);
const staffAddress = ref(props.selectedStaff.address);
const staffEmail = ref(props.selectedStaff.email);

const updateStaff = () => {
  const updatedStaff: Staff = {
    name: staffName.value,
    position: staffPosition.value,
    phone: staffPhone.value,
    address: staffAddress.value,
    email: staffEmail.value
  };

  // emit('updateStaff', updatedStaff); // Gửi sự kiện updateStaff với giá trị cập nhật

  props.closeModal(); // Đóng modal
};
</script>
<template>
  <div :class="$style.activity_container">
    <div :class="$style['activity_container--title']">
      <p>CHỈNH SỬA NHÂN VIÊN</p>
      <font-awesome-icon @click="closeModal" :icon="faXmark" :class="$style.activity_cancel" />
    </div>
    <div :class="$style['activity_container--wrap']">
      <div :class="$style['activity_container--wrap-left']">
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-name">Họ và tên</label>
          <input
            id="staff-name"
            v-model="staffName"
            placeholder="Nhập họ và tên"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-position">Vị trí</label>
          <input
            id="staff-position"
            v-model="staffPosition"
            placeholder="Nhập vị trí"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-phone">Số điện thoại</label>
          <input
            id="staff-phone"
            v-model="staffPhone"
            placeholder="Nhập số điện thoại"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-address">Địa chỉ</label>
          <input
            id="staff-address"
            v-model="staffAddress"
            placeholder="Nhập địa chỉ"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-email">Email</label>
          <input
            id="staff-email"
            v-model="staffEmail"
            placeholder="Nhập email"
            :class="$style['custom-input']"
          />
        </div>
      </div>
      <div :class="$style['activity_container--btn']">
        <button :class="$style.button_cancel">Hủy</button>
        <button :class="$style.button_add">Cập nhật</button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './StaffModal.module.scss';
</style>

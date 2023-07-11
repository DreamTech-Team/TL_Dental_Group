<script setup lang="ts">
import { ref } from 'vue';
import Swal from 'sweetalert2';

const content = defineProps({
  phone: {
    type: String,
    required: true
  },
  hotline: {
    type: String,
    required: true
  },
  email: {
    type: String,
    required: true
  },
  facebook: {
    type: String,
    required: false
  },
  zalo: {
    type: String,
    required: false
  }
});

const emit = defineEmits(['inFocus', 'close']);

//Validate form
const Phone = ref(content.phone);
const Hotline = ref(content.hotline);
const Email = ref(content.email);
const Facebook = ref(content.facebook);
const Zalo = ref(content.zalo);

const updateInfor = (e: Event, tag: string) => {
  const target = e.target as HTMLInputElement;
  switch (tag) {
    case 'Phone':
      Phone.value = target.value;
      break;
    case 'Hotline':
      Hotline.value = target.value;
      break;
    case 'Email':
      Email.value = target.value;
      break;
    case 'Facebook':
      Facebook.value = target.value;
      break;
    case 'Zalo':
      Zalo.value = target.value;
      break;
    default:
      break;
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
  <div :class="$style.part3__overlay">
    <div :class="$style.part3__modal">
      <div :class="$style.part3__modal__heading">
        <h4>Thông tin liên hệ</h4>
      </div>
      <div :class="$style.part3__modal__body">
        <h4>Tên công ty</h4>
        <input
          type="text"
          placeholder="Nhập SĐT"
          v-model="Phone"
          @input="updateInfor($event, 'Phone')"
        />
        <h4>Hotline</h4>
        <input
          type="text"
          placeholder="Nhập Hotline"
          v-model="Hotline"
          @input="updateInfor($event, 'Hotline')"
        />
        <h4>Email</h4>
        <input
          type="email"
          placeholder="Nhập Email"
          v-model="Email"
          @input="updateInfor($event, 'Email')"
        />
        <h4>Link Facebook</h4>
        <input
          type="text"
          placeholder="Nhập Link Facebook"
          v-model="Facebook"
          @input="updateInfor($event, 'Facebook')"
        />
        <h4>Số điện thoại Zalo</h4>
        <input
          type="text"
          placeholder="Nhập SĐT Zalo"
          v-model="Zalo"
          @input="updateInfor($event, 'Zalo')"
        />
      </div>
      <div :class="$style['modal__buttons']">
        <button @click="$emit('close')">Hủy</button>
        <button @click="submitForm">Cập nhật</button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalPart3.module.scss';
</style>

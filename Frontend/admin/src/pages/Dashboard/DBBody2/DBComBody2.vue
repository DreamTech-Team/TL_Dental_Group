<script setup lang="ts">
import Comp from '@/assets/imgs/Dashboard/img_comp.png';
import Camera from '@/assets/icons/Camera.png';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import { ref, nextTick } from 'vue';

const AddressRef = ref<HTMLInputElement | null>(null);
const PhoneRef = ref<HTMLInputElement | null>(null);
const HotlineRef = ref<HTMLInputElement | null>(null);
const GGAdressRef = ref<HTMLInputElement | null>(null);

const Address = ref('135B Trần Hưng Đạo, Quận 1, TP.HCM');
const Phone = ref('0898521456');
const Hotline = ref('442366781');
const GGAdress = ref('https://www.google.com/maps/@9.779349,105.6189045,11z?hl=vi-VN&entry=ttu');
const GGIframe = ref(
  // eslint-disable-next-line max-len
  'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.5736868084896!2d106.69224417517916!3d10.767301989380964!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f15f08d6fa1%3A0x7b2e11ab195377b!2zMTM1YiDEkC4gVHLhuqduIEjGsG5nIMSQ4bqhbywgUGjGsOG7nW5nIEPhuqd1IMOUbmcgTMOjbmgsIFF14bqtbiAxLCBUaMOgbmggcGjhu5EgSOG7kyBDaMOtIE1pbmgsIFZp4buHdCBOYW0!5e0!3m2!1svi!2s!4v1687174673884!5m2!1svi!2s'
);
const selectedImg = ref(Comp);
const fileInput = ref<HTMLInputElement | null>(null);
const isEditable = ref(false);
import Swal from 'sweetalert2';

const resetValue = () => {
  isEditable.value = false;
  Address.value = '135B Trần Hưng Đạo, Quận 1, TP.HCM';
  Phone.value = '0898521456';
  Hotline.value = '442366781';
  GGAdress.value = 'https://www.google.com/maps/@9.779349,105.6189045,11z?hl=vi-VN&entry=ttu';
  selectedImg.value = Comp;

  window.scrollTo({
    top: 1,
    behavior: 'smooth'
  });
};

const alertDialog = (context: string) => {
  Swal.fire({
    title: context,
    icon: 'error',
    showCancelButton: false,
    width: '30rem'
  });
  setTimeout(function () {
    Swal.close();
  }, 1200);
};

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
      selectedImg.value = reader.result as string;
    };
    reader.readAsDataURL(file);
  }
};

const scrollToBottom = () => {
  isEditable.value = true;
  nextTick(() => {
    const bottom = document.documentElement.scrollHeight;
    window.scrollTo({
      top: bottom,
      behavior: 'smooth'
    });
  });
};

const updateInfor = (e: Event, tag: string) => {
  const target = e.target as HTMLInputElement;
  switch (tag) {
    case 'Address':
      Address.value = target.value;
      break;
    case 'Phone':
      Phone.value = target.value;
      break;
    case 'Hotline':
      Hotline.value = target.value;
      break;
    case 'Google':
      GGAdress.value = target.value;
      break;
    default:
      break;
  }
};

const phoneValid = (phone: string) => {
  return /(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\b/.test(phone);
};

const submitForm = () => {
  if (Address.value.length <= 10) {
    Address.value = '';
    AddressRef.value?.focus();
    alertDialog('Địa chỉ quá ngắn');
    return;
  }
  if (!phoneValid(Phone.value)) {
    Phone.value = '';
    PhoneRef.value?.focus();
    alertDialog('SĐT không tồn tại');
    return;
  }
  if (Hotline.value.length <= 5) {
    Hotline.value = '';
    HotlineRef.value?.focus();
    alertDialog('Hotline không tồn tại');
    return;
  }
  if (selectedImg.value.length <= 5) {
    selectedImg.value = '';
    alertDialog('Bạn chưa chọn ảnh');
    return;
  }
  if (GGAdress.value.length <= 5) {
    GGAdress.value = '';
    GGAdressRef.value?.focus();
    alertDialog('Địa chỉ map không tồn tại');
    return;
  }
  Swal.fire({
    title: 'Cập nhật thành công',
    icon: 'success',
    confirmButtonText: 'Hoàn tất',
    width: '30rem'
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.close();
      isEditable.value = false;
    }
  });
};
</script>
<template>
  <div :class="$style.dashboard_body2">
    <div :class="$style.dashboard_body2_left">
      <h3>Địa chỉ</h3>
      <input
        type="text"
        v-model="Address"
        :readonly="!isEditable"
        ref="AddressRef"
        @input="updateInfor($event, 'Address')"
        style="width: 65%"
        :style="{ color: !isEditable ? '#979090' : '' }"
      />
      <h3>Số điện thoại</h3>
      <input
        type="text"
        v-model="Phone"
        :readonly="!isEditable"
        ref="PhoneRef"
        @input="updateInfor($event, 'Phone')"
        style="width: 45%"
        :style="{ color: !isEditable ? '#979090' : '' }"
      />
      <h3>Hotline</h3>
      <input
        type="text"
        v-model="Hotline"
        :readonly="!isEditable"
        ref="HotlineRef"
        @input="updateInfor($event, 'Hotline')"
        style="width: 45%"
        :style="{ color: !isEditable ? '#979090' : '' }"
      />
      <h3>Hình ảnh cơ sở</h3>
      <div :class="$style.dashboard_body2_img">
        <img :src="selectedImg" />
        <input type="file" ref="fileInput" style="display: none" @change="handleFileInputChange" />
        <div v-if="isEditable" @click="openFileInput" :class="$style.dashboard_btn_img">
          <img :src="Camera" />
        </div>
      </div>
    </div>
    <div :class="$style.dashboard_body2_left">
      <h3>Link Google Map</h3>
      <input
        type="text"
        v-model="GGAdress"
        :readonly="!isEditable"
        ref="GGAdressRef"
        @input="updateInfor($event, 'Google')"
        style="width: 100%"
        :style="{ color: !isEditable ? '#979090' : '' }"
      />
      <div :class="$style.dashboard_body2_map">
        <iframe
          :src="GGIframe"
          allowfullscreen="false"
          loading="lazy"
          referrerpolicy="no-referrer-when-downgrade"
          width="100%"
          height="100%"
        ></iframe>
      </div>
      <div v-if="isEditable" :class="$style.dashboard_body2_btns">
        <button @click="resetValue" :class="$style['dashboard_body2_btns-cancel']">Hủy</button>
        <button @click="submitForm" :class="$style['dashboard_body2_btns-update']">Cập nhật</button>
      </div>
    </div>
    <EditBtn v-if="!isEditable" @click="scrollToBottom" style="width: 50px; height: 50px" />
  </div>
</template>

<style module scoped lang="scss">
@import './DBComBody2.module.scss';
</style>

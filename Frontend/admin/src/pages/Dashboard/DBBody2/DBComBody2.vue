<script setup lang="ts">
import Camera from '@/assets/icons/Camera.png';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import { ref, nextTick, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import Swal from 'sweetalert2';

interface MyFile extends File {
  name: string;
}

//Ref to focus
const AddressRef = ref<HTMLInputElement | null>(null);
const PhoneRef = ref<HTMLInputElement | null>(null);
const HotlineRef = ref<HTMLInputElement | null>(null);
const GGAdressRef = ref<HTMLInputElement | null>(null);
const GGIframeRef = ref<HTMLInputElement | null>(null);

//Init value
const InitValue = ref({
  id: '',
  address: '',
  phone: '',
  hotline: '',
  ggaddress: '',
  image: '',
  ggiframe: ''
});

//Properties
const Address = ref();
const Phone = ref();
const Hotline = ref();
const GGAdress = ref();
const GGIframe = ref();
// eslint-disable-next-line max-len
const selectedImg = ref();
const fileInput = ref<HTMLInputElement | null>(null);
const file = ref<MyFile | null>(null);
const isEditable = ref(false);

//Get value
const deps = ref([]);
const getInfor = useAxios<DataResponse>('get', '/facility/', {}, {}, deps.value);
watch(getInfor.response, () => {
  InitValue.value.id = getInfor.response.value?.data?.id;

  Address.value = getInfor.response.value?.data?.address;
  InitValue.value.address = getInfor.response.value?.data?.address;

  Phone.value = getInfor.response.value?.data?.phoneNumber;
  InitValue.value.phone = getInfor.response.value?.data?.phoneNumber;

  Hotline.value = getInfor.response.value?.data?.hotline;
  InitValue.value.hotline = getInfor.response.value?.data?.hotline;

  selectedImg.value = getInfor.response.value?.data?.image;
  InitValue.value.image = getInfor.response.value?.data?.image;

  GGAdress.value = getInfor.response.value?.data?.mapLink;
  InitValue.value.ggaddress = getInfor.response.value?.data?.mapLink;

  GGIframe.value = getInfor.response.value?.data?.mapIframe;
  InitValue.value.ggiframe = getInfor.response.value?.data?.mapIframe;
});

const resetValue = () => {
  isEditable.value = false;
  Address.value = InitValue.value.address;
  Phone.value = InitValue.value.phone;
  Hotline.value = InitValue.value.hotline;
  GGAdress.value = InitValue.value.ggaddress;
  selectedImg.value = InitValue.value.image;
  GGIframe.value = InitValue.value.ggiframe;

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
  file.value = inputElement.files?.[0] || null;

  if (file.value) {
    const reader = new FileReader();
    reader.onload = () => {
      selectedImg.value = reader.result as string;
    };
    reader.readAsDataURL(file.value);
  }
};

//Get url from iframe
const extractSrcFromIframe = (iframeString: string) => {
  const srcPattern = /src="([^"]*)"/;
  const match = iframeString.match(srcPattern);
  if (match) {
    const src = match[1];
    return src;
  } else {
    return '';
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
    case 'Iframe':
      GGIframe.value = target.value;
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
  if (GGIframe.value.length <= 5) {
    GGIframe.value = '';
    GGIframeRef.value?.focus();
    alertDialog('Địa chỉ iframe không tồn tại');
    return;
  }

  const object = {
    id: InitValue.value.id,
    address: Address.value,
    phoneNumber: Phone.value,
    hotline: Hotline.value,
    image: selectedImg.value,
    mapLink: GGAdress.value,
    mapIframe: GGIframe.value
  };

  const formData = new FormData();
  formData.append('data', JSON.stringify(object));
  formData.append('image', file.value as Blob);
  const { response } = useAxios<DataResponse>(
    'patch',
    '/facility/',
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    },
    deps.value
  );

  watch(response, () => {
    if (response.value?.status === 'ok') {
      Swal.fire({
        title: 'Cập nhật thành công',
        icon: 'success',
        confirmButtonText: 'Hoàn tất',
        width: '30rem'
      }).then((result) => {
        if (result.isConfirmed) {
          isEditable.value = false;
          Swal.close();
        }
      });
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
      <h3>
        Link Google Iframe
        <a href="https://www.youtube.com/watch?v=E-5mqComhFM" target="_blank"
          >Hướng dẫn lấy link Iframe</a
        >
      </h3>
      <input
        type="text"
        v-model="GGIframe"
        :readonly="!isEditable"
        ref="GGIframeRef"
        @input="updateInfor($event, 'Iframe')"
        style="width: 100%"
        :style="{ color: !isEditable ? '#979090' : '' }"
      />
      <div :class="$style.dashboard_body2_map">
        <iframe
          :src="extractSrcFromIframe(GGIframe)"
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

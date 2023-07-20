<script setup lang="ts">
import Intro from '@/assets/imgs/About/Intro.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faRotate } from '@fortawesome/free-solid-svg-icons';
import { Ref, ref, onMounted, watch } from 'vue';
import Croppie from 'croppie';
import 'croppie/croppie.css';

const props = defineProps({
  heightWrap: { type: Number, required: true },
  heightCrop: { type: Number, required: true },
  widthWrap: { type: Number, required: true },
  widthCrop: { type: Number, required: true },
  check: { type: Boolean, required: true }
});

const emit = defineEmits(['close', 'open', 'closeIpnut', 'crop']);

const selectedImage: Ref<string | null> = ref(null);
const croppieInstance: Ref<Croppie | null> = ref(null);
const imageFile: Ref<string | null> = ref(null);
const isSelectImage = ref(false);

// Hàm khởi tạo vùng crop ảnh
const initCroppie = () => {
  const container = document.getElementById('croppieContainer');
  if (container) {
    croppieInstance.value = new Croppie(container, {
      viewport: { width: props.widthCrop, height: props.heightCrop },
      boundary: { width: props.widthWrap, height: props.heightWrap },
      showZoomer: false
    });
  }
};

// Khi component chạy thì nó sẽ được khởi tạo
onMounted(initCroppie);

// Hàm xử lí click button để focus vào thẻ input type file
const handleChangeImage = () => {
  const inputElement = document.getElementById('input_file');
  if (inputElement) {
    inputElement.click();
  }
};

watch(() => props.check, handleChangeImage);

// Hàm chuyển đổi từ ArrayBuffer sang string
const arrayBufferToString = (buffer: ArrayBuffer) => {
  const uintArray = new Uint16Array(buffer);
  const charArray: string[] = [];
  for (let i = 0; i < uintArray.length; i++) {
    charArray.push(String.fromCharCode(uintArray[i]));
  }
  return charArray.join('');
};

// Hàm xử lí lấy ảnh từ máy lên
const handleFileInputChange = (event: Event) => {
  const target = event.target as HTMLInputElement;

  if (target.files) {
    const file = target.files[0];
    const reader = new FileReader();

    reader.onload = (e: ProgressEvent<FileReader>) => {
      const result = e.target?.result;
      if (result instanceof ArrayBuffer) {
        selectedImage.value = arrayBufferToString(result);
      } else if (typeof result === 'string') {
        selectedImage.value = result;
      }
      isSelectImage.value = !isSelectImage.value;

      croppieInstance.value.bind({
        url: reader.result as string
      });
    };

    reader.readAsDataURL(file);

    target.value = '';
  }
};

// Hàm kiểm xác định trạng thái của biến isCrop để xác định có muốn tiếp tục crop ảnh hay không?
const handleCheckCrop = () => {
  if (selectedImage.value) {
    emit('open');
  } else {
    emit('close');
  }
};
// Khi chọn ảnh thi hàm handleCheckCrop sẽ được thực thi
watch(() => isSelectImage.value, handleCheckCrop);

// Hàm xử lí crop ảnh
const crop = () => {
  if (croppieInstance.value) {
    emit('close');
    croppieInstance.value.result('base64').then((result: string) => {
      // Xử lý kết quả crop ở đây (ví dụ: lưu vào biến imageFile)
      imageFile.value = result;
      emit('crop', result);
    });
  }
};
</script>
<template>
  <div :class="$style.crop">
    <div id="croppieContainer" ref="croppieContainer"></div>
    <div>
      <button @click="crop">Crop</button>
      <button @click="emit('close'), emit('closeIpnut')">Hủy</button>
    </div>

    <input
      type="file"
      style="display: none"
      id="input_file"
      accept="image/*"
      @change="handleFileInputChange"
    />
  </div>
</template>

<style module scoped lang="scss">
@import './CropImage.module.scss';
</style>

<script setup lang="ts">
import { ref, watch, type Ref } from 'vue';
import InfoCompany from '@/assets/imgs/About/InfoCompany.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faClose, faPencil, faCheck, faRotate } from '@fortawesome/free-solid-svg-icons';
import Editor from '@tinymce/tinymce-vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface MyInputElement extends HTMLInputElement {
  getContent(): string;
}

interface CompanyInfo {
  id: string;
  content: string;
  image: string;
}

const variableChange = ref([]);
const contentInfoComapny = ref<CompanyInfo[]>([]);
const content1 = ref(''); //Lưu nội dung 1
const content2 = ref(''); //Lưu nội dung 2
const selectedImage: Ref<string | null> = ref(null); //Lưu ảnh 1
const selectedImage1: Ref<string | null> = ref(null); //Lưu ảnh 2
const imageSave1 = ref<File | null>(null);
const imageSave2 = ref<File | null>(null);
const isEdit = ref(false);

// Gọi hàm useAxios để lấy response, error, và isLoading
const { response, error, isLoading } = useAxios<DataResponse>(
  'get',
  '/introduce/company-info',
  {},
  {},
  variableChange.value
);

// Truy xuất giá trị response.value và gán vào responseData
watch(response, () => {
  contentInfoComapny.value = response?.value?.data;
  content1.value = contentInfoComapny.value[0].content;
  selectedImage.value = contentInfoComapny.value[0].image;
  content2.value = contentInfoComapny.value[1].content;
  selectedImage1.value = contentInfoComapny.value[1].image;
});

// Hàm xử lí lấy nội dung từ tiny đã thay đổi lưu vào content1
const handleChangeContent1 = (e: Event) => {
  const target = e.target as MyInputElement;

  if (target) {
    content1.value = target.getContent();
  }
};

// Hàm xử lí lấy nội dung từ tiny đã thay đổi lưu vào content2
const handleChangeContent2 = (e: Event) => {
  const target = e.target as MyInputElement;

  if (target) content2.value = target.getContent();
};

// Hàm cập nhật các giá trị khi thay đổi trong tiny(editor)
const handleUpdateContent = () => {
  isEdit.value = false;
  contentInfoComapny.value[0].content = content1.value;
  contentInfoComapny.value[1].content = content2.value;

  const deps = ref([]);

  contentInfoComapny.value.forEach((item, idx) => {
    const object = {
      id: item.id,
      content: item.content,
      image: item.image
    };

    console.log(object);

    const formData = new FormData();
    formData.append('data', JSON.stringify(object));
    formData.append('content', idx === 0 ? content1.value : content2.value);
    if (imageSave1.value && idx === 0) formData.append('image', imageSave1.value as Blob);
    else formData.append('image', imageSave2.value as Blob);
    const { response } = useAxios<DataResponse>(
      'patch',
      '/introduce/company-info',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      },
      deps.value
    );
  });

  watch(response, () => {
    console.log(response.value);
  });
};

// Hàm xử lí click vào chọn ảnh 1
const handleChangeImage = () => {
  const inputElement = document.getElementById('input_file2');
  if (inputElement) {
    inputElement.click();
  }
};

// Hàm xử lí click vào chọn ảnh 2
const handleChangeImage1 = () => {
  const inputElement = document.getElementById('input_file1');
  if (inputElement) {
    inputElement.click();
  }
};

// Hàm chuyển đổi từ ArrayBuffer sang string
const arrayBufferToString = (buffer: ArrayBuffer) => {
  const uintArray = new Uint16Array(buffer);
  const charArray: string[] = [];
  for (let i = 0; i < uintArray.length; i++) {
    charArray.push(String.fromCharCode(uintArray[i]));
  }
  return charArray.join('');
};

// convert Base64 sang File
const base64ToBlob = (base64Data: string) => {
  const byteString = atob(base64Data.split(',')[1]);
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }
  return new Blob([ab], { type: 'image/png' });
};

// Hàm xử lí lấy ảnh 1 từ máy và lưu lại vào biến selectedImage
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

      // Tạo một đối tượng File từ dữ liệu base64
      if (selectedImage.value) {
        const fileData = base64ToBlob(selectedImage.value);
        const image = new File([fileData], 'image.png', { type: 'image/png' });
        imageSave1.value = image;
      }
    };

    reader.readAsDataURL(file);
  }
};

// Hàm xử lí lấy ảnh 2 từ máy và lưu lại vào biến selectedImage1
const handleFileInputChange1 = (event: Event) => {
  const target = event.target as HTMLInputElement;

  if (target.files) {
    const file = target.files[0];
    const reader = new FileReader();

    reader.onload = (e: ProgressEvent<FileReader>) => {
      const result = e.target?.result;
      if (result instanceof ArrayBuffer) {
        selectedImage1.value = arrayBufferToString(result);
      } else if (typeof result === 'string') {
        selectedImage1.value = result;
      }
    };

    if (selectedImage1.value) {
      const fileData = base64ToBlob(selectedImage1.value);
      const image = new File([fileData], 'image1.png', { type: 'image/png' });
      imageSave2.value = image;
    }

    reader.readAsDataURL(file);
  }
};
</script>
<template>
  <div :class="$style.about__infocompany">
    <p :class="$style['about__infocompany-title']">THÔNG TIN CÔNG TY</p>

    <div :class="$style['about__infocompany-content']">
      <div :class="$style['about__infocompany-content-item']">
        <p v-html="content1" v-if="!isEdit"></p>
        <div v-else>
          <editor
            allowedEvents="onChange"
            @change="handleChangeContent1"
            api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
            id="uuid1"
            :initial-value="content1"
            :value="content1"
            :init="{
              selector: 'textarea#uuid1',
              placeholder: 'Nhập phương châm',
              height: 370,
              menubar: false,
              plugins: [
                'advlist autolink lists link image charmap print preview anchor',
                'searchreplace visualblocks code fullscreen',
                'insertdatetime media table paste code help wordcount'
              ],
              toolbar:
                'undo redo | formatselect | fontsize bold italic backcolor forecolor | \
           alignleft aligncenter alignright alignjustify | \
           bullist numlist outdent indent | removeformat | help'
            }"
          />
        </div>

        <div :class="$style['about__infocompany-position']">
          <div :class="$style['about__infocompany-img']" v-if="selectedImage">
            <img :src="selectedImage" alt="" />
          </div>

          <div v-if="isEdit" :class="$style['about__infocompany-button-wrapper']">
            <button :class="$style['about__infocompany-button']" @click="handleChangeImage">
              <font-awesome-icon :icon="faRotate" :class="$style['about__infocompany-button-ic']" />
              <span>Đổi ảnh</span>
            </button>

            <input
              type="file"
              style="display: none"
              id="input_file2"
              accept="image/*"
              @change="handleFileInputChange"
            />
          </div>
        </div>
      </div>

      <div :class="$style['about__infocompany-content-item']">
        <div :class="$style['about__infocompany-position']">
          <div :class="$style['about__infocompany-img']" v-if="selectedImage1">
            <img :src="selectedImage1" alt="" />
          </div>

          <div v-if="isEdit" :class="$style['about__infocompany-button-wrapper']">
            <button :class="$style['about__infocompany-button']" @click="handleChangeImage1">
              <font-awesome-icon :icon="faRotate" :class="$style['about__infocompany-button-ic']" />
              <span>Đổi ảnh</span>
            </button>

            <input
              type="file"
              style="display: none"
              id="input_file1"
              accept="image/*"
              @change="handleFileInputChange1"
            />
          </div>
        </div>
        <p v-html="content2" v-if="!isEdit"></p>
        <div v-else>
          <editor
            id="uuid2"
            @change="handleChangeContent2"
            api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
            :initial-value="content2"
            :value="content2"
            :init="{
              selector: 'textarea#uuid2',
              placeholder: 'Nhập phương châm',
              height: 370,
              menubar: false,
              plugins: [
                'advlist autolink lists link image charmap print preview anchor',
                'searchreplace visualblocks code fullscreen',
                'insertdatetime media table paste code help wordcount'
              ],
              toolbar:
                'undo redo | formatselect | fontsize bold italic backcolor forecolor | \
           alignleft aligncenter alignright alignjustify | \
           bullist numlist outdent indent | removeformat | help'
            }"
          />
        </div>
      </div>
    </div>

    <button
      :class="$style['about__infocompany-button-first']"
      v-if="!isEdit"
      @click="isEdit = true"
    >
      <font-awesome-icon :icon="faPencil" :class="$style['about__infocompany-button-ic']" />
      <span>Chỉnh sửa</span>
    </button>

    <button :class="$style['about__infocompany-button-left']" v-if="isEdit" @click="isEdit = false">
      <font-awesome-icon :icon="faClose" :class="$style['about__infocompany-button-ic']" />
      <span>Hủy bỏ</span>
    </button>

    <button
      :class="$style['about__infocompany-button-right']"
      v-if="isEdit"
      @click="handleUpdateContent"
    >
      <font-awesome-icon :icon="faCheck" :class="$style['about__infocompany-button-ic']" />
      <span>Xác nhận</span>
    </button>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutInfoCompany.module.scss';
</style>

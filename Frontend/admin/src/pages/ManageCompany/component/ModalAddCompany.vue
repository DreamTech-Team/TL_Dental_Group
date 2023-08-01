<script setup lang="ts">
import { Ref, ref, watch, defineProps, PropType } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faCloudArrowUp, faRotate } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import ModalAddProduct from './ModalAddProduct.vue';
import styles from './ModalAddCompany.module.scss';
import base64ToBlob from '@/utils/base64ToBlob';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface ManageCompany {
  id: string;
  name: string;
  logo: string;
  description: string;
  highlight: number;
  slug: string;
  createAt: string;
  outstandingProductId: string;
}

const props = defineProps({
  changeAddedCompany: {
    type: Function as PropType<(newData: ManageCompany) => void>,
    required: true
  }
});
const emit = defineEmits(['close']);

const titleInput = ref('');
const contentInput = ref('');
const selectedImage: Ref<string | null> = ref(null);
const isOpen = ref(false);
const dataAdded = ref<ManageCompany>({
  id: '',
  name: '',
  logo: '',
  description: '',
  highlight: 0,
  slug: '',
  createAt: '',
  outstandingProductId: ''
});

// Các hàm update dữ liệu cho thẻ input
const updateTitle = (e: Event) => {
  const target = e.target as HTMLInputElement;
  titleInput.value = target.value;
};
const updateContent = (e: Event) => {
  const target = e.target as HTMLInputElement;
  contentInput.value = target.value;
};

// Hàm submit dữ liệu, đẩy dữ liệu lên database
const submitForm = () => {
  if (titleInput.value.length < 4 || contentInput.value.length < 4 || !selectedImage.value) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '50rem',
      padding: '0 2rem 2rem 2rem',
      customClass: {
        confirmButton: styles['confirm-button'],
        cancelButton: styles['cancel-button'],
        title: styles['title']
      }
    });
  } else {
    if (selectedImage.value) {
      // Tạo một đối tượng File từ dữ liệu base64
      const fileData = base64ToBlob.covertBase64ToBlob(selectedImage.value);
      const image = new File([fileData], 'image.png', { type: 'image/png' });

      const deps = ref([]);

      const object = {
        name: titleInput.value,
        description: contentInput.value,
        highlight: 0
      };

      const formData = new FormData();
      formData.append('logo', image as Blob);
      formData.append('data', JSON.stringify(object));
      const { response } = useAxios<DataResponse>(
        'post',
        '/company',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        },
        deps.value
      );

      watch(response, () => {
        if (response.value) {
          dataAdded.value = {
            id: response.value?.data?.id,
            name: response.value?.data?.name,
            logo: response.value?.data?.logo,
            description: response.value?.data?.description,
            highlight: response.value?.data?.highlight,
            slug: response.value?.data?.slug,
            createAt: response.value?.data?.createAt,
            outstandingProductId: response.value?.data?.outstandingProductId
          };
          props.changeAddedCompany(dataAdded.value);
        }
      });
    }
    Swal.fire({
      title: 'Thêm thành công',
      icon: 'success',
      confirmButtonText: 'Hoàn tất',
      width: '50rem',
      padding: '0 2rem 2rem 2rem',
      customClass: {
        confirmButton: styles['confirm-button'],
        cancelButton: styles['cancel-button'],
        title: styles['title']
      }
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.close();
        emit('close');
      }
    });
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

// Hàm lấy ảnh từ máy và lưu vào biến selectedImage
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
    };

    reader.readAsDataURL(file);
  }
};

// Hàm kích hoạt thẻ input type file với id là input_file_modal
const handleChangeImage = () => {
  const inputElement = document.getElementById('input_file_modal');
  if (inputElement) {
    inputElement.click();
  }
};

// Hàm kéo thả ảnh vào vùng làm việc(drag and drop)
const addFile = (e: DragEvent) => {
  e.preventDefault();

  if (e.dataTransfer) {
    const { files } = e.dataTransfer;
    if (files.length > 0) {
      const imageFile = files[0];
      selectedImage.value = URL.createObjectURL(imageFile);
    }
  }
};
</script>

<template>
  <div :class="$style.mncompany__overlay" v-if="!isOpen">
    <div :class="$style.mncompany__modal">
      <div :class="$style.mncompany__modal__heading">
        THÊM CÔNG TY
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['mncompany__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.mncompany__modal__body">
        <h4>Tên công ty</h4>
        <input
          type="text"
          placeholder="Nhập tên công ty..."
          :value="titleInput"
          @input="updateTitle"
        />

        <h4>Mô tả công ty</h4>
        <textarea
          type="text"
          placeholder="Nhập mô tả..."
          :value="contentInput"
          @input="updateContent"
        />

        <h4>Logo</h4>
        <div
          :class="$style['wrapper']"
          v-if="!selectedImage"
          @click="handleChangeImage"
          v-cloak
          @drop.prevent="addFile"
          @dragover.prevent
        >
          <font-awesome-icon :icon="faCloudArrowUp" :class="$style['upload-ic']" />

          <span>Thả ảnh hoặc click để tải ảnh lên</span>

          <input
            type="file"
            style="display: none"
            id="input_file_modal"
            accept="image/*"
            @change="handleFileInputChange"
          />
        </div>
        <div :class="$style['wrapper1']" v-else>
          <div>
            <img :src="selectedImage" />
          </div>

          <div :class="$style['about__mottomodal-button-wrapper']">
            <button :class="$style['about__mottomodal-button']" @click="handleChangeImage">
              <font-awesome-icon :icon="faRotate" :class="$style['about__mottomodal-button-ic']" />
              <span>Đổi ảnh</span>
            </button>

            <input
              type="file"
              style="display: none"
              id="input_file_modal"
              accept="image/*"
              @change="handleFileInputChange"
            />
          </div>
        </div>

        <!-- <h4>Sản phẩm nổi bật</h4>
        <div :class="$style['category']">
          <input
            type="text"
            placeholder="Đang trống"
            :value="productInput"
            @change="updateProduct"
          />

          <button @click="isOpen = true">
            <font-awesome-icon :icon="faPencil" :class="$style['category-icon']" />
          </button>
        </div> -->

        <div :class="$style['modal__buttons']">
          <button @click="$emit('close')">Hủy</button>
          <button @click="submitForm">Thêm công ty</button>
        </div>
      </div>
    </div>
  </div>

  <modal-add-product v-else @close="isOpen = false" />
</template>

<style module scoped lang="scss">
@import './ModalAddCompany.module.scss';
</style>

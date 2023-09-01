<script setup lang="ts">
import { type Ref, ref, computed } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faCloudArrowUp } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import styles from './ModalAddProduct.module.scss';

interface Products {
  id: string;
  name: string;
  description: string;
  mainImg: string;
  fkCategory: {
    companyId: {
      id: string;
    };
  };
}
const context = defineProps({
  product: {
    type: Object,
    required: true
  },
  products: {
    type: Array,
    required: true
  },
  idCompany: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['close', 'results']);

// Lấy dữ liệu từ description ra và dưới dạng <p>haha</p> thì sẽ lấy được haha
const formatDescription = (str: string): string | null => {
  try {
    // Parse the HTML string using DOMParser
    const parser = new DOMParser();
    const doc = parser.parseFromString(str, 'text/html');

    // Get the text content from the parsed document
    const pElement = doc.querySelector('p');
    const textContent = pElement ? pElement.textContent : null;

    return textContent;
  } catch (error) {
    console.error('Error parsing HTML:', error);
    return null;
  }
};

console.log(context.product.description);

const contentInput = ref(formatDescription(context.product.description));
const selectedImage: Ref<string | null> = ref(context.product.mainImg);
const dataSearchTerm = ref(context.product.name !== undefined ? context.product.name : '');
const listData = ref<Products[]>(context.products as Products[]);
const isOpen = ref(false);
const idProduct = ref('');

// Hàm search
const filteredItems = computed(() => {
  const searchTerm = dataSearchTerm.value.toLowerCase().trim();
  if (!searchTerm) {
    return listData.value; // Nếu không có từ khóa tìm kiếm, trả về toàn bộ danh sách
  }
  return listData.value.filter((item) => item.name.toLowerCase().includes(searchTerm));
});

// Các hàm update dữ liệu cho thẻ input
const updateTitle = (e: Event) => {
  isOpen.value = true;

  const target = e.target as HTMLInputElement;
  dataSearchTerm.value = target.value;
};
const updateContent = (e: Event) => {
  const target = e.target as HTMLInputElement;
  contentInput.value = target.value;
};

// Hàm submit dữ liệu, đẩy dữ liệu lên database
const submitForm = () => {
  if (contentInput.value !== null) {
    if (dataSearchTerm.value.length < 4 || contentInput.value.length < 4 || !selectedImage.value) {
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
      emit(
        'results',
        idProduct.value,
        dataSearchTerm.value,
        contentInput.value,
        selectedImage.value
      );

      Swal.fire({
        title: 'Cập nhật sản phẩm nổi bật thành công',
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

// Hàm chọn sản phẩm khác trong cùng công ty
const handleOption = (item: Products, id: string) => {
  isOpen.value = false;
  dataSearchTerm.value = item.name;

  listData.value.forEach((value) => {
    if (value.id === id) {
      idProduct.value = id;
      contentInput.value = formatDescription(value.description);
      selectedImage.value = value.mainImg;
    }
  });
};
</script>

<template>
  <div :class="$style.motto__overlay">
    <div :class="$style.motto__modal">
      <div :class="$style.motto__modal__heading">
        CẬP NHẬT SẢN PHẨM NỔI BẬT
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['motto__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.motto__modal__body">
        <h4>Tên sản phẩm</h4>
        <div>
          <input
            type="text"
            v-model="dataSearchTerm"
            placeholder="Nhập tên sản phẩm..."
            @input="updateTitle"
            @focus="isOpen = true"
            @blur="isOpen = false"
          />

          <ul v-if="isOpen">
            <li
              v-for="item in filteredItems"
              :key="item.id"
              @mousedown="handleOption(item, item.id)"
            >
              {{ item.name }}
            </li>
          </ul>
        </div>

        <h4>Mô tả</h4>
        <input
          type="text"
          placeholder="Mô tả được hiển thị khi chọn sản phẩm"
          :value="contentInput"
          @change="updateContent"
          readonly
          :style="{ cursor: 'auto', border: 'none' }"
        />

        <h4>Ảnh sản phẩm</h4>
        <div
          :class="$style['wrapper']"
          v-if="!selectedImage"
          @click="handleChangeImage"
          v-cloak
          @drop.prevent="addFile"
          @dragover.prevent
        >
          <font-awesome-icon :icon="faCloudArrowUp" :class="$style['upload-ic']" />

          <span>Chọn sản phẩm hình ảnh sẽ hiện lên</span>
        </div>
        <div v-else :class="$style['wrapper1']">
          <img :src="selectedImage" />
        </div>

        <div :class="$style['modal__buttons']">
          <button @click="$emit('close')">Hủy</button>
          <button @click="submitForm">Cập nhật</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalAddProduct.module.scss';
</style>

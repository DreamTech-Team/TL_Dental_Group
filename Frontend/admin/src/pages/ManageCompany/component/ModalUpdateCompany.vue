<script setup lang="ts">
import { Ref, ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faRotate, faPencil } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';

const context = defineProps({
  name: {
    type: String,
    required: true
  },
  description: {
    type: String,
    required: true
  },
  logo: {
    type: String,
    required: true
  },
  nameProduct: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['close']);

const nameCompanyInput = ref(context.name);
const descriptionInput = ref(context.description);
const imgLogo = ref(context.logo);
const productInput = ref(context.nameProduct);
const selectedlogo: Ref<string | null> = ref(context.logo);
const isOpen = ref(false);

//Validate form
const updateTitle = (e: Event) => {
  const target = e.target as HTMLInputElement;
  nameCompanyInput.value = target.value;
};
const updateContent = (e: Event) => {
  const target = e.target as HTMLInputElement;
  descriptionInput.value = target.value;
};
const updateProduct = (e: Event) => {
  const target = e.target as HTMLInputElement;
  productInput.value = target.value;
};

const submitForm = () => {
  if (
    nameCompanyInput.value.length < 4 ||
    descriptionInput.value.length < 4 ||
    !selectedlogo.value
  ) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '30rem'
    });
  } else {
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

const handleFileInputChange = (event: Event) => {
  const target = event.target as HTMLInputElement;

  if (target.files) {
    const file = target.files[0];
    const reader = new FileReader();

    reader.onload = (e: ProgressEvent<FileReader>) => {
      const result = e.target?.result;
      if (result instanceof ArrayBuffer) {
        selectedlogo.value = arrayBufferToString(result);
      } else if (typeof result === 'string') {
        selectedlogo.value = result;
      }
    };

    reader.readAsDataURL(file);
  }
};

const handleChangelogo = () => {
  const inputElement = document.getElementById('input_file_modalupdate');
  if (inputElement) {
    inputElement.click();
  }
};
</script>

<template>
  <div :class="$style.mncompany__overlay">
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
          :value="nameCompanyInput"
          @input="updateTitle"
        />

        <h4>Mô tả công ty</h4>
        <textarea
          type="text"
          placeholder="Nhập mô tả..."
          :value="descriptionInput"
          @input="updateContent"
        />

        <h4>Logo</h4>
        <div :class="$style['wrapper1']">
          <div v-if="selectedlogo">
            <img :src="selectedlogo" />
          </div>
          <div v-else>
            <img :src="imgLogo" />
          </div>

          <div :class="$style['about__mottomodal-button-wrapper']">
            <button :class="$style['about__mottomodal-button']" @click="handleChangelogo">
              <font-awesome-icon :icon="faRotate" :class="$style['about__mottomodal-button-ic']" />
              <span>Đổi ảnh</span>
            </button>

            <input
              type="file"
              style="display: none"
              id="input_file_modal"
              accept="logo/*"
              @change="handleFileInputChange"
            />
          </div>
        </div>

        <h4>Sản phẩm nổi bật</h4>
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
        </div>

        <div :class="$style['modal__buttons']">
          <button @click="$emit('close')">Hủy</button>
          <button @click="submitForm">Thêm công ty</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalUpdateCompany.module.scss';
</style>
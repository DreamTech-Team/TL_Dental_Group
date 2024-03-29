<!-- eslint-disable @typescript-eslint/no-explicit-any -->
<script setup lang="ts">
import no_icon from '@/assets/imgs/Policy/icon/ic_noimage.svg';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronLeft, faUpload } from '@fortawesome/free-solid-svg-icons';
import Editor from '@tinymce/tinymce-vue';
import { ref, watch, type PropType } from 'vue';
import type { DataResponse } from '@/hooks/useAxios';
import useAxios from '@/hooks/useAxios';
import styles from './ManagePolicyEdit.module.scss';
import Swal from 'sweetalert2';
import LoadingComponentVue from '@/components/LoadingComponent/LoadingComponent.vue';

interface PolicyDetail {
  id: string;
  name: string;
  symbol: string;
  slug: string;
  detail: string;
  createAt: string;
}

const props = defineProps({
  content: { type: Object as PropType<PolicyDetail>, required: false },
  editType: { type: Number, required: true }
});

const emit = defineEmits(['close', 'handle-update-policy']);

const selectedSymbol = ref(props.content?.symbol || no_icon);
const namePolicy = ref(props.content?.name || '');
const contentPolicyDetail = ref(props.content?.detail || '');
const isLoading = ref({ update: false, add: false });

const paramAxios = ref();

const removeExtraSpaces = (inputString: string) => {
  return inputString.replace(/\s+/g, ' ').trim();
};

const base64ToBlob = (base64Data: string) => {
  const byteString = atob(base64Data.split(',')[1]);
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }
  return new Blob([ab], { type: 'image/png' });
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

// Hàm xử lí lấy ảnh từ máy lên
const handleFileInputChange = (event: Event) => {
  const target = event.target as HTMLInputElement;

  if (target.files) {
    const file = target.files[0];
    const reader = new FileReader();

    reader.onload = (e: ProgressEvent<FileReader>) => {
      const result = e.target?.result;
      if (result instanceof ArrayBuffer) {
        selectedSymbol.value = arrayBufferToString(result);
      } else if (typeof result === 'string') {
        selectedSymbol.value = result;
      }
    };

    reader.readAsDataURL(file);

    target.value = '';
  }
};

const updateDetailContent = (content: any) => {
  contentPolicyDetail.value = content.level.content;

  // console.log(content, contentPolicyDetail.value);
};

const handleAddPolicy = () => {
  if (
    namePolicy.value === '' ||
    contentPolicyDetail.value === '' ||
    selectedSymbol.value === no_icon
  ) {
    Swal.fire({
      title: 'Thông tin chính sách không hợp lệ!',
      icon: 'error',
      customClass: {
        popup: styles['container-popup'],
        confirmButton: styles['confirm-button'],
        denyButton: styles['deny-button']
      }
    });
  } else {
    const dataPolicy = {
      name: removeExtraSpaces(namePolicy.value),
      detail: contentPolicyDetail.value
    };
    const symbolPolicy = new File([base64ToBlob(selectedSymbol.value)], 'image.png', {
      type: 'image/png'
    });

    const formData = new FormData();
    formData.append('symbol', symbolPolicy as Blob);
    formData.append('data', JSON.stringify(dataPolicy));

    const postNewPolicy = useAxios<DataResponse>(
      'post',
      '/policy',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      },
      paramAxios.value
    );

    watch(postNewPolicy.response, (value) => {
      // console.log(value?.data);
      emit('handle-update-policy', value?.data);
      emit('close');
      Swal.fire({
        title: 'Thêm chính sách thành công!',
        icon: 'success',
        customClass: {
          popup: styles['container-popup'],
          confirmButton: styles['confirm-button'],
          denyButton: styles['deny-button']
        }
      });
    });

    watch(postNewPolicy.error, () => {
      Swal.fire({
        title: 'Tên chính sách đã tồn tại!',
        icon: 'error',
        customClass: {
          popup: styles['container-popup'],
          confirmButton: styles['confirm-button'],
          denyButton: styles['deny-button']
        }
      });
      // console.log(value);
    });

    watch(postNewPolicy.isLoading, (value) => {
      isLoading.value.add = value;
    });
  }
};

const handleUpdatePolicy = () => {
  if (
    namePolicy.value === '' ||
    contentPolicyDetail.value === '' ||
    selectedSymbol.value === no_icon
  ) {
    Swal.fire({
      title: 'Thông tin chính sách không hợp lệ!',
      icon: 'error',
      customClass: {
        popup: styles['container-popup'],
        confirmButton: styles['confirm-button'],
        denyButton: styles['deny-button']
      }
    });
  } else {
    const dataPolicy = {
      id: props.content?.id,
      name: namePolicy.value,
      detail: contentPolicyDetail.value
    };
    const symbolPolicy =
      selectedSymbol.value !== props.content?.symbol
        ? new File([base64ToBlob(selectedSymbol.value)], 'image.png', {
            type: 'image/png'
          })
        : props.content?.symbol;

    const formData = new FormData();
    formData.append('symbol', symbolPolicy as Blob);
    formData.append('data', JSON.stringify(dataPolicy));

    const postNewPolicy = useAxios<DataResponse>(
      'patch',
      `/policy/${props.content?.id}`,
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      },
      paramAxios.value
    );

    watch(postNewPolicy.response, (value) => {
      // console.log(value?.data);
      emit('handle-update-policy', value?.data, props.editType);
      emit('close');
      Swal.fire({
        title: 'Cập nhật chính sách thành công!',
        icon: 'success',
        customClass: {
          popup: styles['container-popup'],
          confirmButton: styles['confirm-button'],
          denyButton: styles['deny-button']
        }
      });
    });

    watch(postNewPolicy.error, () => {
      // console.log(value, dataPolicy);
      Swal.fire({
        title: 'Tên chính sách đã tồn tại!',
        icon: 'error',
        customClass: {
          popup: styles['container-popup'],
          confirmButton: styles['confirm-button'],
          denyButton: styles['deny-button']
        }
      });
    });

    watch(postNewPolicy.isLoading, (value) => {
      isLoading.value.update = value;
    });
  }
};

const handleModalCancel = () => {
  Swal.fire({
    title: 'Nội dung của bạn chưa được lưu!',
    icon: 'warning',
    showDenyButton: true,
    confirmButtonText: 'Trở lại',
    denyButtonText: 'Thoát',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    if (result.isDenied) {
      emit('close');
    }
  });
};
</script>
<template>
  <LoadingComponentVue v-if="isLoading.add || isLoading.update" />
  <div :class="$style.container" v-else>
    <div :class="$style.container__back" @click="handleModalCancel">
      <font-awesome-icon :icon="faChevronLeft" />
      <p>Trở về</p>
    </div>

    <div :class="$style.container__heading">
      <p v-if="editType === 1">Thêm chính sách</p>
      <p v-else>Cập nhật chính sách</p>
    </div>
    <div :class="$style.container__body">
      <div :class="$style['container__body-icon']">
        <h3 :class="$style['title-policy']">Biểu tượng</h3>
        <div :class="$style['container__body-icon-block']">
          <div :class="$style['container__body-icon-block-edit']">
            <img :src="selectedSymbol" alt="none" />
          </div>
          <input type="file" id="upload-btn" accept="image/*" @change="handleFileInputChange" />
          <label for="upload-btn">
            <div :class="$style['btn-title']">
              <div style="text-transform: uppercase">Chọn ảnh từ thiết bị</div>
            </div>
            <div :class="$style['btn-icon']">
              <font-awesome-icon :class="$style['btn-icon-item']" :icon="faUpload" />
              <p>Upload</p>
            </div>
          </label>
        </div>
      </div>
      <div :class="$style['container__body-title']">
        <h3 :class="$style['title-policy']">Tên chính sách</h3>
        <input type="text" v-model="namePolicy" />
      </div>
      <div :class="$style['container__body-content']">
        <h3 :class="$style['title-policy']">Nội dung chính sách</h3>
        <editor
          allowedEvents="onChange"
          :onChange="updateDetailContent"
          api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
          :initial-value="contentPolicyDetail"
          :init="{
            selector: 'textarea',
            placeholder: 'Nhập tiêu đề',
            height: 350,
            menubar: false,
            plugins: [
              'advlist',
              'autolink',
              'lists',
              'link',
              'image',
              'charmap',
              'preview',
              'anchor',
              'searchreplace',
              'visualblocks',
              'fullscreen',
              'insertdatetime',
              'media',
              'table',
              'help',
              'wordcount'
            ],
            toolbar:
              'undo redo | casechange blocks | formatselect | bold italic backcolor forecolor | \
           alignleft aligncenter alignright alignjustify | \
           bullist numlst outdent indent | removeformat | code table help | fullscreen'
          }"
        />
      </div>
    </div>
    <div :class="$style['container__btn']">
      <div :class="$style['container__btn-update']">
        <div
          :class="[$style['container__btn-update-submit'], $style['container__btn-update-cancel']]"
          @click="handleModalCancel"
        >
          Hủy
        </div>
        <div
          v-if="editType === 1"
          :class="[$style['container__btn-update-submit'], $style['container__btn-update-update']]"
          @click="handleAddPolicy"
        >
          Thêm
        </div>
        <div
          v-else
          :class="[$style['container__btn-update-submit'], $style['container__btn-update-update']]"
          @click="handleUpdatePolicy"
        >
          Cập nhật
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ManagePolicyEdit.module.scss';
</style>

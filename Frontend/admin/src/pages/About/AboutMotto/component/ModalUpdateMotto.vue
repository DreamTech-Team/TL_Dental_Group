<script setup lang="ts">
import { type PropType, type Ref, ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faRotate } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import styles from './ModalUpdateMotto.module.scss';
import base64ToBlob from '@/utils/base64ToBlob';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const _MAX_WORD_TITLE = 70;
const _MAX_WORD_CONTENT = 250;

interface AboutMotto {
  id: string;
  title: string;
  content: string;
  image: string;
  type: string;
}

const context = defineProps({
  item: {
    type: Object,
    required: true
  },
  change: {
    type: Function as PropType<(newData: AboutMotto, isLoadingItem: boolean) => void>,
    required: true
  }
});

const emit = defineEmits(['close']);

const titleInput = ref(context.item.title);
const contentInput = ref(context.item.content);
const selectedImage: Ref<string | null> = ref(context.item.image);
const isChange = ref(false);
const countWordTitle = ref(_MAX_WORD_TITLE - context.item.title.length);
const countWordContent = ref(_MAX_WORD_CONTENT - context.item.content.length);

// Các hàm update dữ liệu cho thẻ input
const updateTitle = (e: Event) => {
  const target = e.target as HTMLInputElement;
  countWordTitle.value = _MAX_WORD_TITLE - target.value.length;

  titleInput.value = target.value;
};

const updateContent = (e: Event) => {
  const target = e.target as HTMLInputElement;
  countWordContent.value = _MAX_WORD_CONTENT - target.value.length;

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
    if (isChange.value && selectedImage.value) {
      // Tạo một đối tượng File từ dữ liệu base64
      const fileData = base64ToBlob.covertBase64ToBlob(selectedImage.value);
      const image = new File([fileData], 'image.png', { type: 'image/png' });

      const deps = ref([]);

      const object = {
        id: context.item.id,
        title: titleInput.value,
        content: contentInput.value,
        image: context.item.image
      };

      const formData = new FormData();
      formData.append('data', JSON.stringify(object));
      formData.append('image', image as Blob);
      const { response, isLoading } = useAxios<DataResponse>(
        'patch',
        '/introduce/section1',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        },
        deps.value
      );
      watch(isLoading, () => {
        context.change({} as AboutMotto, isLoading.value);
      });

      watch(response, () => {
        if (response) {
          if (response.value?.status === 'ok') {
            context.change(response.value?.data, isLoading.value);
          }
        }
      });
    } else {
      const deps = ref([]);

      const object = {
        id: context.item.id,
        title: titleInput.value,
        content: contentInput.value,
        image: context.item.image
      };

      const formData = new FormData();
      formData.append('data', JSON.stringify(object));
      formData.append('image', context.item.image);
      const { response, isLoading } = useAxios<DataResponse>(
        'patch',
        '/introduce/section1',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        },
        deps.value
      );
      watch(isLoading, () => {
        context.change({} as AboutMotto, isLoading.value);
      });

      watch(response, () => {
        if (response) {
          if (response.value?.status === 'ok') {
            context.change(response.value?.data, isLoading.value);
          }
        }
      });
    }
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

      isChange.value = true;
    };

    reader.readAsDataURL(file);
  }
};

// Hàm kích hoạt thẻ input type file với id là input_file_modal
const handleChangeImage = () => {
  const inputElement = document.getElementById('input_file_modalupdate');
  if (inputElement) {
    inputElement.click();
  }
};
</script>

<template>
  <div :class="$style.motto__overlay">
    <div :class="$style.motto__modal">
      <div :class="$style.motto__modal__heading">
        CẬP NHẬT PHƯƠNG CHÂM
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['motto__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.motto__modal__body">
        <h4>
          Tiêu đề chính<span>
            (Bạn có thể viết <strong>{{ countWordTitle }}</strong> kí tự)</span
          >
        </h4>
        <input
          type="text"
          placeholder="Nhập tiêu đề..."
          :maxlength="_MAX_WORD_TITLE"
          :value="titleInput"
          @input="updateTitle"
        />

        <h4>
          Mô tả<span>
            (Bạn có thể viết <strong>{{ countWordContent }}</strong> kí tự)</span
          >
        </h4>
        <input
          type="text"
          placeholder="Nhập mô tả..."
          :maxlength="_MAX_WORD_CONTENT"
          :value="contentInput"
          @input="updateContent"
        />

        <h4>Ảnh minh họa</h4>
        <div :class="$style['wrapper']">
          <div v-if="selectedImage">
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
              id="input_file_modalupdate"
              accept="image/*"
              @change="handleFileInputChange"
            />
          </div>
        </div>

        <div :class="$style['modal__buttons']">
          <button @click="$emit('close')">Hủy</button>
          <button @click="submitForm">Lưu thay đổi</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalUpdateMotto.module.scss';
</style>

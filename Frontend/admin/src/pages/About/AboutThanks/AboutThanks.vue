<script setup lang="ts">
import { ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faComputerMouse, faFloppyDisk } from '@fortawesome/free-solid-svg-icons';
import Editor from '@tinymce/tinymce-vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import Swal from 'sweetalert2';
import styles from './AboutThanks.module.scss';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface MyInputElement extends HTMLInputElement {
  getContent(): string;
}

interface AboutLetter {
  content: string;
  id: string;
}

const variableChange = ref([]);
const contentLetter = ref<AboutLetter>({ content: '', id: '' });
const isEdit = ref(false);
const content = ref('');
const isLoadingLetter = ref(false);

// Gọi hàm useAxios để lấy response, error, và isLoading
const { response } = useAxios<DataResponse>(
  'get',
  '/introduce/letter',
  {},
  {},
  variableChange.value
);

// Truy xuất giá trị response.value và gán vào responseData
watch(response, () => {
  contentLetter.value = response?.value?.data;
});

// Hàm xử lí lấy nội dung từ tiny đã thay đổi lưu vào content
const handleChangeContent = (e: Event) => {
  const target = e.target as MyInputElement;

  if (target) {
    content.value = target.getContent();
  }
};

// Hàm cập nhật các giá trị khi thay đổi trong tiny(editor)
const handleUpdateContent = () => {
  Swal.fire({
    title: 'Bạn đã thay đổi dữ liệu xong?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    cancelButtonText: 'Hủy',
    confirmButtonText: 'Lưu',
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
      isEdit.value = false;

      const deps = ref([]);

      const object = {
        id: contentLetter.value.id,
        content: contentLetter.value.content
      };

      const { response, isLoading } = useAxios<DataResponse>(
        'patch',
        '/introduce/letter',
        object,
        {},
        deps.value
      );
      isLoadingLetter.value = isLoading.value;
      watch(response, () => {
        if (response.value?.status === 'ok') {
          isLoadingLetter.value = isLoading.value;

          Swal.fire({
            title: 'Cập nhật thành công',
            icon: 'success',
            confirmButtonText: 'Hoàn tất',
            width: '50rem',
            padding: '0 2rem 2rem 2rem',
            timer: 2000,
            customClass: {
              confirmButton: styles['confirm-button'],
              cancelButton: styles['cancel-button'],
              title: styles['title']
            }
          }).then((result) => {
            if (result.isConfirmed) {
              contentLetter.value.content = content.value;

              Swal.close();
            }
          });
        } else {
          isLoadingLetter.value = isLoading.value;

          Swal.fire({
            title: 'Cập nhật thất bại',
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
            }
          });
        }
      });
    }
  });
};
</script>
<template>
  <div :class="$style.about__thanks">
    <div :class="$style['about__thanks-letter']" v-if="!isLoadingLetter">
      <h1>LỜI CẢM ƠN</h1>

      <p v-if="!isEdit" v-html="contentLetter.content"></p>
      <div :class="$style['about__thanks-letter-editor']" v-else>
        <editor
          allowedEvents="onChange"
          @change="handleChangeContent"
          api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
          :initial-value="contentLetter.content"
          :value="contentLetter.content"
          :init="{
            selector: 'textarea',
            placeholder: 'Nhập phương châm',
            height: 500,
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

        <button @click="handleUpdateContent">
          <font-awesome-icon :icon="faFloppyDisk" :class="$style['about__infocompany-button-ic']" />
          <span>Lưu thay đổi</span>
        </button>
      </div>

      <div :class="$style['about__thanks-letter-bg']" @click="isEdit = true" v-if="!isEdit">
        <font-awesome-icon :icon="faComputerMouse" :class="$style['about__thanks-letter-bg-ic']" />
        <span>Click chuột để chỉnh sửa nội dung</span>
      </div>
      <div v-else></div>
    </div>
    <div v-else style="width: 100%; height: 100%">
      <loading-component />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutThanks.module.scss';
</style>

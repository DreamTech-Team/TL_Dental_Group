<script setup lang="ts">
import { ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import Editor from '@tinymce/tinymce-vue';
import Swal from 'sweetalert2';

import useAxios, { type DataResponse } from '@/hooks/useAxios';

const context = defineProps({
  uuid: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  tags: {
    type: String,
    required: true
  },
  image: {
    type: String,
    required: true
  }
});

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', data: { title: string; context: string; image: string }): void;
}>();

//Validate form
interface TextAreaValue {
  level: {
    content: string;
  };
}

const valueInput = ref<string>(context.title);

const titleInput = ref<TextAreaValue>({
  level: {
    content: context.title
  }
});

const updateTitle = (content: TextAreaValue) => {
  titleInput.value.level.content = content.level.content;
  valueInput.value = content.level.content;
};

//TinyMCE Content
const valueTxtArea = ref<string>(context.tags);

const textareaInput = ref<TextAreaValue>({
  level: {
    content: context.tags
  }
});

const updateTags = (content: TextAreaValue) => {
  textareaInput.value.level.content = content.level.content;
  valueTxtArea.value = content.level.content;
};

const submitForm = () => {
  if (textareaInput.value.level.content.length < 35 || titleInput.value.level.content.length < 30) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '30rem'
    });
  } else {
    const deps = ref([]);

    const object = {
      id: context.uuid,
      title: titleInput.value.level.content,
      content: textareaInput.value.level.content,
      image: context.image
    };

    const formData = new FormData();
    formData.append('data', JSON.stringify(object));
    const { response } = useAxios<DataResponse>(
      'patch',
      '/home/section3',
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
            Swal.close();
            emits('update-content', {
              title: titleInput.value.level.content,
              context: textareaInput.value.level.content,
              image: context.image
            });
            emits('close');
          }
        });
      }
    });
  }
};
</script>

<template>
  <div :class="$style.intro__overlay">
    <div :class="$style.intro__modal">
      <div :class="$style.intro__modal__heading">
        CẬP NHẬT LỜI NGỎ
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['intro__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.intro__modal__body">
        <h4>Tiêu đề chính</h4>
        <editor
          id="uuid1"
          allowedEvents="onChange"
          :onchange="updateTitle"
          api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
          :initial-value="titleInput.level.content"
          :value="titleInput.level.content"
          :init="{
            selector: 'textarea#uuid1',
            placeholder: 'Nhập tiêu đề',
            height: 140,
            menubar: false,
            plugins: [
              'advlist autolink lists link image charmap print preview anchor',
              'searchreplace visualblocks code fullscreen',
              'insertdatetime media table paste code help wordcount'
            ],
            toolbar:
              'undo redo | formatselect | bold italic backcolor forecolor | \
           alignleft aligncenter alignright alignjustify | \
           bullist numlist outdent indent | removeformat '
          }"
        />
        <h4>Nội dung</h4>
        <div class="editor">
          <editor
            id="uuid2"
            allowedEvents="onChange"
            :onchange="updateTags"
            api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
            :initial-value="textareaInput.level.content"
            :value="textareaInput.level.content"
            :init="{
              selector: 'textarea#uuid2',
              placeholder: 'Nhập nội dung',
              height: 180,
              menubar: false,
              plugins: [
                'advlist autolink lists link image charmap print preview anchor',
                'searchreplace visualblocks code fullscreen',
                'insertdatetime media table paste code help wordcount'
              ],
              toolbar:
                'undo redo | formatselect | bold italic backcolor forecolor | \
           alignleft aligncenter alignright alignjustify | \
           bullist numlist outdent indent | removeformat '
            }"
          />
        </div>
        <div :class="$style['modal__buttons']">
          <button>Hủy</button>
          <button @click="submitForm">Cập nhật</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalIntro.module.scss';
</style>

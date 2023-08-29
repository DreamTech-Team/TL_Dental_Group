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
const titleInput = ref(context.title);
const updateTitle = (e: Event) => {
  const target = e.target as HTMLInputElement;
  titleInput.value = target.value;
};

//Handle TinyMCE
const valueTxtArea = ref<string>(context.tags);
interface TextAreaValue {
  level: {
    content: string;
  };
}
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
  if (textareaInput.value.level.content.length < 35 || titleInput.value.length < 4) {
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
      title: titleInput.value,
      content: textareaInput.value.level.content,
      image: context.image
    };

    const formData = new FormData();
    formData.append('data', JSON.stringify(object));
    const { response } = useAxios<DataResponse>(
      'patch',
      '/home/section2',
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
              title: titleInput.value,
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
        <h4>Tiêu đề chính</h4>
        <input type="text" placeholder="Tên Phương Châm" :value="titleInput" @input="updateTitle" />
        <h4>Nội dung</h4>
        <editor
          allowedEvents="onChange"
          :onchange="updateTags"
          api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
          :initial-value="textareaInput.level.content"
          :value="textareaInput.level.content"
          :init="{
            selector: 'textarea',
            placeholder: 'Nhập phương châm',
            height: 350,
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
        <div :class="$style['modal__buttons']">
          <button @click="$emit('close')">Hủy</button>
          <button @click="submitForm">Cập nhật</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalMotto.module.scss';
</style>

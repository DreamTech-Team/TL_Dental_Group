<!-- eslint-disable @typescript-eslint/no-explicit-any -->
<script setup lang="ts">
import { ref } from 'vue';
import Swal from 'sweetalert2';
import styles from './ModalUpdateContent.module.scss';
import Editor from '@tinymce/tinymce-vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faPlus, faMinus } from '@fortawesome/free-solid-svg-icons';

const props = defineProps({
  contentEdit: {
    type: Object,
    required: true
  },
  handleUpdated: {
    type: Function,
    required: true
  },
  limitContent: { type: Number, required: true },
  hiddenCustomize: { type: Boolean, required: true }
});

const edited = ref(props.contentEdit);
const emit = defineEmits(['isFocus', 'close']);
const hiddenMore = ref(props.limitContent > props.contentEdit.length || props.limitContent === -1);

const updateTitle = (e: Event, index: number) => {
  const target = e.target as HTMLInputElement;
  edited.value[index].title = target.value;
  // console.log(hiddenMore.value, edited.value.length === 0 && !hiddenMore.value);
};

const handleDeleteContent = (index: number) => {
  Swal.fire({
    title: 'Bạn có chắc chắn muốn xóa không?',
    icon: 'question',
    showDenyButton: true,
    denyButtonText: 'Xóa',
    confirmButtonText: 'Hủy',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    if (result.isDenied) {
      edited.value.splice(index, 1);
      hiddenMore.value = props.limitContent > edited.value.length || props.limitContent === -1;
      // console.log(hiddenMore.value, props.limitContent, edited.value.length);
    }
  });
};

const handleAddContent = (index: number) => {
  edited.value.splice(index + 1, 0, { title: '', content: '' });
  hiddenMore.value = props.limitContent > edited.value.length || props.limitContent === -1;
};

const handleChangeContent = (e: any, index: number) => {
  const target = e.level.content;
  edited.value[index].content = target;
  // console.log(target);
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

const handleModalUpdate = () => {
  Swal.fire({
    title: 'Bạn có chắc chắn muốn cập nhật nội dung không?',
    icon: 'question',
    showDenyButton: true,
    confirmButtonText: 'Cập nhật',
    denyButtonText: 'Hủy',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      props.handleUpdated(edited.value);
      emit('close');
      Swal.fire({
        title: 'Cập nhật nội dung thành công!',
        icon: 'success',
        customClass: {
          popup: styles['container-popup'],
          confirmButton: styles['confirm-button'],
          denyButton: styles['deny-button']
        }
      });
    } else if (result.isDenied) {
      Swal.fire({
        title: 'Nội dung không được cập nhật',
        icon: 'error',
        customClass: {
          popup: styles['container-popup'],
          confirmButton: styles['confirm-button'],
          denyButton: styles['deny-button']
        }
      });
    }
  });
};
</script>
<template>
  <div :class="$style.container">
    <div :class="$style.container__modal">
      <div :class="$style['container__modal-heading']">
        <div class=""></div>
        <div :class="$style['container__modal-heading-title']">Cập nhật nội dung</div>
        <div :class="$style['container__modal-heading-exit']" @click="handleModalCancel">
          <FontAwesomeIcon :icon="faXmark" />
        </div>
      </div>
      <div :class="$style['container__modal-update']">
        <div :class="$style['container__modal-update-block']">
          <div
            :class="$style['container__modal-update-block-item']"
            v-for="(item, index) in edited"
            :key="index"
          >
            <div :class="$style['container__modal-update-block-item-title']">
              <p>Tiêu đề</p>
              <input
                type="text"
                name="title"
                id="title"
                placeholder="Nhập tiêu đề"
                v-model="item.title"
                @input="(e) => updateTitle(e, Number(index))"
              />
            </div>
            <div :class="$style['container__modal-update-block-item-content']">
              <p>Nội dung mô tả</p>
              <editor
                :id="`edid${index}`"
                allowedEvents="onChange"
                :onChange="(e: any) => handleChangeContent(e, Number(index))"
                api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
                :initial-value="item.content"
                :init="{
                  selector: `textarea#edid${index}`,
                  placeholder: 'Nhập tiêu đề',
                  height: 200,
                  width: 587,
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
                    // eslint-disable-next-line max-len
                    'undo redo | casechange blocks | formatselect | bold italic backcolor forecolor | \
           alignleft aligncenter alignright alignjustify | \
           bullist numlst outdent indent | removeformat | code table help'
                }"
              />
            </div>
            <div v-if="!hiddenCustomize" :class="$style['container__modal-update-add']">
              <div
                :class="[
                  $style['container__modal-update-add-block'],
                  $style['container__modal-update-add-sub']
                ]"
                @click="handleDeleteContent(Number(index))"
              >
                <FontAwesomeIcon :icon="faMinus" />
                <div :class="$style['container__modal-update-add-sub-text']">Xóa nội dung</div>
              </div>
              <div
                v-if="hiddenMore"
                :class="[
                  $style['container__modal-update-add-block'],
                  $style['container__modal-update-add-plus']
                ]"
                @click="handleAddContent(Number(index))"
              >
                <FontAwesomeIcon :icon="faPlus" />
                <div :class="$style['container__modal-update-add-plus-text']">Thêm nội dung</div>
              </div>
            </div>
          </div>
        </div>
        <div
          :class="[$style['container__modal-update-add'], $style['container__modal-update-empty']]"
          v-if="edited.length === 0"
        >
          <div
            :class="[
              $style['container__modal-update-add-block'],
              $style['container__modal-update-add-plus']
            ]"
            @click="handleAddContent(0)"
          >
            <FontAwesomeIcon :icon="faPlus" />
            <div :class="$style['container__modal-update-add-plus-text']">Thêm nội dung</div>
          </div>
        </div>
        <div :class="$style['container__modal-update-btn']">
          <div
            :class="[
              $style['container__modal-update-btn-submit'],
              $style['container__modal-update-btn-cancel']
            ]"
            @click="handleModalCancel"
          >
            Hủy
          </div>
          <div
            :class="[
              $style['container__modal-update-btn-submit'],
              $style['container__modal-update-btn-update']
            ]"
            @click="handleModalUpdate"
          >
            Cập nhật
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalUpdateContent.module.scss';
</style>

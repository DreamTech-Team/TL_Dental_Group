<script setup lang="ts">
import { ref } from 'vue';
import Swal from 'sweetalert2';
import styles from './ModalUpdateContent.module.scss';

const props = defineProps({
  contentEdit: {
    type: Object,
    required: true
  },
  handleUpdated: {
    type: Function,
    required: true
  }
});

const edited = ref(props.contentEdit);
const emit = defineEmits(['isFocus', 'close']);

const updateTitle = (e: Event, index: number) => {
  const target = e.target as HTMLInputElement;
  edited.value[index].title = target.value;
};

const updateContent = (e: Event, index: number) => {
  const target = e.target as HTMLInputElement;
  edited.value[index].content = target.value;
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
    }
  });
};

const handleAddContent = (index: number) => {
  edited.value.splice(index + 1, 0, { title: '', content: '' });
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
          <svg viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" fill="none">
            <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
            <g id="SVGRepo_iconCarrier">
              <path
                fill="#000000"
                fill-rule="evenodd"
                d="M9 17a1 1 0 102 0v-6h6a1 1 0 100-2h-6V3a1 1 0 10-2 0v6H3a1 1 0 000 2h6v6z"
              ></path>
            </g>
          </svg>
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
              <textarea
                id="content"
                placeholder="Nhập nội dung mô tả"
                v-model="item.content"
                @input="(e) => updateContent(e, Number(index))"
              />
            </div>
            <div :class="$style['container__modal-update-add']">
              <div
                :class="[
                  $style['container__modal-update-add-block'],
                  $style['container__modal-update-add-sub']
                ]"
                @click="handleDeleteContent(Number(index))"
              >
                <svg
                  viewBox="0 -12 32 32"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  xmlns:xlink="http://www.w3.org/1999/xlink"
                  xmlns:sketch="http://www.bohemiancoding.com/sketch/ns"
                  fill="#000000"
                >
                  <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                  <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                  <g id="SVGRepo_iconCarrier">
                    <title>minus</title>
                    <desc>Created with Sketch Beta.</desc>
                    <defs></defs>
                    <g
                      id="Page-1"
                      stroke="none"
                      stroke-width="1"
                      fill="none"
                      fill-rule="evenodd"
                      sketch:type="MSPage"
                    >
                      <g
                        id="Icon-Set-Filled"
                        sketch:type="MSLayerGroup"
                        transform="translate(-414.000000, -1049.000000)"
                        fill="#000000"
                      >
                        <path
                          d="M442,1049 L418,1049 C415.791,1049 414,1050.79 414,1053 C414,1055.21 415.791,1057 418,1057 L442,1057 C444.209,1057 446,1055.21 446,1053 C446,1050.79 444.209,1049 442,1049"
                          id="minus"
                          sketch:type="MSShapeGroup"
                        ></path>
                      </g>
                    </g>
                  </g>
                </svg>
                <div :class="$style['container__modal-update-add-sub-text']">Xóa nội dung</div>
              </div>
              <div
                :class="[
                  $style['container__modal-update-add-block'],
                  $style['container__modal-update-add-plus']
                ]"
                @click="handleAddContent(Number(index))"
              >
                <svg
                  viewBox="0 0 32 32"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  xmlns:xlink="http://www.w3.org/1999/xlink"
                  xmlns:sketch="http://www.bohemiancoding.com/sketch/ns"
                  fill="#000000"
                >
                  <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                  <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                  <g id="SVGRepo_iconCarrier">
                    <title>plus</title>
                    <desc>Created with Sketch Beta.</desc>
                    <defs></defs>
                    <g
                      id="Page-1"
                      stroke="none"
                      stroke-width="1"
                      fill="none"
                      fill-rule="evenodd"
                      sketch:type="MSPage"
                    >
                      <g
                        id="Icon-Set-Filled"
                        sketch:type="MSLayerGroup"
                        transform="translate(-362.000000, -1037.000000)"
                        fill="#000000"
                      >
                        <path
                          d="M390,1049 L382,1049 L382,1041 C382,1038.79 380.209,1037 378,1037 C375.791,1037 374,1038.79 374,1041 L374,1049 L366,1049 C363.791,1049 362,1050.79 362,1053 C362,1055.21 363.791,1057 366,1057 L374,1057 L374,1065 C374,1067.21 375.791,1069 378,1069 C380.209,1069 382,1067.21 382,1065 L382,1057 L390,1057 C392.209,1057 394,1055.21 394,1053 C394,1050.79 392.209,1049 390,1049"
                          id="plus"
                          sketch:type="MSShapeGroup"
                        ></path>
                      </g>
                    </g>
                  </g>
                </svg>
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
              $style['container__modal-update-add-sub']
            ]"
            @click="handleDeleteContent(0)"
          >
            <svg
              viewBox="0 -12 32 32"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              xmlns:xlink="http://www.w3.org/1999/xlink"
              xmlns:sketch="http://www.bohemiancoding.com/sketch/ns"
              fill="#000000"
            >
              <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
              <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
              <g id="SVGRepo_iconCarrier">
                <title>minus</title>
                <desc>Created with Sketch Beta.</desc>
                <defs></defs>
                <g
                  id="Page-1"
                  stroke="none"
                  stroke-width="1"
                  fill="none"
                  fill-rule="evenodd"
                  sketch:type="MSPage"
                >
                  <g
                    id="Icon-Set-Filled"
                    sketch:type="MSLayerGroup"
                    transform="translate(-414.000000, -1049.000000)"
                    fill="#000000"
                  >
                    <path
                      d="M442,1049 L418,1049 C415.791,1049 414,1050.79 414,1053 C414,1055.21 415.791,1057 418,1057 L442,1057 C444.209,1057 446,1055.21 446,1053 C446,1050.79 444.209,1049 442,1049"
                      id="minus"
                      sketch:type="MSShapeGroup"
                    ></path>
                  </g>
                </g>
              </g>
            </svg>
            <div :class="$style['container__modal-update-add-sub-text']">Xóa nội dung</div>
          </div>
          <div
            :class="[
              $style['container__modal-update-add-block'],
              $style['container__modal-update-add-plus']
            ]"
            @click="handleAddContent(0)"
          >
            <svg
              viewBox="0 0 32 32"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              xmlns:xlink="http://www.w3.org/1999/xlink"
              xmlns:sketch="http://www.bohemiancoding.com/sketch/ns"
              fill="#000000"
            >
              <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
              <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
              <g id="SVGRepo_iconCarrier">
                <title>plus</title>
                <desc>Created with Sketch Beta.</desc>
                <defs></defs>
                <g
                  id="Page-1"
                  stroke="none"
                  stroke-width="1"
                  fill="none"
                  fill-rule="evenodd"
                  sketch:type="MSPage"
                >
                  <g
                    id="Icon-Set-Filled"
                    sketch:type="MSLayerGroup"
                    transform="translate(-362.000000, -1037.000000)"
                    fill="#000000"
                  >
                    <path
                      d="M390,1049 L382,1049 L382,1041 C382,1038.79 380.209,1037 378,1037 C375.791,1037 374,1038.79 374,1041 L374,1049 L366,1049 C363.791,1049 362,1050.79 362,1053 C362,1055.21 363.791,1057 366,1057 L374,1057 L374,1065 C374,1067.21 375.791,1069 378,1069 C380.209,1069 382,1067.21 382,1065 L382,1057 L390,1057 C392.209,1057 394,1055.21 394,1053 C394,1050.79 392.209,1049 390,1049"
                      id="plus"
                      sketch:type="MSShapeGroup"
                    ></path>
                  </g>
                </g>
              </g>
            </svg>
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

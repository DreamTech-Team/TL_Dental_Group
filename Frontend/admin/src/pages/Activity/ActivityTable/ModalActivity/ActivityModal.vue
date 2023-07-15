<script setup lang="ts">
import Editor from '@tinymce/tinymce-vue';
import { ref, computed } from 'vue';
import type { CSSProperties } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus, faMagnifyingGlass, faMinus, faXmark } from '@fortawesome/free-solid-svg-icons';

import { tags } from '../../Activity';
import imageAct from '../../../../assets/imgs/Activity/image.png';
import icPhoto from '../../../../assets/icons/camera.svg';
import { type PropType } from 'vue';
import { id } from 'element-plus/lib/locale/index.js';

const inputId = ref('imageInput');
const currentStep = ref(1);

interface Tags {
  id: number;
  name: string;
  createDate: string;
}

export interface CloseModalFn {
  (...payload: any[]): void;
}

defineProps({
  closeModal: {
    type: Function as PropType<CloseModalFn>,
    required: true
  }
});

const activityContent = ref({
  content: ``
});

const imageActivity = ref(imageAct);
const listTags = ref<Tags[]>(tags);
const selectedTags = ref<Tags[]>([]);
const searchInput = ref('');
const _content = ref(``);
const activityTitle = ref('');
const activitySummary = ref('');

const textareaStyle: CSSProperties = {
  resize: 'none',
  height: 'calc(100vh - 560px)',
  maxHeight: '400px',
  borderRadius: '4px',
  padding: '8px',
  fontSize: '16px',
  fontFamily: 'Roboto',
  border: '1px solid #ccc'
};

const filteredTags = computed(() => {
  const searchTerm = searchInput.value.toLowerCase();
  return listTags.value.filter((item) => item.name.toLowerCase().includes(searchTerm));
});

const handleImageChange = (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      const imageUrl = URL.createObjectURL(file);
      imageActivity.value = imageUrl;
    };
    reader.readAsDataURL(file);
  }
};

const selectedTag = (tag: Tags) => {
  if (!selectedTags.value.includes(tag)) {
    selectedTags.value.push(tag);
    console.log(selectedTags);
  }
};

const removeFromSelected = (index: number) => {
  selectedTags.value.splice(index, 1);
};

const isTagSelected = (tag: Tags) => {
  return selectedTags.value.some((selectedTag) => selectedTag.name === tag.name);
};

const handleChangeContent = (e: { target: { getContent: () => string } }) => {
  _content.value = e.target.getContent();
  console.log(_content.value);
};

const nextStep = () => {
  currentStep.value = 2;
};

const prevStep = () => {
  currentStep.value = 1;
};

// const handleUpdateContent = () => {
//   isEdit.value = false;
//   activityContent.value.content = _content.value;
// };

// Tạo các biến tính toán
const computedListTags = computed(() => listTags.value);
const computedSelectedTags = computed(() => selectedTags.value);
</script>
<template>
  <div :class="$style.activity_container">
    <div :class="$style['activity_container--title']">
      <p>CHỈNH SỬA HOẠT ĐỘNG</p>
      <font-awesome-icon @click="closeModal" :icon="faXmark" :class="$style.activity_cancel" />
    </div>
    <!-- Step1 -->
    <div v-if="currentStep === 1" :class="$style.wrap_step1">
      <div :class="$style['wrap_step1--wrap']">
        <div :class="$style['wrap_step1--wrap-left']">
          <div :class="$style.wrap_title">
            <label :class="$style['wrap_title--text']" for="activity-title"
              >Tiêu đề hoạt động</label
            >
            <input
              id="activity-title"
              v-model="activityTitle"
              placeholder="Nhập tiêu đề hoạt động"
              :class="$style['custom-input']"
            />
          </div>
          <div :class="$style.wrap_sumary">
            <label :class="$style['wrap_title--text']" for="activity-summary"
              >Nội dung tóm tắt</label
            >
            <textarea
              :style="textareaStyle"
              id="activity-summary"
              v-model="activitySummary"
              placeholder="Nhập nội dung tóm tắt"
              :class="$style['custom-textarea']"
            ></textarea>
          </div>

          <div :class="$style.activity_image">
            <p>Cập nhật ảnh bìa</p>
            <div :class="$style.activity_frame_image">
              <img :src="imageActivity" alt="Image" :class="$style.image" />
              <label :for="inputId" :class="$style.add_image">
                <img :src="icPhoto" alt="ic" :class="$style.ic_photo" />
              </label>
              <input
                type="file"
                @change="handleImageChange"
                :id="inputId"
                :class="$style.ic_photo_input"
                ref="imageInput"
                :style="{ opacity: 0, position: 'absolute' }"
              />
            </div>
          </div>
        </div>
        <div :class="$style['wrap_step1--wrap-right']">
          <div :class="$style.tag_choose">
            <div :class="$style['modal__search']">
              <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['home__category-ic']" />
              <input
                type="text"
                v-model="searchInput"
                :class="$style['modal__search-input']"
                placeholder="Tìm kiếm tags"
              />
            </div>
            <div :class="$style.container_tags">
              <div :class="$style.wrap_tags_title">
                <p :class="$style.wrap_tags_left">Tags</p>
                <p :class="$style.wrap_tags_right">Các tags đã chọn</p>
              </div>
              <div :class="$style.wrap_tags_ctn">
                <div :class="$style.wrap_tags_ctn_left" style="overflow-y: auto">
                  <div
                    :class="[
                      $style['tag__left-item'],
                      { [$style['selected-tag']]: isTagSelected(item) }
                    ]"
                    v-for="(item, index) in filteredTags"
                    :title="item.name"
                    :key="index"
                    style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden"
                  >
                    {{ item.name }}
                    <font-awesome-icon
                      :icon="faPlus"
                      :class="$style['tag__modal-st']"
                      @click="selectedTag(item)"
                    />
                  </div>
                </div>
                <div :class="$style.wrap_tags_ctn_right" style="overflow-y: auto">
                  <div
                    :class="$style['tag__right-item']"
                    v-for="(item, index) in selectedTags"
                    :key="index"
                    :title="item.name"
                    style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden"
                  >
                    {{ item.name }}
                    <font-awesome-icon
                      :icon="faMinus"
                      :class="$style['tag__modal-st']"
                      @click="removeFromSelected(index)"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div :class="$style['wrap_step1--btn']">
        <button @click="nextStep" :class="$style.button">Qua bước 2</button>
      </div>
    </div>

    <!-- Step 2 -->
    <div v-else-if="currentStep === 2" :class="$style.wrap_step2">
      <div :class="$style.wrap_step2_editer">
        <editor
          allowedEvents="onChange"
          @change="handleChangeContent"
          api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
          :initial-value="activityContent.content"
          :value="activityContent.content"
          :init="{
            selector: 'textarea',
            placeholder: 'Nhập mô tả chi tiết',
            height: 'calc(100vh - 200px)',
            menubar: false,
            image_title: true,
            automatic_uploads: true,
            file_picker_types: 'image',
            plugins: [
              'advlist autolink lists link image charmap print preview anchor',
              'searchreplace visualblocks code fullscreen',
              'insertdatetime media table paste code help wordcount',
              'image'
            ],
            toolbar:
              'undo redo | formatselect | bold italic forecolor backcolor |\
           alignleft aligncenter alignright alignjustify | \
           bullist numlist outdent indent | removeformat | help | image',
            images_file_types: 'jpg,svg,webp',
            color_map: [
              '000000',
              'Black',
              '808080',
              'Gray',
              'FFFFFF',
              'White',
              'FF0000',
              'Red',
              'FFFF00',
              'Yellow',
              '008000',
              'Green',
              '0000FF',
              'Blue'
            ]
          }"
        />
      </div>
      <div :class="$style.wrap_step2_btn">
        <button :class="$style.wrap_step2_btn_back" @click="prevStep">Quay lại</button>
        <button :class="$style.wrap_step2_btn_done">Hoàn tất</button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ActivityModel.module.scss';
</style>

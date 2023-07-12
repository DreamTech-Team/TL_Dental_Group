<script setup lang="ts">
import { ref, type PropType } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import { tags } from '../../Activity';

const tagExist = ref(true);

export interface CloseModalFn {
  (...payload: any[]): void;
}

const props = defineProps({
  selectedTag: {
    type: Object, // Kiểu dữ liệu của prop
    required: true // Bắt buộc phải truyền giá trị cho prop
  },
  closeModal: {
    type: Function as PropType<CloseModalFn>,
    required: true
  }
});
const tagName = ref(props.selectedTag.name);
</script>
<template>
  <div :class="$style.tag_container">
    <div>
      <div :class="$style['tag_container--title']">
        <p>CHỈNH SỬA HOẠT ĐỘNG</p>
        <font-awesome-icon @click="closeModal" :icon="faXmark" :class="$style.tag_cancel" />
      </div>
      <div :class="$style.wrap_title">
        <label :class="$style['wrap_title--text']" for="activity-title">Tên tag</label>
        <input
          id="activity-title"
          v-model="tagName"
          placeholder="Nhập tên tag"
          :class="$style['custom-input']"
        />
      </div>
      <div v-if="tagExist" :class="$style.tag_exist">
        <p>Tag đã tồn tại</p>
      </div>
    </div>
    <div :class="$style['tag_container--btn']">
      <button :class="$style.button">Thêm</button>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalTag.module.scss';
</style>

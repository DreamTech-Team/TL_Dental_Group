<script setup lang="ts">
import { ref, watch } from 'vue';

const props = defineProps({
  cateType: { type: Number, required: true },
  data: { type: Object, require: true },
  isEmptyItems: { type: Boolean, required: true },
  handleSelected: { type: Function, required: false },
  openEdit: { type: Function, required: false }
});

const emptyItem = '<<Trống>>';

const selectedItem = ref(-1);
const listItem = ref();

const handleClickItem = (index: number, item: object) => {
  if (props.handleSelected)
    if (selectedItem.value === index) {
      selectedItem.value = -1;
      props.handleSelected(null);
    } else {
      selectedItem.value = index;
      props.handleSelected(item);
    }
};

watch(
  () => props.data,
  (value) => {
    listItem.value = value;

    if (!value?.id) {
      selectedItem.value = -1;
    }
  }
);
</script>
<template>
  <div :class="$style.container">
    <div :class="$style['container-list']">
      <div
        v-for="(item, index) in listItem"
        :class="[
          $style['container-list-item'],
          $style[selectedItem === Number(index) ? 'container-list-item-active' : '']
        ]"
        :key="index"
        @click="handleClickItem(index, item)"
      >
        <p>{{ item.name || item.title }}</p>
        <svg
          v-if="props.cateType !== 2"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
          <g id="SVGRepo_iconCarrier">
            <path
              d="M15 7L10 12L15 17"
              stroke="#000000"
              stroke-width="1.5"
              stroke-linecap="round"
              stroke-linejoin="round"
            ></path>
          </g>
        </svg>
      </div>
      <div
        v-if="isEmptyItems"
        :class="[$style['container-list-item'], $style['container-list-item-empty']]"
      >
        <p>{{ emptyItem }}</p>
      </div>
    </div>
    <div :class="$style['container-add']">
      <div :class="$style['container-add-block']" @click="props.openEdit">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
          <g id="SVGRepo_iconCarrier">
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="M12 4C12.5523 4 13 4.44772 13 5V11H19C19.5523 11 20 11.4477 20 12C20 12.5523 19.5523 13 19 13H13V19C13 19.5523 12.5523 20 12 20C11.4477 20 11 19.5523 11 19V13H5C4.44772 13 4 12.5523 4 12C4 11.4477 4.44772 11 5 11H11V5C11 4.44772 11.4477 4 12 4Z"
              fill="#000000"
            ></path>
          </g>
        </svg>
        <p v-if="cateType === 0">Thêm Công ty</p>
        <p v-else-if="cateType === 1">Thêm Danh Mục 1</p>
        <p v-else>Thêm Danh Mục 2</p>
      </div>
    </div>
    <!-- <category-modal-add /> -->
  </div>
</template>

<style module scoped lang="scss">
@import './CategoryList.module.scss';
</style>

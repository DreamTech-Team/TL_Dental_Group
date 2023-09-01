<script setup lang="ts">
import { faChevronLeft, faPlus } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { ref, watch } from 'vue';

const props = defineProps({
  cateType: { type: Number, required: true },
  data: { type: Object, require: true },
  isEmptyItems: { type: Boolean, required: true },
  handleSelected: { type: Function, required: false },
  openEdit: { type: Function, required: false },
  resetSelected: { type: Boolean, required: false }
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
console.log(props.isEmptyItems);

watch(
  () => props.data,
  (value) => {
    listItem.value = value;

    if (!value?.id) {
      selectedItem.value = -1;
    }
  }
);

watch(
  () => props.resetSelected,
  () => {
    if (props.resetSelected) selectedItem.value = -1;
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
        <FontAwesomeIcon v-if="props.cateType !== 2" :icon="faChevronLeft" />
      </div>
      <div
        v-if="isEmptyItems"
        :class="[$style['container-list-item'], $style['container-list-item-empty']]"
      >
        <p>{{ emptyItem }}</p>
      </div>
    </div>
    <div :class="$style['container-add']">
      <div :class="$style['container-add-block']" v-if="cateType !== 0" @click="props.openEdit">
        <FontAwesomeIcon :icon="faPlus" />
        <p v-if="cateType === 1">Thêm Danh Mục 1</p>
        <p v-else>Thêm Danh Mục 2</p>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './CategoryList.module.scss';
</style>

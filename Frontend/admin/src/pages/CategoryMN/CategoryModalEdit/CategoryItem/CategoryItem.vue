<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faTrash, faPen, faPlus, faMinus } from '@fortawesome/free-solid-svg-icons';

import { ref } from 'vue';

const props = defineProps({
  openEdit: { type: Boolean, required: false },
  type: { type: String, require: false },
  data: { type: Object, require: true },
  handleBtn: { type: Function, required: true }
});

const emit = defineEmits(['open-modal-edit']);
</script>
<template>
  <div :class="$style.container">
    <div :class="$style.container__left">
      <img v-if="props.data?.img" :src="props.data?.img" alt="none" />
      <p>{{ props.data?.title }}</p>
    </div>
    <div :class="$style.container__right" v-if="props.openEdit">
      <button :class="$style['btn-room-trash']">
        <font-awesome-icon :icon="faTrash" />
      </button>
      <button
        :class="[$style['btn-room-edit'], $style['container__right-btn']]"
        @click="emit('open-modal-edit')"
      >
        <font-awesome-icon :icon="faPen" />
      </button>
    </div>
    <div :class="$style.container__right" v-else>
      <button
        :class="[$style[''], $style[props.type === 'minus' ? 'btn-room-minus' : 'btn-room-edit']]"
        @click="props.handleBtn(data)"
      >
        <font-awesome-icon :icon="props.type !== 'minus' ? faPlus : faMinus" />
      </button>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './CategoryItem.module.scss';
</style>

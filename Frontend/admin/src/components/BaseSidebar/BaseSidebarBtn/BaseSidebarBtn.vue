<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { watch, reactive } from 'vue';

const props = defineProps({
  obj: { type: Object, required: true },
  show: { type: Boolean, required: true },
  active: { type: Boolean, required: true },
  hanldeConvertPage: { type: Function, required: true }
});

const checkActive = reactive({ value: props.active });

watch(
  () => props.active,
  (newValue) => (checkActive.value = newValue)
);
</script>
<template>
  <router-link :to="obj.link" @click="hanldeConvertPage(obj.link)">
    <div
      :class="[
        $style.container__btn,
        $style[show ? 'show' : ''],
        $style[checkActive.value ? 'active' : '']
      ]"
    >
      <div :class="$style['container__btn-left']">
        <div :class="$style['container__btn-left-block']">
          <font-awesome-icon :class="$style['container__btn-left-block-icon']" :icon="obj.icon" />
        </div>
        <p>{{ obj.label }}</p>
      </div>
    </div>
  </router-link>
</template>

<style module scoped lang="scss">
@import './BaseSidebarBtn.module.scss';
</style>

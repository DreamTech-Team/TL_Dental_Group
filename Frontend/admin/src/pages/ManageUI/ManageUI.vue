<script setup lang="ts">
import { ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faHouse, faArrowCircleRight } from '@fortawesome/free-solid-svg-icons';
</script>
<template>
  <div :class="$style.mn_ui">
    <h2>DANH SÁCH CÔNG TY</h2>

    <div :class="$style['mn_ui--list']">
      <div :class="$style['mn_ui--item']">
        <h3>Trang chủ</h3>

        <font-awesome-icon :icon="faHouse" :class="$style['mn_ui--item-ic']" />

        <div :class="$style['mn_ui--item-footer']">
          <p>Chỉnh sửa</p>

          <font-awesome-icon :icon="faArrowCircleRight" :class="$style['mn_ui--item-footer-ic']" />
        </div>
      </div>

      <div :class="$style['mn_ui--item']">
        <h3>Trang chủ</h3>

        <font-awesome-icon :icon="faHouse" :class="$style['mn_ui--item-ic']" />

        <div :class="$style['mn_ui--item-footer']">
          <p>Chỉnh sửa</p>

          <font-awesome-icon :icon="faArrowCircleRight" :class="$style['mn_ui--item-footer-ic']" />
        </div>
      </div>

      <div :class="$style['mn_ui--item']">
        <h3>Trang chủ</h3>

        <font-awesome-icon :icon="faHouse" :class="$style['mn_ui--item-ic']" />

        <div :class="$style['mn_ui--item-footer']">
          <p>Chỉnh sửa</p>

          <font-awesome-icon :icon="faArrowCircleRight" :class="$style['mn_ui--item-footer-ic']" />
        </div>
      </div>

      <div :class="$style['mn_ui--item']">
        <h3>Trang chủ</h3>

        <font-awesome-icon :icon="faHouse" :class="$style['mn_ui--item-ic']" />

        <div :class="$style['mn_ui--item-footer']">
          <p>Chỉnh sửa</p>

          <font-awesome-icon :icon="faArrowCircleRight" :class="$style['mn_ui--item-footer-ic']" />
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ManageUI.module.scss';
</style>

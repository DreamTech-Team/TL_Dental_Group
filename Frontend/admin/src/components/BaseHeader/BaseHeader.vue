<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPhone, faEnvelope } from '@fortawesome/free-solid-svg-icons';
import Avatar from '@/assets/imgs/adminavt.png';
</script>
<template>
  <div :class="$style.header">
    <div :class="$style['header__infor']">
      <div :class="$style['header__infor-item']">
        <font-awesome-icon :icon="faPhone" :class="$style['header__ic']" />
        <p>(+84) 898923230</p>
      </div>
      <div :class="$style['header__infor-item']">
        <font-awesome-icon :icon="faEnvelope" :class="$style['header__ic']" />
        <p>admin@gmail.com</p>
      </div>
      <div :class="$style['header__infor-item']">
        <img :src="Avatar" alt="avatar" />
        <div :class="$style['header__infor-item--active']"></div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './BaseHeader.module.scss';
</style>

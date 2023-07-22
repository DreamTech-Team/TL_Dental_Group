<script setup lang="ts">
import second from '../../assets/imgs/Authen/bglogon.svg';
import logo from '../../assets/imgs/Activity/image.png';
import ellip1 from '../../assets/imgs/Authen/ellipse178.png';
import ellip3 from '../../assets/imgs/Authen/ellipse179.png';
import ellip2 from '../../assets/imgs/Authen/ellipse177.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faUnlockKeyhole } from '@fortawesome/free-solid-svg-icons';
import { ref } from 'vue';

const username = ref('');
const checkAccount = ref(true);
</script>
<template>
  <div :class="$style.authen">
    <div :class="$style.authen_container">
      <p :class="$style.authen_container_title">Đăng nhập</p>
      <p :class="$style.authen_container_subtitle">Trang web chính: https://www.dental.com</p>
      <div :class="$style.authen_container_wrap">
        <font-awesome-icon :icon="faUnlockKeyhole" :class="$style['authen_container_wrap_ic']" />
        <a href="https://www.dental.com">https://www.dental.com</a>
      </div>
      <div :class="$style.wrap_title">
        <label :class="$style['wrap_title--text']" for="user-name">Email</label>
        <input
          id="user-name"
          v-model="username"
          placeholder="Nhập email"
          :class="$style['custom-input']"
        />
      </div>
      <div :class="$style.wrap_title">
        <label :class="$style['wrap_title--text']" for="user-pass">Mật khẩu</label>
        <input
          id="user-pass"
          v-model="username"
          placeholder="Nhập mật khẩu"
          :class="$style['custom-input']"
        />
      </div>
      <p v-if="checkAccount === true" :class="$style.checkAccount">
        *Email hoặc mật khẩu không chính xác
      </p>
      <div :class="$style.wrap_btn">
        <button>Đăng nhập</button>
      </div>
      <div :class="$style.wrap_route">
        <router-link to="/forgot" :class="$style.wrap_cancel">Quên mật khẩu</router-link>
      </div>
    </div>
    <div :class="$style.authen_welcome">
      <!-- <img :src="second" alt="sdf" :class="$style.authen_welcome_img" /> -->
      <div :class="$style.authen_welcome_img"></div>
      <div :class="$style.authen_welcome_content">
        <img :class="$style.authen_welcome_content_ellip1" :src="ellip1" alt="elip" />
        <img :class="$style.authen_welcome_content_ellip2" :src="ellip1" alt="elip2" />
        <img :class="$style.authen_welcome_content_ellip3" :src="ellip3" alt="elip2" />
        <img :class="$style.authen_welcome_content_img" :src="logo" alt="logo" />
        <p :class="$style.authen_welcome_content_title">CHÀO MỪNG BẠN ĐÃ TRỞ LẠI</p>
        <p :class="$style.authen_welcome_content_text">
          Chào mừng bạn đến với trang đăng nhập quản trị. Vui lòng đăng nhập bằng tên người dùng và
          mật khẩu để tiếp tục quản lý hệ thống.
        </p>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './Authen.module.scss';
</style>

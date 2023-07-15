<script setup lang="ts">
import second from '../../assets/imgs/Authen/bglogon.svg';
import logo from '../../assets/imgs/Activity/image.png';
import ellip1 from '../../assets/imgs/Authen/ellipse178.png';
import ellip3 from '../../assets/imgs/Authen/ellipse179.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faUnlockKeyhole } from '@fortawesome/free-solid-svg-icons';
import { ref } from 'vue';

const username = ref('');
const checkAccount = ref(true);
const currentStep = ref(1);

const prevStep = () => {
  if (currentStep.value > 1) {
    currentStep.value -= 1;
  }
};

const nextStep = () => {
  if (currentStep.value < 3) {
    currentStep.value += 1;
  }
};
</script>
<template>
  <div :class="$style.authen">
    <div v-if="currentStep === 1" :class="$style.authen_container">
      <p :class="$style.authen_container_title">Quên mật khẩu</p>

      <div :class="$style.wrap_title">
        <label :class="$style['wrap_title--text']" for="user-name">Email</label>
        <input
          id="user-name"
          v-model="username"
          placeholder="Nhập email"
          :class="$style['custom-input']"
        />
      </div>
      <p v-if="checkAccount === true" :class="$style.checkAccount">
        *Tài khoản của bạn không tồn lại
      </p>
      <div :class="$style.wrap_btn">
        <button @click="nextStep">Xác nhận tài khoản</button>
      </div>
      <div :class="$style.wrap_route">
        <router-link to="/login" :class="$style.wrap_cancel">Quay lại đăng nhập</router-link>
      </div>
    </div>

    <div v-if="currentStep === 2" :class="$style.authen_container">
      <p :class="$style.authen_container_title">Quên mật khẩu</p>

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
        <label :class="$style['wrap_title--text']" for="user-otp">Nhập mã OTP</label>
        <input
          id="user-otp"
          v-model="username"
          placeholder="Nhập mã OTP"
          :class="$style['custom-input']"
        />
      </div>
      <p v-if="checkAccount === true" :class="$style.checkAccount">*OTP không chính xác</p>
      <div :class="$style.wrap_btn">
        <button @click="nextStep">Xác nhận</button>
      </div>

      <div :class="$style.wrap_route">
        <router-link to="/login" :class="$style.wrap_cancel">Quay lại đăng nhập</router-link>
      </div>
    </div>

    <div v-if="currentStep === 3" :class="$style.authen_container">
      <p :class="$style.authen_container_title">Quên mật khẩu</p>

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
        <label :class="$style['wrap_title--text']" for="user-newpass">Nhập mật khẩu mới</label>
        <input
          id="user-newpass"
          v-model="username"
          placeholder="Nhập mật khẩu mới"
          :class="$style['custom-input']"
        />
      </div>
      <div :class="$style.wrap_title">
        <label :class="$style['wrap_title--text']" for="user-confirmpass"
          >Xác nhận mật khẩu mới</label
        >
        <input
          id="user-confirmpass"
          v-model="username"
          placeholder="Nhập lại mật khẩu mới"
          :class="$style['custom-input']"
        />
      </div>
      <p v-if="checkAccount === true" :class="$style.checkAccount">
        *Xác nhận mật khẩu không chính xác
      </p>
      <div :class="$style.wrap_btn">
        <button @click="nextStep">Đổi mật khẩu</button>
      </div>

      <div :class="$style.wrap_route">
        <router-link to="/login" :class="$style.wrap_cancel">Quay lại đăng nhập</router-link>
      </div>
    </div>
    <div :class="$style.authen_welcome">
      <img :src="second" alt="sdf" :class="$style.authen_welcome_img" />
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

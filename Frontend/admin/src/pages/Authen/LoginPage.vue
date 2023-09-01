<script setup lang="ts">
import logo from '../../assets/imgs/Activity/image.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faUnlockKeyhole } from '@fortawesome/free-solid-svg-icons';
import { ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { useRouter } from 'vue-router';
import { useInforAdminStore } from '@/stores/counter';

const accountInput = ref({ email: '', pass: '' });
const checkAccount = ref(false);
const isLoading = ref(false);
const paramAxios = ref();
const router = useRouter();
const infoAdminStore = useInforAdminStore();

const handleLogin = (e: Event) => {
  e.preventDefault();

  const postAccount = useAxios<DataResponse>(
    'post',
    '/auth/login',
    {
      email: accountInput.value.email,
      password: accountInput.value.pass
    },
    {},
    paramAxios.value
  );

  watch(postAccount.error, (value) => {
    console.log(value);
    checkAccount.value = true;
  });

  watch(postAccount.isLoading, (value) => {
    console.log(value);
    isLoading.value = value;
  });

  watch(postAccount.response, (value) => {
    const newInfor = {
      id: value?.data.user.id,
      email: value?.data.user.email,
      fullName: value?.data.user.fullname,
      phoneNumber: value?.data.user.phonenumber,
      address: value?.data.user.address,
      password: value?.data.user.password,
      roles: value?.data.user.roles,
      token: value?.data.jwt
    };

    localStorage.setItem('infor_admin', JSON.stringify(newInfor));
    infoAdminStore.setInforAdmin(newInfor);
    console.log(localStorage.getItem('infor_admin'));

    router.push('/');
  });
};
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
      <form @submit="handleLogin">
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="user-name">Email</label>
          <input
            id="user-name"
            v-model="accountInput.email"
            placeholder="Nhập email"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="user-pass">Mật khẩu</label>
          <input
            id="user-pass"
            v-model="accountInput.pass"
            type="password"
            placeholder="Nhập mật khẩu"
            :class="$style['custom-input']"
          />
        </div>
        <p v-if="checkAccount === true" :class="$style.checkAccount">
          *Email hoặc mật khẩu không chính xác
        </p>
        <div :class="$style.wrap_btn">
          <button @click="handleLogin">Đăng nhập</button>
        </div>
      </form>
      <!-- <div :class="$style.wrap_route">
        <router-link to="/forgot" :class="$style.wrap_cancel">Quên mật khẩu?</router-link>
      </div> -->
    </div>
    <div :class="$style.authen_welcome">
      <div :class="$style.authen_welcome_img"></div>
      <div :class="$style.authen_welcome_content">
        <div :class="$style.authen_welcome_content_ellip1"></div>
        <div :class="$style.authen_welcome_content_ellip2"></div>
        <div :class="$style.authen_welcome_content_ellip3"></div>
        <img :class="$style.authen_welcome_content_img" :src="logo" alt="logo" />
        <p :class="$style.authen_welcome_content_title">CHÀO MỪNG BẠN ĐÃ TRỞ LẠI</p>
        <p :class="$style.authen_welcome_content_text">
          Chào mừng bạn đến với trang đăng nhập quản trị. Vui lòng đăng nhập bằng tên người dùng và
          mật khẩu để tiếp tục quản lý hệ thống.
        </p>
      </div>
    </div>
    <div v-if="isLoading" :class="$style.authen__loading">
      <div :id="$style.loader"></div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './Authen.module.scss';
</style>

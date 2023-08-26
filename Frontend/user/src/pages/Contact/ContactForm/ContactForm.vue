<script setup lang="ts">
import { ref, toRefs, watch } from 'vue';
import Zalo from '@/assets/imgs/Contact/Zalo.png';
import Telephone from '@/assets/imgs/Contact/Telephone.png';
import Message from '@/assets/imgs/Contact/Message.png';
import Location from '@/assets/imgs/Contact/Location.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faFacebook } from '@fortawesome/free-brands-svg-icons';
import { saveDataContact } from '@/stores/counter';
import styles from './ContactForm.module.scss';
import Swal from 'sweetalert2';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

// Lấy dữ liệu được lưu ở store về
const { dataFacility, dataContact } = toRefs(saveDataContact());

const variableChange = ref([]);
const isCLick = ref(false);
const isSocial = ref(true);
const move = ref(0);
const widthLine = ref(69);
const name = ref('');
const email = ref('');
const phone = ref('');
const content = ref('');
const isLoadingSuccess = ref(false);

// Các hàm lấy dữ liệu từ thẻ input
const updateName = (e: Event) => {
  const target = e.target as HTMLInputElement;

  name.value = target.value;
};

const updateEmail = (e: Event) => {
  const target = e.target as HTMLInputElement;

  email.value = target.value;
};

const updatePhone = (e: Event) => {
  const target = e.target as HTMLInputElement;

  phone.value = target.value;
};

const updateContent = (e: Event) => {
  const target = e.target as HTMLInputElement;

  content.value = target.value;
  console.log(content.value);
};

// Hàm xử lí khi bấm vào tiêu đề để cái line chạy qua chạy lại ở mobile
const handleClick = () => {
  isCLick.value = !isCLick.value;

  if (isCLick.value) move.value = -100;
  else move.value = 0;
};

// Hàm check tab ở Mobile
const handleSocialForm = (e: Event) => {
  const target = e.target as HTMLElement; // Ép kiểu 'e.target' thành 'HTMLElement'

  if (target) {
    if (target.textContent && target.textContent.trim() === 'Mạng xã hội') {
      isSocial.value = true;
      widthLine.value = target.offsetWidth;
    } else if (target.textContent === 'Biểu mẫu') {
      isSocial.value = false;
      widthLine.value = target.offsetWidth;
    }
  }
};

// Hàm gửi dữ liệu liên hệ đi
const submitForm = () => {
  if (
    name.value.length < 4 ||
    phone.value.length < 10 ||
    email.value.length < 8 ||
    content.value.length < 4
  ) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: window.innerWidth < 738 ? '25rem' : '50rem',
      padding: '0 2rem 2rem 2rem',
      customClass: {
        confirmButton: styles[window.innerWidth < 738 ? 'confirm-buttonMB' : 'confirm-button'],
        cancelButton: styles[window.innerWidth < 738 ? 'cancel-buttonMB' : 'cancel-button'],
        title: styles[window.innerWidth < 738 ? 'titleMB' : 'title']
      }
    });
  } else {
    const object = {
      fullname: name.value,
      email: email.value,
      phone: phone.value,
      content: content.value
    };

    const { response, isLoading } = useAxios<DataResponse>(
      'post',
      '/contact',
      object,
      {},
      variableChange.value
    );

    watch(response, () => {
      isLoadingSuccess.value = isLoading.value;
      if (response.value?.status === 'ok')
        Swal.fire({
          title: 'Gửi thành công',
          icon: 'success',
          confirmButtonText: 'Đóng',
          width: window.innerWidth < 738 ? '25rem' : '50rem',
          padding: '0 2rem 2rem 2rem',
          customClass: {
            confirmButton: styles[window.innerWidth < 738 ? 'confirm-buttonMB' : 'confirm-button'],
            cancelButton: styles[window.innerWidth < 738 ? 'cancel-buttonMB' : 'cancel-button'],
            title: styles[window.innerWidth < 738 ? 'titleMB' : 'title']
          }
        });
      name.value = '';
      email.value = '';
      phone.value = '';
      content.value = '';
    });
  }
};
</script>

<template>
  <div :class="$style.contact__form">
    <h1>Liên hệ</h1>

    <div :class="$style['contact__form-position']">
      <div :class="$style['contact__form-wrapper']">
        <div :class="$style['contact__form-social']">
          <p :class="$style['contact__form-social-title']">Bạn có thể kết nối qua</p>

          <button :class="$style['contact__form-social-button-facebook']">
            <a :href="dataContact.facebook.content" target="_blank">
              <font-awesome-icon
                :icon="faFacebook"
                :class="$style['contact__form-social-button-facebook-ic']"
              />
              <p>Facebook</p>
            </a>
          </button>
          <button :class="$style['contact__form-social-button-zalo']">
            <a :href="'https://zalo.me/' + dataContact.zalo.content" target="_blank">
              <img :src="Zalo" :class="$style['contact__form-social-button-zalo-ic']" />
              <p>Zalo</p>
            </a>
          </button>
        </div>

        <div :class="$style['contact__form-content']">
          <div :class="$style['contact__form-content-form']">
            <div>
              <p>Họ và tên<span style="color: red"> *</span></p>
              <input type="text" name="name" :value="name" @input="updateName" />
            </div>

            <div>
              <p>Email<span style="color: red"> *</span></p>
              <input type="email" name="email" :value="email" @input="updateEmail" />
            </div>

            <div>
              <p>Số điện thoại<span style="color: red"> *</span></p>
              <input type="text" name="sdt" :value="phone" @input="updatePhone" maxlength="10" />
            </div>

            <div>
              <p>Nội dung<span style="color: red"> *</span></p>
              <textarea
                name=""
                id=""
                cols="30"
                rows="10"
                :value="content"
                @input="updateContent"
              ></textarea>
            </div>

            <button @click="submitForm">Xác nhận</button>
          </div>
        </div>
      </div>

      <div
        :class="$style['contact__form-info']"
        :style="{ transform: 'translateX' + '(' + move + '%' + ')', right: isCLick ? '0' : '-1px' }"
      >
        <div :class="$style['contact__form-info-row']">
          <div :class="$style['contact__form-info-img']">
            <img :src="Telephone" :class="$style['contact__form-info-img-ic']" />
          </div>
          <p>{{ dataFacility.phoneNumber }}</p>
        </div>

        <div :class="$style['contact__form-info-row']">
          <div :class="$style['contact__form-info-img']">
            <img :src="Message" :class="$style['contact__form-info-img-ic']" />
          </div>
          <p>{{ dataContact.email.content }}</p>
        </div>

        <div :class="$style['contact__form-info-row']">
          <div :class="$style['contact__form-info-img']">
            <img :src="Location" :class="$style['contact__form-info-img-ic']" />
          </div>
          <p>{{ dataFacility.address }}</p>
        </div>

        <button :class="$style['contact__form-info-button']" @click="handleClick">
          <p>{{ isCLick ? 'Liên hệ nhanh' : 'Liên hệ tư vấn ngay' }}</p>
        </button>
      </div>
    </div>
  </div>
  <div :class="$style.contact__formMB">
    <h1>Liên hệ</h1>

    <div :class="$style['contact__form-wrapper']">
      <div
        :class="$style['contact__form-info']"
        :style="{
          transform: 'translateX' + '(' + move + '%' + ')',
          right: isCLick ? '0' : '-1px'
        }"
      >
        <div :class="$style['contact__form-info-row']">
          <div :class="$style['contact__form-info-img']">
            <img :src="Telephone" :class="$style['contact__form-info-img-ic']" />
          </div>
          <p>{{ dataFacility.phoneNumber }}</p>
        </div>

        <div :class="$style['contact__form-info-row']">
          <div :class="$style['contact__form-info-img']">
            <img :src="Message" :class="$style['contact__form-info-img-ic']" />
          </div>
          <p>{{ dataContact.email.content }}</p>
        </div>

        <div :class="$style['contact__form-info-row']">
          <div :class="$style['contact__form-info-img']">
            <img :src="Location" :class="$style['contact__form-info-img-ic']" />
          </div>
          <p>{{ dataFacility.address }}</p>
        </div>
      </div>

      <div :class="$style['contact__form-button']">
        <span @click="handleSocialForm" :class="[isSocial ? $style.active : '']"
          >Mạng xã hội
          <div
            :class="$style['contact__form-button-line']"
            :style="{
              width: widthLine / 1.5 - (isSocial ? 2 : 0) + 'px',
              transform: 'translateX(' + (!isSocial ? '160%' : '-50%') + ')'
            }"
          ></div>
        </span>
        <span @click="handleSocialForm" :class="[!isSocial ? $style.active : '']">Biểu mẫu</span>
      </div>

      <div :class="$style['contact__form-position']">
        <div
          :class="$style['contact__form-social']"
          :style="{ transform: 'translateX(' + (!isSocial ? '-100%' : '0%') + ')' }"
        >
          <p :class="$style['contact__form-social-title']">Bạn có thể kết nối qua</p>

          <button :class="$style['contact__form-social-button-facebook']">
            <a :href="dataContact.facebook.content" target="_blank">
              <font-awesome-icon
                :icon="faFacebook"
                :class="$style['contact__form-social-button-facebook-ic']"
              />
              <p>Facebook</p>
            </a>
          </button>
          <button :class="$style['contact__form-social-button-zalo']">
            <a :href="dataContact.zalo.content" target="_blank">
              <img :src="Zalo" :class="$style['contact__form-social-button-zalo-ic']" />
              <p>Zalo</p>
            </a>
          </button>
        </div>

        <div
          :class="$style['contact__form-content']"
          :style="{ transform: 'translateX(' + (isSocial ? '-100%' : '0%') + ')' }"
        >
          <div :class="$style['contact__form-content-form']">
            <div>
              <p>Họ và tên<span style="color: red"> *</span></p>
              <input type="text" name="name" :value="name" @input="updateName" />
            </div>

            <div>
              <p>Email<span style="color: red"> *</span></p>
              <input type="email" name="email" :value="email" @input="updateEmail" />
            </div>

            <div>
              <p>Số điện thoại<span style="color: red"> *</span></p>
              <input type="text" name="sdt" :value="phone" @input="updatePhone" maxlength="10" />
            </div>

            <div>
              <p>Nội dung<span style="color: red"> *</span></p>
              <textarea
                name=""
                id=""
                cols="30"
                rows="10"
                :value="content"
                @input="updateContent"
              ></textarea>
            </div>

            <button @click="submitForm">Xác nhận</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ContactForm.module.scss';
</style>

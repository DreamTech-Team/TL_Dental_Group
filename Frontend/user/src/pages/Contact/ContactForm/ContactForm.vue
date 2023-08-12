<script setup lang="ts">
import { ref, toRefs } from 'vue';
import Zalo from '@/assets/imgs/Contact/Zalo.png';
import Telephone from '@/assets/imgs/Contact/Telephone.png';
import Message from '@/assets/imgs/Contact/Message.png';
import Location from '@/assets/imgs/Contact/Location.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faFacebook } from '@fortawesome/free-brands-svg-icons';
import { saveDataContact } from '@/stores/counter';

interface Info {
  address: string;
  hotline: string;
  mapLink: string;
  image: string;
  mapIframe: string;
}

interface Contact {
  email: {
    content: string;
  };
  facebook: {
    content: string;
  };
  zalo: {
    content: string;
  };
}

// const props = defineProps({
//   dataFacility: {
//     type: Object,
//     required: true
//   },
//   dataContact: {
//     type: Object,
//     required: true
//   }
// });

const { dataFacility, dataContact } = toRefs(saveDataContact());

const isCLick = ref(false);
const isSocial = ref(true);
const move = ref(0);
const widthLine = ref(69);
// const dataContactRender = ref<Contact>({ ...(props.dataContact as Contact) });
// const dataFacilityRender = ref<Info>({ ...(props.dataFacility as Info) });

const handleClick = () => {
  isCLick.value = !isCLick.value;

  if (isCLick.value) move.value = -100;
  else move.value = 0;
};

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
            <a :href="dataContact.zalo.content" target="_blank">
              <img :src="Zalo" :class="$style['contact__form-social-button-zalo-ic']" />
              <p>Zalo</p>
            </a>
          </button>
        </div>

        <div :class="$style['contact__form-content']">
          <form action="#">
            <div>
              <p>Họ và tên*</p>
              <input type="text" name="name" />
            </div>

            <div>
              <p>Email*</p>
              <input type="email" name="email" />
            </div>

            <div>
              <p>Số điện thoại*</p>
              <input type="text" name="sdt" />
            </div>

            <div>
              <p>Nội dung*</p>
              <textarea name="" id="" cols="30" rows="10"></textarea>
            </div>

            <button>Xác nhận</button>
          </form>
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
          <p>{{ isCLick ? 'Liên hệ nhanh' : 'Liên hệ qua form' }}</p>
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
          <form action="#">
            <div>
              <p>Họ và tên*</p>
              <input type="text" name="name" />
            </div>

            <div>
              <p>Email*</p>
              <input type="email" name="email" />
            </div>

            <div>
              <p>Số điện thoại*</p>
              <input type="text" name="sdt" />
            </div>

            <div>
              <p>Nội dung*</p>
              <textarea name="" id="" cols="30" rows="10"></textarea>
            </div>

            <button>Xác nhận</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ContactForm.module.scss';
</style>

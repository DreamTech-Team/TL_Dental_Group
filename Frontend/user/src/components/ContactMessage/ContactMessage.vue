<script setup lang="ts">
import { ref, watch } from 'vue';
import MessageV2 from '@/assets/imgs/Contact/MessageV2.png';
import Close from '@/assets/imgs/Contact/Close.png';
import MessengerV2 from '@/assets/imgs/Contact/MessengerV2.png';
import ZaloV2 from '@/assets/imgs/Contact/ZaloV2.png';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface Contact {
  facebook: {
    content: string;
  };
  zalo: {
    content: string;
  };
}

const isOpen = ref(false);
const move1 = ref(0);
const move2 = ref(0);
const variableChange = ref([]);
const dataContact = ref<Contact>({
  facebook: {
    content: ''
  },
  zalo: {
    content: ''
  }
});

const getContact = useAxios<DataResponse>(
  'get',
  '/information?type=CONTACT',
  {},
  {},
  variableChange.value
);

watch(getContact.response, () => {
  dataContact.value = getContact.response?.value?.data;
});

// Hàm xử lí trượt 2 messenger và zalo lên
const handleMessage = () => {
  const circleWidth = document.getElementById('circle');
  const circle1 = document.getElementById('circle1');
  if (circleWidth && circle1) {
    isOpen.value = !isOpen.value;

    if (isOpen.value)
      (move1.value = -circleWidth.offsetHeight),
        (move2.value = -circleWidth.offsetHeight - circle1.offsetHeight - 6);
    else (move1.value = 0), (move2.value = 0);
  }
};
</script>
<template>
  <div :class="$style.contact__message">
    <div :class="$style['contact__message-circle']" id="circle" @click="handleMessage">
      <img :src="isOpen ? Close : MessageV2" alt="" :class="isOpen ? $style.show : ''" />
    </div>

    <div :class="[$style.contact__close]">
      <a :href="dataContact.facebook.content" target="_blank">
        <div
          :class="$style['contact__close-circle-social']"
          id="circle1"
          :style="{ transform: 'translateY' + '(' + move1 + 'px' + ')' }"
        >
          <img :src="MessengerV2" alt="" />
        </div>
      </a>

      <a :href="'https://zalo.me/' + dataContact.zalo.content" target="_blank">
        <div
          :class="$style['contact__close-circle-social']"
          id="circle2"
          :style="{ transform: 'translateY' + '(' + move2 + 'px' + ')', zIndex: '-2' }"
        >
          <img :src="ZaloV2" alt="" />
        </div>
      </a>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ContactMessage.module.scss';
</style>

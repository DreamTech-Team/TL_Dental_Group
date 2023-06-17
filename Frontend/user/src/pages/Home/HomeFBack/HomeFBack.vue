<script setup lang="ts">
import { ref, onMounted, computed, onUnmounted } from 'vue';
import Doctor from '@/assets/imgs/Home/Doctor.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faStar, faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';

const feedbacks = ref([
  {
    title: 'DỤNG CỤ CHỈNH NHA ABC',
    src: Doctor,
    speech:
      // eslint-disable-next-line max-len
      'Cảm ơn TL Dental Group, đội ngũ nhân sự tư vấn rất nhiệt tình, nhờ các bạn mà mình có thêm cái nhìn sâu hơn về khả năng ứng dụng của các vật tư sử dụng trong chỉnh nha.',
    name: 'Bác sĩ Huỳnh Vinh',
    pos: 'Bác sĩ Nha Khoa Bệnh viện A',
    rate: 4
  },
  {
    title: 'DỤNG CỤ CHỈNH NHA ABC',
    src: Doctor,
    speech:
      // eslint-disable-next-line max-len
      'Cảm ơn TL Dental Group, đội ngũ nhân sự tư vấn rất nhiệt tình, nhờ các bạn mà mình có thêm cái nhìn sâu hơn về khả năng ứng dụng của các vật tư sử dụng trong chỉnh nha.',
    name: 'Bác sĩ Huỳnh Vinh',
    pos: 'Bác sĩ Nha Khoa Bệnh viện B',
    rate: 5
  },
  {
    title: 'DỤNG CỤ CHỈNH NHA ABC',
    src: Doctor,
    speech:
      // eslint-disable-next-line max-len
      'Cảm ơn TL Dental Group, đội ngũ nhân sự tư vấn rất nhiệt tình, nhờ các bạn mà mình có thêm cái nhìn sâu hơn về khả năng ứng dụng của các vật tư sử dụng trong chỉnh nha.',
    name: 'Bác sĩ Huỳnh Vinh',
    pos: 'Bác sĩ Nha Khoa Bệnh viện C',
    rate: 2
  },
  {
    title: 'DỤNG CỤ CHỈNH NHA ABC',
    src: Doctor,
    speech:
      // eslint-disable-next-line max-len
      'Cảm ơn TL Dental Group, đội ngũ nhân sự tư vấn rất nhiệt tình, nhờ các bạn mà mình có thêm cái nhìn sâu hơn về khả năng ứng dụng của các vật tư sử dụng trong chỉnh nha.',
    name: 'Bác sĩ Huỳnh Vinh',
    pos: 'Bác sĩ Nha Khoa Bệnh viện D',
    rate: 2
  },
  {
    title: 'DỤNG CỤ CHỈNH NHA ABC',
    src: Doctor,
    speech:
      // eslint-disable-next-line max-len
      'Cảm ơn TL Dental Group, đội ngũ nhân sự tư vấn rất nhiệt tình, nhờ các bạn mà mình có thêm cái nhìn sâu hơn về khả năng ứng dụng của các vật tư sử dụng trong chỉnh nha.',
    name: 'Bác sĩ Huỳnh Vinh',
    pos: 'Bác sĩ Nha Khoa Bệnh viện E',
    rate: 2
  }
]);

const wItem = ref(0);
const tranfX = ref(0);
let resizeListener: () => void;

const widthComputed = computed(() => {
  return wItem.value * feedbacks.value.length + 'px';
});

const widthItemComputed = computed(() => {
  return wItem.value + 'px';
});

const scrollLeft = () => {
  if (tranfX.value < 0) tranfX.value += wItem.value;
};

const scrollRight = () => {
  if (-tranfX.value + wItem.value * 3 < wItem.value * feedbacks.value.length) {
    tranfX.value -= wItem.value;
  } else {
    tranfX.value = 0;
  }
};

onMounted(() => {
  const container = document.getElementById('feedback-wrapper');
  if (container) {
    wItem.value = container.offsetWidth / 3;
  }

  resizeListener = function () {
    const container = document.getElementById('feedback-wrapper');
    if (container) {
      wItem.value = container.offsetWidth / 3;
      tranfX.value = 0;
    }
  };

  window.addEventListener('resize', resizeListener);
});

onUnmounted(() => {
  window.removeEventListener('resize', resizeListener);
});
</script>

<template>
  <div :class="$style.home__feedback">
    <h3>ĐÁNH GIÁ CỦA KHÁCH HÀNG</h3>
    <button>XEM TẤT CẢ</button>
    <div :class="$style['home__feedback-ctn']">
      <div :class="$style['home__feedback-wrapper']" id="feedback-wrapper">
        <div
          :class="$style['home__feedback-list']"
          id="feedback-list"
          :style="{ width: widthComputed, transform: 'translateX(' + tranfX + 'px)' }"
        >
          <div
            v-for="feedback in feedbacks"
            :key="feedback.name"
            :class="$style['home__feedback-item']"
            :style="{ width: widthItemComputed }"
          >
            <div :class="$style['home__feedback-img']">
              <img :src="feedback.src" alt="doctor" />
            </div>
            <div :class="$style['home__feedback-speech']">
              {{ feedback.speech }}
            </div>
            <div :class="$style['home__feedback-infor']">
              <strong>{{ feedback.name }}</strong>
              <span>{{ feedback.pos }}</span>
              <div :class="$style['home__feedback-rate']">
                <font-awesome-icon
                  v-for="i in 5"
                  :key="i"
                  :icon="faStar"
                  :class="[
                    $style['home__feedback-star'],
                    i <= feedback.rate ? $style['star-active'] : ''
                  ]"
                />
              </div>
            </div>
            <div :class="$style['home__feedback-icon']"></div>
          </div>
        </div>
        <button :class="$style['home__feedback-left']" @click="scrollLeft">
          <font-awesome-icon :icon="faChevronLeft" :class="$style['home__feedback-ic']" />
        </button>
        <button :class="$style['home__feedback-right']" @click="scrollRight">
          <font-awesome-icon :icon="faChevronRight" :class="$style['home__feedback-ic']" />
        </button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../HomePage.module.scss';
</style>

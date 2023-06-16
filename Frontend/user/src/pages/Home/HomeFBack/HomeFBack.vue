<template>
  <div :class="$style.home__feedback">
    <h3>ĐÁNH GIÁ CỦA KHÁCH HÀNG</h3>
    <button>XEM TẤT CẢ</button>
    <div :class="$style['home__feedback-wrapper']">
      <div :class="$style['home__feedback-list']" id="feedback-list">
        <div
          v-for="feedback in feedbacks"
          :key="feedback.name"
          :class="$style['home__feedback-item']"
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
</template>

<script setup lang="ts">
import { ref } from 'vue';
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

function scrollLeft(): void {
  const container = document.getElementById('feedback-list');
  if (container) {
    container.scrollLeft -= container.offsetWidth;
  }
}

function scrollRight(): void {
  const container = document.getElementById('feedback-list');
  if (container) {
    const scrollAmount = container.offsetWidth;
    container.scrollLeft += scrollAmount;

    // Test if end of list
    if (container.scrollLeft + container.offsetWidth >= container.scrollWidth - 100) {
      container.scrollLeft = 0; // Reverse Scroll
    }
  }
}
</script>

<style module scoped lang="scss">
@import '../HomePage.module.scss';
</style>

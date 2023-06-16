<script setup lang="ts">
import Doctor from '@/assets/imgs/Home/Doctor.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faStar, faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
</script>
<template>
  <div :class="$style.home__feedback">
    <h3>ĐÁNH GIÁ CỦA KHÁCH HÀNG</h3>
    <button>XEM TẤT CẢ</button>
    <div :class="$style['home__feedback-list']">
      <div :class="$style['home__feedback-item']">
        <div :class="$style['home__feedback-img']">
          <img :src="Doctor" alt="doctor" />
        </div>
        <div :class="$style['home__feedback-speech']">
          Cảm ơn TL Dental Group, đội ngũ nhân sự tư vấn rất nhiệt tình, nhờ các bạn mà mình có thêm
          cái nhìn sâu hơn về khả năng ứng dụng của các vật tư sử dụng trong chỉnh nha.
        </div>
        <div :class="$style['home__feedback-infor']">
          <strong>Bác sĩ Huỳnh Vinh</strong>
          <span>Bác sĩ nha khoa bệnh viện A</span>
          <div :class="$style['home__feedback-rate']">
            <font-awesome-icon
              :icon="faStar"
              :class="[$style['home__feedback-star'], $style['star-active']]"
            />
            <font-awesome-icon
              :icon="faStar"
              :class="[$style['home__feedback-star'], $style['star-active']]"
            />
            <font-awesome-icon
              :icon="faStar"
              :class="[$style['home__feedback-star'], $style['star-active']]"
            />
            <font-awesome-icon :icon="faStar" :class="$style['home__feedback-star']" />
            <font-awesome-icon :icon="faStar" :class="$style['home__feedback-star']" />
          </div>
        </div>
        <div :class="$style['home__feedback-icon']"></div>
      </div>
      <div :class="$style['home__feedback-item']">
        <div :class="$style['home__feedback-img']">
          <img :src="Doctor" alt="doctor" />
        </div>
        <div :class="$style['home__feedback-speech']">
          Cảm ơn TL Dental Group, đội ngũ nhân sự tư vấn rất nhiệt tình, nhờ các bạn mà mình có thêm
          cái nhìn sâu hơn về khả năng ứng dụng của các vật tư sử dụng trong chỉnh nha.
        </div>
        <div :class="$style['home__feedback-infor']">
          <strong>Bác sĩ Huỳnh Vinh</strong>
          <span>Bác sĩ nha khoa bệnh viện A</span>
          <div :class="$style['home__feedback-rate']">
            <font-awesome-icon
              :icon="faStar"
              :class="[$style['home__feedback-star'], $style['star-active']]"
            />
            <font-awesome-icon
              :icon="faStar"
              :class="[$style['home__feedback-star'], $style['star-active']]"
            />
            <font-awesome-icon
              :icon="faStar"
              :class="[$style['home__feedback-star'], $style['star-active']]"
            />
            <font-awesome-icon :icon="faStar" :class="$style['home__feedback-star']" />
            <font-awesome-icon :icon="faStar" :class="$style['home__feedback-star']" />
          </div>
        </div>
        <div :class="$style['home__feedback-icon']"></div>
      </div>
      <div :class="$style['home__feedback-item']">
        <div :class="$style['home__feedback-img']">
          <img :src="Doctor" alt="doctor" />
        </div>
        <div :class="$style['home__feedback-speech']">
          Cảm ơn TL Dental Group, đội ngũ nhân sự tư vấn rất nhiệt tình, nhờ các bạn mà mình có thêm
          cái nhìn sâu hơn về khả năng ứng dụng của các vật tư sử dụng trong chỉnh nha.
        </div>
        <div :class="$style['home__feedback-infor']">
          <strong>Bác sĩ Huỳnh Vinh</strong>
          <span>Bác sĩ nha khoa bệnh viện A</span>
          <div :class="$style['home__feedback-rate']">
            <font-awesome-icon
              :icon="faStar"
              :class="[$style['home__feedback-star'], $style['star-active']]"
            />
            <font-awesome-icon
              :icon="faStar"
              :class="[$style['home__feedback-star'], $style['star-active']]"
            />
            <font-awesome-icon
              :icon="faStar"
              :class="[$style['home__feedback-star'], $style['star-active']]"
            />
            <font-awesome-icon :icon="faStar" :class="$style['home__feedback-star']" />
            <font-awesome-icon :icon="faStar" :class="$style['home__feedback-star']" />
          </div>
        </div>
        <div :class="$style['home__feedback-icon']"></div>
      </div>
      <button :class="$style['home__feedback-left']">
        <font-awesome-icon :icon="faChevronLeft" :class="$style['home__feedback-ic']" />
      </button>
      <button :class="$style['home__feedback-right']">
        <font-awesome-icon :icon="faChevronRight" :class="$style['home__feedback-ic']" />
      </button>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../HomePage.module.scss';
</style>

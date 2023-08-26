<script setup lang="ts">
import { ref, onMounted, computed, onUnmounted, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faStar, faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

//Init data structure
const feedbacks = ref([
  {
    title: 'DỤNG CỤ CHỈNH NHA ABC',
    image: '',
    content: '',
    fullname: 'Bác sĩ Huỳnh Vinh',
    position: 'Bác sĩ Nha Khoa Bệnh viện A',
    rating: 4
  }
]);

//Scroll Properties
const isPhone = ref(false);
const isTablet = ref(false);
const MIN_SWIPE_DISTANCE_CM = 3;
const TOUCH_SENSITIVITY = 25;
const touchstartX = ref(0);
const touchendX = ref(0);

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

//Handle scroll list
const checkDirection = () => {
  const distanceX = Math.abs(touchendX.value - touchstartX.value);
  const distanceInCm = distanceX / TOUCH_SENSITIVITY;

  if (distanceInCm >= MIN_SWIPE_DISTANCE_CM) {
    if (touchendX.value < touchstartX.value) {
      scrollRight();
    }
    if (touchstartX.value < touchendX.value) {
      scrollLeft();
    }
  }
};

const handleTouchstart = (e: TouchEvent) => {
  touchstartX.value = e.changedTouches[0].screenX;
};

const handleTouchend = (e: TouchEvent) => {
  touchendX.value = e.changedTouches[0].screenX;
  checkDirection();
};

//Get reviews
const deps = ref([]);
const lenght = ref(0);
const { response } = useAxios<DataResponse>('get', '/home/reviews', {}, {}, deps.value);

watch(response, () => {
  feedbacks.value = response.value?.data;
  lenght.value = feedbacks.value.length;
});

//Function 4.2 to 4.0
const roundNumber = (number: number, decimalPlaces: number) => {
  const factor = Math.pow(10, decimalPlaces);
  return Math.round(number * factor) / factor;
};

onMounted(() => {
  const container = document.getElementById('feedback-wrapper');
  if (container) {
    if (window.innerWidth < 739) {
      isPhone.value = true;
      wItem.value = container.offsetWidth;
    } else if (window.innerWidth >= 739 && window.innerWidth <= 1024) {
      wItem.value = container.offsetWidth / 2;
      isTablet.value = true;
    } else {
      wItem.value = container.offsetWidth / 3;
    }
  }

  resizeListener = function () {
    const container = document.getElementById('feedback-wrapper');
    if (container) {
      if (window.innerWidth < 739) {
        isPhone.value = true;
        wItem.value = container.offsetWidth;
      } else if (window.innerWidth >= 739 && window.innerWidth <= 1024) {
        wItem.value = container.offsetWidth / 2;
        isTablet.value = true;
      } else {
        wItem.value = container.offsetWidth / 3;
      }
      tranfX.value = 0;
    }
  };

  if (window.innerWidth >= 739) {
    window.addEventListener('resize', resizeListener);
  }
});

onUnmounted(() => {
  if (window.innerWidth >= 739) {
    window.removeEventListener('resize', resizeListener);
  }
});
</script>

<template>
  <div :class="$style.home__feedback">
    <h3>ĐÁNH GIÁ CỦA KHÁCH HÀNG</h3>
    <div :class="$style['home__feedback-ctn']">
      <div :class="$style['home__feedback-wrapper']" id="feedback-wrapper">
        <div
          :class="$style['home__feedback-list']"
          id="feedback-list"
          :style="{ width: widthComputed, transform: 'translateX(' + tranfX + 'px)' }"
          @touchstart="handleTouchstart"
          @touchend="handleTouchend"
        >
          <div
            v-for="(feedback, index) in feedbacks"
            :key="index"
            :class="$style['home__feedback-item']"
            :style="{ width: widthItemComputed }"
          >
            <div :class="$style['home__feedback-img']">
              <img :src="feedback.image" alt="doctor" />
            </div>
            <div :class="$style['home__feedback-speech']">
              {{ feedback.content }}
            </div>
            <div :class="$style['home__feedback-infor']">
              <strong>{{ feedback.fullname }}</strong>
              <span>{{ feedback.position }}</span>
              <div :class="$style['home__feedback-rate']">
                <font-awesome-icon
                  v-for="i in 5"
                  :key="i"
                  :icon="faStar"
                  :class="[
                    $style['home__feedback-star'],
                    i <= roundNumber(feedback.rating, 0) ? $style['star-active'] : ''
                  ]"
                />
              </div>
            </div>
            <div :class="$style['home__feedback-icon']"></div>
          </div>
        </div>
        <button v-show="tranfX !== 0" :class="$style['home__feedback-left']" @click="scrollLeft">
          <font-awesome-icon :icon="faChevronLeft" :class="$style['home__feedback-ic']" />
        </button>
        <button
          v-show="
            (!isPhone && !isTablet && lenght > 3) ||
            (isPhone && lenght > 1) ||
            (isTablet && lenght > 2)
          "
          :class="$style['home__feedback-right']"
          @click="scrollRight"
        >
          <font-awesome-icon :icon="faChevronRight" :class="$style['home__feedback-ic']" />
        </button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './HomeFBack.module.scss';
</style>

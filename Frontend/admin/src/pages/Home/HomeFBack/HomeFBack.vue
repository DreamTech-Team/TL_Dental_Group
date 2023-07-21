<script setup lang="ts">
import { ref, watch, onMounted, computed, onUnmounted } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faStar, faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import ModalFBack from './ModalFBack.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

//GET DATA
const deps = ref([]);
const lenght = ref(0);
const { response } = useAxios<DataResponse>('get', '/home/reviews', {}, {}, deps.value);

interface ItemRS {
  id: string;
  image: string;
  content: string;
  fullname: string;
  position: string;
  rating: number;
}

const content = ref<ItemRS[]>([
  {
    id: '',
    image: '',
    content: '',
    fullname: 'Bác sĩ Huỳnh Vinh',
    position: 'Bác sĩ Nha Khoa Bệnh viện A',
    rating: 4
  }
]);

const onUpdateContent = (data: { listrs: ItemRS[] }) => {
  content.value = data.listrs;
};

watch(response, () => {
  content.value = response.value?.data;
  lenght.value = content.value.length;
});

const roundNumber = (number: number, decimalPlaces: number) => {
  const factor = Math.pow(10, decimalPlaces);
  return Math.round(number * factor) / factor;
};

//Logic
const isOpen = ref(false);

const wItem = ref(0);
const tranfX = ref(0);
let resizeListener: () => void;

const widthComputed = computed(() => {
  return wItem.value * content.value.length + 'px';
});

const widthItemComputed = computed(() => {
  return wItem.value + 'px';
});

const scrollLeft = () => {
  if (tranfX.value < 0) tranfX.value += wItem.value;
};

const scrollRight = () => {
  if (-tranfX.value + wItem.value * 3 < wItem.value * content.value.length) {
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
    <div :class="$style['home__feedback-ctn']">
      <div :class="$style['home__feedback-wrapper']" id="feedback-wrapper">
        <div
          :class="$style['home__feedback-list']"
          id="feedback-list"
          :style="{ width: widthComputed, transform: 'translateX(' + tranfX + 'px)' }"
        >
          <div
            v-for="(feedback, index) in content"
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
        <button v-show="lenght > 3" :class="$style['home__feedback-right']" @click="scrollRight">
          <font-awesome-icon :icon="faChevronRight" :class="$style['home__feedback-ic']" />
        </button>
      </div>
    </div>
    <EditBtn style="top: -15px" @click="isOpen = true" />
  </div>
  <ModalFBack
    v-if="isOpen"
    @close="isOpen = false"
    @update-content="onUpdateContent"
    :list-item="content"
  />
</template>

<style module scoped lang="scss">
@import './HomeFBack.module.scss';
</style>

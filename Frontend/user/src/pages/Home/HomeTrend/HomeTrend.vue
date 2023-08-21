<script setup lang="ts">
import { ref, onMounted, computed, onUnmounted, watch } from 'vue';
import ProductCard from '@/components/Card/ProductCard.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface Product {
  id: string;
  name: string;
  slug: string;
  price: number;
  priceSale: number;
  summary: string;
  description: string;
  mainImg: string;
  imgs: string;
  highlight: number;
  createAt: string;
  fkCategory: {
    id: string;
    companyId: {
      id: string;
      name: string;
      logo: string;
      description: string;
      highlight: number;
      slug: string;
      createAt: string;
      outstandingProductId: string;
    };
    cate1Id: {
      id: string;
      title: string;
      img: string;
      highlight: 3;
      slug: string;
      createAt: string;
    };
    cate2Id: {
      id: string;
      title: string;
      slug: string;
      createAt: string;
    };
  };
}

interface Item {
  nameProduct: string;
  price: number;
  summary: string;
  tag: string;
  company: string;
  image: string;
  brand: string;
  slug: string;
}

//Init data structure
const tempproducts = ref([]);
const products = ref<Item[]>([]);

//Properties
const isPhone = ref(false);
const isTablet = ref(false);
const MIN_SWIPE_DISTANCE_CM = 3;
const TOUCH_SENSITIVITY = 20;
const touchstartX = ref(0);
const touchendX = ref(0);

const wItem = ref(0);
const tranfX = ref(0);
let resizeListener: () => void;

const widthComputed = computed(() => {
  return wItem.value * products.value.length + 'px';
});

const widthItemComputed = computed(() => {
  return wItem.value + 'px';
});

const scrollLeft = () => {
  if (tranfX.value < 0) tranfX.value += wItem.value;
};

const scrollRight = () => {
  if (window.innerWidth < 739) {
    if (-tranfX.value + wItem.value * 2 < wItem.value * products.value.length) {
      tranfX.value -= wItem.value;
    } else {
      tranfX.value = 0;
    }
  } else if (window.innerWidth >= 739 && window.innerWidth <= 1024) {
    if (-tranfX.value + wItem.value * 3 < wItem.value * products.value.length) {
      tranfX.value -= wItem.value;
    } else {
      tranfX.value = 0;
    }
  } else {
    if (-tranfX.value + wItem.value * 4 < wItem.value * products.value.length) {
      tranfX.value -= wItem.value;
    } else {
      tranfX.value = 0;
    }
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

//Get product highlight
const deps = ref([]);
const lenght = ref(0);
const { response } = useAxios<DataResponse>('get', '/products/highlight', {}, {}, deps.value);

const updateShowResults = () => {
  products.value = tempproducts.value.map((item: Product) => {
    return {
      nameProduct: item.name,
      price: item.price,
      summary: item.summary,
      tag: item.fkCategory.cate1Id.title,
      company: item.fkCategory.companyId.name,
      image: item.mainImg,
      brand: item.fkCategory.companyId.logo,
      slug: item.slug
    };
  });
  lenght.value = products.value.length;
};

watch(response, () => {
  tempproducts.value = response.value?.data.sort(
    (a: Product, b: Product) => a.highlight - b.highlight
  );
  updateShowResults();
});

onMounted(() => {
  const container = document.getElementById('trend-wrapper');
  if (container) {
    if (window.innerWidth < 739) {
      wItem.value = container.offsetWidth / 2;
      isPhone.value = true;
    } else if (window.innerWidth >= 739 && window.innerWidth <= 1024) {
      wItem.value = container.offsetWidth / 3;
      isTablet.value = true;
    } else {
      wItem.value = container.offsetWidth / 4;
    }
  }

  resizeListener = function () {
    const container = document.getElementById('trend-wrapper');
    if (container) {
      if (window.innerWidth < 739) {
        wItem.value = container.offsetWidth / 2;
        isPhone.value = true;
      } else if (window.innerWidth >= 739 && window.innerWidth <= 1024) {
        wItem.value = container.offsetWidth / 3;
        isTablet.value = true;
      } else {
        wItem.value = container.offsetWidth / 4;
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
  <div :class="$style.home__trend">
    <div :class="$style['home__trend-title']">
      <h3>SẢN PHẨM BÁN CHẠY</h3>
      <div :class="$style['home__trend-line']"></div>
    </div>
    <div :class="$style['home__trend-ctn']">
      <div :class="$style['home__trend-wrapper']" id="trend-wrapper">
        <div
          :class="$style['home__trend-list']"
          :style="{ width: widthComputed, transform: 'translateX(' + tranfX + 'px)' }"
          @touchstart="handleTouchstart"
          @touchend="handleTouchend"
        >
          <product-card
            v-for="(item, index) in products"
            :key="index"
            :product="item"
            :class="$style['home__trend-item']"
            :style="{ width: widthItemComputed, 'max-width': widthItemComputed }"
          />
        </div>
      </div>
      <button v-show="tranfX != 0" :class="$style['home__trend-left']" @click="scrollLeft">
        <font-awesome-icon :icon="faChevronLeft" :class="$style['home__trend-ic']" />
      </button>
      <button
        v-show="
          (!isPhone && !isTablet && lenght > 4) ||
          (isPhone && lenght > 2) ||
          (isTablet && lenght > 3)
        "
        :class="$style['home__trend-right']"
        @click="scrollRight"
      >
        <font-awesome-icon :icon="faChevronRight" :class="$style['home__trend-ic']" />
      </button>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './HomeTrend.module.scss';
</style>

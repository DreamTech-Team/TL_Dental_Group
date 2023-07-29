<script setup lang="ts">
import { ref, watch, onMounted, computed, onUnmounted } from 'vue';
import ProductCard from '@/components/Card/ProductCard.vue';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import ModalTrend from './ModalTrend.vue';
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
}
const tempproducts = ref<Product[]>([]);
const products = ref<Item[]>([]);

//GET DATA
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
      brand: item.fkCategory.companyId.logo
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

//DATA UPDATE
const onUpdateContent = (data: { listrs: Product[] }) => {
  tempproducts.value = data.listrs;
  updateShowResults();
};

const isOpen = ref(false);

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
  if (-tranfX.value + wItem.value * 4 < wItem.value * products.value.length) {
    tranfX.value -= wItem.value;
  } else {
    tranfX.value = 0;
  }
};

onMounted(() => {
  const container = document.getElementById('trend-wrapper');
  if (container) {
    wItem.value = container.offsetWidth / 4;
  }

  resizeListener = function () {
    const container = document.getElementById('trend-wrapper');
    if (container) {
      wItem.value = container.offsetWidth / 4;
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
      <button v-show="lenght > 4" :class="$style['home__trend-right']" @click="scrollRight">
        <font-awesome-icon :icon="faChevronRight" :class="$style['home__trend-ic']" />
      </button>
    </div>
    <EditBtn style="top: 30px" @click="isOpen = true" />
  </div>
  <ModalTrend
    v-if="isOpen"
    @close="isOpen = false"
    :list-item="tempproducts"
    @update-content="onUpdateContent"
  />
</template>

<style module scoped lang="scss">
@import './HomeTrend.module.scss';
</style>

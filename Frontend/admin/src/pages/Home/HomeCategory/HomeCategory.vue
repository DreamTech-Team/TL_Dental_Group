<script setup lang="ts">
import { ref, onMounted, computed, onUnmounted, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import ModalCategory from './ModalCategory.vue';

interface ItemRS {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
  createAt: string;
}

//Data colors
const colors = [
  // eslint-disable-next-line max-len
  `linear-gradient(143.33deg, #A300F0 24.48%, rgba(246, 76, 218, 0.547363) 78.16%, rgba(173, 0, 255, 0.74) 103.49%)`,
  // eslint-disable-next-line max-len
  `linear-gradient(119.43deg, #0168C8 28.13%, rgba(69, 108, 245, 0.547363) 80.05%, rgba(0, 194, 255, 0.74) 104.55%)`,
  // eslint-disable-next-line max-len
  `linear-gradient(148.87deg, #0085FF 26.28%, rgba(0, 200, 188, 0.547363) 78.26%, rgba(5, 223, 92, 0.74) 126.07%)`,
  // eslint-disable-next-line max-len
  `linear-gradient(143.33deg, #0168C8 24.48%, rgba(246, 76, 218, 0.547363) 78.16%, rgba(173, 0, 255, 0.74) 103.49%)`
];

const isOpen = ref(false); //Modal status
const categories = ref<ItemRS[]>([]);

//Properties
const wItem = ref(0);
const tranfX = ref(0);
let resizeListener: () => void;

//GET DATA
const deps = ref([]);
const lenght = ref(0);
const { response } = useAxios<DataResponse>('get', '/cate1?highlight=true', {}, {}, deps.value);

//Get categories width highlight sorted
watch(response, () => {
  categories.value = response.value?.data.sort((a: ItemRS, b: ItemRS) => a.highlight - b.highlight);
  lenght.value = categories.value.length;
});

//handle emit from modal
const onUpdateContent = (data: { listrs: ItemRS[] }) => {
  categories.value = data.listrs;
};

//Calculate width list
const widthComputed = computed(() => {
  return wItem.value * categories.value.length + 'px';
});

//Calculate width item
const widthItemComputed = computed(() => {
  return wItem.value + 'px';
});

const scrollLeft = () => {
  if (tranfX.value < 0) tranfX.value += wItem.value;
};

const scrollRight = () => {
  if (-tranfX.value + wItem.value * 4 < wItem.value * categories.value.length) {
    tranfX.value -= wItem.value;
  } else {
    tranfX.value = 0;
  }
};

//Get color for each category
const getCategoryColor = (index: number) => {
  return colors[index % colors.length];
};

onMounted(() => {
  const container = document.getElementById('category-wrapper');
  if (container) {
    wItem.value = container.offsetWidth / 4;
  }

  resizeListener = function () {
    const container = document.getElementById('category-wrapper');
    if (container) {
      wItem.value = container.offsetWidth / 4;
      tranfX.value = 0;
    }
  };

  if (window.innerWidth >= 739) {
    window.addEventListener('resize', resizeListener);
  }
});

onUnmounted(() => {
  window.removeEventListener('resize', resizeListener);
});
</script>

<template>
  <div :class="$style.home__category">
    <h3>DANH MỤC NỔI BẬT</h3>
    <div :class="$style['home__category-ctn']">
      <div :class="$style['home__category-wrapper']" id="category-wrapper">
        <div
          :class="$style['home__category-list']"
          id="category-list"
          :style="{ width: widthComputed, transform: 'translateX(' + tranfX + 'px)' }"
        >
          <div
            v-for="(item, index) in categories"
            :key="index"
            :class="$style['home__category-item']"
            :style="{ background: getCategoryColor(index), width: widthItemComputed }"
          >
            <p>{{ item.title }}</p>
            <div :class="$style['home__category-ctn']">
              <div :class="$style['home__category-img']">
                <img :src="item.img" :alt="item.title" />
              </div>
            </div>
          </div>
        </div>
        <button v-show="tranfX !== 0" :class="$style['home__category-left']" @click="scrollLeft">
          <font-awesome-icon :icon="faChevronLeft" :class="$style['home__category-ic']" />
        </button>
        <button v-show="lenght > 4" :class="$style['home__category-right']" @click="scrollRight">
          <font-awesome-icon :icon="faChevronRight" :class="$style['home__category-ic']" />
        </button>
      </div>
    </div>
    <EditBtn style="top: -10px" @click="isOpen = true" />
  </div>
  <ModalCategory
    v-if="isOpen"
    @close="isOpen = false"
    :list-item="categories"
    @update-content="onUpdateContent"
  />
</template>

<style module scoped lang="scss">
@import './HomeCategory.module.scss';
</style>

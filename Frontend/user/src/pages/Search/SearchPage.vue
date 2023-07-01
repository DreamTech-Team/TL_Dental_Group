<script setup lang="ts">
import IcSortDown from '@/assets/icons/IcSortDown.svg';
import { ref, onMounted, computed, onUnmounted } from 'vue';
import { products } from '../Product/ProductHandle';
import { bestsale } from '../Search/BestSale';
import ProductCard from '@/components/Card/ProductCard.vue';
import MobileCard from '@/components/MBCard/MobileCard.vue';
import HomeTrend from '../Home/HomeTrend/HomeTrend.vue';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import SimilarProduct from '@/components/SimilarProduct/SimilarProduct.vue';
import BaseCategory from '@/components/Category/BaseCategory.vue';
import BasePagination from '@/components/Pagination/BasePagination.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import NotFounds from '@/assets/imgs/Product/notfound.svg';
import styles from '../Search/SearchPage.module.scss';
import {
  faChevronLeft,
  faChevronRight,
  faArrowDownShortWide
} from '@fortawesome/free-solid-svg-icons';

const wItem = ref(0);
const tranfX = ref(0);
let resizeListener: () => void;
const pathBC = 'timkiem';
const currentPage = ref(1);
const pageSize = ref(12);
const isDesktop = ref(true);
//Scroll Properties
const sortTypeClasses = ref(styles['sort__type']);
const sortContentClasses = ref(styles['sort__content']);
const dropdownListClasses = ref(styles['dropdown-list']);
const dropdownItemClasses = ref(styles['dropdown-item']);

const scrollToTop = (top: number) => {
  window.scrollTo({
    top: top,
    behavior: 'smooth' // Tạo hiệu ứng cuộn mượt
  });
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  if (window.innerWidth < 739) {
    isDesktop.value = false;
    scrollToTop(0);
  } else {
    scrollToTop(400);
  }
};

// const checkScreenSize = () => {
//   if (window.innerWidth < 739) {
//     isDesktop.value = false;
//   } else {
//     isDesktop.value = true;
//   }
// };

// const widthComputed = computed(() => {
//   return wItem.value * bestsale.length + 'px';
// });

// const widthItemComputed = computed(() => {
//   return wItem.value + 'px';
// });

// const scrollLeft = () => {
//   if (tranfX.value < 0) tranfX.value += wItem.value;
// };

// const scrollRight = () => {
//   if (-tranfX.value + wItem.value * 4 < wItem.value * products.length) {
//     tranfX.value -= wItem.value;
//   } else {
//     tranfX.value = 0;
//   }
// };

// Define reactive properties
const isDropdownOpen = ref(false);

// Define methods
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

const closeDropdown = () => {
  isDropdownOpen.value = false;
};

const selectedOption = ref('Mới nhất');
const dropdownOptions = ['Mới nhất', 'Giá tăng dần', 'Giá giảm dần'];

const selectOption = (option: string) => {
  selectedOption.value = option;
  isDropdownOpen.value = false;
};

const displayedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return products.slice(start, end);
});

onMounted(() => {
  const container = document.getElementById('trend-wrapper');

  if (container) {
    if (window.innerWidth < 739) {
      wItem.value = container.offsetWidth / 2;
    } else {
      wItem.value = container.offsetWidth / 4;
    }
  }

  resizeListener = function () {
    if (window.innerWidth < 739) {
      isDesktop.value = false;
    } else {
      isDesktop.value = true;
    }
    const container = document.getElementById('trend-wrapper');
    if (container) {
      if (window.innerWidth < 739) {
        wItem.value = container.offsetWidth / 2;
      } else {
        wItem.value = container.offsetWidth / 4;
      }

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
  <div>
    <div>
      <div v-if="products.length > 0">
        <bread-crumb :tags="pathBC" />
        <div v-if="isDesktop" :class="$style.sort">
          <p :class="$style['sort__info']">
            Tìm thấy <strong>{{ products.length }}</strong> kết quả với từ khóa là
            <strong>“kềm”</strong>
          </p>
          <div></div>
          <div :class="sortTypeClasses" @click="toggleDropdown">
            <p>{{ selectedOption }}</p>
            <img :src="IcSortDown" alt="sort down" />
          </div>

          <div v-if="isDropdownOpen" @click="closeDropdown" :class="sortContentClasses">
            <ul :class="dropdownListClasses">
              <li
                :class="dropdownItemClasses"
                v-for="(option, index) in dropdownOptions"
                :key="index"
                @click="selectOption(option)"
              >
                {{ option }}
              </li>
            </ul>
          </div>
        </div>
        <div v-else :class="$style.sorts">
          <p :class="$style['sorts__info']">
            Tìm thấy <strong>{{ products.length }}</strong> kết quả với từ khóa là
            <strong>“kềm”</strong>
          </p>
          <div :class="$style['mbsort']">
            <div :class="$style['mbsort__type']">
              <font-awesome-icon
                :class="$style['mbsort__type--icon']"
                :icon="faArrowDownShortWide"
              />
              <p :class="$style['mbsort__type--text']">Xếp theo</p>
            </div>
          </div>
        </div>

        <div v-if="isDesktop" :class="$style['product__container']">
          <product-card v-for="(item, index) in displayedProducts" :key="index" :product="item" />
        </div>
        <div v-else :class="$style['product__mbcontainer']">
          <mobile-card
            v-for="(item1, index1) in displayedProducts"
            :key="index1"
            :product="item1"
          />
        </div>
        <div>
          <base-pagination
            :total="Math.ceil(products.length / pageSize)"
            :current-page="currentPage"
            :page-size="pageSize"
            @current-change="handlePageChange"
          />
        </div>
      </div>
      <div v-else>
        <!-- Thẻ thể hiện giá trị rỗng -->
        <div :class="$style.found">
          <div :class="$style['found__content']">
            <img :class="$style['found__content--image']" :src="NotFounds" alt="not found" />
            <p :class="$style['found__content--text']">
              Rất tiếc chúng tôi không tìm thấy kết quả của <strong>“kềm”</strong>
            </p>
            <div :class="$style['found__content--box']">
              <p><strong>Để tìm được kết quả chính xác hơn, xin vui lòng: </strong></p>
              <ul>
                <li>Thử lại bằng từ khóa khác</li>
                <li>Thử lại bằng những từ khóa tổng quát hơn</li>
                <li>Thử lại bằng những từ khóa ngắn gọn hơn</li>
                <li>Kiểm tra lỗi chính tả của từ khóa đã nhập</li>
              </ul>
            </div>
          </div>
          <!-- <product-category :class="$style['found__cate']" /> -->
          <base-category :class="$style['found__cate']" />
        </div>
      </div>
    </div>
    <div>
      <similar-product />
    </div>
  </div>
</template>

<style scoped module lang="scss">
@import '../Search/SearchPage.module.scss';
</style>

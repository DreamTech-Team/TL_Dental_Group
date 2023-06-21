<script setup lang="ts">
import IcSortDown from '@/assets/icons/IcSortDown.svg';
import { ref, onMounted, computed, onUnmounted } from 'vue';
import { products } from '../Product/ProductHandle';
import { bestsale } from '../Search/BestSale';
import ProductCard from '../Product/ProductCard/ProductCard.vue';
// import ProductCard from '@/components/Card/ProductCard.vue';
import ProductCategory from '../Product/ProductCategory/ProductCategory.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import NotFounds from '@/assets/imgs/Product/notfound.svg';
import styles from '../Search/SearchPage.module.scss';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';

const wItem = ref(0);
const tranfX = ref(0);
let resizeListener: () => void;

const widthComputed = computed(() => {
  return wItem.value * products.length + 'px';
});

const widthItemComputed = computed(() => {
  return wItem.value + 'px';
});

const scrollLeft = () => {
  if (tranfX.value < 0) tranfX.value += wItem.value;
};

const scrollRight = () => {
  if (-tranfX.value + wItem.value * 4 < wItem.value * products.length) {
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

const sortTypeClasses = ref(styles['sort__type']);
const sortContentClasses = ref(styles['sort__content']);
const dropdownListClasses = ref(styles['dropdown-list']);
const dropdownItemClasses = ref(styles['dropdown-item']);

// Expose reactive properties and methods
defineExpose({
  isDropdownOpen,
  toggleDropdown,
  closeDropdown
});
</script>
<template>
  <div>
    <div v-if="products.length > 0">
      <div :class="$style.sort">
        <p :class="$style['sort__info']">
          Tìm thấy <strong>100</strong> kết quả với từ khóa là <strong>“kềm”</strong>
        </p>
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
      <div :class="$style['product__container']">
        <product-card v-for="(item, index) in products" :key="index" :product="item" />
      </div>
    </div>
    <div v-else>
      <!-- Thẻ thể hiện giá trị rỗng -->
      <div :class="$style.found">
        <div :class="$style['found__content']">
          <img :class="$style['found__content--image']" :src="NotFounds" alt="not found" />
          <p :class="$style['found__content--text']">
            Rất tiếc chúng tôi không tìm thấy kết quả của “kềm”
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
        <product-category :class="$style['found__cate']" />
      </div>
    </div>

    <div :class="$style.trend">
      <div :class="$style['trend__title']">
        <div :class="$style['trend__title--linef']"></div>
        <h3>SẢN PHẨM NỔI BẬT</h3>
        <div :class="$style['trend__title--line']"></div>
      </div>
      <div :class="$style['trend__title--ctn']">
        <div :class="$style['trend__title--ctn-wrapper']" id="trend-wrapper">
          <div
            :class="$style['home__trend-list']"
            :style="{ width: widthComputed, transform: 'translateX(' + tranfX + 'px)' }"
          >
            <product-card
              v-for="(item, index) in bestsale"
              :key="index"
              :product="item"
              :class="$style['home__trend-item']"
              :style="{ width: widthItemComputed }"
            />
          </div>
        </div>
        <button :class="$style['trend__title--ctn-left']" @click="scrollLeft">
          <font-awesome-icon :icon="faChevronLeft" :class="$style['trend__title--ctn-ic']" />
        </button>
        <button :class="$style['trend__title--ctn-right']" @click="scrollRight">
          <font-awesome-icon :icon="faChevronRight" :class="$style['trend__title--ctn-ic']" />
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped module lang="scss">
@import '../Search/SearchPage.module.scss';
</style>

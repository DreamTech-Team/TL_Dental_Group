<script setup lang="ts">
import ProductBanner from './ProductBanner/ProductBanner.vue';
import ProductCard from '@/components/Card/ProductCard.vue';
import MobileCard from '@/components/MBCard/MobileCard.vue';
import BaseCategory from '@/components/Category/BaseCategory.vue';
import ProductNavigation from './ProductNavigation/ProductNavigation.vue';
import ServiceQuality from '@/components/ServiceQuality/ServiceQuality.vue';
import BasePagination from '@/components/Pagination/BasePagination.vue';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCaretDown } from '@fortawesome/free-solid-svg-icons';
import { computed, onMounted, ref, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { faArrowDownShortWide } from '@fortawesome/free-solid-svg-icons';

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

//Khởi tạo danh sách sản phẩm để hiển thị ra màn hình
const products = ref<Item[]>([]);

const deps = ref([]);
const deps1 = ref([]);
const dataRes = ref([]);
const filterAllProduct = ref([]);
const currentPage = ref(0);
const pageSize = ref(12);
const pathBC = 'sanpham';
const isDesktop = ref(true);
const isActive = ref(false);
const totalProduct = ref();

const slugCategory1 = ref('');
const slugCategory2 = ref('');

// Xử lí sort
const isDropdownOpen = ref(false);
const selectedOption = ref('Sắp xếp');
const options = ['Mới nhất', 'Giá tăng dần', 'Giá giảm dần'];

//Đặt biến API ban đầu: gọi tổng sản phẩm hiện có
const apiTotalProduct = `/products/total${
  slugCategory1.value
    ? `?cate1=${slugCategory1.value}` + (slugCategory2.value ? `&cate2=${slugCategory2.value}` : '')
    : ''
}`;

//Đặt biến API ban đầu: gọi danh sách sản phẩm theo category
const apiProduct = `/products?page=${currentPage.value}&pageSize=${pageSize.value}${
  slugCategory1.value
    ? `&cate1=${slugCategory1.value}` + (slugCategory2.value ? `&cate2=${slugCategory2.value}` : '')
    : ''
}`;

const {
  response: productRes,
  error,
  isLoading
} = useAxios<DataResponse>('get', apiProduct, {}, {}, deps.value);

const {
  response: totalRes,
  error: totalErr,
  isLoading: loadErr
} = useAxios<DataResponse>('get', apiTotalProduct, {}, {}, deps.value);

// Define methods
const toggleDropdown = () => {
  isActive.value = !isActive.value;
  isDropdownOpen.value = !isDropdownOpen.value;
};

const closeDropdown = () => {
  isActive.value = false;
  isDropdownOpen.value = false;
};

function updateSelectedOption(option: string) {
  selectedOption.value = option;
  closeDropdown();
}

const checkScreenSize = () => {
  if (window.innerWidth < 739) {
    isDesktop.value = false;
  } else {
    isDesktop.value = true;
  }
};

const scrollToTop = (top: number) => {
  window.scrollTo({
    top: top,
    behavior: 'smooth' // Tạo hiệu ứng cuộn mượt
  });
};

const handlePageChange = (page: number) => {
  currentPage.value = page - 1;
  if (window.innerWidth < 739) {
    scrollToTop(0);
  } else {
    scrollToTop(400);
  }
};

const handleCategory1Selected = (selectedCategory1: string) => {
  slugCategory1.value = selectedCategory1;
  console.log('Selected Category1:', selectedCategory1);
};
const handleCategory2Selected = (selectedCategory2: string) => {
  slugCategory2.value = selectedCategory2;
  console.log('Selected Category2:', selectedCategory2);
};

//Cập nhật lại nội dung cần để show sản phẩm ra màn hình
const updateShowResults = () => {
  products.value = filterAllProduct.value.map((item: Product) => {
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
};

// Truy xuất giá trị response.value và gán vào responseData
watch(productRes, () => {
  dataRes.value = productRes?.value?.data;
  filterAllProduct.value = productRes?.value?.data?.data;
  updateShowResults();
});

watch(totalRes, () => {
  totalProduct.value = totalRes?.value?.data;
  console.log(apiTotalProduct);
  console.log(apiProduct);
  console.log(totalProduct.value);
});

watch(
  [currentPage, slugCategory1, slugCategory2, apiProduct],
  () => {
    const {
      response: responseChanged,
      error,
      isLoading
    } = useAxios<DataResponse>(
      'get',
      `/products?page=${currentPage.value}&pageSize=${pageSize.value}${
        slugCategory1.value
          ? `&cate1=${slugCategory1.value}` +
            (slugCategory2.value ? `&cate2=${slugCategory2.value}` : '')
          : ''
      }`,
      {},
      {},
      deps1.value
    );

    // Truy xuất giá trị response.value và gán vào responseData
    watch(responseChanged, () => {
      dataRes.value = responseChanged?.value?.data;
      filterAllProduct.value = responseChanged?.value?.data?.data;
      updateShowResults();
    });
  },
  { immediate: false }
);

onMounted(() => {
  checkScreenSize();
  updateSelectedOption('Sắp xếp');
});
window.addEventListener('resize', checkScreenSize);
</script>

<template>
  <div>
    <div :class="$style['product__header']">
      <product-banner :class="$style['product__header-banner']" />
    </div>
    <div :class="$style['product__wrapper']">
      <div style="margin: auto; max-width: 1280px">
        <bread-crumb :tags="pathBC" />
        <div :class="$style['product__content']">
          <!-- category -->
          <base-category
            v-if="isDesktop"
            @slug-category1="handleCategory1Selected"
            @slug-category2="handleCategory2Selected"
          />
          <div :class="$style['product__content-wrap']">
            <div :class="$style['product__content-sort']">
              <p :class="$style['product__content-sort--info']">
                Hiển thị
                <strong>{{ products.length }}</strong> trên <strong>{{ totalProduct }}</strong> sản
                phẩm
              </p>

              <div :class="$style['product__content-sort--type']" @click="toggleDropdown">
                <p>{{ selectedOption }}</p>
                <font-awesome-icon :icon="faCaretDown" />
              </div>

              <div
                v-if="isDropdownOpen"
                @click="closeDropdown"
                :class="$style['product__content-sort--content']"
              >
                <!-- Nội dung dropdown -->
                <ul :class="$style['dropdown-list']">
                  <li
                    :class="$style['dropdown-item']"
                    v-for="option in options"
                    :key="option"
                    @click="updateSelectedOption(option)"
                  >
                    {{ option }}
                  </li>
                </ul>
              </div>
            </div>

            <!-- mobile sort-->
            <div v-if="!isDesktop" :class="$style['product__content-mbsort']">
              <div
                :class="[
                  $style['product__content-mbsort--type'],
                  {
                    [$style['product__content-mbsort--active']]: isActive
                  }
                ]"
                @click="toggleDropdown"
              >
                <font-awesome-icon
                  :class="$style['product__content-mbsort--type--icon']"
                  :icon="faArrowDownShortWide"
                />
                <p :class="$style['product__content-mbsort--type--text']">{{ selectedOption }}</p>
              </div>
              <div
                v-if="isDropdownOpen"
                @click="closeDropdown"
                :class="$style['product__content-mbsort--contents']"
              >
                <!-- Nội dung dropdown -->
                <ul :class="$style['dropdown-list']">
                  <li
                    :class="$style['dropdown-item']"
                    v-for="option in options"
                    :key="option"
                    @click="updateSelectedOption(option)"
                  >
                    {{ option }}
                  </li>
                </ul>
              </div>
            </div>
            <div v-if="isActive" :class="$style.overlay" @click="closeDropdown"></div>
            <!-- mobile content -->
            <div v-if="isDesktop" :class="$style['product__content-container']">
              <product-card
                v-for="(item, index) in products"
                :key="index"
                :product="item"
                :class="$style['product__content-container--card']"
              />
            </div>
            <div v-else :class="$style['product__content-mobile']">
              <mobile-card v-for="(item1, index1) in products" :key="index1" :product="item1" />
            </div>
            <div :class="$style['product__pagination']">
              <base-pagination
                :total="totalProduct ? totalProduct : 0"
                :current-page="currentPage"
                :page-size="pageSize"
                @current-change="handlePageChange"
              />
            </div>
          </div>
        </div>
      </div>
    </div>

    <div>
      <ServiceQuality />
    </div>
  </div>
</template>
s
<style scoped module lang="scss">
@import './ProductPage.module.scss';
</style>

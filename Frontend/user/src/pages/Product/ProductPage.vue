<script setup lang="ts">
import ProductBanner from './ProductBanner/ProductBanner.vue';
import ProductCard from '@/components/Card/ProductCard.vue';
import MobileCard from '@/components/MBCard/MobileCard.vue';
import BaseCategory from '@/components/Category/BaseCategory.vue';
import ProductNavigation from './ProductNavigation/ProductNavigation.vue';
import ServiceQuality from '@/components/ServiceQuality/ServiceQuality.vue';
import BasePagination from '@/components/Pagination/BasePagination.vue';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCaretDown } from '@fortawesome/free-solid-svg-icons';
import { computed, onMounted, ref, watch, onBeforeUnmount } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { useRouter, useRoute } from 'vue-router';
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

const router = useRouter();
const route = useRoute();
const slugCategory1 = ref(route.query.slug1);
const slugCategory2 = ref(route.query.slug2);
const sortPriceType = ref('asc');
const isLoadingProduct = ref(false);

// Xử lí sort
const isDropdownOpen = ref(false);
const selectedOption = ref('Giá tăng dần');
const options = ['Giá tăng dần', 'Giá giảm dần'];

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
}&sortPrice=${sortPriceType.value}`;
const { response: productRes, isLoading } = useAxios<DataResponse>(
  'get',
  apiProduct,
  {},
  {},
  deps.value
);

const { response: totalRes } = useAxios<DataResponse>('get', apiTotalProduct, {}, {}, deps.value);

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
  if (selectedOption.value == 'Giá tăng dần') {
    sortPriceType.value = 'asc';
  } else {
    sortPriceType.value = 'desc';
  }
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
};
const handleCategory2Selected = (selectedCategory2: string) => {
  slugCategory2.value = selectedCategory2;
};

//Cập nhật lại nội dung cần để show sản phẩm ra màn hình
const updateShowResults = () => {
  if (filterAllProduct.value) {
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
  }
};

// Truy xuất giá trị response.value và gán vào responseData
watch([productRes, isLoading], () => {
  isLoadingProduct.value = isLoading.value;
  console.log(isLoadingProduct.value);

  dataRes.value = productRes?.value?.data;
  filterAllProduct.value = productRes?.value?.data?.data;
  updateShowResults();
});

watch(totalRes, () => {
  totalProduct.value = totalRes?.value?.data;
});

watch(
  [currentPage, slugCategory1, slugCategory2, apiProduct, sortPriceType],
  () => {
    const { response: responseChanged, isLoading: isLoadingChange } = useAxios<DataResponse>(
      'get',
      `/products?page=${currentPage.value}&pageSize=${pageSize.value}${
        slugCategory1.value
          ? `&cate1=${slugCategory1.value}` +
            (slugCategory2.value ? `&cate2=${slugCategory2.value}` : '')
          : ''
      }&sortPrice=${sortPriceType.value}`,
      {},
      {},
      deps1.value
    );
    console.log(currentPage.value);

    // Truy xuất giá trị response.value và gán vào responseData
    watch(
      [responseChanged, isLoadingChange],
      () => {
        isLoadingProduct.value = isLoadingChange.value;
        console.log(isLoadingProduct.value);

        dataRes.value = responseChanged?.value?.data;
        filterAllProduct.value = responseChanged?.value?.data?.data;
        updateShowResults();
        const { response: totalRes } = useAxios<DataResponse>(
          'get',
          `/products/total${
            slugCategory1.value
              ? `?cate1=${slugCategory1.value}` +
                (slugCategory2.value ? `&cate2=${slugCategory2.value}` : '')
              : ''
          }`,
          {},
          {},
          deps.value
        );

        watch(totalRes, () => {
          totalProduct.value = totalRes?.value?.data;
        });
      },
      { immediate: true }
    );
  },
  { immediate: false }
);

watch(route, () => {
  slugCategory1.value = route.query.slug1;
  slugCategory2.value = route.query.slug2;

  const { response: responseChanged, isLoading: isLoadingRoute } = useAxios<DataResponse>(
    'get',
    `/products?page=${currentPage.value}&pageSize=${pageSize.value}${
      slugCategory1.value
        ? `&cate1=${slugCategory1.value}` +
          (slugCategory2.value ? `&cate2=${slugCategory2.value}` : '')
        : ''
    }&sortPrice=${sortPriceType.value}`,
    {},
    {},
    deps1.value
  );

  watch(
    [responseChanged, isLoadingRoute],
    () => {
      isLoadingProduct.value = isLoadingRoute.value;
      console.log(isLoadingProduct.value);
      dataRes.value = responseChanged?.value?.data;
      filterAllProduct.value = responseChanged?.value?.data?.data;
      updateShowResults();

      const { response: totalRes } = useAxios<DataResponse>(
        'get',
        `/products/total${
          slugCategory1.value
            ? `?cate1=${slugCategory1.value}` +
              (slugCategory2.value ? `&cate2=${slugCategory2.value}` : '')
            : ''
        }`,
        {},
        {},
        deps.value
      );

      watch(totalRes, () => {
        totalProduct.value = totalRes?.value?.data;
      });
    },
    { immediate: true }
  ),
    { immediate: false };
});
onMounted(() => {
  checkScreenSize();
  // updateSelectedOption('Giá tăng dần');
});

// Lấy dữ liệu từ URL query parameters khi trang được tạo
onMounted(() => {
  const { query } = router.currentRoute.value;
  if (query.slug1) {
    slugCategory1.value = query.slug1.toString();
    // // Xóa toàn bộ query string và cập nhật URL
    // router.replace({ query: {} });
  }
});

window.addEventListener('resize', checkScreenSize);
</script>

<template>
  <div>
    <div :class="$style['product__header']">
      <product-banner :class="$style['product__header-banner']" />
    </div>
    <div :class="$style['product__wrapper']">
      <div style="margin: auto; max-width: 1480px">
        <bread-crumb :tags="pathBC" />
        <div :class="$style['product__content']">
          <div :class="$style['product__cate']">
            <base-category
              v-if="isDesktop"
              @slug-category1="handleCategory1Selected"
              @slug-category2="handleCategory2Selected"
            />
            <div v-if="!isLoadingProduct" :class="$style['product__content-wrap']">
              <div :class="$style['product__content-sort']">
                <p :class="$style['product__content-sort--info']">
                  Hiển thị
                  <strong>{{ products.length }}</strong> trên
                  <strong>{{ totalProduct }}</strong> sản phẩm
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
              <!-- card content -->
              <div
                v-if="isDesktop && products.length > 0"
                :class="$style['product__content-container']"
              >
                <product-card
                  v-for="(item, index) in products"
                  :key="index"
                  :product="item"
                  :class="$style['product__content-container--card']"
                />
              </div>

              <div
                v-else-if="!isDesktop && products.length > 0"
                :class="$style['product__content-mobile']"
              >
                <mobile-card v-for="(item1, index1) in products" :key="index1" :product="item1" />
              </div>

              <div v-if="products.length > 0" :class="$style['product__pagination']"></div>
              <div v-else :class="$style.product__notFound">Không có sản phẩm</div>
            </div>
            <loading-component v-else />
          </div>
          <div v-if="products.length > 0" :class="$style['product__pagination']">
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

    <div>
      <!-- <ServiceQuality /> -->
    </div>
  </div>
</template>
s
<style scoped module lang="scss">
@import './ProductPage.module.scss';
</style>

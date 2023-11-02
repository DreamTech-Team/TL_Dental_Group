<script setup lang="ts">
import ProductBanner from './ProductBanner/ProductBanner.vue';
import ProductCard from '@/components/Card/ProductCard.vue';
import MobileCard from '@/components/MBCard/MobileCard.vue';
import BaseCategory from '@/components/Category/BaseCategory.vue';
import ServiceQuality from '@/components/ServiceQuality/ServiceQuality.vue';
import BasePagination from '@/components/Pagination/BasePagination.vue';
import BreadCrumb from '@/components/BreadCrumb/BreadCrumb.vue';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCaretDown } from '@fortawesome/free-solid-svg-icons';
import { onMounted, ref, watch, onUnmounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { faArrowDownShortWide } from '@fortawesome/free-solid-svg-icons';
import { saveActive } from '@/stores/counter';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface Company {
  id: string;
  name: string;
  logo: string;
  description: string;
  highlight: number;
  slug: string;
  createAt: string;
  outstandingProductId: string;
}

interface Category {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
  createAt: string;
}

interface Product {
  id: string;
  name: string;
  slug: string;
  price: number;
  priceSale: number;
  summary: string;
  description: string;
  mainImg: string;
  imgs: string[];
  highlight: number;
  createAt: string;
  fkCategory: {
    id: string;
    companyId: Company;
    cate1Id: Category;
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

// Biến lưu trạng thái category
const saveState = saveActive();

//Khởi tạo danh sách sản phẩm để hiển thị ra màn hình
const products = ref<Item[]>([]);

const pathBC = 'sanpham';
const isDesktop = ref(true);
const isActive = ref(false);
const deps = ref([]);
const deps1 = ref([]);
const dataRes = ref([]);
const filterAllProduct = ref([]);
const totalProduct = ref();
const isLoadingProduct = ref(false);
const router = useRouter();
const route = useRoute();
const slugCategory1 = ref(route.query.slug1);
const slugCategory2 = ref(route.query.slug2);
const slugCategory3 = ref(route.query.slug3);
const currentPage = ref(0);
const pageSize = ref(12);

// Xử lí sort
const sortPriceType = ref('asc');
const isDropdownOpen = ref(false);
const selectedOption = ref('Giá tăng dần');
const options = ['Giá tăng dần', 'Giá giảm dần'];

//Tổng sản phẩm
const apiTotalProduct = `/products/total${
  slugCategory1.value
    ? `?cate1=${slugCategory1.value}` +
      (slugCategory2.value
        ? `&company=${slugCategory2.value}` +
          (slugCategory3.value ? `&cate2=${slugCategory3.value}` : '')
        : '')
    : ''
}`;

//Danh sách sản phẩm
const apiProduct = `/products?page=${currentPage.value}&pageSize=${pageSize.value}${
  slugCategory1.value
    ? `&cate1=${slugCategory1.value}` +
      (slugCategory2.value
        ? `&company=${slugCategory2.value}` +
          (slugCategory3.value ? `&cate2=${slugCategory3.value}` : '')
        : '')
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
  sortPriceType.value = option === 'Giá tăng dần' ? 'asc' : 'desc';
  closeDropdown();
}

const checkScreenSize = () => {
  isDesktop.value = window.innerWidth >= 739;
};

const scrollToTop = (top: number) => {
  window.scrollTo({ top, behavior: 'smooth' });
};

const handlePageChange = (page: number) => {
  currentPage.value = page - 1;
  scrollToTop(window.innerWidth < 739 ? 0 : 400);
};

const handleCategory1Selected = (selectedCategory1: string) => {
  slugCategory1.value = selectedCategory1;
};
const handleCategory2Selected = (selectedCategory2: string) => {
  slugCategory2.value = selectedCategory2;
};
const handleCategory3Selected = (selectedCategory3: string) => {
  slugCategory3.value = selectedCategory3;
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

  dataRes.value = productRes?.value?.data;
  filterAllProduct.value = productRes?.value?.data?.data;
  updateShowResults();
});

watch(totalRes, () => {
  totalProduct.value = totalRes?.value?.data;
});

watch(
  [currentPage, slugCategory1, slugCategory2, slugCategory3, sortPriceType],
  () => {
    const { response: responseChanged, isLoading: isLoadingChange } = useAxios<DataResponse>(
      'get',
      `/products?page=${currentPage.value}&pageSize=${pageSize.value}${
        slugCategory1.value
          ? `&cate1=${slugCategory1.value}` +
            (slugCategory2.value
              ? `&company=${slugCategory2.value}` +
                (slugCategory3.value ? `&cate2=${slugCategory3.value}` : '')
              : '')
          : ''
      }&sortPrice=${sortPriceType.value}`,
      {},
      {},
      deps1.value
    );

    // Truy xuất giá trị response.value và gán vào responseData
    watch(
      [responseChanged, isLoadingChange],
      () => {
        isLoadingProduct.value = isLoadingChange?.value;

        dataRes.value = responseChanged?.value?.data;
        filterAllProduct.value = responseChanged?.value?.data?.data;
        updateShowResults();

        const { response: totalResponse } = useAxios<DataResponse>(
          'get',
          `/products/total${
            slugCategory1.value
              ? `?cate1=${slugCategory1.value}` +
                (slugCategory2.value
                  ? `&company=${slugCategory2.value}` +
                    (slugCategory3.value ? `&cate2=${slugCategory3.value}` : '')
                  : '')
              : ''
          }`,
          {},
          {},
          deps.value
        );

        watch(
          totalResponse,
          () => {
            if (
              slugCategory1.value !== undefined ||
              slugCategory2.value !== undefined ||
              slugCategory3.value !== undefined
            ) {
              totalProduct.value = totalResponse?.value?.data;
            }
          },
          { immediate: false }
        );
      },
      { immediate: false }
    );
  },
  { immediate: false }
);

watch(route, () => {
  slugCategory1.value = route.query.slug1;
  slugCategory2.value = route.query.slug2;
  slugCategory3.value = route.query.slug3;

  const { response: responseChanged, isLoading: isLoadingRoute } = useAxios<DataResponse>(
    'get',
    `/products?page=${currentPage.value}&pageSize=${pageSize.value}${
      slugCategory1.value
        ? `&cate1=${slugCategory1.value}` +
          (slugCategory2.value
            ? `&company=${slugCategory2.value}` +
              (slugCategory3.value ? `&cate2=${slugCategory3.value}` : '')
            : '')
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
      dataRes.value = responseChanged?.value?.data;
      filterAllProduct.value = responseChanged?.value?.data?.data;
      updateShowResults();

      const { response: totalRes } = useAxios<DataResponse>(
        'get',
        `/products/total${
          slugCategory1.value
            ? `?cate1=${slugCategory1.value}` +
              (slugCategory2.value
                ? `&company=${slugCategory2.value}` +
                  (slugCategory3.value ? `&cate2=${slugCategory3.value}` : '')
                : '')
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
    { immediate: false }
  ),
    { immediate: false };
});

onMounted(() => {
  checkScreenSize();

  // Lấy dữ liệu từ URL query parameters khi trang được tạo
  const { query } = router.currentRoute.value;
  if (query.slug1) {
    slugCategory1.value = query.slug1.toString();
  }
});

onUnmounted(() => {
  if (router.currentRoute.value.name !== 'sanpham') {
    saveState.setActiveCategory(-1);
    saveState.setActiveCategory2(-1);
    saveState.setActiveCategory3(-1);
  }
});

window.addEventListener('resize', checkScreenSize);
</script>

<template>
  <div style="margin: auto; max-width: 1600px">
    <div :class="$style['product__header']">
      <product-banner :class="$style['product__header-banner']" />
    </div>
    <div :class="$style['product__wrapper']">
      <div>
        <bread-crumb :tags="pathBC" />
        <div :class="$style['product__content']">
          <div :class="$style['product__cate']">
            <base-category
              v-if="isDesktop"
              @slug-category1="handleCategory1Selected"
              @slug-category2="handleCategory2Selected"
              @slug-category3="handleCategory3Selected"
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
              <div
                v-else
                :class="$style.product__notFound"
                :style="{ height: '60vh', width: '100%' }"
              >
                Không có sản phẩm
              </div>
            </div>
            <div
              v-else
              :class="$style['product__content-wrap']"
              :style="{ height: 'calc(60vh + 90px)' }"
            >
              <loading-component />
            </div>
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
      <ServiceQuality />
    </div>
  </div>
</template>
s
<style scoped module lang="scss">
@import './ProductPage.module.scss';
</style>

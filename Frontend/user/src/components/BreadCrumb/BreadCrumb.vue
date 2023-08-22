<script setup lang="ts">
import { ref, watch, toRefs } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faHome, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { RouterLink } from 'vue-router';
import { useRoute } from 'vue-router';
import { saveBreadcrumbs, saveSlugCates, saveSlugNews } from '@/stores/breadcrumb';

const { breadcrumbs } = toRefs(saveBreadcrumbs());
const { cates } = toRefs(saveSlugCates());
const { news } = toRefs(saveSlugNews());

const saveBC = saveBreadcrumbs();
const saveCate = saveSlugCates();
const saveNews = saveSlugNews();

interface Dictionaries {
  slug: string;
  name: string;
}

interface News {
  id: string;
  title: string;
  img: string;
  slug: string;
  summary: string;
  detail: string;
  detailMobile: string;
  highlight: number;
  createAt: string;
  tags: [
    {
      id: string;
      name: string;
      slug: string;
      createAt: string;
    }
  ];
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

interface Cate1 {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
  createAt: string;
}

interface Cate2 {
  id: string;
  title: string;
  slug: string;
  createAt: string;
}

const pathAD = defineProps({
  tags: {
    type: String,
    required: true
  }
});

const defaultDictionaries = ref([
  {
    slug: 'tintuc',
    name: 'Tin tức'
  },
  {
    slug: 'gioithieu',
    name: 'Giới thiệu'
  },
  {
    slug: 'chinhsach',
    name: 'Chính sách'
  },
  {
    slug: 'sanpham',
    name: 'Sản phẩm'
  },
  {
    slug: 'lienhe',
    name: 'Liên hệ'
  },
  {
    slug: 'chinhsach',
    name: 'Chính sách'
  },
  {
    slug: 'timkiem',
    name: 'Kết quả tìm kiếm'
  }
]);

if (breadcrumbs.value.length === 0) {
  saveBC.setBreadcrumbs(defaultDictionaries.value);
}

const predefinedItems = ref<Dictionaries[]>(breadcrumbs.value.concat(news.value, cates.value));

//Properties
const route = useRoute();
const pathSegments = ref<string[]>([]);
const pathMain = ref(pathAD.tags ? pathAD.tags : '/chitiet');
const breadcrumbItems = ref();
let isAllCategoryLoaded = false;

//Function Update Slug
const updateSlug = () => {
  pathSegments.value = pathMain.value.split('/').filter((segment) => segment !== '');

  breadcrumbItems.value = pathSegments.value.map((segment: string) => {
    const predefinedItem = predefinedItems.value.find((item) => item.slug === segment);
    return predefinedItem ? predefinedItem.name : segment;
  });
};

watch(pathAD, () => {
  pathMain.value = pathAD.tags;
  updateSlug();
});

//Init dependencies
const deps = ref([]);

const getAllCategory = () => {
  const cate1 = ref<Cate1[]>([]);
  const cate2 = ref<Cate2[]>([]);
  const getCate1 = useAxios<DataResponse>('get', '/cate1', {}, {}, deps.value);
  const getCate2 = useAxios<DataResponse>('get', '/cate2', {}, {}, deps.value);

  if (!isAllCategoryLoaded) {
    watch(getCate1.response, () => {
      const responseData = getCate1.response.value?.data;
      if (responseData && responseData instanceof Array) {
        cate1.value = responseData;
        cate1.value.forEach((item: Cate1) => {
          const news = {
            slug: item.slug,
            name: item.title
          };
          predefinedItems.value.push(news);
        });
        saveCate.setSlugCates(predefinedItems.value);
        updateSlug();
      }
    });

    watch(getCate2.response, () => {
      const responseData2 = getCate2.response.value?.data;
      if (responseData2 && responseData2 instanceof Array) {
        cate2.value = responseData2;
        cate2.value.forEach((item: Cate2) => {
          const news = {
            slug: item.slug,
            name: item.title
          };
          predefinedItems.value.push(news);
        });
        updateSlug();
      }
    });

    isAllCategoryLoaded = true;
  }
};

watch(
  [predefinedItems],
  () => {
    updateSlug();
  },
  { immediate: true }
);

if (route.path.startsWith('/tintuc')) {
  if (news.value.length === 0) {
    const news = ref<News[]>([]);
    const getNews = useAxios<DataResponse>('get', '/news?pageSize=1000000', {}, {}, deps.value);
    watch(getNews.response, () => {
      news.value = getNews.response.value?.data?.data;
      news.value.forEach((item) => {
        const news = {
          slug: item.slug,
          name: item.title
        };
        predefinedItems.value.push(news);
      });
      saveNews.setSlugNews(predefinedItems.value);
      updateSlug();
    });
  } else {
    predefinedItems.value = news.value;
    updateSlug();
  }
} else if (route.path.startsWith('/chitiet')) {
  if (cates.value.length === 0) {
    const products = ref<Product[]>([]);
    const getProducts = useAxios<DataResponse>(
      'get',
      '/products?pageSize=300000',
      {},
      {},
      deps.value
    );
    watch(getProducts.response, () => {
      products.value = getProducts.response.value?.data?.data;
      products.value.forEach((item) => {
        const news = {
          slug: item.slug,
          name: item.name
        };
        predefinedItems.value.push(news);
      });
      saveCate.setSlugCates(predefinedItems.value);
    });
    getAllCategory();
  } else {
    predefinedItems.value = cates.value;
    updateSlug();
  }
}

const navigate = (slug: string) => {
  const currentIndex = breadcrumbItems.value.findIndex((item: string) => item === slug);
  if (currentIndex > -1) {
    const pathSegments = breadcrumbItems.value.slice(0, currentIndex + 1);
    const path = `/${pathSegments
      .map((segment: string) => predefinedItems.value.find((item) => item.name === segment)?.slug)
      .join('/')}`;

    return path;
  }
  return '/';
};
</script>
<template>
  <div :class="$style.news__breadcrum">
    <router-link to="/" :class="$style['news__breadcrum-home']">
      <font-awesome-icon :icon="faHome" :class="$style['news__breadcrum-ic']" />
      <span>Home</span>
    </router-link>
    <template v-for="(item, index) in breadcrumbItems">
      <router-link
        v-if="index < breadcrumbItems.length - 1"
        :key="'link-' + index"
        :to="navigate(item)"
        :class="$style['news__breadcrum-tag']"
      >
        <font-awesome-icon :icon="faChevronRight" :class="$style['news__breadcrum-ic']" />
        <span>{{ item }}</span>
      </router-link>
      <div v-else :key="'div-' + index" :class="$style['news__breadcrum-tag']">
        <font-awesome-icon :icon="faChevronRight" :class="$style['news__breadcrum-ic']" />
        <span>{{ item }}</span>
      </div>
    </template>
  </div>
</template>

<style module scoped lang="scss">
@import './BreadCrumb.module.scss';
</style>

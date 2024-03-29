<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import ModalBanner from './ModalBanner.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface Company {
  outstandingProduct: {
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
        highlight: number;
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
  };
  company: {
    id: string;
    name: string;
    logo: string;
    description: string;
    highlight: number;
    slug: string;
    createAt: string;
    outstandingProductId: string;
  };
}

interface Item {
  src: string;
  alt: string;
  width: string;
  height: string;
  name: string;
  product: string;
}

//Modal Status
const isOpen = ref(false);

//Get information for header
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/home/header', {}, {}, deps.value);

const content = ref({
  id: '',
  title: '',
  context: ''
});

//Get data from axios
watch(response, () => {
  content.value.id = response.value?.data?.id;
  content.value.title = response.value?.data?.title;
  content.value.context = response.value?.data?.content;
});

//Handle data from modal
const onUpdateContent = (data: object) => {
  content.value = { ...content.value, ...data };
};

//Get all highlight compaines
const companies = ref<Company[]>([]);
const deps1 = ref([]);
const results = useAxios<DataResponse>('get', '/company?highlight=true', {}, {}, deps1.value);
const bannerItems = ref([
  { src: '', alt: '', width: '0', height: '0', name: '', product: '', slug: '', idx: 0 }
]);

//Calculate width for each company logo
const calculateWidths = () => {
  return new Promise<void>((resolve) => {
    const imagePromises = bannerItems.value.map((item: Item) => {
      return new Promise<void>((resolve) => {
        const image = new Image();
        image.onload = () => {
          const aspectRatio = image.width / image.height;
          const newWidth = 30 * aspectRatio;
          item.width = newWidth.toFixed(2);
          resolve();
        };
        image.src = item.src;
      });
    });

    Promise.all(imagePromises).then(() => {
      resolve();
    });
  });
};

//Function Random
const getRandomItems = (array: Company[], count: number) => {
  const shuffled = array.slice();
  for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
  }
  return shuffled.slice(0, count);
};

//Properties
const activeIndex = ref(0);
const activeBanner = ref(bannerItems.value[0]);
const showBannerBg = ref(true);
const lineWidth = ref(0);
let resizeListener: () => void;

//Transform line active
const lineTransform = computed(() => {
  let selectedItemLeft = 0;
  for (let i = 0; i < activeIndex.value; i++) {
    const itemWidth = Number(bannerItems.value[i].width) + 30;
    selectedItemLeft += itemWidth;
  }
  selectedItemLeft += (Number(bannerItems.value[activeIndex.value].width) - lineWidth.value) / 2;
  return `translateX(${selectedItemLeft}px)`;
});

const selectedItem = computed(() => {
  return bannerItems.value[activeIndex.value];
});

//Set background for banner
const bannerBgColor = computed(() => {
  const colors = [
    `radial-gradient(50% 50% at 50% 50%, rgba(135, 255, 126, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`,
    `radial-gradient(50% 50% at 50% 50%, rgba(252, 126, 255, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`,
    `radial-gradient(50% 50% at 50% 50%, rgba(255, 126, 126, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`,
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`
  ];
  return colors[activeIndex.value];
});

//Set 4 ellipse color for web
const elipseColor = computed(() => {
  const elcolors = [
    [
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`
    ],
    [
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`
    ],
    [
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(252, 126, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(183, 255, 126, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`
    ],
    [
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(183, 255, 126, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(252, 126, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
      // eslint-disable-next-line max-len
      `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`
    ]
  ];
  return elcolors[activeIndex.value];
});

//To do when moveline
const moveLine = (index: number) => {
  activeIndex.value = index;
  activeBanner.value = bannerItems.value[index];
  showBannerBg.value = false;

  setTimeout(() => {
    showBannerBg.value = true;
  }, 100);

  let totalWidthToLeft = 0;
  for (let i = 0; i < index; i++) {
    const itemWidth = Number(bannerItems.value[i].width) + 30;
    totalWidthToLeft += itemWidth;
  }

  const selectedItemWidth = Number(bannerItems.value[index].width);
  const selectedItemCenter = totalWidthToLeft + selectedItemWidth / 2;
  lineWidth.value = 0.75 * selectedItemWidth;
  const lineLeft = selectedItemCenter - lineWidth.value / 2;

  const lineActive = document.getElementById('line_active');
  if (lineActive) {
    lineActive.style.transform = `translateX(${lineLeft}px)`;
  }
};

//Get highlight products
watch(
  results.response,
  async () => {
    if (results.response.value?.data.length > 0) {
      const randomHighlightedCompanies: Company[] =
        results.response.value?.data >= 4
          ? getRandomItems(results.response.value?.data, 4)
          : results.response.value?.data;
      companies.value = randomHighlightedCompanies;

      bannerItems.value = companies.value.map((company: Company, idx) => {
        return {
          src: company.company.logo,
          alt: company.company.name,
          width: '',
          height: '30',
          name: company.outstandingProduct.name,
          product: company.outstandingProduct.mainImg,
          slug: company.outstandingProduct.slug,
          idx: idx
        };
      });

      await calculateWidths();

      lineWidth.value = Number(bannerItems.value[0].width) * 0.75;
    }
  },
  { immediate: true }
);

onMounted(() => {
  window.addEventListener('resize', resizeListener);
});

onUnmounted(() => {
  window.removeEventListener('resize', resizeListener);
});
</script>

<template>
  <div :class="$style.home__banner">
    <div :class="$style['home__banner-left']">
      <h2>{{ content.title }}</h2>
      <p>{{ content.context }}</p>
      <div id="bannerList" :class="$style['home__banner-list']">
        <div
          v-for="(item, index) in bannerItems"
          :key="index"
          :class="[$style['home__banner-item'], { active: activeIndex === index }]"
          @click="moveLine(index)"
        >
          <img :src="item.src" :alt="item.alt" :width="item.width" :height="item.height" />
        </div>
        <div
          id="line_active"
          :class="$style['line']"
          :style="{ transform: lineTransform, width: lineWidth + 'px' }"
        ></div>
      </div>
    </div>
    <div :class="$style['home__banner-right']">
      <div
        v-for="(item, idx) in bannerItems"
        :key="idx"
        :class="[
          $style['home__banner-bg'],
          $style[idx === selectedItem.idx ? 'home__banner-bg--show' : 'home__banner-bg--hidden']
        ]"
        :style="{ background: bannerBgColor }"
      >
        <div :class="$style['home__banner-radial']">
          <div :class="$style['home__banner-circle']">
            <div :class="$style['home__banner-logo']">
              <img :src="selectedItem.src" :alt="selectedItem.alt" width="127" height="30" />
            </div>
            <div
              :class="$style['home__banner-product']"
              :style="{ backgroundImage: `url(${selectedItem.product})` }"
            ></div>
          </div>
          <p>{{ selectedItem.name }}</p>
        </div>
      </div>
    </div>
    <div :class="$style['home__banner-elipse1']" :style="{ background: elipseColor[0] }"></div>
    <div :class="$style['home__banner-elipse2']" :style="{ background: elipseColor[1] }"></div>
    <div :class="$style['home__banner-elipse3']" :style="{ background: elipseColor[2] }"></div>
    <div :class="$style['home__banner-elipse4']" :style="{ background: elipseColor[3] }"></div>
    <EditBtn @click="isOpen = true" />
  </div>
  <ModalBanner
    v-if="isOpen"
    @close="isOpen = false"
    @update-content="onUpdateContent"
    :uuid="content.id"
    :title="content.title"
    :context="content.context"
  />
</template>

<style module scoped lang="scss">
@import './HomeBanner.module.scss';
</style>

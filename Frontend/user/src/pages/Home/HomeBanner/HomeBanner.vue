<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import router from '@/router/index';
import TheAnimate from '@/components/TheAnimate/TheAnimate.vue';

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

//Define data structure
const bannerItems = ref([
  { src: '', alt: '', width: '0', height: '0', name: '', product: '', slug: '', idx: 0 }
]);

//Scroll Properties
const MIN_SWIPE_DISTANCE_CM = 3.5;
const TOUCH_SENSITIVITY = 15;
const touchstartX = ref(0);
const touchendX = ref(0);
const circleActive = ref(0);

//MOBILE
const tranfX = ref(0);
const widthItemMB = ref(0);
const mobilestatus = ref(true);
const isTouch = ref(false);
let scrollInterval: ReturnType<typeof setInterval>;

//WEB
const activeIndex = ref(0);
const oldActiveIndex = ref(0);
const activeBanner = ref(bannerItems.value[0]);
const showBannerBg = ref(true);
const lineWidth = ref(0);
let resizeListener: () => void;

const widthComputed = computed(() => {
  return widthItemMB.value * bannerItems.value.length + 'px';
});

const widthItemComputed = computed(() => {
  return widthItemMB.value + 'px';
});

//Go to detail Page
const linkDetail = (slug: string) => {
  router.push(`/chitiet/${slug}`);
};

//Scroll Handle
const scrollRight = () => {
  const container = document.getElementById('list_item');
  if (container) {
    const containerWidth = container.offsetWidth;
    const itemWidth = container.offsetWidth;

    const maxScrollLeft = container.scrollWidth - containerWidth;

    const currentScrollLeft = container.scrollLeft;
    const targetScrollLeft = currentScrollLeft + itemWidth + 10;

    if (targetScrollLeft > maxScrollLeft) {
      circleActive.value = 0;
      container.scrollTo({ left: 0, behavior: 'smooth' });
    } else {
      if (circleActive.value == bannerItems.value.length - 1) {
        circleActive.value = 0;
      } else {
        circleActive.value++;
      }

      container.scrollTo({ left: targetScrollLeft, behavior: 'smooth' });
    }
  }
};

const startScroll = () => {
  scrollInterval = setInterval(() => {
    scrollRight();
  }, 5000);
};

const pauseScroll = () => {
  if (scrollInterval !== null) clearInterval(scrollInterval);
};

const scrollLeft = () => {
  const container = document.getElementById('list_item');
  if (container) {
    const itemWidth = container.offsetWidth;

    const currentScrollLeft = container.scrollLeft;
    const targetScrollLeft = currentScrollLeft - itemWidth - 10;

    if (targetScrollLeft < 0) {
      circleActive.value = bannerItems.value.length - 1;
      container.scrollTo({ left: container.scrollWidth, behavior: 'smooth' });
    } else {
      circleActive.value--;
      container.scrollTo({ left: targetScrollLeft, behavior: 'smooth' });
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
  isTouch.value = true;
  pauseScroll();
};

const handleTouchend = (e: TouchEvent) => {
  touchendX.value = e.changedTouches[0].screenX;
  checkDirection();
  if (scrollInterval) clearInterval(scrollInterval);

  setTimeout(() => {
    isTouch.value = false;
  }, 5000);
};

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

const oldSelectedItem = computed(() => {
  return bannerItems.value[oldActiveIndex.value];
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

const oldBannerBgColor = computed(() => {
  const colors = [
    `radial-gradient(50% 50% at 50% 50%, rgba(135, 255, 126, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`,
    `radial-gradient(50% 50% at 50% 50%, rgba(252, 126, 255, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`,
    `radial-gradient(50% 50% at 50% 50%, rgba(255, 126, 126, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`,
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.8) 0%, rgba(242, 255, 255, 0) 100%)`
  ];
  return colors[oldActiveIndex.value];
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

//Set 4 ellipse color for mobile
const elipseColorMB = [
  [
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`
  ],
  [
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(248, 131, 131, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`
  ],
  [
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(183, 255, 126, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(252, 126, 255, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`
  ],
  [
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(252, 126, 255, 0.9) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(252, 126, 255, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`,
    // eslint-disable-next-line max-len
    `radial-gradient(50% 50% at 50% 50%, rgba(126, 232, 255, 0.2) 0%, rgba(242, 255, 255, 0) 100%)`
  ]
];

//To do when moveline
const moveLine = (index: number) => {
  if (index === activeIndex.value) return;
  oldActiveIndex.value = activeIndex.value;

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

//Get information
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/home/header', {}, {}, deps.value);

const content = ref({
  title: '',
  context: ''
});

watch(response, () => {
  content.value.title = response.value?.data?.title;
  content.value.context = response.value?.data?.content;
});

//Get highlight compaines
const companies = ref<Company[]>([]);
const deps1 = ref([]);
const results = useAxios<DataResponse>('get', '/company?highlight=true', {}, {}, deps1.value);

//Calculate width for each logo
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

//Random item
const getRandomItems = (array: Company[], count: number) => {
  const shuffled = array.slice();
  for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
  }
  return shuffled.slice(0, count);
};

//Handle data from axios
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

watch(
  isTouch,
  () => {
    if (!isTouch.value) {
      startScroll();
    }
  },
  { immediate: true }
);

onMounted(() => {
  mobilestatus.value = false;

  // Find width slider
  widthItemMB.value = window.innerWidth;

  // Resize Screen
  resizeListener = function () {
    if (window.innerWidth < 739) {
      mobilestatus.value = true;
      widthItemMB.value = window.innerWidth;
    }
  };

  if (window.innerWidth < 739) {
    window.addEventListener('resize', resizeListener);
  }
});

onUnmounted(() => {
  if (window.innerWidth < 739) {
    window.removeEventListener('resize', resizeListener);
  }
});
</script>

<template>
  <div :class="$style.home__banner">
    <div :class="$style['home__banner-left']">
      <h2 style="position: relative">{{ content.title }} <the-animate /></h2>
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
        :style="{ background: !mobilestatus ? bannerBgColor : '' }"
      >
        <div :class="$style['home__banner-radial']">
          <div :class="$style['home__banner-circle']" @click="linkDetail(selectedItem.slug)">
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

      <div
        v-for="(item, idx) in bannerItems"
        :key="idx"
        :class="[
          $style['home__banner-bg'],
          $style[idx === oldSelectedItem.idx ? 'home__banner-bg--hidden' : '']
        ]"
        :style="{
          background: !mobilestatus && idx === oldSelectedItem.idx ? oldBannerBgColor : ''
        }"
      >
        <div :class="$style['home__banner-radial']">
          <div :class="$style['home__banner-circle']" @click="linkDetail(oldSelectedItem.slug)">
            <div :class="$style['home__banner-logo']">
              <img :src="oldSelectedItem.src" :alt="oldSelectedItem.alt" width="127" height="30" />
            </div>
            <div
              :class="$style['home__banner-product']"
              :style="{ backgroundImage: `url(${oldSelectedItem.product})` }"
            ></div>
          </div>
          <p>{{ oldSelectedItem.name }}</p>
        </div>
      </div>
    </div>
    <div :class="$style['home__banner-elipse1']" :style="{ background: elipseColor[0] }"></div>
    <div :class="$style['home__banner-elipse2']" :style="{ background: elipseColor[1] }"></div>
    <div :class="$style['home__banner-elipse3']" :style="{ background: elipseColor[2] }"></div>
    <div :class="$style['home__banner-elipse4']" :style="{ background: elipseColor[3] }"></div>
  </div>
  <div :class="$style.home__bannerMB">
    <div
      id="list_item"
      :class="$style['home__bannerMB-list']"
      :style="{ width: widthComputed, transform: 'translateX(' + tranfX + 'px)' }"
      @touchstart="handleTouchstart"
      @touchend="handleTouchend"
    >
      <div
        :class="$style['home__bannerMB-item']"
        v-for="(item, index) in bannerItems"
        :key="index"
        :style="{ width: widthItemComputed }"
      >
        <div :class="$style['home__bannerMB-left']">
          <h2>{{ content.title }}</h2>
          <p>{{ content.context }}</p>
        </div>
        <div :class="$style['home__bannerMB-right']">
          <div v-if="showBannerBg" :class="$style['home__bannerMB-bg']">
            <div :class="$style['home__bannerMB-radial']">
              <div :class="$style['home__bannerMB-circle']" @click="linkDetail(item.slug)">
                <div :class="$style['home__bannerMB-logo']">
                  <img :src="item.src" :alt="item.alt" width="60" height="30" />
                </div>
                <div
                  :class="$style['home__bannerMB-product']"
                  :style="{ backgroundImage: `url(${item.product})` }"
                ></div>
              </div>
              <p>{{ item.name }}</p>
            </div>
          </div>
        </div>
        <div
          :class="$style['home__bannerMB-item-elipse5']"
          :style="{ background: elipseColorMB[index][0] }"
        ></div>
        <div
          :class="$style['home__bannerMB-item-elipse6']"
          :style="{ background: elipseColorMB[index][1] }"
        ></div>
        <div
          :class="$style['home__bannerMB-item-elipse7']"
          :style="{ background: elipseColorMB[index][2] }"
        ></div>
        <div
          :class="$style['home__bannerMB-item-elipse8']"
          :style="{ background: elipseColorMB[index][3] }"
        ></div>
      </div>
    </div>
    <div :class="$style['home__bannerMB-active']">
      <div
        :class="$style['home__bannerMB-circle']"
        v-for="index in bannerItems.length"
        :id="index.toString()"
        :key="index"
        :style="{ backgroundColor: index - 1 === circleActive ? '#2696E9' : '' }"
      ></div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './HomeBanner.module.scss';
</style>

<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faSortDown, faXmark, faArrowDownZA, faTags } from '@fortawesome/free-solid-svg-icons';
import { ref, computed, onMounted, watch } from 'vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface Item {
  id: string;
  name: string;
  slug: string;
  createAt: string;
}

const deps = ref([]);

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'update-slug', data: { listrs: Item[] }): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-sort', data: { sort: string }): void;
}>();

//Array Available
const availableSorts = ref<string[]>(['Mới nhất', 'Phổ biến']);
const availableItems = ref<Item[]>([]);
const selectedItems = ref<Item[]>([]);

const { response } = useAxios<DataResponse>('get', '/tags', {}, {}, deps.value);
watch(response, () => {
  availableItems.value = response.value?.data.map((item: Item) => {
    return {
      name: item.name,
      slug: item.slug
    };
  });
});

const activeBtn = ref(-1);
const activeSort = ref(0);
const activeIndex = ref(0);
const lineWidth = ref(0);
const activeTab = ref<HTMLElement | null>(null);

//Open and Close Modal
const isCustomSelectListOpen = ref(false);
const isCustomSelectListOpen1 = ref(false);

//Change Status Select Box
const handleSelect = () => {
  activeBtn.value = 2;
  isCustomSelectListOpen.value = !isCustomSelectListOpen.value;
};

//Change Status Sort Box
const handleSort = () => {
  activeBtn.value = 1;
  isCustomSelectListOpen1.value = !isCustomSelectListOpen1.value;
};

//Handle when click overlay behind
const handleOverlay = () => {
  isCustomSelectListOpen.value = false;
  isCustomSelectListOpen1.value = false;
};

const selectItem1 = (index: number) => {
  activeSort.value = index;

  emits('update-sort', {
    sort: availableSorts.value[index]
  });

  setTimeout(() => {
    isCustomSelectListOpen1.value = false;
  }, 250);
};

const selectItem = (item: Item) => {
  selectedItems.value.push(item);
  availableItems.value = availableItems.value.filter((i) => i !== item);
  isCustomSelectListOpen.value = false; //Close Modal
  emits('update-slug', {
    listrs: selectedItems.value
  });
};

const removeItem = (item: Item) => {
  selectedItems.value = selectedItems.value.filter((i) => i !== item);
  availableItems.value.push(item);
  emits('update-slug', {
    listrs: selectedItems.value
  });
};

//Calculate active tab
const activeTabWidth = computed(() => {
  if (activeTab.value) {
    return activeTab.value.offsetWidth;
  }
  return 0;
});

//Transform line
const lineTransform = computed(() => {
  if (activeTab.value) {
    const activeTabWidthValue = activeTabWidth.value;
    const tabWidth = activeTabWidthValue * 0.6;
    const activeTabOffsetLeft = activeTab.value.offsetLeft;
    const leftOffset = activeTabOffsetLeft + (activeTabWidthValue - tabWidth) / 2 - 14;
    return `translateX(${leftOffset}px)`;
  }
  return '';
});

const moveLine = (index: number) => {
  activeIndex.value = index;

  emits('update-sort', {
    sort: availableSorts.value[index]
  });

  setTimeout(() => {
    activeTab.value = document.getElementById('tabActive');
    lineWidth.value = activeTabWidth.value * 0.6;
  }, 0);
};

watch(activeIndex, () => {
  activeTab.value = document.getElementById('tabActive');
  lineWidth.value = activeTabWidth.value * 0.6;
});

onMounted(() => {
  activeTab.value = document.getElementById('tabActive');
  lineWidth.value = activeTabWidth.value * 0.6;
});
</script>

<template>
  <div :class="$style.news__control">
    <div :class="$style['news__control-left']">
      <div :class="$style['news__control-heading']">
        <h4>Tin tức nổi bật</h4>
        <div :class="$style['news__control-border']"></div>
        <div :class="$style['news__control-tabs']">
          <div
            :id="activeIndex === 0 ? 'tabActive' : ''"
            :class="$style[activeIndex === 0 ? 'news__tabs-item--active' : 'news__tabs-item']"
            @click="moveLine(0)"
          >
            Mới nhất
          </div>
          <div
            :id="activeIndex === 1 ? 'tabActive' : ''"
            :class="$style[activeIndex === 1 ? 'news__tabs-item--active' : 'news__tabs-item']"
            @click="moveLine(1)"
          >
            Phổ biến
          </div>
          <div
            id="line_active"
            :class="$style['news__tabs-line']"
            :style="{ transform: lineTransform, width: `${lineWidth}px` }"
          ></div>
        </div>
      </div>
    </div>
    <div :class="$style['news__control-right']">
      <div :class="$style['news__control-tag']">
        <h4>TAGS</h4>
        <div :class="$style['custom-select']" v-on:click="handleSelect">
          <div :class="$style['custom-select-item']">Chọn</div>
          <font-awesome-icon :icon="faSortDown" :class="$style['select_ic']" />
          <div :class="$style['custom-select-list']" v-show="isCustomSelectListOpen">
            <ul>
              <li
                v-for="(item, index) in availableItems"
                :key="index"
                @click.stop="selectItem(item)"
              >
                {{ item.name }}
              </li>
            </ul>
          </div>
        </div>
        <div :class="$style['news__list-active']">
          <div
            :class="$style['news__list-item']"
            v-for="(item, index) in selectedItems"
            :key="index"
          >
            {{ item.name }}
            <div :class="$style['news__item-cancel']" @click.stop="removeItem(item)">
              <font-awesome-icon :icon="faXmark" :class="$style['cancel_ic']" />
            </div>
          </div>
        </div>
      </div>
      <div :class="$style['news__control-tag-mb']">
        <button
          :class="$style['news__control-tag-btn']"
          :style="
            isCustomSelectListOpen1 ? 'background-color: #0088CC; color: white; z-index: 5' : ''
          "
          v-on:click="handleSort"
        >
          <font-awesome-icon :icon="faArrowDownZA" :class="$style['tag_ic']" />
          {{ availableSorts[activeSort] }}
          <div :class="$style['custom-select-list']" v-show="isCustomSelectListOpen1">
            <ul>
              <li
                v-for="(item, index) in availableSorts"
                :key="index"
                :style="activeSort === index ? 'background-color: #0088CC; color: white;' : ''"
                @click.stop="selectItem1(index)"
              >
                {{ item }}
              </li>
            </ul>
          </div>
        </button>
        <button
          :class="$style['news__control-tag-btn']"
          :style="
            isCustomSelectListOpen ? 'background-color: #0088CC; color: white; z-index: 5' : ''
          "
          v-on:click="handleSelect"
        >
          <font-awesome-icon :icon="faTags" :class="$style['tag_ic']" /> Tags
          <div :class="$style['custom-select-list']" v-show="isCustomSelectListOpen">
            <ul>
              <li
                v-for="(item, index) in availableItems"
                :key="index"
                @click.stop="selectItem(item)"
              >
                {{ item.name }}
              </li>
            </ul>
          </div>
        </button>
        <div :class="$style['news__list-active']" v-show="selectedItems.length > 0">
          <div :class="$style['news__list-left']">
            <h4>TAGS</h4>
          </div>
          <div :class="$style['news__list-right']">
            <div
              :class="$style['news__list-item']"
              v-for="(item, index) in selectedItems"
              :key="index"
            >
              {{ item.name }}
              <div :class="$style['news__item-cancel']" @click.stop="removeItem(item)">
                <font-awesome-icon :icon="faXmark" :class="$style['cancel_ic']" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div
    :class="$style['overlay']"
    v-show="isCustomSelectListOpen || isCustomSelectListOpen1"
    v-on:click="handleOverlay"
  ></div>
</template>

<style module scoped lang="scss">
@import './NewsControl.module.scss';
</style>

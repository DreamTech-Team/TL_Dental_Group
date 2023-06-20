<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faSortDown, faXmark } from '@fortawesome/free-solid-svg-icons';
import { ref, computed, onMounted, watch } from 'vue';

const activeIndex = ref(0);
const lineWidth = ref(0);
const activeTab = ref<HTMLElement | null>(null);
//Open and Close Modal
const isCustomSelectListOpen = ref(false);

//Array Available
const availableItems = ref<string[]>(['Hoạt động', 'Tin tức', 'Tư vấn', 'Hội thảo']);
const selectedItems = ref<string[]>([]);

//Change Status Select Box
const clickHandle = () => {
  isCustomSelectListOpen.value = !isCustomSelectListOpen.value;
};

const SelectItem = (item: string) => {
  selectedItems.value.push(item);
  availableItems.value = availableItems.value.filter((i) => i !== item);
  isCustomSelectListOpen.value = false; //Close Modal
};

const RemoveItem = (item: string) => {
  selectedItems.value = selectedItems.value.filter((i) => i !== item);
  availableItems.value.push(item);
};

//Calculate active tab
const activeTabWidth = computed(() => {
  if (activeTab.value) {
    console.log(activeTab.value.offsetWidth);

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
            Tên tin tức từ A đến Z
          </div>
          <div
            :id="activeIndex === 2 ? 'tabActive' : ''"
            :class="$style[activeIndex === 2 ? 'news__tabs-item--active' : 'news__tabs-item']"
            @click="moveLine(2)"
          >
            Tên tin tức từ Z đến A
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
        <div :class="$style['custom-select']" v-on:click="clickHandle">
          <div :class="$style['custom-select-item']">Chọn</div>
          <font-awesome-icon :icon="faSortDown" :class="$style['select_ic']" />
          <div :class="$style['custom-select-list']" v-show="isCustomSelectListOpen">
            <ul>
              <li
                v-for="(item, index) in availableItems"
                :key="index"
                @click.stop="SelectItem(item)"
              >
                {{ item }}
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
            {{ item }}
            <div :class="$style['news__item-cancel']" @click.stop="RemoveItem(item)">
              <font-awesome-icon :icon="faXmark" :class="$style['cancel_ic']" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './NewsControl.module.scss';
</style>

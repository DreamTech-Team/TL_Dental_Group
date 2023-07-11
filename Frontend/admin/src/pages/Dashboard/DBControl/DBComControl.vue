<script setup lang="ts">
import Icon1 from '@/assets/icons/IconDB1.png';
import Icon2 from '@/assets/icons/IconDB2.png';
import Icon3 from '@/assets/icons/IconDB3.png';
import Icon4 from '@/assets/icons/IconDB4.png';
import { ref, computed, onMounted, watch } from 'vue';

const activeIndex = ref(0);
const lineWidth = ref(0);
const activeTab = ref<HTMLElement | null>(null);

const activeTabWidth = computed(() => {
  if (activeTab.value) {
    return activeTab.value.offsetWidth;
  }
  return 0;
});

const lineTransform = computed(() => {
  if (activeTab.value) {
    const activeTabWidthValue = activeTabWidth.value;
    const tabWidth = activeTabWidthValue * 0.8;
    const activeTabOffsetLeft = activeTab.value.offsetLeft;
    const leftOffset = activeTabOffsetLeft + (activeTabWidthValue - tabWidth) / 2;
    return `translateX(${leftOffset}px)`;
  }
  return '';
});

const moveLine = (index: number) => {
  activeIndex.value = index;

  setTimeout(() => {
    activeTab.value = document.getElementById('tabActive');
    lineWidth.value = activeTabWidth.value * 0.8;
  }, 0);
};

const emit = defineEmits(['update-status']);
const updateStatus = (stt: string) => {
  emit('update-status', stt);
  if (stt === '1') {
    moveLine(0);
  }
  if (stt === '2') {
    moveLine(1);
  }
};

watch(activeIndex, () => {
  activeTab.value = document.getElementById('tabActive');
  lineWidth.value = activeTabWidth.value * 0.8;
});

onMounted(() => {
  activeTab.value = document.getElementById('tabActive');
  lineWidth.value = activeTabWidth.value * 0.8;
});
</script>
<template>
  <div :class="$style.dashboard_control">
    <div :class="$style.dashboard_control_upper">
      <div :class="$style['dashboard_control-item']" style="background-color: #ff9f43">
        <div :class="$style['dashboard_control-item-left']">
          <h3 :class="$style['dashboard_control-item-left-title']">2000</h3>
          <span>Sản phẩm</span>
        </div>
        <div :class="$style['dashboard_control-item-right']">
          <img :src="Icon1" style="width: 67.5px; height: 60px" />
        </div>
      </div>
      <div :class="$style['dashboard_control-item']" style="background-color: #00c2da">
        <div :class="$style['dashboard_control-item-left']">
          <h3 :class="$style['dashboard_control-item-left-title']">3</h3>
          <span>Đối tác</span>
        </div>
        <div :class="$style['dashboard_control-item-right']">
          <img :src="Icon2" style="width: 75px; height: 45px" />
        </div>
      </div>
      <div :class="$style['dashboard_control-item']" style="background-color: #1b2850">
        <div :class="$style['dashboard_control-item-left']">
          <h3 :class="$style['dashboard_control-item-left-title']">400</h3>
          <span>Hoạt động</span>
        </div>
        <div :class="$style['dashboard_control-item-right']">
          <img :src="Icon3" style="width: 60px; height: 52.5px" />
        </div>
      </div>
      <div :class="$style['dashboard_control-item']" style="background-color: #28c76f">
        <div :class="$style['dashboard_control-item-left']">
          <h3 :class="$style['dashboard_control-item-left-title']">10</h3>
          <span>Quản trị viên</span>
        </div>
        <div :class="$style['dashboard_control-item-right']">
          <img :src="Icon4" style="width: 74px; height: 60px" />
        </div>
      </div>
    </div>
    <div :class="$style.dashboard_control_down">
      <div
        :id="activeIndex === 0 ? 'tabActive' : ''"
        @click="() => updateStatus('1')"
        :style="{ color: activeIndex === 0 ? '#115ed3' : '' }"
      >
        Tổng quan công ty
      </div>
      <div
        :id="activeIndex === 1 ? 'tabActive' : ''"
        @click="() => updateStatus('2')"
        :style="{ color: activeIndex === 1 ? '#115ed3' : '' }"
      >
        Địa chỉ
      </div>
      <div
        id="line_active"
        :class="$style.dashboard_control_down_line"
        :style="{ transform: lineTransform, width: `${lineWidth}px` }"
      ></div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './DBComControl.module.scss';
</style>

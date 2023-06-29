<script setup lang="ts">
import { type PropType } from 'vue';
import { ref, onMounted, computed } from 'vue';

type FunctionScroll = (e: any) => void;

const props = defineProps({
  items: Object,
  style: String,
  onSelected: Number
});

// onMounted(() => {});
</script>

<template>
  <div
    v-for="(item, index) in props.items"
    :class="[$style[`container__card-${style}`], $style[`${style}-color-background-${index + 1}`]]"
    :key="item.title.content"
  >
    <div :class="$style['container__card-icon']" v-if="item.icon !== ''">
      <div
        :class="[
          $style['container__card-icon-block'],
          $style[`container__card-icon-block-${item.icon.style}`],
          $style[`${item.icon.style}`]
        ]"
      >
        <img :src="`${item.icon.link}`" alt="none" />
      </div>
    </div>
    <div :class="$style[`container__card-${style}-block`]">
      <transition name="nav-recruit">
        <div
          v-if="Number(index) === props.onSelected"
          :class="[$style[`container__card-${style}-title-active`]]"
        >
          <h3>{{ item.title.content }}</h3>
        </div>
        <div v-else :class="$style[`container__card-${style}-title`]">
          <h3>{{ item.title.content }}</h3>
        </div>
      </transition>
      <div>
        <div :class="$style[`container__card-${style}-content`]" v-if="item.content.content !== ''">
          {{ item.content.content }}
        </div>
        <div
          :class="$style[`container__card-${style}-content`]"
          v-else-if="item.content.style !== ''"
        >
          <ul>
            <li><span>Thứ 2 - 6 :</span> <span>08:00 - 17:00</span></li>
            <li><span>Thứ 2 - 6 :</span> <span>08:00 - 17:00</span></li>
            <li><span>Chủ Nhật :</span> <span>Nghỉ</span></li>
          </ul>
        </div>
      </div>
    </div>
    <div :class="$style['container__card-image']" v-if="item.image.link !== ''">
      <img :src="item.image.link" alt="none" />
    </div>
    <div v-if="props.style === 'type3'">
      <div :class="$style[`container__card-${style}-bg1`]"></div>
      <div :class="$style[`container__card-${style}-bg2`]">
        <div :class="$style[`container__card-${style}-bg2-block`]">
          <div :class="$style[`container__card-${style}-bg2-title`]">
            <h3>{{ item.title.content }}</h3>
          </div>
          <div>
            <div
              :class="$style[`container__card-${style}-bg2-content`]"
              v-if="item.content.content !== ''"
            >
              {{ item.content.content }}
            </div>
            <div
              :class="$style[`container__card-${style}-bg2-content`]"
              v-else-if="item.content.style !== ''"
            >
              <ul>
                <li><span>Thứ 2 - 6 :</span> <span>08:00 - 17:00</span></li>
                <li><span>Thứ 2 - 6 :</span> <span>08:00 - 17:00</span></li>
                <li><span>Chủ Nhật :</span> <span>Nghỉ</span></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentCard.module.scss';
</style>

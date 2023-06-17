<script setup lang="ts">
const props = defineProps({
  items: Object,
  style: String
});
</script>

<template>
  <div
    :class="$style[`container__card-${style}`]"
    v-for="item in props.items"
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
    <div class="">
      <div :class="$style[`container__card-title-${item.title.style}`]">
        <h3>{{ item.title.content }}</h3>
      </div>
      <div>
        <div
          :class="$style[`container__card-content-${item.title.style}`]"
          v-if="item.content.content !== ''"
        >
          {{ item.content.content }}
        </div>
        <div
          :class="$style[`container__card-content-${item.title.style}`]"
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
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentCard.module.scss';
</style>

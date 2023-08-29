<script setup lang="ts">
import { ref, onMounted, watch, type Ref, type PropType } from 'vue';

interface CardElementItem {
  icon: { link: string; style: string };
  title: { content: string; style: string };
  content: { content: string; style: string };
  image: { link: string; style: string };
}

const props = defineProps({
  items: { type: Object as unknown as PropType<CardElementItem[]>, required: true },
  content: { type: Object, required: false },
  style: { type: String, required: false },
  onSelected: { type: Number, required: false },
  handleScrollToTopOfStepRec: { type: Function, required: false }
});

const indexSelected = ref(0);
const onActive = ref(-1);
const listItems: Ref<CardElementItem[]> = ref(props.items || []);
const screenWidth = ref(true);

const selectedContent = (index: number) => {
  if (screenWidth.value) {
    if (index === 0 && props.handleScrollToTopOfStepRec) props.handleScrollToTopOfStepRec();
    else {
      indexSelected.value = index;
      const element = document.getElementById(`type5-${indexSelected.value}`);
      element?.scrollIntoView({ behavior: 'smooth', block: 'center', inline: 'nearest' });
    }
  } else {
    if (onActive.value !== index) onActive.value = index;
    else onActive.value = -1;
  }
};

const checkScreenWidth = () => {
  const currentWidth = window.innerWidth;
  screenWidth.value = currentWidth > 739;
};

watch(
  () => props.items,
  (value) => {
    listItems.value = value;
  }
);

onMounted(() => {
  // if (props.style === 'type3') console.log(props.items);
  window.addEventListener('resize', checkScreenWidth);
});
</script>

<template>
  <div
    v-for="(item, index) in listItems"
    :class="
      style !== 'type4'
        ? [
            $style[`container__card-${style}`],
            $style[`${style}-color-background-${index + 1}`],
            $style[`btn-${style}`]
          ]
        : 'container__card-responsive'
    "
    :key="index"
    @click="style === 'type4' ? selectedContent(index) : {}"
  >
    <div
      :id="`${style}-${index}`"
      :class="
        style === 'type4'
          ? [$style[`container__card-${style}`], $style[`${style}-color-background-${index + 1}`]]
          : $style[`${style}-block`]
      "
    >
      <div :class="$style['container__card-icon']" v-if="item.icon.link !== ''">
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
        <div
          :class="[
            $style[`container__card-${style}-title`],
            $style[
              Number(index) === props.onSelected ? `container__card-${style}-title-active` : ''
            ]
          ]"
        >
          <h3>{{ item.title.content }}</h3>
          <div
            id="icon-rotate"
            :class="[
              $style[`container__card-${style}-icon`],
              $style[onActive === Number(index) ? `container__card-${style}-icon-active` : '']
            ]"
            v-if="props.style === 'type4'"
          >
            <svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" fill="#000000">
              <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
              <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
              <g id="SVGRepo_iconCarrier">
                <path
                  fill="#000000"
                  d="M104.704 338.752a64 64 0 0 1 90.496 0l316.8 316.8 316.8-316.8a64 64 0 0 1 90.496 90.496L557.248 791.296a64 64 0 0 1-90.496 0L104.704 429.248a64 64 0 0 1 0-90.496z"
                ></path>
              </g>
            </svg>
          </div>
        </div>
        <div>
          <div
            :class="$style[`container__card-${style}-content`]"
            v-html="item.content.content"
          ></div>
        </div>
      </div>
      <div :class="$style['container__card-image']" v-if="item.image.style !== ''">
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
                v-html="item.content.content"
              ></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div
      :class="[
        $style['container__card-responsive-item'],
        $style[onActive === Number(index) ? 'container__card-responsive-item-active' : '']
      ]"
      v-for="(item2, index2) in props.content"
      :key="index2"
    >
      <recruitment-card v-if="index === Number(index2)" :items="[item2]" :style="'type5'" />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentCard.module.scss';
</style>

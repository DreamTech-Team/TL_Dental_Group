<!-- eslint-disable @typescript-eslint/no-explicit-any -->
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
  onSelected: { type: Number, required: false }
});

const indexSelected = ref(0);
const onActive = ref(-1);
const listItems: Ref<CardElementItem[]> = ref(props.items || []);

const selectedContent = (index: any) => {
  if (screen.width > 739) {
    indexSelected.value = index;
    const element = document.getElementById(`type5-${indexSelected.value}`);
    element?.scrollIntoView({ behavior: 'smooth', block: 'end', inline: 'nearest' });
  } else {
    if (onActive.value !== index) onActive.value = index;
    else onActive.value = -1;
  }
};

watch(
  () => props.items,
  (value) => {
    listItems.value = value;
  }
);

onMounted(() => {
  // if (props.style === 'type3') console.log(props.items);
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
          ></div>
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

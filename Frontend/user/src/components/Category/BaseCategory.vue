<script setup lang="ts">
import { ref, nextTick, toRefs } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCaretDown } from '@fortawesome/free-solid-svg-icons';
import { useDataRenderStore, saveActive, setAnnimation } from '@/stores/counter';

const { dataRender } = toRefs(useDataRenderStore());
const { selectedCategoryItem } = toRefs(saveActive());
const { isAnimationVisible } = toRefs(setAnnimation());

const saveState = saveActive();
const setAnni = setAnnimation();
const selectedItem = ref(-1);
const selectedCategory1 = ref();
const emit = defineEmits(['slug-category1', 'slug-category2']);

const toggleAnimation = (index: number) => {
  if (isAnimationVisible.value && selectedItem.value == index) {
    isAnimationVisible.value = false;
    setAnni.setAnnimationCategory(isAnimationVisible.value);
    selectedItem.value = -1;
  } else {
    isAnimationVisible.value = true;
    setAnni.setAnnimationCategory(isAnimationVisible.value);
    selectedItem.value = index;
  }
  if (isAnimationVisible.value) {
    nextTick(() => {
      const animationContainer = document.getElementById(`id-${index}`);
      const dropdownContainer = document.getElementById('dropdown-container');

      if (animationContainer && dropdownContainer) {
        const dropdownContainerRect = dropdownContainer.getBoundingClientRect();
        const animationContainerRect = animationContainer.getBoundingClientRect();

        if (
          animationContainerRect.top < dropdownContainerRect.top ||
          animationContainerRect.bottom > dropdownContainerRect.bottom
        ) {
          const scrollPosition = animationContainer.offsetTop - dropdownContainer.offsetTop;
          dropdownContainer.scrollTop = scrollPosition;
        }
      }
    });
  }
};

toggleAnimation(selectedCategoryItem.value.categoryIndex);

const idDefine = (index: number) => {
  return `id-${index}`;
};

const logAndSelectCategory1 = (categoryIndex: number) => {
  selectedCategory1.value = dataRender.value[categoryIndex].slug;
  console.log('Selected Category 1:', selectedCategory1.value);
  emit('slug-category1', selectedCategory1.value);
};

const logAndSelectCategory = (categoryIndex: number, itemIndex: number) => {
  saveState.setActiveCategory(selectedCategoryItem.value);
  const selectedSubCategory = dataRender.value[categoryIndex]?.data[itemIndex]; //category cấp 2
  const selectedCategory = dataRender.value[categoryIndex]; // Giá trị của category cấp 1
  console.log('Selected Category:', selectedCategory.slug);
  console.log('Selected Sub-Category:', selectedSubCategory.slug);
  emit('slug-category2', selectedSubCategory.slug);
  selectedCategoryItem.value = { categoryIndex, itemIndex };
};

const isSelectedCategory = (categoryIndex: number, itemIndex: number) => {
  return (
    selectedCategoryItem.value.categoryIndex === categoryIndex &&
    selectedCategoryItem.value.itemIndex === itemIndex
  );
};
</script>
<template>
  <div id="dropdown-container" :class="$style.category">
    <div :class="$style['category__title']">Danh mục</div>
    <div
      @click="logAndSelectCategory1(index)"
      :class="[$style['category__firstX']]"
      v-for="(item, index) in dataRender"
      :key="index"
    >
      <div
        @click="toggleAnimation(index)"
        :class="[
          $style['category__firstX--choose'],
          { [$style['category__firstX--active']]: isAnimationVisible && selectedItem === index }
        ]"
      >
        <p>{{ item.title }}</p>
        <font-awesome-icon :class="$style['category__firstX--choose-icon']" :icon="faCaretDown" />
      </div>
      <div
        :id="idDefine(index)"
        :class="[
          $style['category__firstX--animation'],
          {
            [$style['category__firstX--show-animation']]:
              isAnimationVisible && selectedItem === index
          }
        ]"
        ref="animationContainer"
      >
        <div
          @click="logAndSelectCategory(index, idx)"
          :class="[
            $style['category__second'],
            { [$style['category__second--selected']]: isSelectedCategory(index, idx) }
          ]"
          v-for="(item1, idx) in item.data"
          :key="idx"
        >
          {{ item1.name }}
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './Category.module.scss';
</style>

<script setup lang="ts">
import { ref, nextTick, toRefs, watch, onMounted, Ref } from 'vue';
import { useRouter } from 'vue-router';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCaretDown } from '@fortawesome/free-solid-svg-icons';
import { useDataRenderStore, saveActive, setAnnimation } from '@/stores/counter';
import convertDataCate from '@/utils/covertDataCate';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '../LoadingComponent/LoadingComponent.vue';

interface ListCategory2 {
  title: string;
  slug: string;
}

interface DataRender {
  title: string;
  slug: string;
  company: { name: string; slug: string; cate2: ListCategory2[] }[];
}

const dataCate = useDataRenderStore();
const saveState = saveActive();
const setAnni = setAnnimation();
// Lấy thông tin đang định tuyến từ Vue Router
const router = useRouter();
const valueChange = ref([]);
const dataRender = ref<DataRender[]>([]);
const isLoadingCategory = ref(false);

const { selectedItem, selectedItem2, selectedItem3, typeCate } = toRefs(saveActive());
const { isAnimationVisible, isAnimationVisible2 } = toRefs(setAnnimation());

const selectedCategory1 = ref('');
const selectedCategory2 = ref('');
const selectedCategory3 = ref('');
const animationContainer = ref<HTMLElement | null>(null);
const emit = defineEmits(['slug-category1', 'slug-category2', 'slug-category3']);

if (dataCate.dataRender.length === 0) {
  const { response, isLoading } = useAxios<DataResponse>('get', '/cate', {}, {}, valueChange.value);

  watch(response, () => {
    isLoadingCategory.value = isLoading.value;
    if (response.value?.data) {
      dataRender.value = convertDataCate.handleDataRender(response.value?.data, dataRender.value);
      dataCate.setDataRender(dataRender.value);
    }
  });
} else {
  dataRender.value = dataCate.dataRender;
}

// Hàm chung để xử lý hiển thị và cuộn
const toggleAnimationCommon = (
  index: number,
  idx: number | undefined,
  containerId: string,
  isAnimationVisibleValue: Ref<boolean>,
  setAnimationCategoryFn: (value: boolean) => void
) => {
  const isVisible = isAnimationVisibleValue.value;
  const isSelected =
    selectedItem.value === index && (idx !== undefined ? selectedItem2.value === idx : true);

  isAnimationVisibleValue.value = isVisible === isSelected ? !isVisible : true;
  setAnimationCategoryFn(isAnimationVisibleValue.value);
  saveState.setTypeCategory('notHeader');

  if (isAnimationVisibleValue.value) {
    nextTick(() => {
      const animationContainer = document.getElementById(
        `id-${index}${idx !== undefined ? `-${idx}` : ''}`
      );
      const dropdownContainer = document.getElementById(containerId);
      if (animationContainer && dropdownContainer) {
        const aRect = animationContainer.getBoundingClientRect();
        const dRect = dropdownContainer.getBoundingClientRect();

        if (aRect.top < dRect.top || aRect.bottom > dRect.bottom) {
          const scrollPos = animationContainer.offsetTop - dropdownContainer.offsetTop;
          dropdownContainer.scrollTop = scrollPos;
        }
      }
    });
  }
};

// Sử dụng hàm chung cho cate cấp 1
const toggleAnimation = (index: number) => {
  toggleAnimationCommon(
    index,
    undefined,
    'dropdown-container',
    isAnimationVisible,
    setAnni.setAnnimationCategory
  );
};

// Sử dụng hàm chung cho cate cấp 2
const toggleAnimation2 = (index: number, idx: number) => {
  toggleAnimationCommon(
    index,
    idx,
    'dropdown-container2',
    isAnimationVisible2,
    setAnni.setAnnimationCategory2
  );
  selectedItem3.value = isAnimationVisible2.value ? -1 : selectedItem3.value;
};

watch(typeCate, () => {
  if (typeCate.value !== 'notHeader') {
    isAnimationVisible.value = true;
    isAnimationVisible2.value = true;
  }
});

onMounted(() => {
  if (typeCate.value !== 'notHeader') {
    isAnimationVisible.value = true;
    isAnimationVisible2.value = true;
  }
});

const idDefine = (index: number, idx: number | undefined = undefined) => {
  return idx !== undefined ? `id-${index}-${idx}` : `id-${index}`;
};

// Đánh dấu cate được chọn (Hàm chung cho cate2 và cate3)
const isSelectedCategory = (categoryIndex: number, itemIndex: number, itemIndex3?: number) => {
  return (
    isAnimationVisible.value &&
    isAnimationVisible2.value &&
    selectedItem.value === categoryIndex &&
    selectedItem2.value === itemIndex &&
    (itemIndex3 === undefined || selectedItem3.value === itemIndex3)
  );
};

const emitCategorySlugs = (category1: string, category2 = '', category3 = '') => {
  emit('slug-category1', category1);
  emit('slug-category2', category2);
  emit('slug-category3', category3);
};

const logAndSelectCategory1 = (categoryIndex: number) => {
  const newCategory1 = dataRender.value[categoryIndex].slug;
  selectedCategory1.value = newCategory1;
  emitCategorySlugs(newCategory1);
  saveState.setActiveCategory(categoryIndex);
};

const logAndSelectCategory2 = (categoryIndex: number, itemIndex: number) => {
  saveState.setActiveCategory(categoryIndex);
  saveState.setTypeCategory('notHeader');
  const selectedCategory = dataRender.value[categoryIndex].slug;
  const selectedSubCategory = dataRender.value[categoryIndex].company[itemIndex].slug;
  saveState.setActiveCategory2(itemIndex);
  emitCategorySlugs(selectedCategory, selectedSubCategory);
};

const logAndSelectCategory3 = (categoryIndex: number, itemIndex: number, itemIndex3: number) => {
  saveState.setTypeCategory('notHeader');
  const selectedCategory = dataRender.value[categoryIndex].slug;
  const selectedSubCategory = dataRender.value[categoryIndex]?.company[itemIndex]?.slug;
  const selectedSubCategory3 =
    dataRender.value[categoryIndex]?.company[itemIndex]?.cate2[itemIndex3]?.slug;
  saveState.setActiveCategory3(itemIndex3);
  selectedCategory1.value = selectedCategory;
  selectedCategory2.value = selectedSubCategory;
  selectedCategory3.value = selectedSubCategory3;
  emitCategorySlugs(selectedCategory, selectedSubCategory, selectedSubCategory3);
  if (router.currentRoute.value.name !== 'sanpham') {
    router.push(
      `/sanpham?slug1=${selectedCategory}&slug2=${selectedSubCategory}&slug3=${selectedSubCategory3}`
    );
  }
};

watch([selectedCategory1], () => {
  selectedItem2.value = -1;

  const matchedIndex = dataRender.value.findIndex((item) => item.slug === selectedCategory1.value);
  matchedIndex !== -1
    ? ((selectedItem.value = matchedIndex), logAndSelectCategory1(selectedItem.value))
    : null;
});
</script>
<template>
  <div id="dropdown-container" :class="$style.category" v-if="!isLoadingCategory">
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
      <!-- category 2 -->
      <div
        id="dropdown-container2"
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
          @click.stop="logAndSelectCategory2(index, idx)"
          :class="[
            $style['category__second'],
            {
              [$style['category__second--selected']]:
                isAnimationVisible && selectedItem === index && selectedItem2 === idx
            }
          ]"
          v-for="(item1, idx) in item.company"
          :key="idx"
        >
          <div
            :id="idDefine(index, idx)"
            @click="toggleAnimation2(index, idx)"
            :class="[$style.category__item]"
          >
            {{ item1.name }}
            <div
              :style="{
                paddingLeft: '10px !important'
              }"
            >
              <font-awesome-icon
                :class="$style['category__firstX--choose-icon']"
                :icon="faCaretDown"
              />
            </div>
          </div>
          <!-- category 3 -->
          <div
            :class="[
              $style['category__firstX--animation'],
              {
                [$style['category__firstX--show-animation']]: isSelectedCategory(index, idx)
              }
            ]"
            ref="animationContainer2"
          >
            <div
              @click.stop="logAndSelectCategory3(index, idx, idx2)"
              :class="[
                $style['category__third'],
                {
                  [$style['category__third--selected']]: isSelectedCategory(index, idx, idx2)
                }
              ]"
              v-for="(item2, idx2) in item1.cate2"
              :key="idx2"
            >
              {{ item2.title }}
            </div>
          </div>
          <!-- end category 3 -->
        </div>
      </div>
      <!-- end category 2 -->
    </div>
  </div>
  <loading-component v-else />
</template>
<style module scoped lang="scss">
@import './Category.module.scss';
</style>

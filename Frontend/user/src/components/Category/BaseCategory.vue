<script setup lang="ts">
import { ref, nextTick, toRefs, watch, watchEffect } from 'vue';
// import { RouteRecordName, useRoute } from 'vue-router';
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
const { selectedItem, selectedItem2, selectedItem3, typeCate } = toRefs(saveActive());
const { isAnimationVisible, isAnimationVisible2 } = toRefs(setAnnimation());
const saveState = saveActive();

const setAnni = setAnnimation();
const selectedCategory1 = ref('');
const selectedCategory2 = ref('');
const selectedCategory3 = ref('');
const checkColor = ref(false);
const emit = defineEmits(['slug-category1', 'slug-category2', 'slug-category3']);
const animationContainer = ref<HTMLElement | null>(null);

// Lấy thông tin đang định tuyến từ Vue Router
const router = useRouter();

const valueChange = ref([]);
const dataRender = ref<DataRender[]>([]);
const isLoadingCategory = ref(false);

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

const toggleAnimation = (index: number) => {
  if (isAnimationVisible.value && selectedItem.value === index) {
    isAnimationVisible.value = false;
    setAnni.setAnnimationCategory(isAnimationVisible.value);
    saveState.setTypeCategory('notHeader');
  } else {
    isAnimationVisible.value = true;
    setAnni.setAnnimationCategory(isAnimationVisible.value);
    saveState.setTypeCategory('notHeader');
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

const toggleAnimation2 = (index: number, idx: number) => {
  if (isAnimationVisible2.value && selectedItem.value === index && selectedItem2.value === idx) {
    isAnimationVisible2.value = false;
    setAnni.setAnnimationCategory2(isAnimationVisible2.value);
    saveState.setTypeCategory('notHeader');
  } else {
    selectedItem3.value = -1;
    isAnimationVisible2.value = true;
    setAnni.setAnnimationCategory2(isAnimationVisible2.value);
    saveState.setTypeCategory('notHeader');
  }
  if (isAnimationVisible2.value) {
    nextTick(() => {
      // console.log(index + '----' + idx);
      const animationContainer = document.getElementById(`id-${index}-${idx}`);
      const dropdownContainer = document.getElementById('dropdown-container2');
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

watch(typeCate, () => {
  if (typeCate.value !== 'notHeader') {
    isAnimationVisible.value = true;
    isAnimationVisible2.value = true;

    if (typeCate.value.includes('cate2Header')) {
      console.log(typeCate.value[typeCate.value.length - 1]);
    }
  }
});

const idDefine = (index: number, idx: number | undefined = undefined) => {
  if (idx !== undefined) {
    return `id-${index}-${idx}`;
  }
  return `id-${index}`;
};

const isSelectedCategory2 = (categoryIndex: number, itemIndex: number) => {
  return (
    isAnimationVisible.value &&
    isAnimationVisible2.value &&
    selectedItem.value === categoryIndex &&
    selectedItem2.value === itemIndex
  );
};

const isSelectedCategory3 = (categoryIndex: number, itemIndex: number, itemIndex3: number) => {
  checkColor.value =
    isAnimationVisible.value &&
    isAnimationVisible2.value &&
    selectedItem.value === categoryIndex &&
    selectedItem2.value === itemIndex &&
    selectedItem3.value === itemIndex3;

  return checkColor.value;
};

const logAndSelectCategory1 = (categoryIndex: number) => {
  // Kiểm tra trang hiện tại
  const newCategory1 = dataRender.value[categoryIndex].slug;
  // Reset selectedCategory2 only if a new category 1 is selected
  if (newCategory1 !== selectedCategory1.value) {
    selectedCategory1.value = newCategory1;
    emit('slug-category1', selectedCategory1.value);
    emit('slug-category2', '');
    emit('slug-category3', '');
    // Đặt lại giá trị selectedItem để xóa màu category cấp 2 trước đó
    saveState.setActiveCategory(categoryIndex);
  }
};

// Hàm chọn category cấp 2
const logAndSelectCategory2 = (categoryIndex: number, itemIndex: number) => {
  // Lưu index của category cấp 2 được chọn
  saveState.setActiveCategory(categoryIndex);
  saveState.setActiveCategory2(itemIndex);
  saveState.setTypeCategory('notHeader');
  const selectedCategory = dataRender.value[categoryIndex].slug;
  const selectedSubCategory = dataRender.value[categoryIndex].company[itemIndex].slug;

  if (
    selectedCategory === selectedCategory1.value &&
    selectedSubCategory !== selectedCategory2.value
  ) {
    emit('slug-category1', selectedCategory);
    emit('slug-category2', selectedSubCategory);
    emit('slug-category3', '');
  }
};

const logAndSelectCategory3 = (categoryIndex: number, itemIndex: number, itemIndex3: number) => {
  saveState.setActiveCategory3(itemIndex3);
  saveState.setTypeCategory('notHeader');
  const selectedCategory = dataRender.value[categoryIndex].slug; // Giá trị của category cấp 1
  const selectedSubCategory = dataRender.value[categoryIndex]?.company[itemIndex]?.slug;
  const selectedSubCategory3 =
    dataRender.value[categoryIndex]?.company[itemIndex]?.cate2[itemIndex3]?.slug;

  if (selectedCategory === selectedCategory1.value) {
    selectedCategory1.value = selectedCategory;
    selectedCategory2.value = selectedSubCategory;
    selectedCategory3.value = selectedSubCategory3;
    emit('slug-category1', selectedCategory);
    emit('slug-category2', selectedSubCategory);
    emit('slug-category3', selectedSubCategory3);

    if (router.currentRoute.value.name !== 'sanpham') {
      // Chuyển hướng về trang sản phẩm và truyền dữ liệu qua URL
      router.push(
        `/sanpham?slug1=${selectedCategory}&slug2=${selectedSubCategory}&slug3=${selectedSubCategory3}`
      );
    }
  }
};

watch([selectedCategory1, selectedCategory2], () => {
  // selectedItem2.value = -1;

  const matchedIndex = dataRender.value.findIndex((item) => item.slug === selectedCategory1.value);
  if (matchedIndex !== -1) {
    selectedItem.value = matchedIndex;
    logAndSelectCategory1(selectedItem.value);
  } else {
    console.log(`Category "${selectedCategory1.value}" not found in dataRender`);
  }
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
          @click="logAndSelectCategory2(index, idx)"
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
            <font-awesome-icon
              :class="$style['category__firstX--choose-icon']"
              :icon="faCaretDown"
            />
          </div>
          <!-- category 3 -->
          <div
            :class="[
              $style['category__firstX--animation'],
              {
                [$style['category__firstX--show-animation']]: isSelectedCategory2(index, idx)
              }
            ]"
            ref="animationContainer2"
          >
            <div
              @click="logAndSelectCategory3(index, idx, idx2)"
              :class="[
                $style['category__third'],
                {
                  [$style['category__third--selected']]: isSelectedCategory3(index, idx, idx2)
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

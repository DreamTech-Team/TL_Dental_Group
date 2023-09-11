<script setup lang="ts">
import { ref, nextTick, toRefs, watch } from 'vue';
// import { RouteRecordName, useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCaretDown, faCircleArrowDown } from '@fortawesome/free-solid-svg-icons';
import { useDataRenderStore, saveActive, setAnnimation } from '@/stores/counter';
import convertDataCate from '@/utils/covertDataCate';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '../LoadingComponent/LoadingComponent.vue';

interface ListCategory1 {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
}

interface ListCategory2 {
  title: string;
  slug: string;
}
interface ListCategory3 {
  id: string;
  title: string;
  slug: string;
}

interface ListCategories {
  id: string;
  cate1Id: ListCategory1;
  cate2Id: ListCategory2;
  cate3Id: ListCategory3;
}

interface DataRender {
  title: string;
  slug: string;
  company: { name: string; slug: string; cate2: ListCategory2[] }[];
}

const dataCate = useDataRenderStore();
const { selectedCategoryItem, typeCate } = toRefs(saveActive());
const { isAnimationVisible } = toRefs(setAnnimation());
const saveState = saveActive();

const setAnni = setAnnimation();
const selectedItem = ref(-1);
const selectedItem2 = ref(-1);
const selectedItem3 = ref(-1);
const selectedCategory1 = ref();
const selectedCategory2 = ref('');
const selectedCategory3 = ref('');
const isAnimationVisible1 = ref(false);
const isAnimationVisible2 = ref(false);
const emit = defineEmits(['slug-category1', 'slug-category2', 'slug-category3']);
// Tạo biến lưu index của category cấp 1 được chọn và category cấp 2 được chọn
const selectedCategory1Index = ref(-1);
const selectedCategory2Index = ref(-1);
// Lấy thông tin đang định tuyến từ Vue Router
const router = useRouter();

const valueChange = ref([]);
const listCategory1 = ref<ListCategory1[]>([]);
const listCategory2 = ref<ListCategory2[]>([]);
const listCategory3 = ref<ListCategory2[]>([]);
const dataRender = ref<DataRender[]>([]);
const isLoadingCategory = ref(false);

if (dataCate.dataRender.length === 0) {
  const { response, isLoading } = useAxios<DataResponse>('get', '/cate', {}, {}, valueChange.value);

  watch(response, () => {
    isLoadingCategory.value = isLoading.value;

    if (response.value?.data) {
      // response.value?.data.forEach((item: ListCategories) => {
      //   listCategory1.value.push(item.cate1Id);
      //   listCategory2.value.push(item.cate2Id);
      //   listCategory3.value.push(item.cate2Id);
      // });
      // console.log(listCategory2.value);

      dataRender.value = convertDataCate.handleDataRender(response.value?.data, dataRender.value);

      dataCate.setDataRender(dataRender.value);
    }
  });
} else {
  dataRender.value = dataCate.dataRender;
}

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

const toggleAnimation2 = (index: number, idx: number) => {
  if (isAnimationVisible2.value && selectedItem2.value === idx) {
    isAnimationVisible2.value = false;
    console.log('Vinh 2: ' + selectedItem2.value);
    // isAnimationVisible2.value = false;
    // setAnni.setAnnimationCategory(isAnimationVisible2.value);
    selectedItem2.value = -1;
    selectedItem3.value = -1;
  } else {
    console.log('Vinh 22: ' + selectedItem2.value);
    selectedItem3.value = -1;

    isAnimationVisible2.value = true;
    // setAnni.setAnnimationCategory(isAnimationVisible2.value);
    selectedItem2.value = idx;
    console.log(selectedItem2.value);
  }
  if (isAnimationVisible2.value) {
    console.log('Next: ' + isAnimationVisible2.value);

    nextTick(() => {
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

toggleAnimation(selectedCategoryItem.value.categoryIndex);
// toggleAnimation2(selectedCategoryItem.value.categoryIndex, selectedCategoryItem.value.itemIndex);

watch(typeCate, () => {
  if (typeCate.value !== 'notHeader') {
    toggleAnimation(selectedCategoryItem.value.categoryIndex);

    if (typeCate.value.includes('cate2Header')) {
      console.log(typeCate.value[typeCate.value.length - 1]);
    }
  }
});

const idDefine = (index: number, idx: number | undefined = undefined) => {
  if (idx !== undefined) {
    // console.log(`id-${index}-${idx}`);
    return `id-${index}-${idx}`;
  }
  // console.log(`id-${index}`);
  return `id-${index}`;
};

const logAndSelectCategory1 = (categoryIndex: number) => {
  console.log('selectedCategory1');

  // Kiểm tra trang hiện tại
  const newCategory1 = dataRender.value[categoryIndex].slug;
  // Reset selectedCategory2 only if a new category 1 is selected
  if (newCategory1 !== selectedCategory1.value) {
    selectedCategory1.value = newCategory1;
    selectedCategory2.value = ''; // Reset selectedCategory2
    emit('slug-category1', selectedCategory1.value);
    emit('slug-category2', selectedCategory2.value);
    emit('slug-category3', selectedCategory3.value);

    // Đặt lại giá trị selectedItem để xóa màu category cấp 2 trước đó
    selectedItem.value = categoryIndex;
  }
};

// Hàm chọn category cấp 2
const logAndSelectCategory2 = (categoryIndex: number, itemIndex: number) => {
  // saveState.setActiveCategory({ categoryIndex, itemIndex });
  saveState.setTypeCategory('notHeader');
  console.log('categoryIndex: ' + categoryIndex);
  console.log('itemIndex' + itemIndex);

  // Lưu index của category cấp 2 được chọn
  selectedItem2.value = itemIndex;

  // const selectedSubCategory = dataRender.value[categoryIndex]?.data[itemIndex]; //category cấp 2
  // const selectedCategory = dataRender.value[categoryIndex]; // Giá trị của category cấp 1
  // emit('slug-category2', selectedSubCategory.slug);
  // selectedCategory2.value = selectedSubCategory.slug; // Update selectedCategory2

  if (router.currentRoute.value.name !== 'sanpham') {
    // Chuyển hướng về trang sản phẩm và truyền dữ liệu qua URL
    router.push(`/sanpham?slug1=${selectedCategory1.value}&slug2=${selectedCategory2.value}`);
  }
};

const logAndSelectCategory3 = (categoryIndex: number, itemIndex: number, itemIndex3: number) => {
  selectedItem3.value = itemIndex3;
  console.log('itemIndex1: ' + categoryIndex);
  console.log('itemIndex2' + itemIndex);
  console.log('itemIndex3' + itemIndex3);
};

const isSelectedCategory = (categoryIndex: number, itemIndex: number) => {
  // const selectedSubCategory = dataRender.value[categoryIndex]?.data[itemIndex]; //category cấp 2

  return (
    selectedCategoryItem.value.categoryIndex === categoryIndex &&
    selectedCategoryItem.value.itemIndex === itemIndex
  );
};

const isSelectedCategory3 = (categoryIndex: number, itemIndex: number, itemIndex3: number) => {
  return (
    selectedCategoryItem.value.categoryIndex === categoryIndex &&
    selectedCategoryItem.value.itemIndex === itemIndex &&
    selectedItem3.value === itemIndex3
  );
};

watch([selectedCategory1, selectedCategory2], () => {
  console.log('watch selectedCategory1');
  selectedItem2.value = -1;
  selectedCategoryItem.value.itemIndex = -1;

  const matchedIndex = dataRender.value.findIndex((item) => item.slug === selectedCategory1.value);
  if (matchedIndex !== -1) {
    selectedItem.value = matchedIndex;
    logAndSelectCategory1(selectedItem.value);
  } else {
    console.log(`Category "${selectedCategory1.value}" not found in dataRender`);
  }
});

watch(isAnimationVisible2, () => {
  console.log('ccccc' + isAnimationVisible2.value);
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
            { [$style['category__second--selected']]: isSelectedCategory(index, idx) }
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
                [$style['category__firstX--show-animation']]:
                  isAnimationVisible2 && selectedItem2 === idx
              }
            ]"
            ref="animationContainer2"
          >
            <div
              @click="logAndSelectCategory3(index, idx, idx2)"
              :class="[
                $style['category__third'],
                { [$style['category__third--selected']]: isSelectedCategory3(index, idx, idx2) }
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

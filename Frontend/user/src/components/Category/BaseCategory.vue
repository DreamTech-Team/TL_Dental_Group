<script setup lang="ts">
import { ref, nextTick, toRefs, watch } from 'vue';
// import { RouteRecordName, useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCaretDown } from '@fortawesome/free-solid-svg-icons';
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
  id: string;
  title: string;
  slug: string;
}

interface ListCategories {
  id: string;
  cate1Id: ListCategory1;
  cate2Id: ListCategory2;
}

interface DataRender {
  title: string;
  slug: string;
  data: { name: string; slug: string }[];
}

const dataCate = useDataRenderStore();
const { selectedCategoryItem } = toRefs(saveActive());
const { isAnimationVisible } = toRefs(setAnnimation());
const saveState = saveActive();

const setAnni = setAnnimation();
const selectedItem = ref(-1);
const selectedCategory1 = ref();
const selectedCategory2 = ref('');
const emit = defineEmits(['slug-category1', 'slug-category2']);
// Tạo biến lưu index của category cấp 1 được chọn và category cấp 2 được chọn
const selectedCategory1Index = ref(-1);
const selectedCategory2Index = ref(-1);
// Lấy thông tin đang định tuyến từ Vue Router
const router = useRouter();

const valueChange = ref([]);
const listCategory1 = ref<ListCategory1[]>([]);
const listCategory2 = ref<ListCategory2[]>([]);
const dataRender = ref<DataRender[]>([]);
const isLoadingCategory = ref(false);

if (dataCate.dataRender.length === 0) {
  const { response, isLoading } = useAxios<DataResponse>('get', '/cate', {}, {}, valueChange.value);

  watch(response, () => {
    isLoadingCategory.value = isLoading.value;

    if (response.value?.data) {
      response.value?.data.forEach((item: ListCategories) => {
        listCategory1.value.push(item.cate1Id);
        listCategory2.value.push(item.cate2Id);
      });

      dataRender.value = convertDataCate.covertBase64ToBlob(
        listCategory1.value,
        listCategory2.value,
        dataRender.value
      );

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

toggleAnimation(selectedCategoryItem.value.categoryIndex);

watch(selectedCategoryItem, (newValue, oldValue) => {
  toggleAnimation(newValue.categoryIndex);
});

const idDefine = (index: number) => {
  return `id-${index}`;
};

const logAndSelectCategory1 = (categoryIndex: number) => {
  // Kiểm tra trang hiện tại
  const newCategory1 = dataRender.value[categoryIndex].slug;

  if (newCategory1 !== selectedCategory1.value) {
    selectedCategory1.value = newCategory1;

    // Reset index của category cấp 2 khi chọn một category cấp 1 khác
    selectedCategory2.value = '';
    selectedCategory2Index.value = -1;

    selectedCategory1Index.value = categoryIndex;

    emit('slug-category1', selectedCategory1.value);
    emit('slug-category2', selectedCategory2.value);
  }
};

// Hàm chọn category cấp 2
const logAndSelectCategory = (categoryIndex: number, itemIndex: number) => {
  saveState.setActiveCategory({ categoryIndex, itemIndex });

  // Lưu index của category cấp 2 được chọn
  selectedCategory2Index.value = itemIndex;

  const selectedSubCategory = dataRender.value[categoryIndex]?.data[itemIndex]; //category cấp 2
  const selectedCategory = dataRender.value[categoryIndex]; // Giá trị của category cấp 1

  emit('slug-category2', selectedSubCategory.slug);
  selectedCategory2.value = selectedSubCategory.slug; // Update selectedCategory2

  if (router.currentRoute.value.name !== 'sanpham') {
    // Chuyển hướng về trang sản phẩm và truyền dữ liệu qua URL
    router.push(`/sanpham?slug1=${selectedCategory1.value}&slug2=${selectedCategory2.value}`);
  }
};

const isSelectedCategory = (categoryIndex: number, itemIndex: number) => {
  return (
    selectedCategoryItem.value.categoryIndex === categoryIndex &&
    selectedCategoryItem.value.itemIndex === itemIndex
  );
};

watch([selectedCategory1, selectedCategory2], () => {
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
  <loading-component v-else />
</template>

<style module scoped lang="scss">
@import './Category.module.scss';
</style>

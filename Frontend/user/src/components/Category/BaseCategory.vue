<script setup lang="ts">
import { ref, nextTick, watch } from 'vue';
import IcDownCategory from '@/assets/icons/IcsortDown.svg';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCaretDown } from '@fortawesome/free-solid-svg-icons';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { data } from '@/pages/RecruitmentDetails/RecruitmentDetailsHandle';

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
  data: { name: string }[];
}

const valueChange = ref([]);
const isAnimationVisible = ref(false);
const selectedItem = ref(-1);
const selectedCategoryItem = ref({ categoryIndex: -1, itemIndex: -1 });
const dataRender = ref<DataRender[]>([]);
const listCategories = ref<ListCategories[]>([]);
const listCategory1 = ref<ListCategory1[]>([]);
const listCategory2 = ref<ListCategory2[]>([]);

const { response } = useAxios<DataResponse>('get', '/cate', {}, {}, valueChange.value);

watch(response, () => {
  if (response.value?.data) {
    response.value?.data.forEach((item: ListCategories) => {
      listCategory1.value.push(item.cate1Id);
      listCategory2.value.push(item.cate2Id);
    });

    const uniqueElementsSet = new Set<string>();
    const duplicatesPositions: { [key: string]: number[] } = {};
    const duplicatesPositions1: { [key: string]: number[] } = {};

    listCategory1.value.forEach((item, index) => {
      const { id } = item;

      if (uniqueElementsSet.has(id)) {
        if (!duplicatesPositions[id]) {
          duplicatesPositions[id] = [index];
        } else {
          duplicatesPositions[id].push(index);
        }
      } else {
        uniqueElementsSet.add(id);
        duplicatesPositions1[id] = [index];
        duplicatesPositions1[id].push(index);
      }
    });

    Object.keys(duplicatesPositions).forEach((id) => {
      duplicatesPositions[id].push(duplicatesPositions1[id][0]);
      delete duplicatesPositions1[id];
    });

    const uniqueElementsArray = Array.from(uniqueElementsSet).map((id) => {
      return listCategory1.value.find((item) => item.id === id);
    });

    uniqueElementsArray.forEach((item) => {
      if (item) {
        const object = {
          title: '',
          data: [
            {
              name: ''
            }
          ]
        };
        const dataCate2: { name: string }[] = [];

        if (duplicatesPositions1[item.id]) {
          object.title = item.title;

          if (listCategory2.value[duplicatesPositions1[item.id][0]] !== null) {
            dataCate2.push({ name: listCategory2.value[duplicatesPositions1[item.id][0]].title });
          }

          object.data = dataCate2;

          dataRender.value.push(object);
        }
        if (duplicatesPositions[item.id]) {
          object.title = item.title;

          duplicatesPositions[item.id].forEach((replace) => {
            if (listCategory2.value[replace] !== null) {
              dataCate2.push({ name: listCategory2.value[replace].title });
            }
          });

          object.data = dataCate2;

          dataRender.value.push(object);
        }
      }
    });
  }
});

const toggleAnimation = (index: number) => {
  if (isAnimationVisible.value && selectedItem.value == index) {
    isAnimationVisible.value = false;
    selectedItem.value = -1;
  } else {
    isAnimationVisible.value = true;
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

const idDefine = (index: number) => {
  return `id-${index}`;
};

const logAndSelectCategory = (categoryIndex: number, itemIndex: number) => {
  console.log(dataRender.value[categoryIndex].data[itemIndex]);
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
    <div :class="[$style['category__firstX']]" v-for="(item, index) in dataRender" :key="index">
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

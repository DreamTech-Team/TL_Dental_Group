import { ref } from 'vue';
import { defineStore } from 'pinia';

interface DataRender {
  title: string;
  data: { name: string }[];
}
interface Active {
  categoryIndex: number;
  itemIndex: number;
}

export const useDataRenderStore = defineStore('dataRender', () => {
  const dataRender = ref<DataRender[]>([]);

  const setDataRender = (newDataRender: DataRender[]) => {
    dataRender.value = newDataRender;
  };

  return { dataRender, setDataRender };
});

export const saveActive = defineStore('saveActiveCategory', () => {
  const selectedCategoryItem = ref<Active>({
    categoryIndex: -1,
    itemIndex: -1
  });

  const setActiveCategory = (newActive: Active) => {
    selectedCategoryItem.value = { ...newActive };

    // console.log(active.value);
  };

  return { selectedCategoryItem, setActiveCategory };
});

export const setAnnimation = defineStore('setAnnimation', () => {
  const isAnimationVisible = ref(false);

  const setAnnimationCategory = (newActive: boolean) => {
    isAnimationVisible.value = newActive;

    console.log(isAnimationVisible.value);
  };

  return { isAnimationVisible, setAnnimationCategory };
});

// export const useCounterStore = defineStore('counter', () => {
//   const count = ref(0);
//   const doubleCount = computed(() => count.value * 2);
//   function increment() {
//     count.value++;
//   }

//   return { count, doubleCount, increment };
// });

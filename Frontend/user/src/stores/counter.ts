import { ref } from 'vue';
import { defineStore } from 'pinia';

interface DataRender {
  title: string;
  slug: string;
  data: { name: string; slug: string }[];
}
interface Active {
  categoryIndex: number;
  itemIndex: number;
}
interface Info {
  address: string;
  phoneNumber: string;
  hotline: string;
  mapLink: string;
  image: string;
  mapIframe: string;
}

interface Contact {
  email: {
    content: string;
  };
  facebook: {
    content: string;
  };
  zalo: {
    content: string;
  };
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
    console.log(newActive);

    selectedCategoryItem.value = { ...newActive };
  };

  return { selectedCategoryItem, setActiveCategory };
});

export const setAnnimation = defineStore('setAnnimation', () => {
  const isAnimationVisible = ref(false);

  const setAnnimationCategory = (newActive: boolean) => {
    isAnimationVisible.value = newActive;
  };

  return { isAnimationVisible, setAnnimationCategory };
});

export const saveDataContact = defineStore('saveDataContact', () => {
  const dataFacility = ref<Info>({
    address: '',
    phoneNumber: '',
    hotline: '',
    mapLink: '',
    image: '',
    mapIframe: ''
  });
  const dataContact = ref<Contact>({
    email: {
      content: ''
    },
    facebook: {
      content: ''
    },
    zalo: {
      content: ''
    }
  });

  const setDataFacility = (newDataRender: Info) => {
    dataFacility.value = newDataRender;
  };
  const setDataContact = (newDataRender: Contact) => {
    dataContact.value = newDataRender;
  };

  return { dataFacility, dataContact, setDataFacility, setDataContact };
});

// export const useCounterStore = defineStore('counter', () => {
//   const count = ref(0);
//   const doubleCount = computed(() => count.value * 2);
//   function increment() {
//     count.value++;
//   }

//   return { count, doubleCount, increment };
// });

import { ref } from 'vue';
import { defineStore } from 'pinia';

interface ListCategory2 {
  title: string;
  slug: string;
}

interface DataRender {
  title: string;
  slug: string;
  company: { name: string; slug: string; cate2: ListCategory2[] }[];
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

// Lưu data của Category
export const useDataRenderStore = defineStore('dataRender', () => {
  const dataRender = ref<DataRender[]>([]);

  const setDataRender = (newDataRender: DataRender[]) => {
    dataRender.value = newDataRender;
  };

  return { dataRender, setDataRender };
});

//Hàm lưu trạng thái của category(cate1 và 2 đang có active hay không)
export const saveActive = defineStore('saveActiveCategory', () => {
  const selectedItem = ref(-1);
  const selectedItem2 = ref(-1);
  const selectedItem3 = ref(-1);
  const typeCate = ref('');

  const setActiveCategory = (cate: number) => {
    selectedItem.value = cate;
  };

  const setActiveCategory2 = (cate2: number) => {
    selectedItem2.value = cate2;
  };

  const setActiveCategory3 = (cate3: number) => {
    selectedItem3.value = cate3;
  };

  const setTypeCategory = (type: string) => {
    typeCate.value = type;
  };

  return {
    selectedItem,
    selectedItem2,
    selectedItem3,
    typeCate,
    setActiveCategory,
    setActiveCategory2,
    setActiveCategory3,
    setTypeCategory
  };
});

//Hàm lưu annimation của category
export const setAnnimation = defineStore('setAnnimation', () => {
  const isAnimationVisible = ref(false);
  const isAnimationVisible2 = ref(false);

  const setAnnimationCategory = (newActive: boolean) => {
    isAnimationVisible.value = newActive;
  };

  const setAnnimationCategory2 = (newActive: boolean) => {
    isAnimationVisible2.value = newActive;
  };

  return { isAnimationVisible, isAnimationVisible2, setAnnimationCategory, setAnnimationCategory2 };
});

// Hàm lưu data contact
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

  // Hàm lưu data facility
  const setDataFacility = (newDataRender: Info) => {
    dataFacility.value = newDataRender;
  };
  const setDataContact = (newDataRender: Contact) => {
    dataContact.value = newDataRender;
  };

  return { dataFacility, dataContact, setDataFacility, setDataContact };
});

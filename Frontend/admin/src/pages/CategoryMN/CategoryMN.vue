<!-- eslint-disable @typescript-eslint/no-explicit-any -->
<script setup lang="ts">
import { ref, watch, type Ref } from 'vue';
import Swal from 'sweetalert2';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

import CategoryList from './CategoryList/CategoryList.vue';
import CategoryModalEdit from './CategoryModalEdit/CategoryModalEdit.vue';
import styles from './CategoryMN.module.scss';

interface Cate1Object {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
  createAt: string;
}

interface CompanyObject {
  id: string;
  name: string;
  logo: string;
  description: string;
  highlight: number;
  slug: string;
  createAt: string;
  outstandingProductId: string;
}

interface CheckCate1 {
  id: string;
  isEmpty: boolean;
}

const itemSelected = ref({
  company: {
    id: '',
    name: '',
    logo: '',
    description: '',
    highlight: 0,
    slug: '',
    createAt: '',
    outstandingProductId: ''
  },
  cate1: { id: '', title: '', img: '', highlight: 0, slug: '', createAt: '' },
  cate2: {}
});

const listCompany: Ref<{ lst: CompanyObject[]; lstEmpty: boolean; isLoading: boolean }> = ref({
  lst: [],
  lstEmpty: false,
  isLoading: false
});
const listCategory1: Ref<{ lst: Cate1Object[]; lstEmpty: boolean; isLoading: boolean }> = ref({
  lst: [],
  lstEmpty: false,
  isLoading: false
});
const listCategory2: Ref<{ lst: Cate1Object[]; lstEmpty: boolean; isLoading: boolean }> = ref({
  lst: [],
  lstEmpty: false,
  isLoading: false
});
const listCategoryFull = ref({ cate1: [{}], cate2: [{}] });
const openModalAdd = ref(false);
const listCategoryEdit: Ref<{ fullCate: Cate1Object[]; currentCate: Cate1Object[] }> = ref({
  fullCate: [],
  currentCate: []
});
const checkCate1: Ref<CheckCate1[]> = ref([]);
const numCate = ref(1);
const resetSelectedCate1 = ref(false);

// Gọi hàm useAxios để lấy response, error, và isLoading
const getCompany = useAxios<DataResponse>('get', '/company', {}, {}, listCompany.value.lst);
const getCategoryFull1 = useAxios<DataResponse>(
  'get',
  '/cate1',
  {},
  {},
  listCategoryFull.value.cate1
);
const getCategoryFull2 = useAxios<DataResponse>(
  'get',
  '/cate2',
  {},
  {},
  listCategoryFull.value.cate1
);

//Hàm mở cate1 khi select company
const handleSelectCompany = (infCompapy: any) => {
  itemSelected.value.company = infCompapy;
  listCategory2.value.lst = [];
  listCategory2.value.lstEmpty = false;

  if (infCompapy) {
    const getCategories = useAxios<DataResponse>(
      'get',
      `/cate?companyId=${infCompapy.id}&cate1Id&cate2Id`,
      {},
      {},
      listCategory1.value.lst
    );

    watch(getCategories.response, (value) => {
      if (value?.data) {
        const dataCate = value?.data;
        const cate1IdArray: any[] = [];
        for (let index = 0; index < dataCate.length; index++) {
          const element = dataCate[index];
          cate1IdArray.push(element.cate1Id);
          checkCate1.value.push({ id: element.id, isEmpty: false });
        }

        checkCate1.value = checkCate1.value.filter((element, index, arr) => {
          return arr.findIndex((e) => e.id === element.id) === index;
        });

        checkCate1.value.forEach((element) => {
          cate1IdArray.findIndex((e) => e.id === element.id);
        });

        listCategory1.value.lst = cate1IdArray.filter((element, index, arr) => {
          return arr.findIndex((e) => e.id === element.id) === index;
        });

        listCategory1.value.lstEmpty = listCategory1.value.lst.length === 0;
        listCategory2.value.lstEmpty =
          listCategory2.value.lst.length === 0 &&
          !listCategory1.value.lstEmpty &&
          itemSelected.value.cate1 === null;

        // console.log(listCategory1.value.lst);
      } else {
        console.log('Error data category');
      }
    });
    watch(getCategories.isLoading, (value) => {
      listCategory1.value.isLoading = value;
    });
  } else {
    listCategory1.value.lst = [];
    listCategory1.value.lstEmpty = false;
  }
};

//Hàm mở cate 2 khi select cate1
const handleSelectCategory1 = (infCategory1: any) => {
  itemSelected.value.cate1 = infCategory1;
  // resetSelectedCate1.value = false;

  if (infCategory1) {
    // console.log(itemSelected.value, infCategory1);

    const getCategories = useAxios<DataResponse>(
      'get',
      `/cate?companyId=${itemSelected.value.company.id}&cate1Id=${infCategory1.id}&cate2Id`,
      {},
      {},
      listCategory2.value.lst
    );

    watch(getCategories.response, (value) => {
      if (value?.data) {
        const dataCate = value?.data;
        listCategory2.value.lst = [];
        for (let index = 0; index < dataCate.length; index++) {
          const element = dataCate[index];
          listCategory2.value.lst.push(element.cate2Id);
        }

        if (!listCategory2.value.lst[0]) {
          listCategory2.value.lst = [];
          listCategory2.value.lstEmpty = true;
        } else {
          listCategory2.value.lstEmpty = false;
        }

        // console.log(listCategory2.value.lst);
      } else {
        console.log('Error data category');
      }
    });

    watch(getCategories.error, (value) => {
      console.log(value);
    });

    watch(getCategories.isLoading, (value) => {
      listCategory2.value.isLoading = value;
    });
  } else {
    listCategory2.value.lst = [];
    listCategory2.value.lstEmpty = false;
  }
};

//Hàm xóa các object trùng nhau của mảng
const removeObjectsFromArray1 = (arr1: any, arr2: any) => {
  return arr2.filter((item: any) => !arr1.some((value: any) => item.id === value.id));
};

//Hàm mở edit cate1
const handleOpenEditCategory1 = () => {
  resetSelectedCate1.value = true;

  if (itemSelected.value.company.id !== '') {
    listCategoryEdit.value.fullCate = removeObjectsFromArray1(
      listCategory1.value.lst,
      listCategoryFull.value.cate1
    );

    listCategoryEdit.value.currentCate = listCategory1.value.lst;
    openModalAdd.value = true;
    numCate.value = 1;

    // console.log(listCategoryEdit.value.fullCate);
  } else {
    Swal.fire({
      title: 'Bạn chưa chọn công ty!',
      icon: 'error',
      customClass: {
        popup: styles['container-popup'],
        confirmButton: styles['confirm-button'],
        denyButton: styles['deny-button']
      }
    });
  }

  listCategory2.value.lst = [];
  listCategory2.value.lstEmpty = false;
};

const handleOpenEditCategory2 = () => {
  if (itemSelected.value.company.id !== '' && itemSelected.value.cate1.id !== '') {
    listCategoryEdit.value.fullCate = removeObjectsFromArray1(
      listCategory2.value.lst,
      listCategoryFull.value.cate2
    );

    listCategoryEdit.value.currentCate = listCategory2.value.lst;
    openModalAdd.value = true;
    numCate.value = 2;

    // console.log(listCategoryEdit.value.fullCate);
  } else {
    Swal.fire({
      title: 'Bạn chưa chọn danh mục cấp 1!',
      icon: 'error',
      customClass: {
        popup: styles['container-popup'],
        confirmButton: styles['confirm-button'],
        denyButton: styles['deny-button']
      }
    });
  }
};

const removeDuplicatesFromArray = (arr1: any, arr2: any) => {
  // Sử dụng phương thức filter để tạo mảng mới chỉ chứa các đối tượng không trùng lặp
  const result = arr1.filter(
    (obj1: { id: any }) => !arr2.some((obj2: { id: any }) => obj1.id === obj2.id)
  );

  return result;
};

//Hàm update cate sau khi chỉnh sửa
const handleUpdateCateCompany = (itemsAdd: any, itemsDelete: any, type: number) => {
  let tmp = type === 1 ? [...listCategory1.value.lst] : [...listCategory2.value.lst];
  tmp = removeDuplicatesFromArray(tmp, itemsDelete).concat(itemsAdd);

  if (type === 1) {
    listCategory1.value.lst = tmp;
    listCategory1.value.lstEmpty = listCategory1.value.lst.length === 0;
    listCategory2.value.lst = [];
    listCategory2.value.lstEmpty = false;
  } else if (type === 2) {
    listCategory2.value.lst = tmp;
    listCategory2.value.lstEmpty = listCategory2.value.lst.length === 0;
  }
};

//Hàm nhận data từ get company Axios
watch(getCompany.response, (value) => {
  // console.log(getCompany.response);

  if (getCompany.error.value) console.log('error:' + getCompany.error.value);
  else if (value?.data) {
    listCompany.value.lst = value?.data;
  } else {
    console.log('data: ' + value?.data);
  }
});

watch(getCompany.isLoading, (value) => {
  listCompany.value.isLoading = value;
});

//Hàm nhận data từ get full cate1 Axios
watch(getCategoryFull1.response, (value) => {
  if (value?.data) {
    listCategoryFull.value.cate1 = value?.data;
  } else {
    console.log('data: ' + value?.data);
  }
});

//Hàm nhận data từ get full cate2 Axios
watch(getCategoryFull2.response, (value) => {
  if (value?.data) {
    listCategoryFull.value.cate2 = value?.data;
  } else {
    console.log('data: ' + value?.data);
  }
});
</script>
<template>
  <div :class="$style.container">
    <div :class="$style.container__modal" v-if="openModalAdd">
      <category-modal-edit
        :numCate="numCate"
        :inf-selected="itemSelected"
        :cate-full="listCategoryEdit.fullCate"
        :cate-current="listCategoryEdit.currentCate"
        :handle-update="handleUpdateCateCompany"
        @close="openModalAdd = false"
      />
    </div>
    <!-- <div :class="$style.container__back">
      <FontAwesomeIcon :icon="faChevronLeft" />
      <p>Trở về</p>
    </div> -->
    <div :class="$style.container__content">
      <div :class="$style['container__content-left']">
        <div :class="$style['container__content-left-topic']">
          <h3 :class="$style['heading-style']">Công ty</h3>
          <div :class="$style['container__content-left-topic-item']">
            <category-list
              :cate-type="0"
              :data="listCompany.lst"
              :is-empty-items="listCompany.lstEmpty"
              :handle-selected="handleSelectCompany"
              :is-loading="listCompany.isLoading"
            />
          </div>
        </div>
        <div :class="$style['container__content-left-topic']">
          <h3 :class="$style['heading-style']">Danh mục 1</h3>
          <div :class="$style['container__content-left-topic-item']">
            <category-list
              :cate-type="1"
              :data="listCategory1.lst"
              :is-empty-items="listCategory1.lstEmpty"
              :handle-selected="handleSelectCategory1"
              :open-edit="handleOpenEditCategory1"
              :reset-selected="resetSelectedCate1"
              :is-loading="listCategory1.isLoading"
            />
          </div>
        </div>
        <div :class="$style['container__content-left-topic']">
          <h3 :class="$style['heading-style']">Danh mục 2</h3>
          <div :class="$style['container__content-left-topic-item']">
            <category-list
              :cate-type="2"
              :data="listCategory2.lst"
              :is-empty-items="listCategory2.lstEmpty"
              :open-edit="handleOpenEditCategory2"
              :is-loading="listCategory2.isLoading"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './CategoryMN.module.scss';
</style>

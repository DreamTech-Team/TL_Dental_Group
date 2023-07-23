<!-- eslint-disable @typescript-eslint/no-explicit-any -->
<script setup lang="ts">
import { ref, watch } from 'vue';
import CategoryList from './CategoryList/CategoryList.vue';
import { ic_logo } from '@/assets/imgs/Recruitment/RecruitmentImgs';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const selectedImage = ref(ic_logo);
const itemSelected = ref({ company: {}, category1: {}, category2: {} });
const listCompany = ref({ lst: [{}], lstEmpty: false });
const listCategory1 = ref({ lst: [{}], lstEmpty: false });
const listCategory2 = ref({ lst: [{}], lstEmpty: false });
const openEditImage = ref(false);

// Gọi hàm useAxios để lấy response, error, và isLoading
const getCompany = useAxios<DataResponse>('get', '/company', {}, {}, listCompany.value.lst);
// const getCategories = useAxios<DataResponse>('get', '/cate', {}, {}, listCategory1.value.lst);

//Choose image
const handleFileInputChange = (event: Event) => {
  const inputElement = event.target as HTMLInputElement;
  const file = inputElement.files?.[0];

  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      selectedImage.value = reader.result as string;
    };
    reader.readAsDataURL(file);
  }
};

const handleSelectCompany = (infCompapy: any) => {
  itemSelected.value.company = infCompapy;

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
        const cate1IdArray = [];
        for (let index = 0; index < dataCate.length; index++) {
          const element = dataCate[index];
          cate1IdArray.push(element.cate1Id);
        }

        listCategory1.value.lst = cate1IdArray.filter((element, index, arr) => {
          return arr.findIndex((e) => e.id === element.id) === index;
        });

        listCategory1.value.lstEmpty = listCategory1.value.lst.length === 0;
        listCategory2.value.lstEmpty =
          listCategory2.value.lst.length === 0 &&
          !listCategory1.value.lstEmpty &&
          itemSelected.value.category1 === null;

        // console.log(listCategory1.value.lst);
      } else {
        console.log('Error data category');
      }
    });
  } else {
    listCategory1.value.lst = [];
    listCategory1.value.lstEmpty = false;
  }

  listCategory2.value.lst = [];
  listCategory2.value.lstEmpty = false;
};

const handleSelectCategory1 = (infCategory1: any) => {
  itemSelected.value.category1 = infCategory1;

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

    // watch(getCategories.error, (value) => {
    //   console.log(value);
    // });
  } else {
    listCategory2.value.lst = [];
    listCategory2.value.lstEmpty = false;
  }
};

watch(getCompany.response, (value) => {
  // console.log(getCompany.response);

  if (getCompany.error.value) console.log('error:' + getCompany.error.value);
  else if (value?.data) {
    listCompany.value.lst = value?.data;
  } else {
    console.log('data: ' + value?.data);
  }
});
</script>
<template>
  <div :class="$style.container">
    <div :class="$style.container__back">
      <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
        <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
        <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
        <g id="SVGRepo_iconCarrier">
          <path
            d="M15 7L10 12L15 17"
            stroke="#000000"
            stroke-width="1.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          ></path>
        </g>
      </svg>
      <p>Trở về</p>
    </div>
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
              :handle-selected="() => {}"
            />
          </div>
        </div>
      </div>
      <div :class="$style['container__content-right']">
        <h3 :class="$style['heading-style']">Chỉnh sửa</h3>
        <div :class="$style['container__content-right-edit']">
          <p>Tên Danh Mục</p>
          <input type="text" placeholder="Nhập tên danh mục" />
        </div>
        <div :class="$style['container__content-right-image']">
          <div :class="$style['container__content-right-image-block']">
            <img :src="selectedImage" />
            <div v-if="openEditImage" :class="$style['block__img-edit']">
              <input type="file" @change="(e) => handleFileInputChange(e)" />
              <svg
                viewBox="-368.64 -368.64 1761.28 1761.28"
                class="icon"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                fill="#000000"
              >
                <g id="SVGRepo_bgCarrier" stroke-width="0">
                  <rect
                    x="-368.64"
                    y="-368.64"
                    width="1761.28"
                    height="1761.28"
                    rx="880.64"
                    fill="#7ed0ec"
                    strokewidth="0"
                  ></rect>
                </g>
                <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                <g id="SVGRepo_iconCarrier">
                  <path
                    d="M723.2 258.133333H302.933333L375.466667 149.333333c8.533333-12.8 21.333333-19.2 36.266666-19.2h204.8c14.933333 0 27.733333 6.4 36.266667 19.2l70.4 108.8zM298.666667 234.666667H170.666667V196.266667C170.666667 181.333333 181.333333 170.666667 196.266667 170.666667h76.8c14.933333 0 25.6 10.666667 25.6 25.6V234.666667z"
                    fill="#5E35B1"
                  ></path>
                  <path
                    d="M853.333333 896H170.666667c-46.933333 0-85.333333-38.4-85.333334-85.333333V298.666667c0-46.933333 38.4-85.333333 85.333334-85.333334h682.666666c46.933333 0 85.333333 38.4 85.333334 85.333334v512c0 46.933333-38.4 85.333333-85.333334 85.333333z"
                    fill="#5E35B1"
                  ></path>
                  <path
                    d="M725.333333 533.333333c0-117.333333-96-213.333333-213.333333-213.333333-51.2 0-98.133333 17.066667-134.4 46.933333l25.6 34.133334C433.066667 377.6 469.333333 362.666667 512 362.666667c93.866667 0 170.666667 76.8 170.666667 170.666666h-74.666667l96 119.466667 96-119.466667H725.333333zM620.8 665.6C590.933333 689.066667 552.533333 704 512 704c-93.866667 0-170.666667-76.8-170.666667-170.666667h74.666667L320 413.866667 224 533.333333H298.666667c0 117.333333 96 213.333333 213.333333 213.333334 51.2 0 98.133333-17.066667 134.4-46.933334l-25.6-34.133333z"
                    fill="#E8EAF6"
                  ></path>
                </g>
              </svg>
            </div>
          </div>
        </div>
        <div :class="$style['container__content-right-btn']">
          <div :class="$style['container__content-right-btn-block']">
            <button type="button">Xóa</button>
            <button type="button">Cập nhật</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './CategoryMN.module.scss';
</style>

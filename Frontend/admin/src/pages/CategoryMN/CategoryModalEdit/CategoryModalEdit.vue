<!-- eslint-disable @typescript-eslint/no-explicit-any -->
<script setup lang="ts">
import { ref, watch, type Ref, type PropType } from 'vue';
import Swal from 'sweetalert2';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

import {
  faMagnifyingGlass,
  faPlus,
  faGears,
  faXmarkCircle
} from '@fortawesome/free-solid-svg-icons';

import useAxios, { type DataResponse } from '@/hooks/useAxios';
import styles from './CategoryModalEdit.module.scss';
import CategoryItem from './CategoryItem/CategoryItem.vue';
import CategoryAddItem from './CategoryAddItem/CategoryAddItem.vue';

interface Cate1Object {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
  createAt: string;
}

const props = defineProps({
  infSelected: { type: Object, required: true },
  cateFull: { type: Object, require: true },
  cateCurrent: { type: Object as unknown as PropType<Cate1Object[]>, require: true },
  handleUpdate: { type: Function, require: false },
  typeEdit: { type: Number, required: true }
});

const emit = defineEmits(['close']);
const openEditCategory = ref(false);
const openAddCategory = ref(false);
const listCateFull = ref(props.cateFull);
const listCateCurrent = ref(props.cateCurrent);
const listChange: Ref<{ add: Cate1Object[]; delete: Cate1Object[] }> = ref({ add: [], delete: [] });
const paramAxios = ref();

//Hàm xóa một phần tử của mảng
const removeElementFromArray = (arr: any, elementId: string) => {
  if (!Array.isArray(arr)) {
    // Handle the case when 'arr' is not an array
    console.error('Input is not an array.');
    return arr;
  }

  const newArray = [...arr]; // Copy the original array
  const index = newArray.findIndex((item) => item.id === elementId);
  if (index !== -1) {
    newArray.splice(index, 1); // Remove the element from the copied array
  }
  return newArray; // Return the modified array
};

//Hàm thêm cate
const handlePushCate = (cate: Cate1Object) => {
  if (Array.isArray(listCateCurrent.value)) {
    listCateCurrent.value = [...listCateCurrent.value, cate];
  } else {
    listCateCurrent.value = [cate];
  }
  listCateFull.value = removeElementFromArray(listCateFull.value, cate.id);

  if (listChange.value.add.length === 0) listChange.value.add = [];
  listChange.value.add.push(cate);
  listChange.value.delete = removeElementFromArray(listChange.value.delete, cate.id);

  // console.log(listChange.value.add, listChange.value.delete);
};

//Hàm xóa cate
const handlePopCate = (cate: Cate1Object) => {
  listCateFull.value?.push(cate);
  listCateCurrent.value = removeElementFromArray(listCateCurrent.value, cate.id);

  if (listChange.value.delete.length === 0) listChange.value.delete = [];
  listChange.value.delete.push(cate);
  listChange.value.add = removeElementFromArray(listChange.value.add, cate.id);

  // console.log(listChange.value.add, listChange.value.delete);
};

const handleModalCancel = () => {
  Swal.fire({
    title: 'Nội dung của bạn chưa được lưu!',
    icon: 'warning',
    showDenyButton: true,
    confirmButtonText: 'Trở lại',
    denyButtonText: 'Thoát',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    if (result.isDenied) {
      emit('close');
    }
  });
};

const handleModalUpdate = () => {
  Swal.fire({
    title: 'Bạn có chắc chắn muốn cập nhật nội dung không?',
    icon: 'question',
    showDenyButton: true,
    confirmButtonText: 'Cập nhật',
    denyButtonText: 'Hủy',
    customClass: {
      popup: styles['container-popup'],
      confirmButton: styles['confirm-button'],
      denyButton: styles['deny-button']
    }
  }).then((result) => {
    if (result.isConfirmed) {
      const listPushCate: unknown[] = [];
      const listPopCate: unknown[] = [];

      // Thêm dữ liệu
      if (listChange.value.add.length > 0) {
        listChange.value.add.forEach((item) => {
          const obj =
            props.typeEdit === 1
              ? { companyId: props.infSelected.company.id, cate1Id: item.id, cate2Id: '' }
              : {
                  companyId: props.infSelected.company.id,
                  cate1Id: props.infSelected.cate1.id,
                  cate2Id: item.id
                };

          const postPushCate = useAxios<DataResponse>('post', '/cate', obj, {}, paramAxios.value);

          watch(postPushCate.response, (value) => {
            if (value?.status === 'ok')
              listPushCate.push(props.typeEdit === 1 ? value?.data.cate1Id : value?.data.cate2Id);
          });
        });
      }

      // Xóa dữ liệu
      if (listChange.value.delete.length > 0) {
        listChange.value.delete.forEach((item) => {
          const linkDelete =
            props.typeEdit === 1
              ? `/cate?cate1Id=${item.id}&companyId=${props.infSelected.company.id}`
              : // eslint-disable-next-line max-len
                `/cate?cate1Id=${props.infSelected.cate1.id}&cate2Id=${item.id}&companyId=${props.infSelected.company.id}`;

          const postPushCate = useAxios<DataResponse>(
            'delete',
            linkDelete,
            {},
            {},
            paramAxios.value
          );

          watch(postPushCate.response, (value) => {
            if (value?.status === 'ok') listPopCate.push(item);
            // console.log(postPushCate);
          });
        });
      }

      Swal.fire({
        title: 'Đang cập nhật dữ liệu...',
        // timerProgressBar: true,
        timer: 2000,
        customClass: {
          popup: styles['container-popup'],
          loader: styles['container-loader']
        },
        didOpen: () => {
          Swal.showLoading();
        }
      }).then((result) => {
        if (result.dismiss === Swal.DismissReason.timer) {
          if (props.handleUpdate) props.handleUpdate(listPushCate, listPopCate, props.typeEdit);

          emit('close');
          Swal.fire({
            title: 'Cập nhật dữ liệu thành công!',
            icon: 'success',
            customClass: {
              popup: styles['container-popup'],
              confirmButton: styles['confirm-button'],
              denyButton: styles['deny-button']
            }
          });
        }
      });
    } else if (result.isDenied) {
      Swal.fire({
        title: 'Nội dung không được cập nhật',
        icon: 'error',
        customClass: {
          popup: styles['container-popup'],
          confirmButton: styles['confirm-button'],
          denyButton: styles['deny-button']
        }
      });
    }
  });
};
</script>
<template>
  <div :class="$style.container">
    <div :class="$style.container__add" v-if="openAddCategory">
      <category-add-item />
    </div>
    <div :class="$style.container__modal">
      <div :class="$style['container__modal-heading']">
        <div class=""></div>
        <div :class="$style['container__modal-heading-title']">
          Thêm danh mục cấp {{ typeEdit }}
        </div>
        <div :class="$style['container__modal-heading-exit']" @click="handleModalCancel">
          <svg viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" fill="none">
            <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
            <g id="SVGRepo_iconCarrier">
              <path
                fill="#000000"
                fill-rule="evenodd"
                d="M9 17a1 1 0 102 0v-6h6a1 1 0 100-2h-6V3a1 1 0 10-2 0v6H3a1 1 0 000 2h6v6z"
              ></path>
            </g>
          </svg>
        </div>
      </div>
      <div :class="$style['container__modal-body']">
        <div :class="$style['container__modal-body-left']">
          <div :class="$style['container__modal-body-left-heading']">
            <div :class="$style['container__modal-body-left-heading-title']">
              Các danh mục có thể thêm
            </div>
            <div :class="$style['container__modal-body-left-heading-add']">
              <button>
                <font-awesome-icon :icon="faPlus" />
                Tạo Danh Mục
              </button>
            </div>
          </div>
          <div :class="$style['container__modal-body-left-block']">
            <div :class="$style['container__modal-body-left-block-customize']">
              <div :class="$style['container__modal-body-left-block-customize-search']">
                <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['icon-search']" />
                <input type="text" placeholder="Tìm kiếm" />
              </div>
              <div
                :class="[
                  $style['container__modal-body-left-block-customize-settings'],
                  $style[openEditCategory ? 'close' : '']
                ]"
              >
                <button @click="openEditCategory = !openEditCategory">
                  <font-awesome-icon :icon="openEditCategory ? faXmarkCircle : faGears" />
                </button>
              </div>
            </div>
            <div :class="$style['container__modal-body-left-block-items']">
              <div v-for="(item, index) in listCateFull" :key="index">
                <CategoryItem
                  :open-edit="openEditCategory"
                  :data="item"
                  :handle-btn="handlePushCate"
                />
              </div>
            </div>
          </div>
        </div>
        <div :class="$style['container__modal-body-right']">
          <div :class="$style['container__modal-body-right-heading']">
            Danh mục hiện có của công ty {{ infSelected.company.name }}
          </div>
          <div :class="$style['container__modal-body-right-block']">
            <div v-for="(item, index) in listCateCurrent" :key="index">
              <CategoryItem :type="'minus'" :data="item" :handle-btn="handlePopCate" />
            </div>
          </div>
        </div>
      </div>
      <div :class="$style['container__modal-update']">
        <div :class="$style['container__modal-update-btn']">
          <div
            :class="[
              $style['container__modal-update-btn-submit'],
              $style['container__modal-update-btn-cancel']
            ]"
            @click="handleModalCancel"
          >
            Hủy
          </div>
          <div
            :class="[
              $style['container__modal-update-btn-submit'],
              $style['container__modal-update-btn-update']
            ]"
            @click="handleModalUpdate"
          >
            Cập nhật
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './CategoryModalEdit.module.scss';
</style>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus, faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import ModalAdd from './components/ModalAdd/ModalAdd.vue';
import ModalUpdate from './components/ModalUpdate/ModalUpdate.vue';
import Pagination from '@/components/Pagination/BasePagination.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import Loading from '@/components/LoadingComponent/LoadingComponent.vue';

interface ProductItem {
  id: string;
  name: string;
  slug: string;
  price: number;
  priceSale: number;
  summary: string;
  description: string;
  mainImg: string;
  imgs: string;
  highlight: number;
  createAt: string;
  fkCategory: {
    id: string;
    companyId: {
      id: string;
      name: string;
      logo: string;
      description: string;
      highlight: number;
      slug: string;
      createAt: string;
      outstandingProductId: string;
    };
    cate1Id: {
      id: string;
      title: string;
      img: string;
      highlight: 3;
      slug: string;
      createAt: string;
    };
    cate2Id: {
      id: string;
      title: string;
      slug: string;
      createAt: string;
    };
  };
}

interface Item {
  id: string;
  name: string;
  src: string;
  company: string;
  price: string;
}

interface MyErrorResponse {
  response?: {
    data?: {
      message?: string;
    };
  };
}

//Array to store initial values
const tempArrays = ref<ProductItem[]>([]);
const products = ref<Item[]>([]);
const chosenObject = ref<ProductItem | undefined>();

const isOpen = ref(false); //Open Modal Add
const isOpen1 = ref(false); //Open Modal Update
const searchText = ref('');
const results = ref(products); //Final Render
const scrollContainer = ref<HTMLElement | null>(null); //Scroll table to top when change page

const debounceTimer = ref<number | null>(null); //searchData delay
const loadingStatus = ref(false);

const totalPage = ref(0);
const currentPage = ref(0);

//Function 1000 to 1.000
const formatNumberWithCommas = (num: number) => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
};

const deps = ref([]);

//Get total
const getTotal = useAxios<DataResponse>('get', '/products/total', {}, {}, deps.value);

watch(getTotal.response, () => {
  totalPage.value = getTotal.response.value?.data;
});

//GET DATA
const { response, isLoading } = useAxios<DataResponse>(
  'get',
  `/products?page=${currentPage.value}&pageSize=10`,
  {},
  {},
  deps.value
);

//Convert array to compatible with data
watch(response, () => {
  tempArrays.value = response.value?.data?.data;
  results.value = tempArrays.value.map((item: ProductItem) => {
    return {
      id: item.id,
      name: item.name,
      src: item.mainImg,
      company: item.fkCategory.companyId.name,
      price: formatNumberWithCommas(item.price) + ' VNĐ'
    };
  });
});

//Data from Modal Add
const onUpdateContent = () => {
  window.location.reload();
};

//Data from Modal Update
const onUpdateContent2 = (data: { productAdd: ProductItem }) => {
  results.value.forEach((item) => {
    if (item.id === data.productAdd.id) {
      item.name = data.productAdd.name;
      item.src = data.productAdd.mainImg;
      item.company = data.productAdd.fkCategory.companyId.name;
      item.price = formatNumberWithCommas(data.productAdd.price) + ' VNĐ';
    }
  });
  tempArrays.value.forEach((item, index) => {
    if (item.id === data.productAdd.id) {
      tempArrays.value[index] = data.productAdd;
    }
  });
};

//Function call API Search
const searchProduct = () => {
  const searchProduct = useAxios<DataResponse>(
    'get',
    `/products/search?key=${searchText.value}`,
    {},
    {},
    deps.value
  );

  watch(searchProduct.response, () => {
    tempArrays.value = searchProduct.response.value?.data?.data;
    results.value = tempArrays.value.map((item: ProductItem) => {
      return {
        id: item.id,
        name: item.name,
        src: item.mainImg,
        company: item.fkCategory.companyId.name,
        price: formatNumberWithCommas(item.price) + ' VNĐ'
      };
    });
  });
};

//Search Products
watch(searchText, () => {
  if (debounceTimer.value) {
    clearTimeout(debounceTimer.value);
  }

  // Create a new timeout to call API after 1 second
  debounceTimer.value = setTimeout(() => {
    searchProduct();
  }, 500);
});

// Computed property for displaying filtered products
const filteredProducts = computed(() => {
  return results.value;
});

//Pagination Handle
const scrollToTop = () => {
  if (scrollContainer.value && scrollContainer.value instanceof HTMLElement) {
    scrollContainer.value.scrollTop = 0;
  }
};

//Change Page
const handlePageChange = (page: number) => {
  currentPage.value = page - 1;
  scrollToTop();
};

//Limit Character
const truncateText = (text: string, maxLength: number) => {
  if (text && text.length > maxLength) {
    return text.slice(0, maxLength) + '...';
  }
  return text;
};

watch(currentPage, () => {
  const { response, isLoading } = useAxios<DataResponse>(
    'get',
    `/products?page=${currentPage.value}&pageSize=10`,
    {},
    {},
    deps.value
  );

  //Convert array to compatible with data
  watch(response, () => {
    tempArrays.value = response.value?.data?.data;
    results.value = tempArrays.value.map((item: ProductItem) => {
      return {
        id: item.id,
        name: item.name,
        src: item.mainImg,
        company: item.fkCategory.companyId.name,
        price: formatNumberWithCommas(item.price) + ' VNĐ'
      };
    });
  });

  watch(isLoading, () => {
    loadingStatus.value = isLoading.value;
  });
});

watch(isLoading, () => {
  loadingStatus.value = isLoading.value;
});

//Open modal Update
const updateProduct = (id: string) => {
  chosenObject.value = tempArrays.value.find((obj: ProductItem) => obj.id === id);
  isOpen1.value = true;
};

//Delete Product
const deleteProduct = (id: string) => {
  Swal.fire({
    title: 'Bạn có chắc muốn xóa?',
    text: 'Dữ liệu sẽ không thể khôi phục sau khi xóa!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Xóa',
    cancelButtonText: 'Hủy'
  }).then((result) => {
    if (result.isConfirmed) {
      const deleteProduct = useAxios<DataResponse>('delete', `/products/${id}`, {}, {}, deps.value);

      watch(deleteProduct.response, () => {
        if (deleteProduct.response.value?.status === 'ok') {
          Swal.fire({
            title: 'Xóa thành công',
            icon: 'success',
            confirmButtonText: 'Hoàn tất',
            width: '30rem'
          }).then((result) => {
            if (result.isConfirmed) {
              Swal.close();
              const { response } = useAxios<DataResponse>(
                'get',
                `/products?page=${currentPage.value}&pageSize=10`,
                {},
                {},
                deps.value
              );

              //Convert array to compatible with data
              watch(response, () => {
                tempArrays.value = response.value?.data?.data;
                results.value = tempArrays.value.map((item: ProductItem) => {
                  return {
                    id: item.id,
                    name: item.name,
                    src: item.mainImg,
                    company: item.fkCategory.companyId.name,
                    price: formatNumberWithCommas(item.price) + ' VNĐ'
                  };
                });
              });
            }
          });
        }
      });

      watch(deleteProduct.error, () => {
        const errorValue: MyErrorResponse | null = deleteProduct.error
          .value as MyErrorResponse | null;
        if (errorValue !== null) {
          if (
            errorValue?.response?.data?.message ===
            'Failed to delete product. Exist a company has this product!'
          ) {
            Swal.fire({
              title: 'Sản phẩm nổi bật của công ty không thể xóa',
              icon: 'error',
              showCancelButton: false,
              width: '30rem'
            });
            setTimeout(function () {
              Swal.close();
            }, 1200);
            return;
          }
        }
      });
    }
  });
};
</script>
<template>
  <div :class="$style.mn_product">
    <h3>DANH SÁCH SẢN PHẨM</h3>
    <div :class="$style.mn_product_control">
      <button @click="isOpen = true">
        <font-awesome-icon :icon="faPlus" :class="$style['mn_product_control-ic1']" />
        THÊM SẢN PHẨM
      </button>
      <div :class="$style['mn_product_control-input1']">
        <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['mn_product_control-ic2']" />
        <input v-model="searchText" placeholder="Tìm kiếm" />
      </div>
    </div>
    <div :class="$style.mn_product_body">
      <table :class="$style.mn_product_table_1">
        <thead>
          <tr>
            <th style="width: 8%">STT</th>
            <th style="width: 40%">Tên sản phẩm</th>
            <th style="width: 14%">Công ty</th>
            <th style="width: 24%">Giá</th>
            <th style="width: 14%">Chỉnh sửa</th>
          </tr>
        </thead>
      </table>
      <div :class="$style.mn_product_table_ctn" ref="scrollContainer">
        <loading v-if="loadingStatus" />
        <table :class="$style.mn_product_table">
          <tbody>
            <template v-if="filteredProducts.length > 0">
              <tr v-for="(item, index) in filteredProducts" :key="index">
                <td style="width: 8%">{{ currentPage * 10 + index + 1 }}</td>
                <td style="width: 40%">
                  <div :class="$style['table_item']">
                    <img :src="item.src" alt="SP" :class="$style['table_img']" />
                    <span>{{ truncateText(item.name, 50) }}</span>
                  </div>
                </td>
                <td style="width: 14%">{{ item.company }}</td>
                <td style="width: 24%">{{ item.price }}</td>
                <td style="width: 14%">
                  <button :class="$style['btn-room-trash']" @click="deleteProduct(item.id)">
                    <font-awesome-icon :icon="faTrash" />
                  </button>
                  <button :class="$style['edit-room-btn']" @click="updateProduct(item.id)">
                    <font-awesome-icon :icon="faPen" />
                  </button>
                </td>
              </tr>
            </template>
            <template v-else>
              <tr>
                <td colspan="5">DANH SÁCH TRỐNG</td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
    </div>

    <div :class="$style['mn_product_pagination']">
      <pagination
        :total="totalPage"
        :current-page="currentPage"
        :page-size="10"
        @current-change="handlePageChange"
      />
    </div>
  </div>
  <ModalAdd v-if="isOpen" @close="isOpen = false" @update-content="onUpdateContent" />
  <ModalUpdate
    v-if="isOpen1"
    @close="isOpen1 = false"
    :update-object="chosenObject ? chosenObject : null"
    @update-content="onUpdateContent2"
  />
</template>

<style module scoped lang="scss">
@import './ProductMN.module.scss';
</style>

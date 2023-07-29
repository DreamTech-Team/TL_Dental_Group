<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus, faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import ModalAdd from './components/ModalAdd/ModalAdd.vue';
import ModalUpdate from './components/ModalUpdate/ModalUpdate.vue';
import Pagination from '@/components/Pagination/BasePagination.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

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

//Array to store initial values
const tempArrays = ref<ProductItem[]>([]);
const products = ref<Item[]>([]);
const chosenObject = ref<ProductItem | undefined>();

const isOpen = ref(false); //Open Modal Add
const isOpen1 = ref(false); //Open Modal Update
const searchText = ref('');
const results = ref(products); //Final Render

const debounceTimer = ref<number | null>(null); //searchData delay

const currentPage = ref(1);
const pageSize = ref(10);

//Function 1000 to 1.000
const formatNumberWithCommas = (num: number) => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
};

//GET DATA
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/products?sort="asc"', {}, {}, deps.value);

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
const onUpdateContent = (data: { productAdd: ProductItem }) => {
  tempArrays.value.push(data.productAdd);

  const newObject = {
    id: data.productAdd.id,
    name: data.productAdd.name,
    src: data.productAdd.mainImg,
    company: data.productAdd.fkCategory.companyId.name,
    price: formatNumberWithCommas(data.productAdd.price) + ' VNĐ'
  };

  results.value.push(newObject);
};

//Data from Modal Update
const onUpdateContent2 = (data: { productAdd: ProductItem }) => {
  console.log(data.productAdd);
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
const scrollToTop = (top: number) => {
  window.scrollTo({
    top: top,
    behavior: 'smooth'
  });
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  scrollToTop(0);
};

const displayProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredProducts.value.slice(start, end);
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
          results.value = results.value.filter((product) => product.id !== id);
          Swal.fire({
            title: 'Xóa thành công',
            icon: 'success',
            confirmButtonText: 'Hoàn tất',
            width: '30rem'
          }).then((result) => {
            if (result.isConfirmed) {
              Swal.close();
            }
          });
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
      <div :class="$style.mn_product_table_ctn">
        <table :class="$style.mn_product_table">
          <tbody>
            <template v-if="filteredProducts.length > 0">
              <tr v-for="(item, index) in displayProducts" :key="index">
                <td style="width: 8%">{{ index + 1 }}</td>
                <td style="width: 40%">
                  <div :class="$style['table_item']">
                    <img :src="item.src" alt="SP" :class="$style['table_img']" />
                    <span>{{ item.name }}</span>
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
        :total="Math.ceil(filteredProducts.length / pageSize)"
        :current-page="currentPage"
        :page-size="pageSize"
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
  <!-- <ModalUpdate v-if="isOpen1" @close="isOpen1 = false" :updateObject="chosenObject" /> -->
</template>

<style module scoped lang="scss">
@import './ProductMN.module.scss';
</style>

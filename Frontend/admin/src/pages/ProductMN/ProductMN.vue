<script setup lang="ts">
import { ref, computed } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus, faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import { products } from './Products';
import Swal from 'sweetalert2';
import ModalAdd from './components/ModalAdd.vue';
import Pagination from '@/components/Pagination/BasePagination.vue';

const isOpen = ref(false);
const searchText = ref('');
const results = ref(products);

const currentPage = ref(1);
const pageSize = ref(10);

const filteredProducts = computed(() => {
  if (searchText.value.trim() === '') {
    return results.value;
  } else {
    const searchTerm = searchText.value.toLowerCase();
    return results.value.filter((product) => product.name.toLowerCase().includes(searchTerm));
  }
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

const displayNews = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredProducts.value.slice(start, end);
});

const deleteProduct = (id: number) => {
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
      results.value = results.value.filter((product) => product.id !== id);

      Swal.fire({
        title: 'Xóa thành công',
        icon: 'success',
        confirmButtonText: 'Hoàn tất',
        width: '30rem'
      });

      setTimeout(function () {
        Swal.close();
      }, 1200);
    }
  });
};
</script>
<template>
  <div :class="$style.mn_product">
    <h3>DANH SÁCH SẢN PHẨM</h3>
    <div :class="$style.mn_product_control">
      <button>
        <font-awesome-icon :icon="faPlus" :class="$style['mn_product_control-ic1']" />
        THÊM SẢN PHẨM
      </button>
      <div :class="$style['mn_product_control-input1']">
        <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['mn_product_control-ic2']" />
        <input v-model="searchText" placeholder="Tìm kiếm" />
      </div>
    </div>
    <table :class="$style.mn_product_table">
      <thead>
        <tr>
          <th style="width: 8%">STT</th>
          <th style="width: 40%">Tên sản phẩm</th>
          <th style="width: 14%">Công ty</th>
          <th style="width: 24%">Giá</th>
          <th style="width: 14%">Chỉnh sửa</th>
        </tr>
      </thead>
      <tbody>
        <template v-if="filteredProducts.length > 0">
          <tr v-for="(item, index) in displayNews" :key="index">
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
              <button :class="$style['edit-room-btn']"><font-awesome-icon :icon="faPen" /></button>
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
    <div :class="$style['mn_product_pagination']">
      <pagination
        :total="Math.ceil(filteredProducts.length / pageSize)"
        :current-page="currentPage"
        :page-size="pageSize"
        @current-change="handlePageChange"
      />
    </div>
  </div>
  <ModalAdd v-if="isOpen" @close="isOpen = false" />
</template>

<style module scoped lang="scss">
@import './ProductMN.module.scss';
</style>

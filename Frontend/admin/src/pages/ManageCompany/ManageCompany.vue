<script setup lang="ts">
import { ref, computed } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faTrash, faPen, faSearch } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import Logo from '@/assets/imgs/logo_nobg.png';
import Pagination from '@/components/Pagination/BasePagination.vue';
import ModalAddCompany from './component/ModalAddCompany.vue';
import ModalUpdateCompany from './component/ModalUpdateCompany.vue';
import styles from './ManageCompany.module.scss';

const companies = [
  {
    id: 1,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 2,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 3,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 4,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 5,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 6,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 7,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 8,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 9,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 10,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 11,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 12,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 13,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  },
  {
    id: 14,
    name: 'PMC Company',
    description: 'Công ty chuyên bán sỉ lẻ thiết bị nha kho',
    logo: Logo,
    imgProduct: Logo,
    nameProduct: 'kềm chỉnh nha'
  }
];

const companyRender = ref(companies);
const isOpenAdd = ref(false);
const isOpenUpdate = ref(false);
const searchText = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const indexRow = ref(0);

const filteredProducts = computed(() => {
  if (searchText.value.trim() === '') {
    return companyRender.value;
  } else {
    const searchTerm = searchText.value.toLowerCase();
    return companyRender.value.filter((company) => company.name.toLowerCase().includes(searchTerm));
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

const handleUpdateModal = (id: number) => {
  isOpenUpdate.value = true;

  indexRow.value = id;
};

const deleteCompany = (id: number) => {
  Swal.fire({
    title: 'Bạn có chắc muốn xóa công ty này không?',
    text: 'Dữ liệu sẽ không thể khôi phục sau khi xóa!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Xóa',
    cancelButtonText: 'Hủy',
    width: '50rem',
    padding: '0 2rem 2rem 2rem',
    customClass: {
      confirmButton: styles['confirm-button'],
      cancelButton: styles['cancel-button'],
      title: styles['title']
    }
  }).then((result) => {
    if (result.isConfirmed) {
      companyRender.value = companyRender.value.filter((product) => product.id !== id);

      Swal.fire({
        title: 'Xóa thành công',
        icon: 'success',
        confirmButtonText: 'Hoàn tất',
        timer: 2000,
        width: '50rem',
        padding: '0 2rem 2rem 2rem',
        customClass: {
          confirmButton: styles['confirm-button'],
          cancelButton: styles['cancel-button'],
          title: styles['title']
        }
      });

      setTimeout(function () {
        // Swal.close();
      }, 1200);
    }
  });
};
</script>
<template>
  <div :class="$style.mn_company">
    <div :class="$style['mn_company--header']">
      <div :class="$style['mn_company--header-left']">
        <h1>CÔNG TY HỢP TÁC</h1>
        <p>Thêm công ty nào?</p>
        <span @click="isOpenAdd = true">Thêm công ty</span>
      </div>

      <div :class="$style['mn_company--header-right']">
        <img :src="Logo" alt="" />

        <div>
          <p>TL Dental Group</p>
          <span>Dental distributors</span>
        </div>
      </div>
    </div>

    <div :class="$style['mn_company--search']">
      <h2>DANH SÁCH CÔNG TY</h2>

      <div>
        <input type="text" placeholder="Tìm kiếm" v-model="searchText" />

        <font-awesome-icon :icon="faSearch" :class="$style['mn_company--search-ic']" />
      </div>
    </div>

    <div :class="$style['mn_company--table']">
      <div :class="$style['mn_company--table-titlebar']">
        <p style="width: 8%">STT</p>
        <p style="width: 12%">Tên công ty</p>
        <p style="width: 30%">Mô tả công ty</p>
        <p style="width: 8%">Logo</p>
        <p style="width: 30%">Sản phẩm nổi bật</p>
        <p style="width: 12%">Chỉnh sửa</p>
      </div>

      <div :class="$style['mn_company--table-list']" v-if="filteredProducts.length > 0">
        <div
          :class="$style['mn_company--table-row']"
          v-for="(company, index) in displayNews"
          :key="index"
        >
          <p :class="$style['mn_company--table-row-1']">{{ index + 1 }}</p>
          <p :class="$style['mn_company--table-row-2']">{{ company.name }}</p>
          <p :class="$style['mn_company--table-row-3']">{{ company.description }}</p>
          <div :class="$style['mn_company--table-row-4']">
            <img :src="company.logo" alt="" />
          </div>
          <div :class="$style['mn_company--table-row-5']">
            <img :src="company.imgProduct" alt="" />
            <p>{{ company.nameProduct }}</p>
          </div>
          <div :class="$style['mn_company--table-row-6']">
            <button @click="deleteCompany(company.id)">
              <font-awesome-icon :icon="faTrash" :class="$style['mn_company--table-ic']" />
            </button>

            <button @click="handleUpdateModal(company.id)">
              <font-awesome-icon :icon="faPen" :class="$style['mn_company--table-ic']" />
            </button>
          </div>
        </div>
      </div>
      <div :class="$style['mn_company--table-row-notfonund']" v-else>
        <p>Không tìm thấy kết quả mong muốn</p>
      </div>
    </div>

    <div :class="$style['mn_company--pagination']">
      <pagination
        :total="Math.ceil(filteredProducts.length / pageSize)"
        :current-page="currentPage"
        :page-size="pageSize"
        @current-change="handlePageChange"
      />
    </div>
  </div>

  <modal-add-company v-if="isOpenAdd" @close="isOpenAdd = false" />
  <modal-update-company
    v-if="isOpenUpdate"
    @close="isOpenUpdate = false"
    :name="companyRender[indexRow - 1].name"
    :description="companyRender[indexRow - 1].description"
    :logo="companyRender[indexRow - 1].logo"
    :nameProduct="companyRender[indexRow - 1].nameProduct"
  />
</template>

<style module scoped lang="scss">
@import './ManageCompany.module.scss';
</style>

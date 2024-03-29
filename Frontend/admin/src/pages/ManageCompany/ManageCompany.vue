<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faTrash, faPen, faSearch } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import Logo from '@/assets/imgs/logo_nobg.png';
import Pagination from '@/components/Pagination/BasePagination.vue';
import ModalAddCompany from './component/ModalAddCompany.vue';
import ModalUpdateCompany from './component/ModalUpdateCompany.vue';
import styles from './ManageCompany.module.scss';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface ManageCompany {
  id: string;
  name: string;
  logo: string;
  description: string;
  highlight: number;
  slug: string;
  createAt: string;
  outstandingProductId: string;
}

interface Products {
  id: string;
  name: string;
  description: string;
  mainImg: string;
  fkCategory: {
    companyId: {
      id: string;
    };
  };
}

interface Cate {
  companyId: {
    id: string;
  };
}

const variableChange = ref([]);
const variableChangeCompany = ref([]);
const variableChangeProduct = ref([]);
const companyRender = ref<ManageCompany[]>([]);
const products = ref<Products[]>([]);
const cate = ref<Cate[]>([]);
const isOpenAdd = ref(false);
const isOpenUpdate = ref(false);
const searchText = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const indexRow = ref(0);
const indexProduct = ref(-1);
const idCompany = ref('');
let outstandingRender = {
  image: '',
  name: ''
};
const isLoadingCompany = ref(false);
const isExistProduct = ref(false);

const getCompany = useAxios<DataResponse>('get', '/company', {}, {}, variableChangeCompany.value);

const getProducts = useAxios<DataResponse>('get', '/products', {}, {}, variableChangeProduct.value);

const getCate = useAxios<DataResponse>('get', '/cate', {}, {}, variableChangeProduct.value);

watch(getCompany.isLoading, () => {
  isLoadingCompany.value = getCompany.isLoading.value;
});

watch(getCompany.response, () => {
  companyRender.value = getCompany.response.value?.data
    .slice()
    .sort((a: ManageCompany, b: ManageCompany) => {
      return new Date(b.createAt).getTime() - new Date(a.createAt).getTime();
    });
});

watch(getProducts.response, () => {
  products.value = getProducts.response.value?.data?.data;
});

watch(getCate.response, () => {
  cate.value = getCate.response.value?.data;
});

// Hàm xử lí search
const filteredProducts = computed(() => {
  if (searchText.value.trim() === '') {
    return companyRender.value;
  } else {
    const searchTerm = searchText.value.toLowerCase();
    return companyRender.value.filter((company) => company.name.toLowerCase().includes(searchTerm));
  }
});

// tính toán mỗi page có bao nhiêu phần tử để render lên màn hình
const displayNews = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredProducts.value.slice(start, end);
});

//Pagination Handle
const scrollToTop = (top: number) => {
  window.scrollTo({
    top: top,
    behavior: 'smooth'
  });
};

// Xử lí thay khi chuyển trang
const handlePageChange = (page: number) => {
  currentPage.value = page;
  scrollToTop(0);
};

// Xử lí mở modal chỉnh sửa một công ty
const handleUpdateModal = (idx: number, idOut: string, idCom: string) => {
  isOpenUpdate.value = true;

  if (idOut !== null) {
    products.value.forEach((item, idx) => {
      if (item.id === idOut) {
        indexProduct.value = idx;
      }
    });
  } else {
    indexProduct.value = -1;
  }

  idCompany.value = idCom;
  indexRow.value = idx;
};

// Xử lí xóa một công ty
const deleteCompany = (id: string) => {
  cate.value.forEach((item) => {
    if (item.companyId.id === id) {
      isExistProduct.value = true;
    }
  });
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
      if (!isExistProduct.value) {
        const deps = ref([]);

        const { response, isLoading } = useAxios<DataResponse>(
          'delete',
          '/company/' + id,
          {},
          {},
          deps.value
        );
        watch(isLoading, () => {
          isLoadingCompany.value = isLoading.value;
        });
        watch(response, () => {
          companyRender.value = companyRender.value.filter((product) => product.id !== id);
          if (!isLoading.value) {
            if (response.value?.status === 'ok') {
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
            } else {
              Swal.fire({
                title: 'Xóa thất bại',
                icon: 'error',
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
            }
          }
        });
      } else {
        Swal.fire({
          title: 'Không thể xóa công ty đã có danh mục',
          icon: 'error',
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
        isExistProduct.value = false;
      }
    }
  });
};

// Hàm lấy dữ liệu trả về khi ModalAddCompany trả dữ liệu đã add về
const handleAddedChange = (dataAdded: ManageCompany, isLoading: boolean) => {
  isLoadingCompany.value = isLoading;
  isOpenAdd.value = false;

  if (!isLoading) {
    if (Object.keys(dataAdded).length !== 0) companyRender.value.unshift(dataAdded);
    Swal.fire({
      title: 'Thêm thành công',
      icon: 'success',
      confirmButtonText: 'Hoàn tất',
      width: '50rem',
      padding: '0 2rem 2rem 2rem',
      timer: 2000,
      customClass: {
        confirmButton: styles['confirm-button'],
        cancelButton: styles['cancel-button'],
        title: styles['title']
      }
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.close();
      }
    });
  }
};

// Hàm lấy dữ liệu trả về khi ModalUpdate trả dữ liệu đã update về
const handleChangeUpdate = (dataUpdated: ManageCompany, isLoading: boolean) => {
  isLoadingCompany.value = isLoading;
  isOpenUpdate.value = false;

  if (!isLoading) {
    Swal.fire({
      title: 'Cập nhật thành công',
      icon: 'success',
      confirmButtonText: 'Hoàn tất',
      width: '50rem',
      padding: '0 2rem 2rem 2rem',
      timer: 2000,
      customClass: {
        confirmButton: styles['confirm-button'],
        cancelButton: styles['cancel-button'],
        title: styles['title']
      }
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.close();
      }
    });
    companyRender.value.forEach((item) => {
      if (item.id === dataUpdated.id) {
        item.name = dataUpdated.name;
        item.description = dataUpdated.description;
        item.logo = dataUpdated.logo;
      }
    });
  }
};

// Hàm lấy dữ liệu trả về khi ModalUpdate trả dữ liệu đã update sản phẩm nổi bât về
const handleChangeUpdateOutstanding = (outstanding: ManageCompany) => {
  if (outstanding.outstandingProductId !== null) {
    companyRender.value[indexRow.value].outstandingProductId = outstanding.outstandingProductId;
  }
};

// Hàm lấy dữ liệu trả về khi update Highlight công ty
const handleUpdateHighLight = (index: number) => {
  const checkbox = document.getElementById(`myCheckbox${index}`);

  if (checkbox instanceof HTMLInputElement) {
    // Gọi hàm useAxios để lấy response, error, và isLoading
    const { response, isLoading } = useAxios<DataResponse>(
      'patch',
      '/company/highlight/' +
        companyRender.value[index].slug +
        '?' +
        'highlight=' +
        (checkbox.checked ? 1 : 0),
      {},
      {},
      variableChange.value
    );
    watch(isLoading, () => {
      isLoadingCompany.value = isLoading.value;
    });

    watch(response, () => {
      if (!isLoading.value) {
        if (response.value?.status === 'ok') {
          Swal.fire({
            title: 'Cập nhật công ty nổi bật thành công',
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
          companyRender.value[index].highlight = checkbox.checked ? 1 : 0;
        } else {
          Swal.fire({
            title: 'Cập nhật công ty nổi bậc thất bại',
            icon: 'error',
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
        }
      }
    });
  }
};

// Hàm kiểm tra xem công ty có sản phẩm nổi bật hay không
const handleRenderOutstanding = (index: number) => {
  const { outstandingProductId } = companyRender.value[index];

  if (outstandingProductId !== null) {
    const outstandingProduct = products.value.find((item) => item.id === outstandingProductId);
    if (outstandingProduct) {
      outstandingRender = {
        image: outstandingProduct.mainImg,
        name: outstandingProduct.name
      };
      return true;
    }
  }
  return false;
};
</script>
<template>
  <div :class="$style.mn_company">
    <div :class="$style['mn_company--header']">
      <div :class="$style['mn_company--header-left']">
        <h1>CÔNG TY HỢP TÁC</h1>
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
        <p style="width: 28%">Mô tả công ty</p>
        <p style="width: 8%">Logo</p>
        <p style="width: 28%">Sản phẩm nổi bật</p>
        <p style="width: 10%">Chỉnh sửa</p>
        <p style="width: 6%">Nổi bật</p>
      </div>

      <div v-if="!isLoadingCompany">
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
            <div :class="$style['mn_company--table-row-5']" v-if="handleRenderOutstanding(index)">
              <img :src="outstandingRender.image" alt="" />
              <p>{{ outstandingRender.name }}</p>
            </div>
            <div :class="$style['mn_company--table-row-5']" v-else></div>
            <div :class="$style['mn_company--table-row-6']">
              <button @click="deleteCompany(company.id)">
                <font-awesome-icon :icon="faTrash" :class="$style['mn_company--table-ic']" />
              </button>

              <button @click="handleUpdateModal(index, company.outstandingProductId, company.id)">
                <font-awesome-icon :icon="faPen" :class="$style['mn_company--table-ic']" />
              </button>
            </div>
            <input
              :title="company.highlight === 0 ? 'Công ty không nổi bậc' : 'Công ty nổi bậc'"
              type="checkbox"
              :style="{
                width: '6%',
                cursor: company.outstandingProductId === null ? 'auto' : 'pointer'
              }"
              :id="`myCheckbox${index}`"
              @change="handleUpdateHighLight(index)"
              :disabled="company.outstandingProductId === null ? true : false"
              :checked="company.highlight !== 0 ? true : false"
            />
          </div>
        </div>
        <div :class="$style['mn_company--table-row-notfonund']" v-else>
          <p>Không tìm thấy kết quả mong muốn</p>
        </div>
      </div>
      <loading-component style="height: calc(100vh - 400px)" v-else />
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
  <modal-add-company
    v-if="isOpenAdd"
    :changeAddedCompany="handleAddedChange"
    @close="isOpenAdd = false"
  />
  <modal-update-company
    v-if="isOpenUpdate"
    @close="isOpenUpdate = false"
    :item="companyRender[indexRow]"
    :productOutstanding="indexProduct !== -1 ? products[indexProduct] : {}"
    :products="products"
    :idCompany="idCompany"
    :change="handleChangeUpdate"
    :changeOutstanding="handleChangeUpdateOutstanding"
  />
</template>

<style module scoped lang="scss">
@import './ManageCompany.module.scss';
</style>

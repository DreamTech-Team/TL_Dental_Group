<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faTrash, faSearch } from '@fortawesome/free-solid-svg-icons';
// import Swal from 'sweetalert2';
import Pagination from '@/components/Pagination/BasePagination.vue';
import ModalInfoCustomer from './component/ModalInfoCustomer.vue';
// import Chart from 'chart.js/auto';
// import styles from './ManageCustomer.module.scss';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import dayjs from 'dayjs'; // Import dayjs library
import 'dayjs/locale/vi'; // Import Vietnamese locale (optional)
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface Info {
  id: string;
  fullname: string;
  email: string;
  phone: string;
  content: string;
  contacted: boolean;
  createAt: string;
}

const variableChange = ref([]);

const infoCustomerRender = ref<Info[]>([]);
const isOpen = ref(false);
const searchText = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const indexRow = ref(0);
// const widthLine = ref(75);
// const widthLine1 = ref(0);
// const widthLine2 = ref(0);
const isChart = ref(true);
const time = ref('');
const isLoadingInfo = ref(false);

const { response, isLoading } = useAxios<DataResponse>(
  'get',
  '/contact?sort=desc',
  {},
  {},
  variableChange.value
);

watch(isLoading, () => {
  isLoadingInfo.value = isLoading.value;
});

watch(response, () => {
  if (response.value?.status === 'ok') {
    infoCustomerRender.value = response.value?.data?.data;
    pageSize.value = parseInt(response.value?.data?.pageSize);
    total.value = response.value?.data?.total;
    currentPage.value = parseInt(response.value?.data?.page);
  }
});

// Hàm xử lí search
const filteredProducts = computed(() => {
  if (searchText.value.trim() === '') {
    return infoCustomerRender.value;
  } else {
    const searchTerm = searchText.value.toLowerCase();
    return infoCustomerRender.value.filter((customer) =>
      customer.fullname.toLowerCase().includes(searchTerm)
    );
  }
});

// tính toán mỗi page có bao nhiêu phần tử để render lên màn hình
const displayNews = computed(() => {
  const start = currentPage.value * pageSize.value;
  const end = start + pageSize.value;

  console.log(start);
  console.log(end);

  return filteredProducts.value.slice(start, end);
});

// // Xử lí render biểu đồ
// let chartInstance: Chart;
// const renderChart = () => {
//   const chartCanvas = document.getElementById('chartCanvas') as HTMLCanvasElement;

//   if (!chartCanvas) return;

//   const ctx = chartCanvas.getContext('2d');
//   if (!ctx) return;

//   chartInstance = new Chart(ctx, {
//     type: 'line',
//     data: {
//       labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
//       datasets: [
//         {
//           label: 'Khách hàng',
//           data: [65, 59, 80, 81, 56, 55, 40],
//           borderColor: 'blue',
//           fill: false,
//           backgroundColor: '#9BD0F5'
//         }
//         // {
//         //   label: 'Data 2',
//         //   data: [28, 48, 40, 19, 86, 27, 90],
//         //   borderColor: 'blue',
//         //   fill: false
//         // }
//       ]
//     },
//     options: {
//       responsive: true,
//       maintainAspectRatio: false,
//       plugins: {
//         legend: {
//           labels: {
//             // This more specific font property overrides the global property
//             font: {
//               size: 16
//             }
//           }
//         }
//       }
//     }
//   });
// };
// // Xử lý việc render biểu đồ khi component được mounted
// onMounted(renderChart);
// // Xử lý việc xóa biểu đồ khi component bị hủy
// onBeforeUnmount(() => {
//   if (chartInstance) {
//     chartInstance.destroy();
//   }
// });

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

// // Check Tab và xử lí line dưới các title của tab
// const handleLine = (e: Event) => {
//   const target = e.target as HTMLInputElement;
//   const widthSpan1 = document.getElementById('text1');
//   const widthSpan2 = document.getElementById('text2');

//   if (widthSpan1 && widthSpan2) {
//     widthLine1.value = widthSpan1.offsetWidth;
//     widthLine2.value = widthSpan2.offsetWidth;

//     if (target.textContent === 'Thông số') {
//       isChart.value = true;
//       widthLine.value = widthSpan1.offsetWidth;
//     } else {
//       isChart.value = false;
//       widthLine.value = widthSpan2.offsetWidth;
//     }
//   }
// };

const formatTime = (inputString: string) => {
  const outputFormat = 'HH:mm, DD/MM/YYYY';

  dayjs.locale('vi'); // Set locale to Vietnamese (optional)
  const parsedDateTime = dayjs(inputString, { locale: 'vi' });
  return parsedDateTime.format(outputFormat);
};

// Hàm mở modal show thông tin
const handleShowInfo = (id: number) => {
  isOpen.value = true;

  time.value = formatTime(infoCustomerRender.value[id].createAt);

  indexRow.value = id;
};

// // Hàm xóa một khách hàng
// const deleteInfoCustomer = (id: string, e: Event) => {
//   e.stopPropagation();

//   Swal.fire({
//     title: 'Bạn có chắc muốn xóa công ty này không?',
//     text: 'Dữ liệu sẽ không thể khôi phục sau khi xóa!',
//     icon: 'warning',
//     showCancelButton: true,
//     confirmButtonColor: '#3085d6',
//     cancelButtonColor: '#d33',
//     confirmButtonText: 'Xóa',
//     cancelButtonText: 'Hủy',
//     width: '50rem',
//     padding: '0 2rem 2rem 2rem',
//     customClass: {
//       confirmButton: styles['confirm-button'],
//       cancelButton: styles['cancel-button'],
//       title: styles['title']
//     }
//   }).then((result) => {
//     if (result.isConfirmed) {
//       infoCustomerRender.value = infoCustomerRender.value.filter((product) => product.id !== id);

//       Swal.fire({
//         title: 'Xóa thành công',
//         icon: 'success',
//         confirmButtonText: 'Hoàn tất',
//         timer: 2000,
//         width: '50rem',
//         padding: '0 2rem 2rem 2rem',
//         customClass: {
//           confirmButton: styles['confirm-button'],
//           cancelButton: styles['cancel-button'],
//           title: styles['title']
//         }
//       });

//       setTimeout(function () {
//         // Swal.close();
//       }, 1200);
//     }
//   });
// };

// Ngăn cản sự kiện mở modal show info khi click vào checkbox
const handleCheckBox = (e: Event, index: number) => {
  e.stopPropagation();

  const checkbox = document.getElementById(`myCheckbox${index}`);

  if (checkbox instanceof HTMLInputElement) {
    const { response, isLoading } = useAxios<DataResponse>(
      'patch',
      '/contact/' + infoCustomerRender.value[index].id + `?contacted=${checkbox.checked}`,
      {},
      {},
      variableChange.value
    );
    watch(isLoading, () => {
      isLoadingInfo.value = isLoading.value;
    });

    watch(response, () => {
      if (!isLoading.value) {
        if (response.value?.status === 'ok') {
          infoCustomerRender.value[index].contacted = checkbox.checked;
        }
      }
    });
  }
};
</script>
<template>
  <div :class="$style.mn_customer">
    <h2>DANH SÁCH CÔNG TY</h2>

    <div :class="$style['mn_customer--search']">
      <div :class="$style['mn_customer--search-input']" :style="{ justifyContent: 'flex-end' }">
        <!-- <div :class="$style['mn_customer--search-input-left']">
          <span id="text1" @click="handleLine" :class="$style[isChart ? 'active' : '']"
            >Thông số</span
          >
          <span id="text2" @click="handleLine" :class="$style[!isChart ? 'active' : '']"
            >Danh sách khách hàng</span
          >
          <div
            :class="$style['mn_customer--search-input-left-line']"
            :style="{
              width: widthLine + 'px',
              transform: 'translateX' + '(' + (isChart ? 0 : widthLine1 + 20) + 'px' + ')'
            }"
          ></div>
        </div> -->

        <div :class="$style['mn_customer--search-input-right']" v-if="isChart">
          <input type="text" placeholder="Tìm kiếm" v-model="searchText" />

          <font-awesome-icon :icon="faSearch" :class="$style['mn_customer--search-ic']" />
        </div>
        <div
          :class="$style['mn_customer--search-input-right']"
          :style="{ width: '250px', height: '40px' }"
          v-else
        ></div>
      </div>
    </div>

    <div :class="$style['mn_customer--table']" v-if="isChart">
      <div :class="$style['mn_customer--table-titlebar']">
        <p style="width: 8%">STT</p>
        <p style="width: 16%">Họ và tên</p>
        <p style="width: 16%">Số điện thoại</p>
        <p style="width: 24%">Email</p>
        <p style="width: 20%">Thời gian</p>
        <p style="width: 16%">Đã liên hệ</p>
        <!-- <p style="width: 12%">Xóa</p> -->
      </div>

      <loading-component v-if="isLoadingInfo" style="height: calc(100vh - 295px)" />
      <div v-else>
        <div :class="$style['mn_customer--table-list']" v-if="filteredProducts.length > 0">
          <div
            :class="$style['mn_customer--table-row']"
            v-for="(customer, index) in displayNews"
            :key="index"
            @click="handleShowInfo(index)"
          >
            <p :class="$style['mn_customer--table-row-1']">{{ index + 1 }}</p>
            <p :class="$style['mn_customer--table-row-2']">{{ customer.fullname }}</p>
            <p :class="$style['mn_customer--table-row-3']">{{ customer.phone }}</p>
            <p :class="$style['mn_customer--table-row-4']">{{ customer.email }}</p>
            <p :class="$style['mn_customer--table-row-5']">{{ formatTime(customer.createAt) }}</p>
            <input
              :title="customer.contacted ? 'Đã liên hệ' : 'Chưa liên hệ'"
              type="checkbox"
              :id="'myCheckbox' + index"
              style="width: 16%"
              :checked="customer.contacted"
              @click="(e) => handleCheckBox(e, index)"
            />
            <!-- <div :class="$style['mn_customer--table-row-6']">
        <button
          @click="
            (e) => {
              deleteInfoCustomer(customer.id, e);
            }
          "
        >
          <font-awesome-icon :icon="faTrash" :class="$style['mn_customer--table-ic']" />
        </button>
      </div> -->
          </div>
        </div>
        <div :class="$style['mn_customer--table-row-notfonund']" v-else>
          <p>Không tìm thấy kết quả mong muốn</p>
        </div>
      </div>
    </div>

    <!-- <div :class="$style['mn_customer--canvas']" v-show="isChart">
      <canvas id="chartCanvas" ref="chartCanvas"></canvas>
    </div> -->

    <div :class="$style['mn_customer--pagination']" v-if="!isChart">
      <pagination
        :total="Math.ceil(total / pageSize)"
        :current-page="currentPage"
        :page-size="pageSize"
        @current-change="handlePageChange"
      />
    </div>
  </div>

  <modal-info-customer
    v-if="isOpen"
    @close="isOpen = false"
    :customer="infoCustomerRender[indexRow]"
    :time="time"
  />
</template>

<style module scoped lang="scss">
@import './ManageCustomer.module.scss';
</style>

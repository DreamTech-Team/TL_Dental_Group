<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, onActivated } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faTrash, faSearch } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import Pagination from '@/components/Pagination/BasePagination.vue';
import ModalInfoCustomer from './component/ModalInfoCustomer.vue';
import Chart from 'chart.js/auto';
import styles from './ManageCustomer.module.scss';

const infoCustomers = [
  {
    id: 1,
    name: 'Trần Văn A',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 2,
    name: 'Trần Văn A1',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 3,
    name: 'Trần Văn A2',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 4,
    name: 'Trần Văn A3',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 5,
    name: 'Trần Văn A',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 6,
    name: 'Trần Văn A',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 7,
    name: 'Trần Văn A',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 8,
    name: 'Trần Văn A',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 9,
    name: 'Trần Văn A',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 10,
    name: 'Trần Văn A',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 11,
    name: 'Trần Văn A',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 12,
    name: 'Trần Văn A',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  },
  {
    id: 13,
    name: 'Trần Văn A',
    phonenumber: '0388123865',
    email: 'tranvana2023@gmail.com',
    time: '17:30, 10/06/2023'
  }
];

const infoCustomerRender = ref(infoCustomers);
const isOpen = ref(false);
const searchText = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const indexRow = ref(0);
const widthLine = ref(75);
const widthLine1 = ref(0);
const widthLine2 = ref(0);
const isChart = ref(true);

let chartInstance: Chart;

const renderChart = () => {
  const chartCanvas = document.getElementById('chartCanvas') as HTMLCanvasElement;
  console.log(chartCanvas);

  if (!chartCanvas) return;

  const ctx = chartCanvas.getContext('2d');
  if (!ctx) return;

  chartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
      datasets: [
        {
          label: 'Khách hàng',
          data: [65, 59, 80, 81, 56, 55, 40],
          borderColor: 'blue',
          fill: false,
          backgroundColor: '#9BD0F5'
        }
        // {
        //   label: 'Data 2',
        //   data: [28, 48, 40, 19, 86, 27, 90],
        //   borderColor: 'blue',
        //   fill: false
        // }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          labels: {
            // This more specific font property overrides the global property
            font: {
              size: 16
            }
          }
        }
      }
    }
  });
};

// Xử lý việc render biểu đồ khi component được mounted
onMounted(renderChart);

// Xử lý việc xóa biểu đồ khi component bị hủy
onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.destroy();
  }
});

onActivated(() => {
  if (isChart.value) {
    renderChart();
  }
});

const filteredProducts = computed(() => {
  if (searchText.value.trim() === '') {
    return infoCustomerRender.value;
  } else {
    const searchTerm = searchText.value.toLowerCase();
    return infoCustomerRender.value.filter((company) =>
      company.name.toLowerCase().includes(searchTerm)
    );
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

const handleLine = (e: Event) => {
  const target = e.target as HTMLInputElement;
  const widthSpan1 = document.getElementById('text1');
  const widthSpan2 = document.getElementById('text2');

  if (widthSpan1 && widthSpan2) {
    widthLine1.value = widthSpan1.offsetWidth;
    widthLine2.value = widthSpan2.offsetWidth;

    if (target.textContent === 'Thông số') {
      isChart.value = true;
      widthLine.value = widthSpan1.offsetWidth;
      console.log(isChart.value);
    } else {
      isChart.value = false;
      widthLine.value = widthSpan2.offsetWidth;
      console.log(isChart.value);
    }
  }
};

const handleShowInfo = (id: number) => {
  isOpen.value = true;

  indexRow.value = id;
};

const deleteInfoCustomer = (id: number, e: Event) => {
  e.stopPropagation();

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
      infoCustomerRender.value = infoCustomerRender.value.filter((product) => product.id !== id);

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
  <div :class="$style.mn_customer">
    <h2>DANH SÁCH CÔNG TY</h2>

    <div :class="$style['mn_customer--search']">
      <div :class="$style['mn_customer--search-input']">
        <div :class="$style['mn_customer--search-input-left']">
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
        </div>

        <div :class="$style['mn_customer--search-input-right']" v-if="!isChart">
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

    <div :class="$style['mn_customer--table']" v-if="!isChart">
      <div :class="$style['mn_customer--table-titlebar']">
        <p style="width: 8%">STT</p>
        <p style="width: 16%">Họ và tên</p>
        <p style="width: 16%">Số điện thoại</p>
        <p style="width: 28%">Email</p>
        <p style="width: 20%">Thời gian</p>
        <p style="width: 12%">Xóa</p>
      </div>

      <div :class="$style['mn_customer--table-list']" v-if="filteredProducts.length > 0">
        <div
          :class="$style['mn_customer--table-row']"
          v-for="(customer, index) in displayNews"
          :key="index"
          @click="handleShowInfo(customer.id)"
        >
          <p :class="$style['mn_customer--table-row-1']">{{ index + 1 }}</p>
          <p :class="$style['mn_customer--table-row-2']">{{ customer.name }}</p>
          <p :class="$style['mn_customer--table-row-3']">{{ customer.phonenumber }}</p>
          <p :class="$style['mn_customer--table-row-4']">{{ customer.email }}</p>
          <p :class="$style['mn_customer--table-row-5']">{{ customer.time }}</p>
          <!-- <div :class="$style['mn_customer--table-row-4']">
            <img :src="company.email" alt="" />
          </div> -->
          <!-- <div :class="$style['mn_customer--table-row-5']">
            <img :src="company.time" alt="" />
            <p>{{ company.nameProduct }}</p>
          </div> -->
          <div :class="$style['mn_customer--table-row-6']">
            <button
              @click="
                (e) => {
                  deleteInfoCustomer(customer.id, e);
                }
              "
            >
              <font-awesome-icon :icon="faTrash" :class="$style['mn_customer--table-ic']" />
            </button>
          </div>
        </div>
      </div>
      <div :class="$style['mn_customer--table-row-notfonund']" v-else>
        <p>Không tìm thấy kết quả mong muốn</p>
      </div>
    </div>

    <div :class="$style['mn_customer--canvas']">
      <canvas id="chartCanvas" ref="chartCanvas" v-show="isChart"></canvas>
    </div>

    <div :class="$style['mn_customer--pagination']" v-if="!isChart">
      <pagination
        :total="Math.ceil(filteredProducts.length / pageSize)"
        :current-page="currentPage"
        :page-size="pageSize"
        @current-change="handlePageChange"
      />
    </div>
  </div>

  <modal-info-customer
    v-if="isOpen"
    @close="isOpen = false"
    :name="infoCustomerRender[indexRow - 1].name"
    :phonenumber="infoCustomerRender[indexRow - 1].phonenumber"
    :email="infoCustomerRender[indexRow - 1].email"
    :time="infoCustomerRender[indexRow - 1].time"
  />
</template>

<style module scoped lang="scss">
@import './ManageCustomer.module.scss';
</style>

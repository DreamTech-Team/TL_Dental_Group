<script setup lang="ts">
import { ref, computed } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
// import ModalAdd from './components/ModalAdd.vue';
import Pagination from '@/components/Pagination/BasePagination.vue';
import UpdateActivity from './ModalActivity/UpdateActivity.vue';
import { activities } from '../Activity';

const searchText = ref('');
const results = ref(activities);

const currentPage = ref(1);
const pageSize = ref(10);
const isModalOpen = ref(false);
const activeTab = ref('activity');

const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};
interface SelectActivity {
  id: number;
  name: string;
  tags: string[];
  summary?: string;
  description?: string;
  date: string;
}

const selectedActivity = ref<Record<string, never> | SelectActivity>({});

// Trong phần code xử lý sự kiện
const editActivity = (activity: SelectActivity) => {
  selectedActivity.value = activity;
  isModalOpen.value = true; // Mở modal
  console.log(activity);
};

const filteredActivitiess = computed(() => {
  if (searchText.value.trim() === '') {
    return results.value;
  } else {
    const searchTerm = searchText.value.toLowerCase();
    return results.value.filter((activity) => activity.name.toLowerCase().includes(searchTerm));
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
  return filteredActivitiess.value.slice(start, end);
});

const truncateText = (text: string, maxLength: number) => {
  if (text.length > maxLength) {
    return text.slice(0, maxLength) + '...';
  }
  return text;
};

const deleteActivity = (id: number) => {
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
      results.value = results.value.filter((activity) => activity.id !== id);

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
  <div>
    <div :class="$style.mn_activityTable">
      <div :class="$style.mn_activity_control">
        <span></span>
        <div :class="$style['mn_activity_control-input1']">
          <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['mn_activity_control-ic2']" />
          <input v-model="searchText" placeholder="Tìm kiếm" />
        </div>
      </div>
      <div :class="$style.mn_activity_body">
        <table :class="$style.mn_activity_table1">
          <thead>
            <tr>
              <th style="width: 5%">STT</th>
              <th style="width: 25%">Tên hoạt động</th>
              <th style="width: 30%">Tags</th>
              <th style="width: 25%">Thời gian diễn ra</th>
              <th style="width: 15%">Chỉnh sửa</th>
            </tr>
          </thead>
        </table>
        <div :class="$style.mn_activity_table_ctn">
          <table :class="$style.mn_activity_table">
            <tbody>
              <template v-if="filteredActivitiess.length > 0">
                <tr v-for="(item, index) in displayNews" :key="index">
                  <td style="max-width: 5%">{{ index + 1 }}</td>
                  <td style="max-width: 25%">
                    {{ truncateText(item.name, 33) }}
                  </td>
                  <td style="width: 30%">{{ truncateText(item.tags.join(', '), 40) }}</td>

                  <td style="width: 25%">{{ item.date }}</td>
                  <td style="width: 15%">
                    <button :class="$style['btn-room-trash']" @click="deleteActivity(item.id)">
                      <font-awesome-icon :icon="faTrash" />
                    </button>
                    <button @click="editActivity(item)" :class="$style['edit-room-btn']">
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
      <div :class="$style['mn_activity_pagination']">
        <pagination
          :total="Math.ceil(filteredActivitiess.length / pageSize)"
          :current-page="currentPage"
          :page-size="pageSize"
          @current-change="handlePageChange"
        />
      </div>
    </div>
    <div :class="$style.activity_overlay" v-if="isModalOpen">
      <update-activity
        :selectedActivity="selectedActivity"
        :isModalOpen="isModalOpen"
        :closeModal="closeModal"
        v-if="activeTab === 'activity'"
        @click.stop
        @close="closeModal"
      />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../Activity.module.scss';
</style>

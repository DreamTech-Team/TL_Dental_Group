<script setup lang="ts">
import { ref, computed } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
// import ModalAdd from './components/ModalAdd.vue';
import Pagination from '@/components/Pagination/BasePagination.vue';
import UpdateTag from './ModalTag/UpdateTag.vue';
import { tags } from '../Activity';

const searchText = ref('');
const resultTags = ref(tags);
const currentPage = ref(1);
const pageSize = ref(10);
const isModalOpen = ref(false);
const activeTab = ref('tags');

interface Tags {
  id: number;
  name: string;
  createDate: string;
}

const selectedTag = ref<Record<string, never> | Tags>({});

// Trong phần code xử lý sự kiện
const editTag = (tags: Tags) => {
  selectedTag.value = tags;
  isModalOpen.value = true; // Mở modal
  console.log(tags);
};

const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

const filteredTags = computed(() => {
  if (searchText.value.trim() === '') {
    return resultTags.value;
  } else {
    const searchTerm = searchText.value.toLowerCase();
    return resultTags.value.filter((tag) => tag.name.toLowerCase().includes(searchTerm));
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

const displayTags = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredTags.value.slice(start, end);
});

const truncateText = (text: string, maxLength: number) => {
  if (text.length > maxLength) {
    return text.slice(0, maxLength) + '...';
  }
  return text;
};

const deleteTag = (id: number) => {
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
      resultTags.value = resultTags.value.filter((tag) => tag.id !== id);

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
    <div>
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
              <th style="width: 25%">Tên tag</th>
              <th style="width: 30%">Ngày tạo tag</th>
              <th style="width: 15%">Chỉnh sửa</th>
            </tr>
          </thead>
        </table>

        <div :class="$style.mn_activity_table_ctn">
          <table :class="$style.mn_activity_table">
            <tbody>
              <template v-if="filteredTags.length > 0">
                <tr v-for="(item, index) in displayTags" :key="index">
                  <td style="width: 5%">{{ index + 1 }}</td>
                  <td style="width: 25%">
                    {{ truncateText(item.name, 40) }}
                  </td>
                  <td style="width: 30%">{{ item.createDate }}</td>
                  <td style="width: 15%">
                    <button :class="$style['btn-room-trash']" @click="deleteTag(item.id)">
                      <font-awesome-icon :icon="faTrash" />
                    </button>
                    <button @click="editTag(item)" :class="$style['edit-room-btn']">
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
          :total="Math.ceil(filteredTags.length / pageSize)"
          :current-page="currentPage"
          :page-size="pageSize"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <div :class="$style.activity_overlay" v-if="isModalOpen">
      <update-tag
        :selectedTag="selectedTag"
        :closeModal="closeModal"
        v-if="activeTab === 'tags'"
        @click.stop
        @close="closeModal"
      />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../Activity.module.scss';
</style>

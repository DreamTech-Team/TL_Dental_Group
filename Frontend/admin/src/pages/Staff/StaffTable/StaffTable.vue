<script setup lang="ts">
import { ref, computed, watch, watchEffect } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
// import ModalAdd from './components/ModalAdd.vue';
import UpdateStaff from './StaffModal/UpdateStaff.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import Loading from '@/components/LoadingComponent/LoadingComponent.vue';

interface StaffItem {
  id: string;
  email: string;
  fullname: string;
  phonenumber: string;
  address: string;
  password: string;
  changed: boolean;
  createAt: string;
  roles: string;
}
interface MyErrorResponse {
  response?: {
    data?: {
      message?: string;
    };
  };
}
const props = defineProps({
  staffArray: {
    type: Array as () => StaffItem[],
    required: true
  },
  handleStaffDeleted: {
    type: Function,
    required: true
  }
});

const results = ref(props.staffArray);
const searchKeyword = ref('');
const deps = ref([]);
const isLoading = ref(false);

const isModalOpen = ref(false);
const activeTab = ref('activity');

const closeModal = () => {
  isModalOpen.value = false;
};

const selectedStaff = ref<Record<string, never> | StaffItem>({});

// Trong phần code xử lý sự kiện
const editActivity = (staff: StaffItem) => {
  selectedStaff.value = staff;
  isModalOpen.value = true; // Mở modal
  console.log(staff);
};

const filteredStaffs = computed(() => {
  return props.staffArray.filter((staff) =>
    staff.fullname.toLowerCase().includes(searchKeyword.value.toLowerCase())
  );
});

const handleUpdateStaff = (updatedStaff: StaffItem) => {
  filteredStaffs.value.forEach((item) => {
    if (item.id === updatedStaff.id) {
      (item.fullname = updatedStaff.fullname),
        (item.email = updatedStaff.email),
        (item.roles = updatedStaff.roles),
        (item.phonenumber = updatedStaff.phonenumber),
        (item.address = updatedStaff.address),
        (item.password = updatedStaff.password),
        (item.changed = updatedStaff.changed),
        (item.createAt = updatedStaff.createAt),
        (item.id = updatedStaff.id);
    }
  });
};

watch(props, () => {
  results.value = props.staffArray;
});

const truncateText = (text: string, maxLength: number) => {
  if (text.length > maxLength) {
    return text.slice(0, maxLength) + '...';
  }
  return text;
};

const deleteStaff = async (id: string) => {
  Swal.fire({
    title: 'Bạn có chắc muốn xóa?',
    text: 'Dữ liệu sẽ không thể khôi phục sau khi xóa!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Xóa',
    cancelButtonText: 'Hủy'
  }).then(async (result) => {
    if (result.isConfirmed) {
      try {
        // Gọi API DELETE bằng cách sử dụng `useAxios`
        const deleteStaffRes = useAxios<DataResponse>(
          'delete',
          `/employees/${id}`, // Đặt endpoint URL xóa phần tử cụ thể dựa vào id
          {},
          {},
          deps.value
        );
        watch(deleteStaffRes.isLoading, () => {
          isLoading.value = deleteStaffRes.isLoading.value;
          // console.log(isLoading.value);
        });
        watch(deleteStaffRes.response, () => {
          // console.log(deleteTagRes.response.value);
          if (deleteStaffRes.response.value?.status === 'ok') {
            props.handleStaffDeleted(id);
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

        watch(deleteStaffRes.error, () => {
          const errorValue: MyErrorResponse | null = deleteStaffRes.error
            .value as MyErrorResponse | null;
          if (errorValue !== null) {
            if (errorValue?.response?.data?.message === "Staff's email already taken") {
              Swal.fire({
                title: 'Xóa không thành công!',
                icon: 'error',
                confirmButtonText: 'Đóng',
                width: '50rem',
                padding: '0 2rem 2rem 2rem'
              });
              return;
            }
          }
        });
      } catch (error) {
        console.error('Error deleting item:', error);
        Swal.fire({
          title: 'Xóa không thành công',
          text: 'Có lỗi xảy ra khi xóa!',
          icon: 'error',
          width: '30rem'
        });
      }
    }
  });
};
</script>
<template>
  <div>
    <div :class="$style.mn_activityTable">
      <div :class="$style.mn_activity_control">
        <div :class="$style['mn_activity_control-input1']">
          <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['mn_activity_control-ic2']" />
          <input v-model="searchKeyword" placeholder="Tìm kiếm" />
        </div>
      </div>
      <div :class="$style.mn_activity_body">
        <table :class="$style.mn_activity_table1">
          <thead>
            <tr>
              <th style="width: 5%">STT</th>
              <th style="width: 18%">Họ và tên</th>
              <th style="width: 10%">Vị trí</th>
              <th style="width: 12%">Số điện thoại</th>
              <th style="width: 24%">Địa chỉ</th>
              <th style="width: 20%">Email</th>
              <th style="width: 16%">Chỉnh sửa</th>
            </tr>
          </thead>
        </table>
        <div v-if="!isLoading" :class="$style.mn_activity_table_ctn">
          <table :class="$style.mn_activity_table">
            <tbody>
              <template v-if="filteredStaffs.length > 0">
                <tr v-for="(item, index) in filteredStaffs" :key="index">
                  <td style="width: 5%">{{ index + 1 }}</td>
                  <td style="width: 18%">{{ truncateText(item.fullname, 20) }}</td>
                  <td style="width: 10%">{{ truncateText(item.roles, 10) }}</td>
                  <td style="width: 12%">{{ truncateText(item.phonenumber, 15) }}</td>
                  <td style="width: 25%">{{ truncateText(item.address, 30) }}</td>
                  <td style="width: 20%">{{ truncateText(item.email, 20) }}</td>
                  <td style="width: 10%">
                    <button :class="$style['btn-room-trash']" @click="deleteStaff(item.id)">
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
        <Loading v-else />
      </div>
    </div>
    <div :class="$style.activity_overlay" v-if="isModalOpen">
      <update-staff
        :selectedStaff="selectedStaff"
        :isModalOpen="isModalOpen"
        :change="handleUpdateStaff"
        :closeModal="closeModal"
        v-if="activeTab === 'activity'"
        @click.stop
        @close="closeModal"
      />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../ManageStaff.module.scss';
</style>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import styles from './StaffModal.module.scss';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import Loading from '@/components/LoadingComponent/LoadingComponent.vue';

import { type PropType } from 'vue';
import Swal from 'sweetalert2';

interface MyErrorResponse {
  response?: {
    data?: {
      message?: string;
    };
  };
}

interface Staff {
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

const dataAdded = ref<Staff>({
  fullname: '',
  email: '',
  roles: '',
  phonenumber: '',
  address: '',
  password: '',
  changed: false,
  createAt: '',
  id: ''
});

export interface CloseModalFn {
  (...payload: any[]): void;
}

const props = defineProps({
  selectedStaff: {
    type: Object,
    required: true
  },
  closeModal: {
    type: Function as PropType<CloseModalFn>,
    required: true
  },
  change: {
    type: Function as PropType<(newData: Staff) => void>,
    required: true
  }
});

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', data: { dataStaffAdded: Staff }): void;
}>();

const fullname = ref(props.selectedStaff.fullname);
const roles = ref(props.selectedStaff.roles === 'ROLE_ADMIN' ? 'Admin' : 'Nhân viên');
console.log(roles.value);

const phonenumber = ref(props.selectedStaff.phonenumber);
const address = ref(props.selectedStaff.address);
const emailStaff = ref(props.selectedStaff.email);
const deps = ref([]);
const isLoading = ref(false);

// Hàm submit dữ liệu, đẩy dữ liệu lên database
const submitForm = () => {
  console.log(props.selectedStaff.id);

  if (fullname.value.length < 1) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '50rem',
      padding: '0 2rem 2rem 2rem',
      customClass: {
        confirmButton: styles['confirm-button'],
        cancelButton: styles['cancel-button'],
        title: styles['title']
      }
    });
  } else {
    if (roles.value === 'Nhân viên') {
      roles.value = 'ROLE_STAFF';
    } else {
      roles.value = 'ROLE_ADMIN';
    }

    isLoading.value = true;
    const object = {
      fullname: fullname.value,
      email: emailStaff.value,
      roles: roles.value,
      phonenumber: phonenumber.value,
      address: address.value
    };

    console.log(object);

    const createStaff = useAxios<DataResponse>(
      'patch',
      `/employees/${props.selectedStaff.id}`,
      object,
      {},
      deps.value
    );

    watch(createStaff.response, () => {
      if (createStaff.response.value?.status === 'ok') {
        isLoading.value = false;
        Swal.fire({
          title: 'Cập nhật thành công',
          icon: 'success',
          confirmButtonText: 'Hoàn tất',
          width: '30rem'
        }).then((result) => {
          if (result.isConfirmed) {
            Swal.close();
            dataAdded.value = {
              id: createStaff.response.value?.data?.id,
              email: createStaff.response.value?.data?.email,
              fullname: createStaff.response.value?.data?.fullname,
              phonenumber: createStaff.response.value?.data?.phonenumber,
              address: createStaff.response.value?.data?.address,
              password: createStaff.response.value?.data?.password,
              changed: createStaff.response.value?.data?.changed,
              createAt: createStaff.response.value?.data?.createAt,
              roles: createStaff.response.value?.data?.roles
            };
            props.change(dataAdded.value);
            emits('update-content', {
              dataStaffAdded: createStaff.response.value?.data
            });
            emits('close');
          }
        });
      }
    });

    watch(createStaff.error, () => {
      const errorValue: MyErrorResponse | null = createStaff.error.value as MyErrorResponse | null;
      if (errorValue !== null) {
        if (errorValue?.response?.data?.message) {
          if (errorValue?.response?.data?.message.includes('could not execute statement')) {
            Swal.fire({
              title: 'Cập nhật nhân viên không thành công!',
              icon: 'error',
              confirmButtonText: 'Đóng',
              width: '50rem',
              padding: '0 2rem 2rem 2rem',
              customClass: {
                confirmButton: styles['confirm-button'],
                cancelButton: styles['cancel-button'],
                title: styles['title']
              }
            });
            return;
          }
        }
      }
    });
  }
};
</script>
<template>
  <div :class="$style.activity_container">
    <div :class="$style['activity_container--title']">
      <p>CHỈNH SỬA NHÂN VIÊN</p>
      <font-awesome-icon @click="closeModal" :icon="faXmark" :class="$style.activity_cancel" />
    </div>
    <div :class="$style['activity_container--wrap']">
      <div :class="$style['activity_container--wrap-left']">
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-name">Họ và tên</label>
          <input
            id="staff-name"
            v-model="fullname"
            placeholder="Nhập họ và tên"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-position">Vị trí</label>
          <select id="staff-position" v-model="roles" :class="$style['custom-select']">
            <!-- Loại bỏ option disabled và thêm thuộc tính selected -->
            <option value="Nhân viên">Nhân viên</option>
            <option value="Admin">Admin</option>
          </select>
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-phone">Số điện thoại</label>
          <input
            id="staff-phone"
            v-model="phonenumber"
            placeholder="Nhập số điện thoại"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-address">Địa chỉ</label>
          <input
            id="staff-address"
            v-model="address"
            placeholder="Nhập địa chỉ"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="staff-email">Email</label>
          <input
            id="staff-email"
            v-model="emailStaff"
            placeholder="Nhập email"
            :class="$style['custom-input']"
          />
        </div>
      </div>
      <div :class="$style['activity_container--btn']">
        <button @click="emits('close')" :class="$style.button_cancel">Hủy</button>
        <button @click="submitForm" :class="$style.button_add">Cập nhật</button>
      </div>
    </div>

    <div v-show="isLoading" :class="$style.loading__overlay">
      <Loading />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './StaffModal.module.scss';
</style>

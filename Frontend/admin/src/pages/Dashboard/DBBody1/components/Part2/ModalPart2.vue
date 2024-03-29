<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import Swal from 'sweetalert2';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const content = defineProps({
  ceo: {
    type: String,
    required: true
  },
  address: {
    type: String,
    required: true
  },
  day: {
    type: String,
    required: true
  }
});

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', data: { ceo: string; address: string; day: string }): void;
}>();

const convertToDate = (dateString: string): Date | null => {
  const parts: string[] = dateString.split('/');
  if (parts.length !== 3) {
    return null;
  }

  const day: number = parseInt(parts[0]);
  const month: number = parseInt(parts[1]) - 1;
  const year: number = parseInt(parts[2]);

  if (isNaN(day) || isNaN(month) || isNaN(year)) {
    return null;
  }

  const dateObject: Date = new Date(year, month, day);
  return dateObject;
};

const formatDate = (inputDate: string) => {
  const dateParts: string[] = inputDate.split('-');
  if (dateParts.length !== 3) {
    throw new Error('Invalid date format');
  }

  const year: string = dateParts[0];
  const month: string = dateParts[1];
  const day: string = dateParts[2];

  return `${day}/${month}/${year}`;
};

//Validate form
const CEO = ref(content.ceo);
const Address = ref(content.address);
const Day = ref(convertToDate(content.day));

const formattedDate = computed(() => {
  if (Day.value instanceof Date && !isNaN(Day.value.getTime())) {
    const year = Day.value.getFullYear();
    const month = String(Day.value.getMonth() + 1).padStart(2, '0');
    const day = String(Day.value.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  } else {
    return '';
  }
});

const updateInfor = (e: Event, tag: string) => {
  const target = e.target as HTMLInputElement;
  switch (tag) {
    case 'CEO':
      CEO.value = target.value;
      break;
    case 'Address':
      Address.value = target.value;
      break;
    case 'Day':
      Day.value = target.valueAsDate;
      break;
    default:
      break;
  }
};

const submitForm = () => {
  if (CEO.value.length < 4 || Address.value.length < 4) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '30rem'
    });
  } else {
    const deps = ref([]);
    const object = {
      fullnameCEO: CEO.value,
      addressCEO: Address.value,
      inaugurationCEO: formatDate(formattedDate.value)
    };
    const { response } = useAxios<DataResponse>(
      'patch',
      '/information/ceo',
      object,
      {},
      deps.value
    );

    watch(response, () => {
      if (response.value?.status === 'ok') {
        Swal.fire({
          title: 'Cập nhật thành công',
          icon: 'success',
          confirmButtonText: 'Hoàn tất',
          width: '30rem'
        }).then((result) => {
          if (result.isConfirmed) {
            Swal.close();
            emits('update-content', {
              ceo: CEO.value,
              address: Address.value,
              day: formatDate(formattedDate.value)
            });
            emits('close');
          }
        });
      }
    });
  }
};
</script>

<template>
  <div :class="$style.part2__overlay">
    <div :class="$style.part2__modal">
      <div :class="$style.part2__modal__heading">
        <h4>Thông tin CEO</h4>
      </div>
      <div :class="$style.part2__modal__body">
        <h4>Họ tên</h4>
        <input
          type="text"
          placeholder="Nhập tên CEO"
          v-model="CEO"
          @input="updateInfor($event, 'CEO')"
        />
        <h4>Địa chỉ</h4>
        <input
          type="text"
          placeholder="Nhập địa chỉ"
          v-model="Address"
          @input="updateInfor($event, 'Address')"
        />
        <h4>Ngày nhậm chức</h4>
        <input type="date" :value="formattedDate" @change="updateInfor($event, 'Day')" />
      </div>
      <div :class="$style['modal__buttons']">
        <button @click="$emit('close')">Hủy</button>
        <button @click="submitForm">Cập nhật</button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalPart2.module.scss';
</style>

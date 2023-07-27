<script setup lang="ts">
import { ref, watch } from 'vue';
import Swal from 'sweetalert2';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const content = defineProps({
  namecomp: {
    type: String,
    required: true
  },
  slogan: {
    type: String,
    required: true
  },
  years: {
    type: String,
    required: true
  }
});

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', data: { namecomp: string; slogan: string; years: string }): void;
}>();

//Validate form
const Comp = ref(content.namecomp);
const Slogan = ref(content.slogan);
const Years = ref(content.years.split(' ')[0]);
const Tags = ref(content.years.split(' ')[1]);

const updateInfor = (e: Event, tag: string) => {
  const target = e.target as HTMLInputElement;
  switch (tag) {
    case 'Comp':
      Comp.value = target.value;
      break;
    case 'Slogan':
      Slogan.value = target.value;
      break;
    case 'Years':
      Years.value = target.value;
      break;
    default:
      break;
  }
};

const submitForm = () => {
  if (Comp.value.length < 4 || Slogan.value.length < 4 || Years.value.length === 0) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '30rem'
    });
  } else {
    const deps = ref([]);
    const object = {
      name: Comp.value,
      slogan: Slogan.value,
      duringTime: Years.value + ' ' + Tags.value
    };
    const { response } = useAxios<DataResponse>(
      'patch',
      '/information/general',
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
              namecomp: Comp.value,
              slogan: Slogan.value,
              years: Years.value + ' ' + Tags.value
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
  <div :class="$style.part1__overlay">
    <div :class="$style.part1__modal">
      <div :class="$style.part1__modal__heading">
        <h4>Thông tin chung</h4>
      </div>
      <div :class="$style.part1__modal__body">
        <h4>Tên công ty</h4>
        <input
          type="text"
          placeholder="Nhập tên công ty"
          v-model="Comp"
          @input="updateInfor($event, 'Comp')"
        />
        <h4>Slogan</h4>
        <input
          type="text"
          placeholder="Nhập Slogan"
          v-model="Slogan"
          @input="updateInfor($event, 'Slogan')"
        />
        <h4>Khoảng thời gian hoạt động</h4>
        <div :class="$style.part1__modal__years">
          <input
            type="text"
            placeholder="Nhập thời gian"
            v-model="Years"
            @input="updateInfor($event, 'Years')"
          />
          <select v-model="Tags">
            <option>YEARS</option>
            <option>MONTHS</option>
          </select>
        </div>
      </div>
      <div :class="$style['modal__buttons']">
        <button @click="$emit('close')">Hủy</button>
        <button @click="submitForm">Cập nhật</button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalPart1.module.scss';
</style>

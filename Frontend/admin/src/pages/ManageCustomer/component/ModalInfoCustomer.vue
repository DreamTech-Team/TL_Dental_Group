<script setup lang="ts">
import { Ref, ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faRotate, faPencil } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';

interface Info {
  id: string;
  fullname: string;
  email: string;
  phone: string;
  content: string;
  contacted: string;
  createAt: string;
}

const context = defineProps({
  customer: {
    type: Object,
    required: true
  },
  time: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['close']);

const dataCustomer = ref(context.customer as Info);
const timeData = ref(context.time);
</script>

<template>
  <div :class="$style.mncompany__overlay">
    <div :class="$style.mncompany__modal">
      <div :class="$style.mncompany__modal__heading">
        THÔNG TIN CHI TIẾT
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['mncompany__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.mncompany__modal__body">
        <h4>
          Họ và tên: <span>{{ dataCustomer.fullname }}</span>
        </h4>
        <h4>
          Số điện thoại: <span>{{ dataCustomer.phone }}</span>
        </h4>
        <h4>
          Email: <span>{{ dataCustomer.email }}</span>
        </h4>
        <h4>
          Nội dung: <span>{{ dataCustomer.content }}</span>
        </h4>
        <h4>
          Thời gian: <span>{{ timeData }}</span>
        </h4>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalInfoCustomer.module.scss';
</style>

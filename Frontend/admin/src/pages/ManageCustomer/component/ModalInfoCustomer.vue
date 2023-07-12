<script setup lang="ts">
import { Ref, ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faRotate, faPencil } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';

const context = defineProps({
  name: {
    type: String,
    required: true
  },
  phonenumber: {
    type: String,
    required: true
  },
  email: {
    type: String,
    required: true
  },
  time: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['close']);

const nameCustomer = ref(context.name);
const phonenumberCustomer = ref(context.phonenumber);
const emailCustomer = ref(context.email);
const timeCustomer = ref(context.time);
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
          Họ và tên: <span>{{ nameCustomer }}</span>
        </h4>
        <h4>
          Số điện thoại: <span>{{ phonenumberCustomer }}</span>
        </h4>
        <h4>
          Email: <span>{{ emailCustomer }}</span>
        </h4>
        <h4>
          Thời gian: <span>{{ timeCustomer }}</span>
        </h4>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalInfoCustomer.module.scss';
</style>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import ManageRecruitment from './RecruitmentTable.vue';
import ModalAdd from './ModalRecruitment/ModalAdd.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

const isOpen = ref(false); //Open Modal Add
const activeTab = ref('activity');
const total = ref(0);

//Data from Modal Add
const onUpdateContent = () => {
  window.location.reload();
};

const deps = ref([]);

//GET DATA
const { response } = useAxios<DataResponse>(
  'get',
  `/recruitment/?page=0&pageSize=10000`,
  {},
  {},
  deps.value
);

//Convert array to compatible with data
watch(response, () => {
  total.value = response.value?.data?.total;
});
</script>
<template>
  <div>
    <div>
      <div :class="$style.mn_activity">
        <h3>QUẢN LÝ TUYỂN DỤNG</h3>

        <div v-if="activeTab === 'activity'" :class="$style['mn_activity--total']">
          <p>
            Có tất cả <span :class="$style.highlight">{{ total }}</span> nhân viên
          </p>

          <button @click="isOpen = true" :class="$style.mn_activity_control">
            <font-awesome-icon :icon="faPlus" :class="$style['mn_activity--total-ic1']" />
            THÊM BÀI TUYỂN DỤNG
          </button>
        </div>
        <manage-recruitment :total="total" />
      </div>
      <div :class="$style.activity_overlay" v-if="isOpen">
        <modal-add v-if="isOpen" @close="isOpen = false" @update-content="onUpdateContent" />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './MNRecruitment.mudole.scss';
</style>

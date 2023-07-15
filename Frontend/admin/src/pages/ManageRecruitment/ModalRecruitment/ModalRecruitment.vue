<script setup lang="ts">
import Editor from '@tinymce/tinymce-vue';
import { ref, computed } from 'vue';
import type { CSSProperties } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faPlus, faMagnifyingGlass, faMinus, faXmark } from '@fortawesome/free-solid-svg-icons';
import { type PropType } from 'vue';

export interface CloseModalFn {
  (...payload: any[]): void;
}

const props = defineProps({
  closeModal: {
    type: Function as PropType<CloseModalFn>,
    required: true
  }
});

const textareaStyle: CSSProperties = {
  resize: 'none',
  height: 'calc(100vh - 630px)',
  maxHeight: '400px',
  borderRadius: '4px',
  padding: '8px',
  fontSize: '16px',
  fontFamily: 'Roboto',
  border: '1px solid #ccc'
};

const textareaStyle2: CSSProperties = {
  resize: 'none',
  height: 'calc(100vh - 500px)',
  maxHeight: '400px',
  borderRadius: '4px',
  padding: '8px',
  fontSize: '16px',
  fontFamily: 'Roboto',
  border: '1px solid #ccc'
};

const _content = ref(``);
const recruitmentTitle = ref('');
const addressInfo = ref('');
const jobDescription = ref('');
const candidateRequirements = ref('');
const rightForCandidate = ref('');
const contact = ref('');
const selectedTime = ref('');
const treat = ref('');

const selectedPosition = ref('');
const currentStep = ref(1);

const positionOptions = [
  { value: 'option1', label: 'Tùy chọn 1' },
  { value: 'option2', label: 'Tùy chọn 2' },
  { value: 'option3', label: 'Tùy chọn 3' }
];
const timeOptions = [
  { value: 'option1', label: 'Tùy chọn 1' },
  { value: 'option2', label: 'Tùy chọn 2' },
  { value: 'option3', label: 'Tùy chọn 3' }
];

const saveInformation = () => {
  // Tiến hành lưu thông tin hoặc các xử lý khác
};

const prevStep = () => {
  if (currentStep.value > 1) {
    currentStep.value -= 1;
  } else if (currentStep.value === 1) {
    props.closeModal(); // Đóng modal khi currentStep là 1
  }
};

const nextStep = () => {
  // Kiểm tra các điều kiện trước khi chuyển sang bước tiếp theo

  if (currentStep.value < 4) {
    currentStep.value += 1;
  } else {
    // Nếu đang ở bước cuối cùng, thực hiện lưu thông tin hoặc tiến hành các xử lý khác
    saveInformation();
  }
};

const handleChangeContent = (e: { target: { getContent: () => string } }) => {
  _content.value = e.target.getContent();
  console.log(_content.value);
};
</script>
<template>
  <div :class="$style.activity_container">
    <div :class="$style['activity_container--title']">
      <p>THÊM BÀI TUYỂN DỤNG</p>
      <font-awesome-icon @click="closeModal" :icon="faXmark" :class="$style.activity_cancel" />
    </div>

    <!-- Step 1 -->
    <div v-if="currentStep === 1" :class="$style['activity_container--wrap']">
      <div :class="$style['activity_container--wrap-left']">
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="recruitment-name"
            >Đăng bài tuyển dụng</label
          >
          <input
            id="recruitment-name"
            v-model="recruitmentTitle"
            placeholder="Nhập họ và tên"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="recruitment-address">Địa chỉ</label>
          <input
            id="recruitment-address"
            v-model="addressInfo"
            placeholder="Nhập vị trí"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.options_wrap">
          <div>
            <p :class="$style.options_title">Vị trí</p>
            <select :class="$style.options_wrap_btn" v-model="selectedPosition">
              <option value="">Chọn một tùy chọn</option>
              <option v-for="option in positionOptions" :key="option.value" :value="option.value">
                {{ option.label }}
              </option>
            </select>
          </div>
          <div>
            <p :class="$style.options_title">Thời gian làm</p>
            <select :class="$style.options_wrap_btn" v-model="selectedTime">
              <option value="">Chọn một tùy chọn</option>
              <option v-for="option in timeOptions" :key="option.value" :value="option.value">
                {{ option.label }}
              </option>
            </select>
          </div>
        </div>
      </div>
      <div :class="$style['activity_container--wrap--btn']">
        <button @click="prevStep" :class="$style.button_cancel">Hủy</button>
        <button @click="nextStep" :class="$style.button_add">Tiếp tục</button>
      </div>
    </div>

    <!-- Step2 -->
    <div v-else-if="currentStep === 2" :class="$style['activity_container--wrap']">
      <div :class="$style['activity_container--wrap-left']">
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="recruitment-description"
            >Mô tả công việc</label
          >
          <textarea
            :style="textareaStyle"
            id="recruitment-description"
            v-model="jobDescription"
            placeholder="Nhập mô tả"
            :class="$style['custom-textarea']"
          ></textarea>
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="recruitment-require"
            >Yêu cầu ứng viên</label
          >
          <textarea
            :style="textareaStyle"
            id="recruitment-require"
            v-model="candidateRequirements"
            placeholder="Nhập yêu cầu"
            :class="$style['custom-textarea']"
          ></textarea>
        </div>
      </div>
      <div :class="$style['activity_container--wrap--btn']">
        <button @click="prevStep" :class="$style.button_cancel">Quay lại</button>
        <button @click="nextStep" :class="$style.button_add">Tiếp tục</button>
      </div>
    </div>

    <!-- Step3 -->
    <div v-else-if="currentStep === 3" :class="$style['activity_container--wrap']">
      <div :class="$style['activity_container--wrap-left']">
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="recruitment-description">Quyền lợi</label>
          <textarea
            :style="textareaStyle"
            id="recruitment-description"
            v-model="rightForCandidate"
            placeholder="Nhập quyền lời"
            :class="$style['custom-textarea']"
          ></textarea>
        </div>
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="recruitment-require">Liên hệ</label>
          <textarea
            :style="textareaStyle"
            id="recruitment-require"
            v-model="contact"
            placeholder="Nhập thông tin liên hệ"
            :class="$style['custom-textarea']"
          ></textarea>
        </div>
      </div>
      <div :class="$style['activity_container--wrap--btn']">
        <button @click="prevStep" :class="$style.button_cancel">Quay lại</button>
        <button @click="nextStep" :class="$style.button_add">Tiếp tục</button>
      </div>
    </div>

    <!-- Step4 -->
    <div v-else :class="$style['activity_container--wrap']">
      <div :class="$style['activity_container--wrap-left']">
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="recruitment-description">Đãi ngộ</label>
          <textarea
            :style="textareaStyle2"
            id="recruitment-description"
            v-model="treat"
            placeholder="Nhập đãi ngộ"
            :class="$style['custom-textarea']"
          ></textarea>
        </div>
      </div>
      <div :class="$style['activity_container--wrap--btn']">
        <button @click="prevStep" :class="$style.button_cancel">Quay lại</button>
        <button @click="nextStep" :class="$style.button_add">Đăng bài</button>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalRecruitment.module.scss';
</style>

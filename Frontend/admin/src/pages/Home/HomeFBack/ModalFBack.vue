<script setup lang="ts">
import { ref } from 'vue';
import Doctor from '@/assets/imgs/Home/Doctor.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import Swal from 'sweetalert2';
import IconCam from '@/assets/icons/Camera.png';
import {
  faXmark,
  faStar,
  faChevronLeft,
  faChevronRight,
  faPlus,
  faTrash,
  faExclamationCircle
} from '@fortawesome/free-solid-svg-icons';

const feedbacks = ref([
  {
    src: Doctor,
    speech:
      // eslint-disable-next-line max-len
      'Cảm ơn TL Dental Group, đội ngũ nhân sự tư vấn rất nhiệt tình, nhờ các bạn mà mình có thêm cái nhìn sâu hơn về khả năng ứng dụng của các vật tư sử dụng trong chỉnh nha.',
    name: 'Bác sĩ Anh Khôi',
    pos: 'Bác sĩ Nha Khoa Bệnh viện A',
    rate: 4
  },
  {
    src: Doctor,
    speech:
      // eslint-disable-next-line max-len
      'Cảm ơn TL Dental Group, đội ngũ nhân sự tư vấn rất nhiệt tình, nhờ các bạn mà mình có thêm cái nhìn sâu hơn về khả năng ứng dụng của các vật tư sử dụng trong chỉnh nha.',
    name: 'Bác sĩ Huỳnh Vinh',
    pos: 'Bác sĩ Nha Khoa Bệnh viện B',
    rate: 5
  },
  {
    src: Doctor,
    speech:
      // eslint-disable-next-line max-len
      'Cảm ơn TL Dental Group, đội ngũ nhân sự tư vấn rất nhiệt tình, nhờ các bạn mà mình có thêm cái nhìn sâu hơn về khả năng ứng dụng của các vật tư sử dụng trong chỉnh nha.',
    name: 'Bác sĩ Hữu Chính',
    pos: 'Bác sĩ Nha Khoa Bệnh viện C',
    rate: 3
  }
]);

const selectedItem = ref(0);
const currentItem = ref(-1);
const rating = ref(feedbacks.value[selectedItem.value].rate);
const hover = ref(0);
const name = ref(feedbacks.value[selectedItem.value].name);
const pos = ref(feedbacks.value[selectedItem.value].pos);
const speech = ref(feedbacks.value[selectedItem.value].speech);
const selectedImage = ref('');
const emit = defineEmits(['inFocus', 'close']);

//Adding new one
const addStatus = ref(false);

//Rating
const setRating = (index: number) => {
  rating.value = index;
};

const setHover = (index: number) => {
  hover.value = index;
};

//Information
const changeInfor = () => {
  name.value = feedbacks.value[selectedItem.value].name;
  pos.value = feedbacks.value[selectedItem.value].pos;
  speech.value = feedbacks.value[selectedItem.value].speech;
  setRating(feedbacks.value[selectedItem.value].rate);
  setHover(rating.value);
};

//Reset inputs
const resetInfor = () => {
  name.value = '';
  pos.value = '';
  speech.value = '';
  setRating(0);
  setHover(0);
};

//Cancel adding
const cancelAdding = () => {
  addStatus.value = false;
  feedbacks.value.pop();
  selectedItem.value = currentItem.value;
};

const scrollLeft = () => {
  if (selectedItem.value === 0) {
    selectedItem.value = feedbacks.value.length - 1;
  } else {
    selectedItem.value--;
  }
  changeInfor();
};

const scrollRight = () => {
  if (selectedItem.value === feedbacks.value.length - 1) {
    selectedItem.value = 0;
  } else {
    selectedItem.value++;
  }
  changeInfor();
};

//Choose image
const handleFileInputChange = (event: Event) => {
  const inputElement = event.target as HTMLInputElement;
  const file = inputElement.files?.[0];

  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      selectedImage.value = reader.result as string;
      feedbacks.value[selectedItem.value].src = selectedImage.value;
    };
    reader.readAsDataURL(file);
  }
};

//Validate variables
const isName = ref(false);
const isPos = ref(false);
const isCont = ref(false);

const nameInput = ref<HTMLInputElement | null>(null);
const posInput = ref<HTMLInputElement | null>(null);
const speechInput = ref<HTMLInputElement | null>(null);
const fileInput = ref<HTMLInputElement | null>(null);

//Open file image
const openFileInput = () => {
  fileInput.value?.click();
};

const deleteItem = () => {
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
      feedbacks.value.splice(selectedItem.value, 1);
      if (selectedItem.value === feedbacks.value.length) {
        selectedItem.value = 0;
      }

      changeInfor();

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

const validateInform = () => {
  if (nameInput.value && name.value.length <= 5) {
    nameInput.value.focus();
    isName.value = true;
    return;
  } else {
    isName.value = false;
  }

  if (posInput.value && pos.value.length <= 3) {
    posInput.value.focus();
    isPos.value = true;
    return;
  } else {
    isPos.value = false;
  }

  if (speechInput.value && speech.value.length <= 15) {
    speechInput.value.focus();
    isCont.value = true;
    return;
  } else {
    isCont.value = false;
  }

  feedbacks.value[selectedItem.value].name = name.value;
  feedbacks.value[selectedItem.value].pos = pos.value;
  feedbacks.value[selectedItem.value].speech = speech.value;
  feedbacks.value[selectedItem.value].rate = rating.value;
};

const updateForm = () => {
  validateInform();

  if (!isName.value && !isCont.value && !isPos.value) {
    if (!addStatus.value) {
      Swal.fire({
        title: 'Cập nhật thành công',
        icon: 'success',
        confirmButtonText: 'Hoàn tất',
        width: '30rem'
      });
      emit('close');
      setTimeout(function () {
        Swal.close();
      }, 1200);
    } else {
      Swal.fire({
        title: 'Thêm thành công',
        icon: 'success',
        confirmButtonText: 'Hoàn tất',
        width: '30rem'
      });
      addStatus.value = false;
      setTimeout(function () {
        Swal.close();
      }, 1200);
    }
  }
};

const addItem = () => {
  Swal.fire({
    title: 'Lưu dữ liệu hiện tại và thêm mới?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Thêm',
    cancelButtonText: 'Hủy'
  }).then((result) => {
    if (result.isConfirmed) {
      currentItem.value = selectedItem.value;
      validateInform();
      if (!isName.value && !isCont.value && !isPos.value) {
        addStatus.value = true;
        const newItem = {
          src: Doctor,
          speech:
            // eslint-disable-next-line max-len
            'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s',
          name: 'Bác sĩ ABC',
          pos: 'Bác sĩ Nha khoa bệnh viện A',
          rate: 0
        };
        feedbacks.value.push(newItem);
        selectedItem.value = feedbacks.value.length - 1;
        resetInfor();
      }
    }
  });
};
</script>

<template>
  <div :class="$style.feedback__overlay">
    <div :class="$style.feedback__modal">
      <div :class="$style.feedback__modal__heading">
        <template v-if="addStatus === false"> CHỈNH SỬA ĐÁNH GIÁ </template>
        <template v-else> THÊM ĐÁNH GIÁ </template>
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['feedback__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.feedback__modal__body">
        <div :class="$style['feedback__modal__body-left']">
          <h4 style="margin-top: 30px">TẤT CẢ ĐÁNH GIÁ</h4>
          <div :class="$style['modal__feedback-ctn']">
            <div :class="$style['modal__feedback-item']">
              <div :class="$style['modal__feedback-img']">
                <img :src="feedbacks[selectedItem].src" alt="doctor" />
                <div :class="$style.modal_imagebtn">
                  <button style="border: none" @click="openFileInput">
                    <input
                      type="file"
                      ref="fileInput"
                      style="display: none"
                      @change="handleFileInputChange"
                    />
                    <img :src="IconCam" alt="icon" />
                  </button>
                </div>
              </div>
              <div :class="$style['modal__feedback-speech']">
                {{ feedbacks[selectedItem].speech }}
              </div>
              <div :class="$style['modal__feedback-infor']">
                <strong>{{ feedbacks[selectedItem].name }}</strong>
                <span>{{ feedbacks[selectedItem].pos }}</span>
                <div :class="$style['modal__feedback-rate']">
                  <font-awesome-icon
                    v-for="i in 5"
                    :key="i"
                    :icon="faStar"
                    :class="[
                      $style['modal__feedback-star'],
                      i <= feedbacks[selectedItem].rate ? $style['star-active'] : ''
                    ]"
                  />
                </div>
              </div>
              <div :class="$style['modal__feedback-icon']"></div>
            </div>
            <button v-if="!addStatus" :class="$style['modal__feedback-left']" @click="scrollLeft">
              <font-awesome-icon :icon="faChevronLeft" :class="$style['modal__feedback-ic']" />
            </button>
            <button v-if="!addStatus" :class="$style['modal__feedback-right']" @click="scrollRight">
              <font-awesome-icon :icon="faChevronRight" :class="$style['modal__feedback-ic']" />
            </button>
          </div>
          <div :class="$style['modal__feedback-btns']">
            <button v-if="!addStatus" @click="deleteItem">
              <font-awesome-icon :icon="faTrash" :class="$style['feedback__add-ic']" />
            </button>
            <button v-if="!addStatus" @click="addItem">
              <font-awesome-icon :icon="faPlus" :class="$style['feedback__add-ic']" />
            </button>
          </div>
        </div>
        <div :class="$style['feedback__modal__body-right']">
          <div :class="$style['feedback__right-input']" style="margin-top: 30px">
            <h4>Thông tin người đánh giá</h4>
            <input type="text" placeholder="Bác sĩ Huỳnh Vinh" v-model="name" ref="nameInput" />
            <div v-if="isName" :class="$style['error__input']">
              <font-awesome-icon :icon="faExclamationCircle" :class="$style['warning-ic']" />
              Tên không được quá ngắn
            </div>
          </div>
          <div :class="$style['feedback__right-input']">
            <h4>Nghề nghiệp/ Chức vụ</h4>
            <input type="text" placeholder="Bác sĩ Nha Khoa A" v-model="pos" ref="posInput" />
            <div v-if="isPos" :class="$style['error__input']">
              <font-awesome-icon :icon="faExclamationCircle" :class="$style['warning-ic']" />
              Chức vụ không hợp lệ
            </div>
          </div>
          <div :class="$style['feedback__right-input']">
            <h4>Nội dung đánh giá</h4>
            <textarea
              placeholder="Cảm ơn TL Dental Group, đội ngũ nhân sự uy tín..."
              v-model="speech"
              ref="speechInput"
            />
            <div v-if="isCont" :class="$style['error__input']">
              <font-awesome-icon :icon="faExclamationCircle" :class="$style['warning-ic']" />
              Nội dung quá ngắn
            </div>
          </div>
          <div :class="$style['feedback__right-input']">
            <h4>Đánh giá sao</h4>
            <div :class="$style['star-rating']">
              <button
                v-for="index in 5"
                :key="index"
                :style="
                  index <= (hover || rating) ? 'color: rgb(255, 205, 29);' : 'color: #E3E3E4;'
                "
                @click="setRating(index)"
                @mouseenter="setHover(index)"
                @mouseleave="setHover(rating)"
                type="button"
              >
                <font-awesome-icon :class="$style['star']" :icon="faStar" />
              </button>
            </div>
          </div>
          <div :class="$style['modal__buttons']">
            <button v-if="addStatus === false" @click="$emit('close')">Hủy</button>
            <button v-if="addStatus === true" @click="cancelAdding">Hủy</button>
            <button v-if="addStatus === false" @click="updateForm">Cập nhật</button>
            <button v-if="addStatus === true" @click="updateForm">Thêm</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalFBack.modue.scss';
</style>
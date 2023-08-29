<script setup lang="ts">
import { ref, watch, type PropType } from 'vue';
import Doctor from '@/assets/imgs/Home/DoctorDF.jpg';
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
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import CropImage from '@/components/CropImage/CropImage.vue';
import Loading from '@/components/LoadingComponent/LoadingComponent.vue';

interface ItemRS {
  id: string;
  image: string;
  content: string;
  fullname: string;
  position: string;
  rating: number;
}

//Prop
const content = defineProps({
  listItem: {
    type: Array as PropType<ItemRS[]>,
    required: false
  }
});

//Properties
const isLoading = ref(false);
const feedbacks = ref(content.listItem?.length ? content.listItem : []);
const selectedItem = ref(0);
const currentItem = ref(-1);
const rating = ref(feedbacks.value[selectedItem.value].rating);
const hover = ref(0);
const fullname = ref(feedbacks.value[selectedItem.value].fullname);
const position = ref(feedbacks.value[selectedItem.value].position);
const speech = ref(feedbacks.value[selectedItem.value].content);
const urlFile = ref();
const isCrop = ref(false);
const isOpenInput = ref(false);
const fileData = ref();
const finalImage = ref();
const addStatus = ref(false); //Adding new one

//Emit to parent
const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (
    // eslint-disable-next-line no-unused-vars
    e: 'update-content',
    // eslint-disable-next-line no-unused-vars
    data: { listrs: ItemRS[] }
  ): void;
}>();

//Function 4.2 to 4.0
const roundNumber = (number: number, decimalPlaces: number) => {
  const factor = Math.pow(10, decimalPlaces);
  return Math.round(number * factor) / factor;
};

//Rating
const setRating = (index: number) => {
  rating.value = index;
};

const setHover = (index: number) => {
  hover.value = index;
};

//Information
const changeInfor = () => {
  fullname.value = feedbacks.value[selectedItem.value].fullname;
  position.value = feedbacks.value[selectedItem.value].position;
  speech.value = feedbacks.value[selectedItem.value].content;
  setRating(feedbacks.value[selectedItem.value].rating);
  setHover(rating.value);
};

//Reset inputs
const resetInfor = () => {
  fullname.value = '';
  position.value = '';
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

//Validate variables
const isName = ref(false);
const isPos = ref(false);
const isCont = ref(false);

//Ref to focus when wrong
const nameInput = ref<HTMLInputElement | null>(null);
const posInput = ref<HTMLInputElement | null>(null);
const speechInput = ref<HTMLInputElement | null>(null);

//Open file image
const openFileInput = () => {
  isOpenInput.value = !isOpenInput.value;
};

const base64ToBlob = (base64Data: string) => {
  const byteString = atob(base64Data.split(',')[1]);
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }
  return new Blob([ab], { type: 'image/png' });
};

//Function crop image
const handleCroppedImage = (result: string) => {
  if (result) {
    urlFile.value = result;
    feedbacks.value[selectedItem.value].image = urlFile.value;
    fileData.value = base64ToBlob(result);
    finalImage.value = new File([fileData.value], 'image.png', { type: 'image/png' });
  }
};

//Delete Item
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
      isLoading.value = true;
      const deps = ref([]);
      const object = {};
      const { response } = useAxios<DataResponse>(
        'delete',
        `/home/reviews/${feedbacks.value[selectedItem.value].id}`,
        object,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        },
        deps.value
      );

      watch(response, () => {
        isLoading.value = false;
        if (response.value?.status === 'ok') {
          emits('update-content', {
            listrs: feedbacks.value
          });
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
    }
  });
};

//Validate before handling
const validateInform = () => {
  if (nameInput.value && fullname.value.length <= 5) {
    nameInput.value.focus();
    isName.value = true;
    return;
  } else {
    isName.value = false;
  }

  if (posInput.value && position.value.length <= 3) {
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

  if (addStatus.value && finalImage.value === undefined) {
    Swal.fire({
      title: 'Bạn chưa chọn ảnh',
      icon: 'error',
      showConfirmButton: false,
      width: '30rem'
    });
    setTimeout(function () {
      Swal.close();
    }, 1200);
    return;
  }

  feedbacks.value[selectedItem.value].fullname = fullname.value;
  feedbacks.value[selectedItem.value].position = position.value;
  feedbacks.value[selectedItem.value].content = speech.value;
  feedbacks.value[selectedItem.value].rating = rating.value;
};

//Adding and updating
const updateForm = () => {
  validateInform();

  if (!isName.value && !isCont.value && !isPos.value) {
    isLoading.value = true;
    if (!addStatus.value) {
      const deps = ref([]);
      const object = {
        id: feedbacks.value[selectedItem.value].id,
        fullname: feedbacks.value[selectedItem.value].fullname,
        position: feedbacks.value[selectedItem.value].position,
        rating: feedbacks.value[selectedItem.value].rating,
        content: feedbacks.value[selectedItem.value].content,
        image: feedbacks.value[selectedItem.value].image
      };
      const formData = new FormData();
      formData.append('data', JSON.stringify(object));
      formData.append('image', finalImage.value as Blob);
      const { response } = useAxios<DataResponse>(
        'patch',
        '/home/reviews',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        },
        deps.value
      );

      watch(response, () => {
        if (response.value?.status === 'ok') {
          isLoading.value = false;
          emits('update-content', {
            listrs: feedbacks.value
          });
          Swal.fire({
            title: 'Cập nhật thành công',
            icon: 'success',
            confirmButtonText: 'Hoàn tất',
            width: '30rem'
          });
          setTimeout(function () {
            Swal.close();
          }, 1200);
        }
      });
    } else {
      const deps = ref([]);
      const object = {
        fullname: feedbacks.value[selectedItem.value].fullname,
        position: feedbacks.value[selectedItem.value].position,
        rating: feedbacks.value[selectedItem.value].rating,
        content: feedbacks.value[selectedItem.value].content
      };
      const formData = new FormData();
      formData.append('data', JSON.stringify(object));
      formData.append('image', finalImage.value as Blob);
      const { response } = useAxios<DataResponse>(
        'post',
        '/home/reviews',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        },
        deps.value
      );
      watch(response, () => {
        if (response.value?.status === 'ok') {
          isLoading.value = false;
          feedbacks.value[selectedItem.value].id = response.value.data?.id;
          emits('update-content', {
            listrs: feedbacks.value
          });
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
      });
    }
  }
};

//Create new temp item
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
        const newItem: ItemRS = {
          id: '',
          image: Doctor,
          content:
            // eslint-disable-next-line max-len
            'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s',
          fullname: 'Bác sĩ ABC',
          position: 'Bác sĩ Nha khoa bệnh viện A',
          rating: 0
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
                <img :src="feedbacks[selectedItem].image" alt="doctor" />
                <div :class="$style.modal_imagebtn">
                  <button
                    style="border: none; background-color: transparent"
                    @click="openFileInput"
                  >
                    <img :src="IconCam" alt="icon" />
                  </button>
                </div>
              </div>
              <div :class="$style['modal__feedback-speech']">
                {{ feedbacks[selectedItem].content }}
              </div>
              <div :class="$style['modal__feedback-infor']">
                <strong>{{ feedbacks[selectedItem].fullname }}</strong>
                <span>{{ feedbacks[selectedItem].position }}</span>
                <div :class="$style['modal__feedback-rate']">
                  <font-awesome-icon
                    v-for="i in 5"
                    :key="i"
                    :icon="faStar"
                    :class="[
                      $style['modal__feedback-star'],
                      i <= roundNumber(feedbacks[selectedItem].rating, 0)
                        ? $style['star-active']
                        : ''
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
            <input type="text" placeholder="Bác sĩ Huỳnh Vinh" v-model="fullname" ref="nameInput" />
            <div v-if="isName" :class="$style['error__input']">
              <font-awesome-icon :icon="faExclamationCircle" :class="$style['warning-ic']" />
              Tên không được quá ngắn
            </div>
          </div>
          <div :class="$style['feedback__right-input']">
            <h4>Nghề nghiệp/ Chức vụ</h4>
            <input type="text" placeholder="Bác sĩ Nha Khoa A" v-model="position" ref="posInput" />
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
                  index <= (roundNumber(hover, 0) || roundNumber(rating, 0))
                    ? 'color: rgb(255, 205, 29);'
                    : 'color: #E3E3E4;'
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
      <div v-show="isLoading" :class="$style.loading__overlay">
        <Loading />
      </div>
    </div>
  </div>
  <crop-image
    :heightCrop="250"
    :widthCrop="270"
    :heightWrap="250"
    :widthWrap="270"
    :check="isOpenInput"
    v-show="isCrop"
    @close="isCrop = false"
    @open="isCrop = true"
    @crop="handleCroppedImage"
  />
</template>

<style module scoped lang="scss">
@import './ModalFBack.modue.scss';
</style>

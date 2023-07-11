<script setup lang="ts">
import Motto from '@/assets/imgs/About/Motto.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faPencil,
  faCheck,
  faClose,
  faChevronRight,
  faChevronLeft,
  faPlus
} from '@fortawesome/free-solid-svg-icons';
import { ref } from 'vue';
import ModalAddMotto from './component/ModalAddMotto.vue';
import ModalUpdateMotto from './component/ModalUpdateMotto.vue';
import Swal from 'sweetalert2';

const mottoItems = [
  {
    title: 'Sự đổi mới sáng tạo 1',
    content:
      'Apple luôn tập trung vào việc đổi mới và tạo ra các sản phẩm và dịch vụ đột phá. Công ty luôn khuyến khích nhân viên tìm kiếm cách tiếp cận vấn đề từ góc độ khác biệt và đưa ra giải pháp sáng tạo.',
    img: Motto
  },
  {
    title: 'Sự đổi mới sáng tạo 2',
    content:
      'Apple luôn tập trung vào việc đổi mới và tạo ra các sản phẩm và dịch vụ đột phá. Công ty luôn khuyến khích nhân viên tìm kiếm cách tiếp cận vấn đề từ góc độ khác biệt và đưa ra giải pháp sáng tạo.',
    img: Motto
  },
  {
    title: 'Sự đổi mới sáng tạo 3',
    content:
      'Apple luôn tập trung vào việc đổi mới và tạo ra các sản phẩm và dịch vụ đột phá. Công ty luôn khuyến khích nhân viên tìm kiếm cách tiếp cận vấn đề từ góc độ khác biệt và đưa ra giải pháp sáng tạo.',
    img: Motto
  },
  {
    title: 'Sự đổi mới sáng tạo 4',
    content:
      'Apple luôn tập trung vào việc đổi mới và tạo ra các sản phẩm và dịch vụ đột phá. Công ty luôn khuyến khích nhân viên tìm kiếm cách tiếp cận vấn đề từ góc độ khác biệt và đưa ra giải pháp sáng tạo.',
    img: Motto
  },
  {
    title: 'Sự đổi mới sáng tạo 5',
    content:
      'Apple luôn tập trung vào việc đổi mới và tạo ra các sản phẩm và dịch vụ đột phá. Công ty luôn khuyến khích nhân viên tìm kiếm cách tiếp cận vấn đề từ góc độ khác biệt và đưa ra giải pháp sáng tạo.',
    img: Motto
  },
  {
    title: 'Sự đổi mới sáng tạo 6',
    content:
      'Apple luôn tập trung vào việc đổi mới và tạo ra các sản phẩm và dịch vụ đột phá. Công ty luôn khuyến khích nhân viên tìm kiếm cách tiếp cận vấn đề từ góc độ khác biệt và đưa ra giải pháp sáng tạo.',
    img: Motto
  },
  {
    title: 'Sự đổi mới sáng tạo 7',
    content:
      'Apple luôn tập trung vào việc đổi mới và tạo ra các sản phẩm và dịch vụ đột phá. Công ty luôn khuyến khích nhân viên tìm kiếm cách tiếp cận vấn đề từ góc độ khác biệt và đưa ra giải pháp sáng tạo.',
    img: Motto
  },
  {
    title: 'Sự đổi mới sáng tạo 8',
    content:
      'Apple luôn tập trung vào việc đổi mới và tạo ra các sản phẩm và dịch vụ đột phá. Công ty luôn khuyến khích nhân viên tìm kiếm cách tiếp cận vấn đề từ góc độ khác biệt và đưa ra giải pháp sáng tạo.',
    img: Motto
  }
];

const move = ref(0);
const moveEdit = ref(0);
const items = ref(mottoItems);
const indexItems = ref(0);
const isDisableLeft = ref(false);
const isDisableLeftEdit = ref(true);
const isDisableRight = ref(false);
const isDisableRightEdit = ref(false);
const isEdit = ref(false);
const isOpenAdd = ref(false);
const isOpenUpdate = ref(false);

const handleClickLeft = () => {
  isDisableRight.value = false;

  const widthItem = document.getElementById('1');

  if (widthItem) {
    if (move.value === 0) (move.value = widthItem.offsetWidth + 150), (isDisableLeft.value = true);
    else {
      move.value += widthItem.offsetWidth + 150;
    }
  }
};

const handleClickRight = () => {
  isDisableLeft.value = false;

  const widthItem = document.getElementById('1');

  if (widthItem) {
    if (move.value === (3 - items.value.length) * (widthItem.offsetWidth + 150))
      (move.value -= widthItem.offsetWidth + 150), (isDisableRight.value = true);
    else {
      move.value -= widthItem.offsetWidth + 150;
    }
  }
};

const handleClickLeftEdit = () => {
  isDisableRightEdit.value = false;

  indexItems.value -= 1;

  if (indexItems.value <= 0)
    ((indexItems.value = 0), (moveEdit.value += 383)), (isDisableLeftEdit.value = true);
  else moveEdit.value += 383;
};

const handleClickRightEdit = () => {
  isDisableLeftEdit.value = false;

  indexItems.value += 1;

  console.log(indexItems.value);

  if (indexItems.value >= items.value.length - 1) {
    moveEdit.value -= 383;
    indexItems.value = items.value.length - 1;
    isDisableRightEdit.value = true;
  } else moveEdit.value -= 383;
};

const handleRemoveItem = () => {
  items.value.splice(indexItems.value, 1);
  indexItems.value--;

  if (indexItems.value < 0) {
    indexItems.value = 0;
  } else if (indexItems.value === items.value.length - 1) {
    moveEdit.value += 383;
  } else {
    indexItems.value += 1;
  }

  // Hiển thị thông báo xóa thành công
  Swal.fire({
    title: 'Xóa thành công',
    icon: 'success',
    showConfirmButton: false // Ẩn nút xác nhận
  });

  Swal.fire({
    title: 'Xóa thành công',
    icon: 'success',
    timer: 2000,
    width: '30rem'
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.close();
    }
  });

  return items.value;
};

const handleRemove = () => {
  Swal.fire({
    title: 'Bạn có muốn xóa đối tượng này không?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Xóa',
    cancelButtonText: 'Hủy',
    width: '30rem'
  }).then((result) => {
    if (result.isConfirmed) {
      handleRemoveItem();
      // Swal.close();
    }
  });
};
</script>

<template>
  <div :class="$style.about__motto">
    <p :class="$style['about__motto-title']">PHƯƠNG CHÂM - GIÁ TRỊ</p>

    <div :class="$style['about__motto-position']" v-if="!isEdit">
      <div
        :class="$style['about__motto-slider']"
        id="motto-list"
        :style="{ transform: 'translateX' + '(' + move + 'px' + ')' }"
      >
        <div
          :class="$style['about__motto-slider-item']"
          v-for="(item, index) in items"
          :key="index"
          :id="'' + index"
        >
          <img :src="item.img" alt="" />

          <p :class="$style['about__motto-slider-item-title']">{{ item.title }}</p>

          <p :class="$style['about__motto-slider-item-content']">
            {{ item.content }}
          </p>
        </div>
      </div>

      <div :class="$style['about__motto-curtain']">
        <div :class="$style['about__motto-curtain-left']"></div>

        <div :class="$style['about__motto-curtain-button']">
          <button
            :class="$style['about__motto-curtain-button-left']"
            @click="handleClickLeft"
            :disabled="isDisableLeft"
          >
            <font-awesome-icon
              :icon="faChevronLeft"
              :class="$style['about__motto-curtain-button-ic']"
            />
          </button>
          <button
            :class="$style['about__motto-curtain-button-right']"
            @click="handleClickRight"
            :disabled="isDisableRight"
          >
            <font-awesome-icon
              :icon="faChevronRight"
              :class="$style['about__motto-curtain-button-ic']"
            />
          </button>
        </div>

        <div :class="$style['about__motto-curtain-right']"></div>
      </div>
    </div>

    <div :class="$style['about__motto-wrapper']" v-if="isEdit">
      <div :class="$style['about__motto-left']">
        <div>
          <div :class="$style['about__motto-left-button']">
            <button
              :class="$style['about__motto-left-button-left']"
              @click="handleClickLeftEdit"
              :disabled="isDisableLeftEdit"
            >
              <font-awesome-icon
                :icon="faChevronLeft"
                :class="$style['about__motto-left-button-left-ic']"
              />
            </button>
          </div>

          <!-- <div :class="$style['about__motto-slider1']">
            <div :class="$style['about__motto-slider1-item']">
              <img :src="item.img" alt="" />

              <p :class="$style['about__motto-slider1-item-title']">{{ item.title }}</p>

              <p :class="$style['about__motto-slider1-item-content']">
                {{ item.content }}
              </p>
            </div>
          </div> -->
          <div :class="$style['about__motto-slider1-wrapper']">
            <div
              :class="$style['about__motto-slider1']"
              id="motto-list"
              :style="{ transform: 'translateX' + '(' + moveEdit + 'px' + ')' }"
            >
              <div
                :class="$style['about__motto-slider1-item']"
                v-for="(item, index) in items"
                :key="index"
                :id="'' + index"
              >
                <img :src="item.img" alt="" />

                <p :class="$style['about__motto-slider1-item-title']">{{ item.title }}</p>

                <p :class="$style['about__motto-slider1-item-content']">
                  {{ item.content }}
                </p>
              </div>
            </div>
          </div>

          <div :class="$style['about__motto-left-button']">
            <button
              :class="$style['about__motto-left-button-right']"
              @click="handleClickRightEdit"
              :disabled="isDisableRightEdit"
            >
              <font-awesome-icon
                :icon="faChevronRight"
                :class="$style['about__motto-left-button-right-ic']"
              />
            </button>
          </div>
        </div>

        <div>
          <button :class="$style['about__motto-button-update']" @click="isOpenUpdate = true">
            <font-awesome-icon :icon="faPencil" :class="$style['about__motto-button-update-ic']" />
            <span>Chỉnh sửa</span>
          </button>

          <button :class="$style['about__motto-button-close']" @click="handleRemove">
            <font-awesome-icon :icon="faClose" :class="$style['about__motto-button-close-ic']" />
            <span>Xóa bỏ</span>
          </button>
        </div>
      </div>

      <div :class="$style['about__motto-right']">
        <div :class="$style['about__motto-right-solid']">
          <div :class="$style['about__motto-right-solid-button']" @click="isOpenAdd = true">
            <font-awesome-icon
              :icon="faPlus"
              :class="$style['about__motto-right-solid-button-ic']"
            />
            <span>Thêm phương châm</span>
          </div>
        </div>
      </div>
    </div>

    <button :class="$style['about__motto-button-first']" v-if="!isEdit" @click="isEdit = true">
      <font-awesome-icon :icon="faPencil" :class="$style['about__motto-button-ic']" />
      <span>Chỉnh sửa</span>
    </button>

    <button :class="$style['about__motto-button-left']" v-if="isEdit" @click="isEdit = false">
      <font-awesome-icon :icon="faClose" :class="$style['about__motto-button-ic']" />
      <span>Hủy bỏ</span>
    </button>

    <button :class="$style['about__motto-button-right']" v-if="isEdit" @click="isEdit = false">
      <font-awesome-icon :icon="faCheck" :class="$style['about__motto-button-ic']" />
      <span>Xác nhận</span>
    </button>
  </div>

  <modal-add-motto v-if="isOpenAdd" @close="isOpenAdd = false" />
  <modal-update-motto
    v-if="isOpenUpdate"
    @close="isOpenUpdate = false"
    :title="items[indexItems].title"
    :content="items[indexItems].content"
    :image="items[indexItems].img"
  />
</template>

<style module scoped lang="scss">
@import './AboutMotto.module.scss';
</style>

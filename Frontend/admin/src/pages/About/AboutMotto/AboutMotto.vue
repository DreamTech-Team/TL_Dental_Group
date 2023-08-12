<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faPencil,
  faCheck,
  faClose,
  faChevronRight,
  faChevronLeft,
  faPlus
} from '@fortawesome/free-solid-svg-icons';
import ModalAddMotto from './component/ModalAddMotto.vue';
import ModalUpdateMotto from './component/ModalUpdateMotto.vue';
import Swal from 'sweetalert2';
import styles from './AboutMotto.module.scss';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import LoadingComponent from '@/components/LoadingComponent/LoadingComponent.vue';

interface AboutMotto {
  id: string;
  title: string;
  content: string;
  image: string;
  type: string;
}

const variableChange = ref([]);
const mottoItems = ref<AboutMotto[]>([]);
const isOneItem = ref(false);
const move = ref(0); // biến lưu chiều dài của một item khi bấm nút qua lại
const moveEdit = ref(0); // biến lưu chiều dài của một item khi bấm nút qua lại khi trong trạng thái chỉnh sửa
const items = ref(mottoItems); // biến để lấy dữ liệu để render
const indexItems = ref(0); // biến lưu vị trí hiện tại
// biến kiểm tra xem đã hết phần tử bên trái khi bấm nút qua lại chưa
const isDisableLeft = ref(false);
const isDisableLeftEdit = ref(true); // biến kiểm tra xem đã hết phần tử bên trái khi bấm nút qua lại chưa trong trang thái Edit
const isDisableRight = ref(false); // biến kiểm tra xem đã hết phần tử bên phải khi bấm nút qua lại chưa
const isDisableRightEdit = ref(false); // biến kiểm tra xem đã hết phần tử bên phải khi bấm nút qua lại chưa trong trạng thái Edit
const isEdit = ref(false);
const isOpenAdd = ref(false);
const isOpenUpdate = ref(false);
const widthItemEdit = ref(0);
const isLoadingItem = ref(false);

// Hàm lấy width của một thẻ trong trạng thái Edit
const handleGetWidthItem = () => {
  const widthItem = document.getElementById('0');

  if (widthItem) widthItemEdit.value = widthItem.offsetWidth;
};

// Gọi hàm useAxios để lấy response, error, và isLoading
const { response } = useAxios<DataResponse>(
  'get',
  '/introduce/section1',
  {},
  {},
  variableChange.value
);

// Truy xuất giá trị response.value và gán vào responseData
watch(response, () => {
  mottoItems.value = response?.value?.data;

  // Xử lí trường hợp có 1 hoặc 2 item
  if (mottoItems.value.length === 1) {
    isOneItem.value = true;
    isDisableRight.value = true;
    isDisableLeft.value = true;
    isDisableLeftEdit.value = true;
    isDisableRightEdit.value = true;
  } else if (mottoItems.value.length === 2) {
    isDisableRight.value = true;
  }
});

// Hàm bấm nút sang trái
const handleClickLeft = () => {
  isDisableRight.value = false;

  const widthItem = document.getElementById('0');

  if (widthItem) {
    if (move.value === 0) (move.value = widthItem.offsetWidth + 150), (isDisableLeft.value = true);
    else {
      move.value += widthItem.offsetWidth + 150;
    }
  }
};

// Hàm bấm nút sang phải
const handleClickRight = () => {
  isDisableLeft.value = false;

  const widthItem = document.getElementById('0');

  if (widthItem) {
    if (move.value === (3 - items.value.length) * (widthItem.offsetWidth + 150))
      (move.value -= widthItem.offsetWidth + 150), (isDisableRight.value = true);
    else {
      move.value -= widthItem.offsetWidth + 150;
    }
  }
};

// Hàm bấm nút sang trái trong trạng thái Edit
const handleClickLeftEdit = () => {
  isDisableRightEdit.value = false;

  indexItems.value -= 1;

  if (indexItems.value <= 0)
    ((indexItems.value = 0), (moveEdit.value += widthItemEdit.value)),
      (isDisableLeftEdit.value = true);
  else moveEdit.value += widthItemEdit.value;
};

// Hàm bấm nút sang phải trong trạng thái Edit
const handleClickRightEdit = () => {
  isDisableLeftEdit.value = false;

  indexItems.value += 1;

  if (indexItems.value >= items.value.length - 1) {
    moveEdit.value -= widthItemEdit.value;
    indexItems.value = items.value.length - 1;
    isDisableRightEdit.value = true;
  } else moveEdit.value -= widthItemEdit.value;
};

// Hàm thao tác xóa đi một Item phương châm
const handleRemoveItem = () => {
  const deps = ref([]);

  const { response, isLoading } = useAxios<DataResponse>(
    'delete',
    '/introduce/section1/' + items.value[indexItems.value].id,
    {},
    {},
    deps.value
  );
  isLoadingItem.value = isLoading.value;
  items.value.splice(indexItems.value, 1);

  indexItems.value = 0;
  moveEdit.value = 0;
  move.value = 0;
  isDisableLeftEdit.value = true;
  isDisableRightEdit.value = false;

  watch(response, () => {
    isLoadingItem.value = isLoading.value;

    if (response.value?.status === 'ok') {
      // Hiển thị thông báo xóa thành công
      Swal.fire({
        title: 'Xóa thành công',
        icon: 'success',
        timer: 2000,
        width: '50rem',
        padding: '0 2rem 2rem 2rem',
        customClass: {
          confirmButton: styles['confirm-button'],
          cancelButton: styles['cancel-button'],
          title: styles['title']
        }
      }).then((result) => {
        if (result.isConfirmed) {
          // Xử lí trường hợp có 1 hoặc 2 item
          if (items.value.length === 1) {
            isOneItem.value = true;
            isDisableRight.value = true;
            isDisableLeft.value = true;
            isDisableLeftEdit.value = true;
            isDisableRightEdit.value = true;
          } else if (items.value.length === 2) {
            if (indexItems.value === 1) {
              isDisableRight.value = true;
              // move.value += widthItemEdit.value + 150;
            } else if (indexItems.value === 0) {
              isDisableRight.value = true;
              // move.value -= widthItemEdit.value + 150;
            }
          } else {
            isDisableLeft.value = false;
            isDisableRight.value = false;
          }
          Swal.close();
        }
      });
    }
  });

  return items.value;
};

// Hàm thực thi xóa
const handleRemove = () => {
  Swal.fire({
    title: 'Bạn có muốn xóa đối tượng này không?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Xóa',
    cancelButtonText: 'Hủy',
    width: '50rem',
    padding: '0 2rem 2rem 2rem',
    customClass: {
      confirmButton: styles['confirm-button'],
      cancelButton: styles['cancel-button'],
      title: styles['title']
    }
  }).then((result) => {
    if (result.isConfirmed) {
      handleRemoveItem();
    }
  });
};

const handleChangeUpdate = (dataUpdated: AboutMotto, isLoading: boolean) => {
  isLoadingItem.value = isLoading;
  isOpenUpdate.value = false;

  if (!isLoading) {
    Swal.fire({
      title: 'Cập nhật thành công',
      icon: 'success',
      confirmButtonText: 'Hoàn tất',
      width: '50rem',
      padding: '0 2rem 2rem 2rem',
      timer: 2000,
      customClass: {
        confirmButton: styles['confirm-button'],
        cancelButton: styles['cancel-button'],
        title: styles['title']
      }
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.close();
      }
    });
  }

  items.value.forEach((item, idx) => {
    if (item.id === dataUpdated.id) {
      items.value[idx].title = dataUpdated.title;
      items.value[idx].content = dataUpdated.content;
      items.value[idx].image = dataUpdated.image;
    }
  });
};

const handleChangeAdd = (dataAdded: AboutMotto, isLoading: boolean) => {
  isLoadingItem.value = isLoading;
  isOpenAdd.value = false;

  if (!isLoading) {
    items.value.unshift(dataAdded);

    Swal.fire({
      title: 'Thêm thành công',
      icon: 'success',
      confirmButtonText: 'Hoàn tất',
      width: '50rem',
      padding: '0 2rem 2rem 2rem',
      customClass: {
        confirmButton: styles['confirm-button'],
        cancelButton: styles['cancel-button'],
        title: styles['title']
      }
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.close();
      }
    });

    indexItems.value = 0;
    moveEdit.value = 0;
    move.value = 0;
    isDisableLeftEdit.value = true;

    // Xử lí trường hợp có 1 hoặc 2 item
    if (items.value.length === 1) {
      isOneItem.value = true;
      isDisableRight.value = true;
      isDisableLeft.value = true;
    } else if (items.value.length === 2) {
      isDisableLeft.value = false;
      isOneItem.value = false;
    } else if (items.value.length === 3) {
      isDisableLeft.value = false;
      isDisableRight.value = false;
      isOneItem.value = false;
    }
  }
};
</script>

<template>
  <div :class="$style.about__motto">
    <p :class="$style['about__motto-title']">PHƯƠNG CHÂM - GIÁ TRỊ</p>

    <div :class="$style['about__motto-position']" v-if="!isEdit">
      <div
        :class="$style['about__motto-slider']"
        id="motto-list"
        :style="{
          transform: 'translateX' + '(' + move + 'px' + ')',
          justifyContent: isOneItem ? 'center' : ''
        }"
      >
        <div
          :class="$style['about__motto-slider-item']"
          v-for="(item, index) in items"
          :key="index"
          :id="'' + index"
        >
          <img :src="item.image" alt="" />

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
        <div :class="$style['about__motto-left-wrapper']">
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

          <div
            :class="$style['about__motto-slider1-wrapper']"
            :style="{ width: widthItemEdit + 'px' }"
          >
            <div
              :class="$style['about__motto-slider1']"
              id="motto-list"
              :style="{ transform: 'translateX' + '(' + moveEdit + 'px' + ')' }"
              v-if="!isLoadingItem"
            >
              <div
                :class="$style['about__motto-slider1-item']"
                v-for="(item, index) in items"
                :key="index"
                :id="'itemedit' + index"
              >
                <img :src="item.image" alt="" />

                <p :class="$style['about__motto-slider1-item-title']">{{ item.title }}</p>

                <p :class="$style['about__motto-slider1-item-content']">
                  {{ item.content }}
                </p>
              </div>
            </div>
            <loading-component v-else />
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

    <button
      :class="$style['about__motto-button-first']"
      v-if="!isEdit"
      @click="(isEdit = true), handleGetWidthItem()"
    >
      <font-awesome-icon :icon="faPencil" :class="$style['about__motto-button-ic']" />
      <span>Chỉnh sửa</span>
    </button>

    <button :class="$style['about__motto-button-left']" v-if="isEdit" @click="isEdit = false">
      <font-awesome-icon :icon="faClose" :class="$style['about__motto-button-ic']" />
      <span>Trở về</span>
    </button>
  </div>

  <modal-add-motto v-if="isOpenAdd" @close="isOpenAdd = false" :change="handleChangeAdd" />
  <modal-update-motto
    v-if="isOpenUpdate"
    @close="isOpenUpdate = false"
    :change="handleChangeUpdate"
    :item="items[indexItems]"
  />
</template>

<style module scoped lang="scss">
@import './AboutMotto.module.scss';
</style>

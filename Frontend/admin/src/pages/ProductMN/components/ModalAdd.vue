<script setup lang="ts">
import { ref, computed } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faDownload } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';

const emit = defineEmits(['inFocus', 'close']);

const companies = ['Biocon', 'Yamaha', 'Dental', 'VinFast'];
const categories = ['Dụng cụ chỉnh nha', 'Vật liệu chỉnh nha', 'Mắc cài kim loại'];

const indexCur = ref(1);

//Part 1
const Name = ref('');
const Price = ref();
const Comp = ref('');
const Category = ref('');

const updateName = (e: Event) => {
  const target = e.target as HTMLInputElement;
  Name.value = target.value;
};
const updatePrice = (e: Event) => {
  const target = e.target as HTMLInputElement;
  Price.value = target.value;
};

//Part 2
const Policy = ref('');
const Des = ref('');

const updatePolicy = (e: Event) => {
  const target = e.target as HTMLInputElement;
  Policy.value = target.value;
};
const updateDes = (e: Event) => {
  const target = e.target as HTMLInputElement;
  Des.value = target.value;
};

//Part 3
const Avatar = ref({ url: '', name: '' });
const avtInput = ref<HTMLInputElement | null>(null);
const listHeight = computed(() => {
  if (Avatar.value.url === '' && Avatar.value.name === '') {
    return '120px';
  } else {
    return '160px';
  }
});

const openFileAvatar = () => {
  avtInput.value?.click();
};

const resetAvatar = () => {
  Avatar.value.name = '';
  Avatar.value.url = '';
};

const handleFileInputChange = (event: Event) => {
  const inputElement = event.target as HTMLInputElement;
  const file = inputElement.files?.[0];

  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      Avatar.value = {
        url: reader.result as string,
        name: file.name
      };
    };
    reader.readAsDataURL(file);
  }
};

//SUB Images
const imgsIn = ref<HTMLInputElement | null>(null);
const SubImages = ref<Array<{ url: string; name: string }>>([]);
const deleteImage = (index: number) => {
  SubImages.value.splice(index, 1);
};
const openFileImgs = () => {
  imgsIn.value?.click();
};

const handleSubIMGInputChange = (event: Event) => {
  const inputElement = event.target as HTMLInputElement;
  const { files } = inputElement;

  if (files && files.length > 0) {
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      const reader = new FileReader();
      reader.onload = () => {
        const image = {
          url: reader.result as string,
          name: file.name
        };
        SubImages.value.push(image); // Thêm ảnh vào mảng images
      };
      reader.readAsDataURL(file);
    }
  }
};

const alertDialog = (context: string, page: number) => {
  Swal.fire({
    title: context,
    icon: 'error',
    showCancelButton: false,
    width: '30rem'
  });
  setTimeout(function () {
    Swal.close();
  }, 1200);
  indexCur.value = page;
};

const submitForm = () => {
  if (Name.value.length <= 10) {
    Name.value = '';
    alertDialog('Tên không hợp lệ', 1);
    return;
  }
  if (Price.value <= 5000) {
    Price.value = '';
    alertDialog('Giá không hợp lệ', 1);
    return;
  }
  if (Comp.value === '') {
    alertDialog('Bạn chưa chọn công ty', 1);
    return;
  }
  if (Category.value === '') {
    alertDialog('Bạn chưa chọn danh mục', 1);
    return;
  }
  if (Policy.value.length <= 10) {
    alertDialog('Chính sách quá ngắn', 2);
    return;
  }
  if (Des.value.length <= 10) {
    alertDialog('Mô tả quá ngắn', 2);
    return;
  }
  if (Avatar.value.name === '' && Avatar.value.url === '') {
    alertDialog('Bạn chưa chọn Avatar', 3);
    return;
  }
  if (SubImages.value.length === 0) {
    alertDialog('Bạn chưa thêm ảnh khác', 3);
    return;
  }

  Swal.fire({
    title: 'Cập nhật thành công',
    icon: 'success',
    confirmButtonText: 'Hoàn tất',
    width: '30rem'
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.close();
      emit('close');
    }
  });
};
</script>

<template>
  <div :class="$style.adding__overlay">
    <div :class="$style.adding__modal">
      <div :class="$style.adding__modal__heading">
        THÊM SẢN PHẨM
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['adding__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.adding__modal__body">
        <template v-if="indexCur === 1">
          <h4>Tên sản phẩm</h4>
          <input
            type="text"
            placeholder="Nhập vào tên sản phẩm"
            v-model="Name"
            ref="NameRef"
            @input="updateName"
          />
          <h4>Giá sản phẩm</h4>
          <input
            type="number"
            placeholder="Nhập vào giá sản phẩm"
            v-model="Price"
            ref="PriceRef"
            @input="updatePrice"
          />
          <h4>Tên công ty</h4>
          <select v-model="Comp">
            <option disabled value="">Chọn công ty</option>
            <option v-for="(item, index) in companies" :key="index">{{ item }}</option>
          </select>
          <h4>Danh mục</h4>
          <select v-model="Category">
            <option disabled value="">Chọn danh mục</option>
            <option v-for="(item, index) in categories" :key="index">{{ item }}</option>
          </select>
        </template>
        <template v-if="indexCur === 2">
          <h4>Chính sách bảo hành đổi trả</h4>
          <textarea
            placeholder="Nhập chính sách bảo hành đổi trả"
            v-model="Policy"
            @input="updatePolicy"
          />
          <h4>Mô tả chi tiết</h4>
          <textarea placeholder="Nhập mô tả chi tiết" v-model="Des" @input="updateDes" />
        </template>
        <template v-if="indexCur === 3">
          <h4>Hình ảnh đại diện</h4>
          <div
            v-if="Avatar.name === ''"
            :class="$style['adding__modal-upload']"
            @click="openFileAvatar"
          >
            <font-awesome-icon :icon="faDownload" :class="$style['adding__modal-upload-ic']" />
            <span>Chọn file hoặc kéo vào đây</span>
          </div>
          <input type="file" ref="avtInput" style="display: none" @change="handleFileInputChange" />
          <template v-if="Avatar.name !== ''">
            <div :class="$style['adding__item-ctn']">
              <img :src="Avatar.url" alt="SP" :class="$style['item_img']" />
              <span>{{ Avatar.name }}</span>
              <button @click="resetAvatar">
                <font-awesome-icon :icon="faXmark" :class="$style['adding__item-ic']" />
              </button>
            </div>
            <div :class="$style['adding__item-button']">
              <button @click="openFileAvatar">CHỈNH SỬA</button>
            </div>
          </template>
          <h4>Hình ảnh thêm</h4>
          <div
            v-if="SubImages.length === 0"
            :class="$style['adding__modal-upload']"
            @click="openFileImgs"
          >
            <font-awesome-icon :icon="faDownload" :class="$style['adding__modal-upload-ic']" />
            <span>Chọn file hoặc kéo vào đây</span>
          </div>
          <input
            type="file"
            ref="imgsIn"
            multiple
            style="display: none"
            @change="handleSubIMGInputChange"
          />
          <template v-if="SubImages.length > 0">
            <div :class="$style['adding__img-list']" :style="{ maxHeight: listHeight }">
              <div
                :class="$style['adding__item-ctn']"
                v-for="(item, index) in SubImages"
                :key="index"
              >
                <img :src="item.url" alt="SP" :class="$style['item_img']" />
                <span>{{ item.name }}</span>
                <button @click="deleteImage(index)">
                  <font-awesome-icon :icon="faXmark" :class="$style['adding__item-ic']" />
                </button>
              </div>
            </div>
          </template>
          <div v-if="SubImages.length > 0" :class="$style['adding__item-button']">
            <button @click="openFileImgs">THÊM ẢNH</button>
          </div>
        </template>
      </div>
      <div :class="$style.adding__modal__buttons">
        <button v-if="indexCur === 1" @click="$emit('close')">Hủy</button>
        <button v-if="indexCur > 1" @click="indexCur--">Quay lại</button>
        <button v-if="indexCur < 3" @click="indexCur++">Tiếp tục</button>
        <button v-if="indexCur === 3" @click="submitForm">Thêm</button>
      </div>
      <div :class="$style.adding__modal__line">
        <div
          :class="$style['modal__line-item']"
          v-for="index in 3"
          :key="index"
          :style="{ backgroundColor: index === indexCur ? '#0088CC' : '' }"
        ></div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalAdd.module.scss';
</style>

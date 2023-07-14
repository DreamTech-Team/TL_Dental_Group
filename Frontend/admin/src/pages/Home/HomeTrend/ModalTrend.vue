<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faMinus, faXmark } from '@fortawesome/free-solid-svg-icons';
import DCNK from '@/assets/imgs/Home/DCNK2.png';
import { VueDraggableNext } from 'vue-draggable-next';

interface Category {
  order: number;
  name: string;
  category: string;
  check: boolean;
  src: string;
}

//List present categories
const listCategories = ref<Category[]>([
  {
    order: 0,
    name: 'Kẹp gấp mắc cài R6,7/ kẹp gấp Tube - PMC ORTHO',
    category: 'Dụng cụ chỉnh nha',
    check: false,
    src: DCNK
  },
  {
    order: 1,
    name: 'Dung dịch Sodium Hypochlorite 3% bơm rửa ống tủy răng',
    category: 'Vật liệu chỉnh nha',
    check: false,
    src: DCNK
  },
  {
    order: 2,
    name: 'Composite đặc trám răng Nano (Restorite Nano Hybrid)',
    category: 'Mắc cài tự động',
    check: false,
    src: DCNK
  }
]);

//Drag Status
const drag = ref(false);

//list selected categories
const selectedCategories = ref<Category[]>([]);

//Handle modal
const initListSelected = () => {
  listCategories.value.forEach((item) => {
    if (item.check) {
      const selectedItem: Category = {
        order: item.order,
        name: item.name,
        category: item.category,
        check: true,
        src: item.src
      };
      selectedCategories.value.push(selectedItem);
    }
  });
};

const toggleCheck = (index: number) => {
  const item: Category = listCategories.value[index];
  item.check = !item.check;

  if (item.check) {
    const selectedItem: Category = {
      ...item,
      name: item.name
    };
    selectedCategories.value.push(selectedItem);
  } else {
    const selectedIndex: number = selectedCategories.value.findIndex(
      (selectedItem) => selectedItem.name === item.name
    );
    if (selectedIndex !== -1) {
      selectedCategories.value.splice(selectedIndex, 1);
    }
  }
};

const removeFromSelected = (order: number) => {
  const index = selectedCategories.value.findIndex((element) => element.order === order);
  if (index !== -1) {
    selectedCategories.value.splice(index, 1);
    const originalItem = listCategories.value.find((item) => item.order === order);
    if (originalItem) {
      originalItem.check = false;
    }
  }
};

onMounted(() => {
  initListSelected();
});
</script>

<template>
  <div :class="$style.trend__overlay">
    <div :class="$style.trend__modal">
      <div :class="$style.trend__modal__heading">
        CẬP NHẬT SẢN PHẨM TIÊU BIỂU
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['trend__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.trend__modal__body">
        <div :class="$style['trend__modal__body-left']">
          <div :class="$style['modal__search']">
            <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['home__trend-ic']" />
            <input :class="$style['modal__search-input']" placeholder="Tìm kiếm danh mục" />
          </div>
          <table :class="$style['modal__table_heading']">
            <thead>
              <tr>
                <th style="width: 45%; text-align: left">TÊN SẢN PHẨM</th>
                <th style="width: 25%">DANH MỤC</th>
                <th style="width: 30%">ĐÁNH DẤU NỔI BẬT</th>
              </tr>
            </thead>
          </table>
          <div :class="$style['modal__table-ctn']">
            <table :class="$style['modal__table']">
              <tbody>
                <tr
                  v-for="(item, index) in listCategories"
                  :key="index"
                  :style="item.check ? 'background-color: #95D1F2;' : ''"
                >
                  <td style="width: 45%; text-align: left">
                    <div :class="$style['item__infor']">
                      <img :src="item.src" alt="img" />
                      <p>{{ item.name }}</p>
                    </div>
                  </td>
                  <td style="width: 25%">{{ item.category }}</td>
                  <td style="width: 30%; text-align: center">
                    <input
                      type="checkbox"
                      :class="$style['checkbox1']"
                      :checked="item.check"
                      @click="toggleCheck(index)"
                    />
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div :class="$style['modal__buttons']">
            <button>Hủy</button>
            <button>Cập nhật</button>
          </div>
        </div>
        <div :class="$style['trend__modal__body-right']">
          <h3>SẢN PHẨM NỔI BẬT ĐÃ CHỌN</h3>
          <div :class="$style['trend__right-list']">
            <vue-draggable-next v-model="selectedCategories">
              <transition-group type="transition">
                <div
                  v-for="element in selectedCategories"
                  :key="element.order"
                  :class="{
                    [$style['trend__right-item']]: drag,
                    [$style['trend__right-item1']]: !drag
                  }"
                  @mousedown="drag = true"
                  @mouseup="drag = false"
                >
                  <p>{{ element.name }}</p>

                  <font-awesome-icon
                    :icon="faMinus"
                    :class="$style['trend__modal-st']"
                    @click="removeFromSelected(element.order)"
                  />
                </div>
              </transition-group>
            </vue-draggable-next>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalTrend.module.scss';
</style>

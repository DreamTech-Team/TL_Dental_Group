<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faMinus, faXmark } from '@fortawesome/free-solid-svg-icons';
import { VueDraggableNext } from 'vue-draggable-next';

interface Category {
  order: number;
  name: string;
  company: string;
  check: boolean;
}

//List present categories
const listCategories = ref<Category[]>([
  {
    order: 0,
    name: 'Lò xo chỉnh nha',
    company: 'Công ty ABC',
    check: false
  },
  {
    order: 1,
    name: 'Dụng cụ chỉnh nha',
    company: 'Công ty DEF',
    check: false
  },
  {
    order: 2,
    name: 'Lò xo chỉnh nha',
    company: 'Công ty XYZ',
    check: false
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
        name: `${item.name} ${item.company.substring(
          item.company.lastIndexOf(' '),
          item.company.length
        )}`,
        company: item.company,
        check: true
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
      name: `${item.name} ${item.company.substring(
        item.company.lastIndexOf(' '),
        item.company.length
      )}`
    };
    selectedCategories.value.push(selectedItem);
  } else {
    const selectedIndex: number = selectedCategories.value.findIndex(
      (selectedItem) =>
        selectedItem.name ===
        `${item.name} ${item.company.substring(item.company.lastIndexOf(' '), item.company.length)}`
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
  <div :class="$style.category__overlay">
    <div :class="$style.category__modal">
      <div :class="$style.category__modal__heading">
        CẬP NHẬT DANH MỤC NỔI BẬT
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['category__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.category__modal__body">
        <div :class="$style['category__modal__body-left']">
          <div :class="$style['modal__search']">
            <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['home__category-ic']" />
            <input :class="$style['modal__search-input']" placeholder="Tìm kiếm danh mục" />
          </div>
          <table :class="$style['modal__table_heading']">
            <thead>
              <tr>
                <th style="width: 10%">STT</th>
                <th style="width: 30%">TÊN DANH MỤC</th>
                <th style="width: 30%">CÔNG TY</th>
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
                  <td style="width: 10%">{{ index + 1 }}</td>
                  <td style="width: 30%">{{ item.name }}</td>
                  <td style="width: 30%">{{ item.company }}</td>
                  <td style="width: 30%">
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
        <div :class="$style['category__modal__body-right']">
          <h3>DANH MỤC NỔI BẬT ĐÃ CHỌN</h3>
          <div :class="$style['category__right-list']">
            <vue-draggable-next v-model="selectedCategories">
              <transition-group type="transition">
                <div
                  v-for="element in selectedCategories"
                  :key="element.order"
                  :class="{
                    [$style['category__right-item']]: drag,
                    [$style['category__right-item1']]: !drag
                  }"
                  @mousedown="drag = true"
                  @mouseup="drag = false"
                >
                  {{ element.name }}
                  <font-awesome-icon
                    :icon="faMinus"
                    :class="$style['category__modal-st']"
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
@import './ModalCategory.module.scss';
</style>

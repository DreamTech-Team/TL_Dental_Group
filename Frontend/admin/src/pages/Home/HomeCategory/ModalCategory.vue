<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faMinus, faXmark } from '@fortawesome/free-solid-svg-icons';

interface Category {
  name: string;
  company: string;
  check: boolean;
}

//List present categories
const listCategories = ref<Category[]>([
  {
    name: 'Lò xo chỉnh nha',
    company: 'Công ty ABC',
    check: false
  },
  {
    name: 'Dụng cụ chỉnh nha',
    company: 'Công ty DEF',
    check: false
  },
  {
    name: 'Lò xo chỉnh nha',
    company: 'Công ty XYZ',
    check: false
  }
]);

//list selected categories
const selectedCategories = ref<Category[]>([]);

//Handle modal
const initListSelected = () => {
  listCategories.value.forEach((item) => {
    if (item.check) {
      const selectedItem: Category = {
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

const removeFromSelected = (index: number) => {
  const selectedItem: Category = selectedCategories.value[index];
  const originalItem: Category | undefined = listCategories.value.find(
    (item) =>
      item.name === selectedItem.name.substring(0, selectedItem.name.lastIndexOf(' ') - 1) &&
      item.company === selectedItem.company
  );
  if (originalItem) {
    originalItem.check = false;
  }
  selectedCategories.value.splice(index, 1);
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
            <div
              :class="$style['category__right-item']"
              v-for="(item, index) in selectedCategories"
              :key="index"
            >
              {{ item.name }}
              <font-awesome-icon
                :icon="faMinus"
                :class="$style['category__modal-st']"
                @click="removeFromSelected(index)"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalCategory.module.scss';
</style>

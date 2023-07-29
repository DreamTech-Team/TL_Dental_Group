<script setup lang="ts">
import { ref, type PropType, watch, computed } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faMinus, faXmark } from '@fortawesome/free-solid-svg-icons';
import { VueDraggableNext } from 'vue-draggable-next';
import Swal from 'sweetalert2';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface ItemRS {
  id: string;
  name: string;
  slug: string;
  price: number;
  priceSale: number;
  summary: string;
  description: string;
  mainImg: string;
  imgs: string;
  highlight: number;
  createAt: string;
  fkCategory: {
    id: string;
    companyId: {
      id: string;
      name: string;
      logo: string;
      description: string;
      highlight: number;
      slug: string;
      createAt: string;
      outstandingProductId: string;
    };
    cate1Id: {
      id: string;
      title: string;
      img: string;
      highlight: 3;
      slug: string;
      createAt: string;
    };
    cate2Id: {
      id: string;
      title: string;
      slug: string;
      createAt: string;
    };
  };
}

interface Product {
  slug: string;
  name: string;
  category: string;
  check: boolean;
  src: string;
  highlight: number;
}

//Props
const content = defineProps({
  listItem: {
    type: Array as PropType<ItemRS[]>,
    required: false
  }
});

//EMIT
const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', data: { listrs: ItemRS[] }): void;
}>();

//List present products
const listProducts = ref<Product[]>([]);

//GET ALL CATEGORY
const containers = ref([]);
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/products', {}, {}, deps.value);

//Search
const searchQuery = ref('');
const filteredProducts = computed(() => {
  return listProducts.value.filter((item) =>
    item.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

//Drag Status
const drag = ref(false);

//list selected categories
const selectedProducts = ref<Product[]>([]);

//Handle modal
const initListSelected = () => {
  listProducts.value.forEach((item) => {
    if (item.check) {
      const selectedItem: Product = {
        slug: item.slug,
        name: item.name,
        category: item.category,
        check: true,
        src: item.src,
        highlight: item.highlight
      };
      selectedProducts.value.push(selectedItem);
    }
  });
  selectedProducts.value.sort((a: Product, b: Product) => a.highlight - b.highlight);
};

//Function when check
const toggleCheck = (index: number) => {
  const item: Product = listProducts.value[index];
  item.check = !item.check;

  if (item.check) {
    const selectedItem: Product = {
      ...item,
      name: item.name
    };
    selectedProducts.value.push(selectedItem);
  } else {
    const selectedIndex: number = selectedProducts.value.findIndex(
      (selectedItem) => selectedItem.name === item.name
    );
    if (selectedIndex !== -1) {
      selectedProducts.value.splice(selectedIndex, 1);
    }
  }
};

//Remove when selected
const removeFromSelected = (slug: string) => {
  Swal.fire({
    title: 'Bạn có chắc muốn xóa?',
    text: 'Highlight sẽ được gỡ bỏ khi xác nhận',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Xóa',
    cancelButtonText: 'Hủy'
  }).then((result) => {
    if (result.isConfirmed) {
      const deps = ref([]);
      const object = [
        {
          slug: slug,
          highlight: 0
        }
      ];
      const { response } = useAxios<DataResponse>(
        'patch',
        '/products/highlight',
        object,
        {},
        deps.value
      );

      watch(response, () => {
        if (response.value?.status === 'ok') {
          const index = selectedProducts.value.findIndex((element) => element.slug === slug);
          if (index !== -1) {
            selectedProducts.value.splice(index, 1);
            const originalItem = selectedProducts.value.find((item) => item.slug === slug);
            if (originalItem) {
              originalItem.check = false;
            }
          }
          emits('update-content', {
            listrs: content.listItem?.filter((item: ItemRS) => item.slug !== slug) || []
          });

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

//Confirm update
const updateHighlight = () => {
  let highlightCounter = 1;

  const updatedSelectedProducts = selectedProducts.value.map((item) => {
    return {
      slug: item.slug,
      highlight: highlightCounter++
    };
  });

  const deps = ref([]);
  const { response } = useAxios<DataResponse>(
    'patch',
    '/products/highlight',
    updatedSelectedProducts,
    {},
    deps.value
  );

  watch(response, () => {
    if (response.value?.status === 'ok') {
      const updatedContainers = containers.value
        .map((container: ItemRS) => {
          const updatedProduct = updatedSelectedProducts.find(
            (item) => item.slug === container.slug
          );
          if (updatedProduct) {
            return {
              ...container,
              highlight: updatedProduct.highlight
            };
          }
          return null;
        })
        .filter((item) => item !== null) as ItemRS[];

      updatedContainers.sort((a, b) => a.highlight - b.highlight);

      emits('update-content', {
        listrs: updatedContainers
      });

      Swal.fire({
        title: 'Cập nhật thành công',
        icon: 'success',
        confirmButtonText: 'Hoàn tất',
        width: '30rem'
      });

      emits('close');

      setTimeout(function () {
        Swal.close();
      }, 1200);
    }
  });
};

//Get data from axios
watch(response, () => {
  containers.value = response.value?.data?.data;
  listProducts.value = response.value?.data?.data.map((item: ItemRS) => {
    return {
      slug: item.slug,
      name: item.name,
      category: item.fkCategory.cate2Id.title,
      check: item.highlight != 0 ? true : false,
      src: item.mainImg,
      highlight: item.highlight
    };
  });

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
            <input
              v-model="searchQuery"
              :class="$style['modal__search-input']"
              placeholder="Tìm kiếm danh mục"
            />
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
                  v-for="(item, index) in filteredProducts"
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
                      :disabled="item.check"
                      @click="toggleCheck(index)"
                    />
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div :class="$style['modal__buttons']">
            <button @click="$emit('close')">Hủy</button>
            <button @click="updateHighlight">Cập nhật</button>
          </div>
        </div>
        <div :class="$style['trend__modal__body-right']">
          <h3>SẢN PHẨM NỔI BẬT ĐÃ CHỌN</h3>
          <div :class="$style['trend__right-list']">
            <vue-draggable-next v-model="selectedProducts">
              <transition-group type="transition">
                <div
                  v-for="element in selectedProducts"
                  :key="element.slug"
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
                    @click="removeFromSelected(element.slug)"
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

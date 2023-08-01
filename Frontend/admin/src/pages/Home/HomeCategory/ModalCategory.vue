<script setup lang="ts">
import { ref, watch, computed, type PropType } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faMagnifyingGlass, faMinus, faXmark } from '@fortawesome/free-solid-svg-icons';
import { VueDraggableNext } from 'vue-draggable-next';
import Swal from 'sweetalert2';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface ItemRS {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
  createAt: string;
}

interface Category {
  slug: string;
  name: string;
  company: string;
  highlight: number;
  check: boolean;
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

//List present categories
const listCategories = ref<Category[]>([]);

//GET ALL CATEGORY
const containers = ref([]);
const deps = ref([]);
const { response } = useAxios<DataResponse>('get', '/cate1', {}, {}, deps.value);

//Search
const searchQuery = ref('');
const filteredCategories = computed(() => {
  return listCategories.value.filter((item) =>
    item.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

//Drag Status
const drag = ref(false);

//list selected categories
const selectedCategories = ref<Category[]>([]);

//Handle modal
const initListSelected = () => {
  listCategories.value.forEach((item) => {
    if (item.check) {
      const selectedItem: Category = {
        slug: item.slug,
        name: item.name,
        company: item.company,
        highlight: item.highlight,
        check: true
      };
      selectedCategories.value.push(selectedItem);
    }
  });
  selectedCategories.value.sort((a: Category, b: Category) => a.highlight - b.highlight);
};

//Function when check
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

//Function when remove from selected
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
        '/cate1/highlight',
        object,
        {},
        deps.value
      );

      watch(response, () => {
        if (response.value?.status === 'ok') {
          const index = selectedCategories.value.findIndex((element) => element.slug === slug);
          if (index !== -1) {
            selectedCategories.value.splice(index, 1);
            const originalItem = listCategories.value.find((item) => item.slug === slug);
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

//Function when confirm update
const updateHighlight = () => {
  let highlightCounter = 1;

  const updatedSelectedCategories = selectedCategories.value.map((item) => {
    return {
      slug: item.slug,
      highlight: highlightCounter++
    };
  });

  const deps = ref([]);
  const { response } = useAxios<DataResponse>(
    'patch',
    '/cate1/highlight',
    updatedSelectedCategories,
    {},
    deps.value
  );

  watch(response, () => {
    if (response.value?.status === 'ok') {
      const updatedContainers = containers.value
        .map((container: ItemRS) => {
          const updatedCategory = updatedSelectedCategories.find(
            (item) => item.slug === container.slug
          );
          if (updatedCategory) {
            return {
              ...container,
              highlight: updatedCategory.highlight
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
  containers.value = response.value?.data;
  listCategories.value = response.value?.data.map((item: ItemRS) => {
    return {
      slug: item.slug,
      name: item.title,
      company: item.title,
      check: item.highlight != 0 ? true : false,
      highlight: item.highlight
    };
  });
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
            <input
              v-model="searchQuery"
              :class="$style['modal__search-input']"
              placeholder="Tìm kiếm danh mục"
            />
          </div>
          <table :class="$style['modal__table_heading']">
            <thead>
              <tr>
                <th style="width: 10%">STT</th>
                <th style="width: 60%">TÊN DANH MỤC</th>
                <th style="width: 30%">ĐÁNH DẤU NỔI BẬT</th>
              </tr>
            </thead>
          </table>
          <div :class="$style['modal__table-ctn']">
            <table :class="$style['modal__table']">
              <tbody>
                <tr
                  v-for="(item, index) in filteredCategories"
                  :key="index"
                  :style="item.check ? 'background-color: #95D1F2;' : ''"
                >
                  <td style="width: 10%">{{ index + 1 }}</td>
                  <td style="width: 60%">{{ item.name }}</td>
                  <td style="width: 30%">
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
        <div :class="$style['category__modal__body-right']">
          <h3>DANH MỤC NỔI BẬT ĐÃ CHỌN</h3>
          <div :class="$style['category__right-list']">
            <vue-draggable-next v-model="selectedCategories">
              <transition-group type="transition">
                <div
                  v-for="element in selectedCategories"
                  :key="element.slug"
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
@import './ModalCategory.module.scss';
</style>

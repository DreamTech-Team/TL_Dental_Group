<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faSortDown, faXmark } from '@fortawesome/free-solid-svg-icons';
import { ref } from 'vue';

//Open and Close Modal
const isCustomSelectListOpen = ref(false);

//Array Available
const availableItems = ref<string[]>(['Hoạt động', 'Tin tức', 'Tư vấn', 'Hội thảo']);
const selectedItems = ref<string[]>([]);

const clickHandle = () => {
  isCustomSelectListOpen.value = !isCustomSelectListOpen.value;
};

const SelectItem = (item: string) => {
  selectedItems.value.push(item);
  availableItems.value = availableItems.value.filter((i) => i !== item);
  isCustomSelectListOpen.value = false; //Close Modal
};

const RemoveItem = (item: string) => {
  selectedItems.value = selectedItems.value.filter((i) => i !== item);
  availableItems.value.push(item);
};
</script>

<template>
  <div :class="$style.news__control">
    <div :class="$style['news__control-left']">
      <div :class="$style['news__control-heading']">
        <h4>Tin tức nổi bật</h4>
        <div :class="$style['news__control-border']"></div>
        <div :class="$style['news__control-tabs']">
          <div :class="$style['news__tabs-item--active']">Mới nhất</div>
          <div :class="$style['news__tabs-item']">Tên tin tức từ A đến Z</div>
          <div :class="$style['news__tabs-item']">Tên tin tức từ Z đến A</div>
          <div :class="$style['news__tabs-line']"></div>
        </div>
      </div>
    </div>
    <div :class="$style['news__control-right']">
      <div :class="$style['news__control-tag']">
        <h4>TAGS</h4>
        <div :class="$style['custom-select']" v-on:click="clickHandle">
          <div :class="$style['custom-select-item']">Chọn</div>
          <font-awesome-icon :icon="faSortDown" :class="$style['select_ic']" />
          <div :class="$style['custom-select-list']" v-show="isCustomSelectListOpen">
            <ul>
              <li
                v-for="(item, index) in availableItems"
                :key="index"
                @click.stop="SelectItem(item)"
              >
                {{ item }}
              </li>
            </ul>
          </div>
        </div>
        <div :class="$style['news__list-active']">
          <div
            :class="$style['news__list-item']"
            v-for="(item, index) in selectedItems"
            :key="index"
          >
            {{ item }}
            <div :class="$style['news__item-cancel']" @click.stop="RemoveItem(item)">
              <font-awesome-icon :icon="faXmark" :class="$style['cancel_ic']" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import '../NewsPage.module.scss';
</style>

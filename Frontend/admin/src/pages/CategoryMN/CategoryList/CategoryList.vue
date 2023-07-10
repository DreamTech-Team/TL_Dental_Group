<script setup lang="ts">
import { ref } from 'vue';

const database = [
  {
    nameCompany: 'Cong ty ABC',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: [
          'Mac cai chinh nha',
          'Thun chinh nha',
          'Hook chinh nha',
          'Day thun chinh nha',
          'Lo xo chinh nha',
          'Kem chinh nha'
        ]
      }
    ]
  },
  {
    nameCompany: 'Cong ty ABCD',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: ['Mac cai chinh nha', 'Thun chinh nha', 'Hook chinh nha', 'Day thun chinh nha']
      }
    ]
  },
  {
    nameCompany: 'Cong ty ABCDE',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: ['Hook chinh nha', 'Day thun chinh nha', 'Lo xo chinh nha', 'Kem chinh nha']
      }
    ]
  },
  {
    nameCompany: 'Cong ty ABCDEF',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: ['Mac cai chinh nha', 'Thun chinh nha', 'Lo xo chinh nha', 'Kem chinh nha']
      }
    ]
  },
  {
    nameCompany: 'Cong ty ABC',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: [
          'Mac cai chinh nha',
          'Thun chinh nha',
          'Hook chinh nha',
          'Day thun chinh nha',
          'Lo xo chinh nha',
          'Kem chinh nha'
        ]
      }
    ]
  },
  {
    nameCompany: 'Cong ty ABCD',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: ['Mac cai chinh nha', 'Thun chinh nha', 'Hook chinh nha', 'Day thun chinh nha']
      }
    ]
  },
  {
    nameCompany: 'Cong ty ABCDE',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: ['Hook chinh nha', 'Day thun chinh nha', 'Lo xo chinh nha', 'Kem chinh nha']
      }
    ]
  },
  {
    nameCompany: 'Cong ty ABCDEF',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: ['Mac cai chinh nha', 'Thun chinh nha', 'Lo xo chinh nha', 'Kem chinh nha']
      }
    ]
  },
  {
    nameCompany: 'Cong ty ABC',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: [
          'Mac cai chinh nha',
          'Thun chinh nha',
          'Hook chinh nha',
          'Day thun chinh nha',
          'Lo xo chinh nha',
          'Kem chinh nha'
        ]
      }
    ]
  },
  {
    nameCompany: 'Cong ty ABCD',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: ['Mac cai chinh nha', 'Thun chinh nha', 'Hook chinh nha', 'Day thun chinh nha']
      }
    ]
  },
  {
    nameCompany: 'Cong ty ABCDE',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: ['Hook chinh nha', 'Day thun chinh nha', 'Lo xo chinh nha', 'Kem chinh nha']
      }
    ]
  },
  {
    nameCompany: 'Cong ty ABCDEF',
    category1: [
      {
        name: 'Vat lieu chinh nha ABC',
        category2: ['Mac cai chinh nha', 'Thun chinh nha', 'Lo xo chinh nha', 'Kem chinh nha']
      }
    ]
  }
];

const props = defineProps({ cateType: { type: Number, required: true } });

const selectedItem = ref(-1);
</script>
<template>
  <div :class="$style.container">
    <div :class="$style['container-list']">
      <div
        v-for="(item, index) in database"
        :class="[
          $style['container-list-item'],
          $style[selectedItem === index ? 'container-list-item-active' : '']
        ]"
        :key="index"
        @click="selectedItem = Number(index)"
      >
        <p>{{ item.nameCompany }}</p>
        <svg
          v-if="props.cateType !== 2"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
          <g id="SVGRepo_iconCarrier">
            <path
              d="M15 7L10 12L15 17"
              stroke="#000000"
              stroke-width="1.5"
              stroke-linecap="round"
              stroke-linejoin="round"
            ></path>
          </g>
        </svg>
      </div>
    </div>
    <div :class="$style['container-add']">
      <div :class="$style['container-add-block']">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
          <g id="SVGRepo_iconCarrier">
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="M12 4C12.5523 4 13 4.44772 13 5V11H19C19.5523 11 20 11.4477 20 12C20 12.5523 19.5523 13 19 13H13V19C13 19.5523 12.5523 20 12 20C11.4477 20 11 19.5523 11 19V13H5C4.44772 13 4 12.5523 4 12C4 11.4477 4.44772 11 5 11H11V5C11 4.44772 11.4477 4 12 4Z"
              fill="#000000"
            ></path>
          </g>
        </svg>
        <p>Thêm Công ty</p>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './CategoryList.module.scss';
</style>

<script setup lang="ts">
import {
  girl1,
  girl2,
  img1,
  img2,
  img3,
  img4,
  img5,
  ceo,
  imgHand
} from '@/assets/imgs/Recruitment/RecruitmentImgs';
import RecruitmentCard from './RecruitmentCard/RecruitmentCard.vue';
import RecruitmentCardWork from './RecruitmentCardWork/RecruitmentCardWork.vue';
import {
  posterItems,
  visionItems,
  valueItems,
  recStep,
  recStepItems,
  recruitWorkItems
} from './RecruitmentHandle';
// import { ref } from 'vue';

// const propItems = ref(items);
</script>
<template>
  <div :class="$style.container">
    <div :class="$style.container__poster">
      <div :class="$style['container__poster-img']">
        <div :class="$style['container__poster-img-content']">
          <div class="">
            <img :src="girl1" alt="none" />
          </div>
          <div class="">
            <img :src="girl2" alt="none" />
          </div>
        </div>
      </div>
      <div :class="$style['container__poster-title']">
        <h4>Với bề dày hơn <span>2 năm</span> kinh doanh và phát triển</h4>
        <h3>Công ty sở hữu số lượng vật liệu và dụng cụ nha khoa</h3>
        <h2>LỚN NHẤT VIỆT NAM</h2>
        <h1>#TOP1</h1>
      </div>
      <div :class="$style['container__poster-value']">
        <div :class="$style['container__poster-value-item']">
          Hơn <span>2000</span> vật liệu chỉnh nha
        </div>
      </div>
      <div :class="$style['container__poster-btn']">
        <div :class="$style['container__poster-btn-item']">
          <p>Xem vị trí tuyển dụng</p>
        </div>
      </div>
      <div :class="$style['container__poster-block']">
        <div :class="$style['container__poster-block-card']">
          <recruitment-card :items="posterItems" :style="'type1'" />
        </div>
      </div>
    </div>
    <div :class="$style.container__vision">
      <div :class="$style['container__vision-img1']">
        <img :src="imgHand" alt="none" />
      </div>
      <div :class="$style['container__vision-img2']">
        <img :src="ceo" alt="none" />
      </div>
      <div :class="$style['container__vision-content']">
        <recruitment-card :items="visionItems" :style="'type2'" />
      </div>
    </div>
    <div :class="$style.container__value">
      <div :class="$style['container__value-heading']">
        <div :class="$style['container__value-heading-title']">
          <div :class="$style['container__value-heading-title-main']">
            <div :class="$style['container__value-heading-title-main-1']">Giá Trị</div>
            <div :class="$style['container__value-heading-title-main-2']">Cốt Lõi</div>
          </div>
          <div :class="$style['container__value-heading-title-3']">Từ TL Dental Group</div>
        </div>
        <div :class="$style['container__value-heading-content']">
          <p>
            Tài sản đầu tiên là Sức khỏe. Chúng ta không chỉ xây dựng thương hiệu cá nhân, mà chúng
            ta xây dựng đội ngũ và đội ngũ xây dựng Thương hiệu
          </p>
        </div>
      </div>
      <div :class="$style['container__value-list']">
        <recruitment-card :items="valueItems" :style="'type3'" />
      </div>
    </div>
    <div :class="$style.container__envir">
      <div :class="$style['container__envir-title']">Môi Trường Làm Việc Chuyên Nghiệp</div>
      <div :class="$style['container__envir-block']">
        <div
          :class="[
            $style['container__envir-block-img1'],
            $style['container__envir-block-img1-bottom']
          ]"
        >
          <img :src="img1" alt="none" />
        </div>
        <div :class="$style['container__envir-block-img2']">
          <img :src="img2" alt="none" />
          <div
            :class="[
              $style['container__envir-block-img2-circle'],
              $style['container__envir-block-img2-circle-1']
            ]"
          ></div>
        </div>
        <div
          :class="[
            $style['container__envir-block-img1'],
            $style['container__envir-block-img1-center']
          ]"
        >
          <img :src="img3" alt="none" />
        </div>
        <div
          :class="[
            $style['container__envir-block-img2'],
            $style['container__envir-block-img2-bottom']
          ]"
        >
          <div>
            <div
              :class="[
                $style['container__envir-block-img2-circle'],
                $style['container__envir-block-img2-circle-2']
              ]"
            ></div>
            <img :src="img4" alt="none" />
          </div>
        </div>
        <div
          :class="[
            $style['container__envir-block-img1'],
            $style['container__envir-block-img1-top']
          ]"
        >
          <img :src="img5" alt="none" />
        </div>
      </div>
    </div>
    <div :class="$style.container__recruit">
      <div :class="$style['container__recruit-left']">
        <div :class="$style['container__recruit-left-title']">
          <span>Tuyển dụng TL Dental Group</span>
          <h2>Quy Trình Tuyển Dụng</h2>
        </div>
        <div :class="$style['container__recruit-left-nav']">
          <recruitment-card :items="recStep" :style="'type4'" />
        </div>
      </div>
      <div :class="$style['container__recruit-right']">
        <recruitment-card :items="recStepItems" :style="'type5'" />
      </div>
    </div>
    <div :class="$style.container__work">
      <div :class="$style['container__work-heading']">
        <div :class="$style['container__work-heading-title']">Các Vị Trí Tuyển Dụng</div>
        <div :class="$style['container__work-heading-filter']">
          <input type="text" name="filter" id="" placeholder="Tìm kiếm" />
        </div>
      </div>
      <div :class="$style['container__work-staff']">
        <div
          :class="$style['container__work-staff-item']"
          v-for="item in recruitWorkItems"
          :key="item.id"
        >
          <recruitment-card-work :infor="item" />
        </div>
      </div>
      <div :class="$style['container__work-btn']">
        <p>Xem thêm</p>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentPage.module.scss';
</style>

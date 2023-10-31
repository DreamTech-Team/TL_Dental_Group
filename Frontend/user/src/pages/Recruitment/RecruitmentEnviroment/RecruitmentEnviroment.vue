<script setup lang="ts">
import { img1, img2, img3, img4, img5 } from '@/assets/imgs/Recruitment/RecruitmentImgs';
</script>

<template>
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
        :class="[$style['container__envir-block-img1'], $style['container__envir-block-img1-top']]"
      >
        <img :src="img5" alt="none" />
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './RecruitmentEnviroment.module.scss';
</style>

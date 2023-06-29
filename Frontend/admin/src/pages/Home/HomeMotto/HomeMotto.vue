<script setup lang="ts">
import Meeting from '@/assets/imgs/Home/Meeting.png';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
</script>
<template>
  <div :class="$style.home__motto">
    <div :class="$style['home__motto-left']">
      <h4>PHƯƠNG CHÂM</h4>
      <span
        >Lấy uy tín khách hàng là hàng đầu. <br />
        <br />
        Sản phẩm cho mọi người. An toàn và chất lượng. Trao sản phẩm và bảo hành trọn đời. <br />
        Sản phẩm cho mọi người. An toàn và chất lượng. Trao sản phẩm và bảo hành trọn đời. <br />
        Sản phẩm cho mọi người. An toàn và chất lượng. Trao sản phẩm và bảo hành trọn đời. Sản
        người. An toàn và chất lượng. <br />
        <br />
        Trao sản phẩm và bảo hành trọn đời. Lấy uy tín khách hàng <br />
        hàng đầu. Sản phẩm cho mọi người. An toàn và chất lượng. Trao sản phẩm và bảo hành trọn đời.
      </span>
      <EditBtn style="right: 50px" />
    </div>
    <div :class="$style['home__motto-right']">
      <h4 :class="$style['home__motto-title']">PHƯƠNG CHÂM</h4>
      <img :src="Meeting" alt="meeting" />
      <CamBtn />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './HomeMotto.module.scss';
</style>

<script setup lang="ts">
import Comp from '@/assets/imgs/Home/ABComp.png';
import Icon1 from '@/assets/imgs/Home/RSIcon1.png';
import Icon2 from '@/assets/imgs/Home/RSIcon2.png';
import Icon3 from '@/assets/imgs/Home/RSIcon3.png';
import ICEdit from '@/assets/icons/Edit.png';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
</script>
<template>
  <div :class="$style.home__reason">
    <div :class="$style['home__reason-left']">
      <img :src="Comp" alt="company" />
      <CamBtn />
    </div>
    <div :class="$style['home__reason-right']">
      <h4 :class="$style['home__reason-title']">TẠI SAO NÊN LỰA CHỌN TL DENTAL GROUP</h4>
      <h4>#1 Nhà Cung Cấp Sản Phẩm Nha Khoa - Chỉnh Nha Tại Việt Nam</h4>
      <div :class="$style['home__reason-list']">
        <div :class="$style['home__reason-item']">
          <div :class="$style['home__reason-icon']">
            <img :src="Icon1" alt="icon1" />
            <button>
              <img :src="ICEdit" alt="iconed" />
            </button>
          </div>
          <div :class="$style['home__reason-description']">
            <h5>Sản phẩm chính hãng</h5>
            <p>
              Quý khách hàng sẽ được trải nghiệm những sản phẩm chính hãng từ các thương hiệu uy
              tín, chất lượng cao.
            </p>
          </div>
        </div>
        <div :class="$style['home__reason-item']">
          <div :class="$style['home__reason-icon']">
            <img :src="Icon2" alt="icon2" />
            <button>
              <img :src="ICEdit" alt="iconed" />
            </button>
          </div>
          <div :class="$style['home__reason-description']">
            <h5>Mức giá cạnh tranh</h5>
            <p>
              TL Gental Group với phương châm kinh doanh đôi bên cùng có lợi, quý khách được cung
              cấp giá cả cạnh tranh hàng đầu thị trường.
            </p>
          </div>
        </div>
        <div :class="$style['home__reason-item']">
          <div :class="$style['home__reason-icon']">
            <img :src="Icon3" alt="icon3" />
            <button>
              <img :src="ICEdit" alt="iconed" />
            </button>
          </div>
          <div :class="$style['home__reason-description']">
            <h5>Chân thành, tin cậy và uy tín</h5>
            <p>
              Sự phát triển của quý bác sỹ cũng chính là sự thành công của TL Gental Group. Từ đó,
              tạo nên hệ thống đối tác bền vững và lâu dài.
            </p>
          </div>
        </div>
      </div>
    </div>
    <EditBtn />
  </div>
</template>

<style module scoped lang="scss">
@import './HomeReason.module.scss';
</style>

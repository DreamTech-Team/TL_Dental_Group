<script setup lang="ts">
import { ref } from 'vue';
import Comp from '@/assets/imgs/Home/ABComp.png';
import Icon1 from '@/assets/imgs/Home/RSIcon1.png';
import Icon2 from '@/assets/imgs/Home/RSIcon2.png';
import Icon3 from '@/assets/imgs/Home/RSIcon3.png';
import ICEdit from '@/assets/icons/Edit.png';
import EditBtn from '@/components/EditBtn/EditBtn.vue';
import CamBtn from '@/components/ImageBtn/ImageBtn.vue';
import ModalCam from './CamReason.vue';

const content = ref({
  image: Comp,
  title: 'TẠI SAO NÊN LỰA CHỌN TL DENTAL GROUP',
  description: '#1 Nhà Cung Cấp Sản Phẩm Nha Khoa - Chỉnh Nha Tại Việt Nam',
  listrs: [
    {
      icon: Icon1,
      title: 'Sản phẩm chính hãng',
      description:
        // eslint-disable-next-line max-len
        'Quý khách hàng sẽ được trải nghiệm những sản phẩm chính hãng từ các thương hiệu uy tín, chất lượng cao.'
    },
    {
      icon: Icon2,
      title: 'Mức giá cạnh tranh',
      description:
        // eslint-disable-next-line max-len
        'TL Gental Group với phương châm kinh doanh đôi bên cùng có lợi, quý khách được cung cấp giá cả cạnh tranh hàng đầu thị trường.'
    },
    {
      icon: Icon3,
      title: 'Chân thành, tin cậy và uy tín',
      description:
        // eslint-disable-next-line max-len
        'Sự phát triển của quý bác sỹ cũng chính là sự thành công của TL Gental Group. Từ đó, tạo nên hệ thống đối tác bền vững và lâu dài.'
    }
  ]
});

const isOpenCam = ref(false);
</script>
<template>
  <div :class="$style.home__reason">
    <div :class="$style['home__reason-left']">
      <img :src="content.image" alt="company" />
      <CamBtn @click="isOpenCam = true" />
    </div>
    <div :class="$style['home__reason-right']">
      <h4 :class="$style['home__reason-title']">{{ content.title }}</h4>
      <h4>{{ content.description }}</h4>
      <div :class="$style['home__reason-list']">
        <div
          :class="$style['home__reason-item']"
          v-for="(item, index) in content.listrs"
          :key="index"
        >
          <div :class="$style['home__reason-icon']">
            <img :src="item.icon" alt="icon1" />
            <button>
              <img :src="ICEdit" alt="iconed" />
            </button>
          </div>
          <div :class="$style['home__reason-description']">
            <h5>{{ item.title }}</h5>
            <p>
              {{ item.description }}
            </p>
          </div>
        </div>
      </div>
    </div>
    <EditBtn />
  </div>
  <ModalCam v-if="isOpenCam" :image="content.image" @close="isOpenCam = false" />
</template>

<style module scoped lang="scss">
@import './HomeReason.module.scss';
</style>

<script setup lang="ts">
import InfoCompany from '@/assets/imgs/About/InfoCompany.png';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faClose, faPencil, faCheck, faRotate } from '@fortawesome/free-solid-svg-icons';
import { ref } from 'vue';
import Editor from '@tinymce/tinymce-vue';

const isEdit = ref(false);
const InfoCompanyContent = ref({
  content1: `Apple Inc. là một công ty công nghệ đa quốc gia có trụ sở chính tại Cupertino, California,
          Hoa Kỳ. Được thành lập vào ngày 1 tháng 4 năm 1976 bởi Steve Jobs, Steve Wozniak và Ronald
          Wayne, Apple đã trở thành một trong những công ty công nghệ lớn nhất và được biết đến rộng
          rãi trên toàn thế giới.<br /><br />
          Apple nổi tiếng với việc phát triển, sản xuất và kinh doanh các sản phẩm điện tử tiêu
          dùng, phần mềm và dịch vụ liên quan. Các sản phẩm nổi tiếng của Apple bao gồm iPhone (dòng
          điện thoại thông minh), iPad (máy tính bảng), Mac (máy tính cá nhân và laptop), Apple
          Watch (đồng hồ thông minh), và AirPods (tai nghe không dây).`,
  content2: `Ngoài ra, Apple cũng phát triển và cung cấp nền tảng phần mềm như hệ điều hành macOS, iOS
          và watchOS, cùng với các ứng dụng và dịch vụ như iTunes, App Store, iCloud và Apple
          Music.<br /><br />
          Apple đã đạt được nhiều thành công và có ảnh hưởng lớn đến ngành công nghiệp công nghệ
          thông tin. Công ty này được biết đến với sự tập trung vào thiết kế, chất lượng và trải
          nghiệm người dùng, và thường được coi là một trong những công ty công nghệ hàng đầu thế
          giới.<br /><br />
          Đáng chú ý, Apple cũng đã đưa ra các dự án và nỗ lực về bảo vệ môi trường và bền vững, bao
          gồm cam kết sử dụng năng lượng tái tạo và tái chế các thành phần trong sản phẩm của họ.`
});

const _content1 = ref(InfoCompanyContent.value.content1);
const _content2 = ref(InfoCompanyContent.value.content2);
const selectedImage = ref(null);
const selectedImage1 = ref(null);

const handleChangeContent1 = (e: Event) => {
  if (e.target) {
    _content1.value = e.target.getContent();
  }
};

const handleChangeContent2 = (e) => {
  if (e.target) _content2.value = e.target.getContent();
};

const handleUpdateContent = () => {
  isEdit.value = false;
  InfoCompanyContent.value.content1 = _content1.value;
  InfoCompanyContent.value.content2 = _content2.value;
};

const handleChangeImage = () => {
  document.getElementById('input_file2').click();
};

const handleChangeImage1 = () => {
  document.getElementById('input_file1').click();
};

// const handleFileInputChange = (event) => {
//   const file = event.target.files[0];
//   const reader = new FileReader();

//   reader.onload = (e) => {
//     selectedImage.value = e.target.result;
//   };

//   reader.readAsDataURL(file);
// };

const handleFileInputChange = (event) => {
  const file = event.target.files[0];
  const reader = new FileReader();

  reader.onload = (e) => {
    selectedImage.value = e.target.result;
  };

  reader.readAsDataURL(file);
};

const handleFileInputChange1 = (event) => {
  const file1 = event.target.files[0];
  const reader1 = new FileReader();

  reader1.onload = (e) => {
    selectedImage1.value = e.target.result;
  };

  reader1.readAsDataURL(file1);
};
</script>
<template>
  <div :class="$style.about__infocompany">
    <p :class="$style['about__infocompany-title']">THÔNG TIN CÔNG TY</p>

    <div :class="$style['about__infocompany-content']">
      <div :class="$style['about__infocompany-content-item']">
        <p v-html="InfoCompanyContent.content1" v-if="!isEdit"></p>
        <editor
          v-if="isEdit"
          allowedEvents="onChange"
          @change="handleChangeContent1"
          api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
          :initial-value="InfoCompanyContent.content1"
          :value="InfoCompanyContent.content1"
          :init="{
            selector: 'textarea',
            placeholder: 'Nhập phương châm',
            height: 370,
            menubar: false,
            plugins: [
              'advlist autolink lists link image charmap print preview anchor',
              'searchreplace visualblocks code fullscreen',
              'insertdatetime media table paste code help wordcount'
            ],
            toolbar:
              'undo redo | formatselect | bold italic backcolor forecolor | \
           alignleft aligncenter alignright alignjustify | \
           bullist numlist outdent indent | removeformat | help'
          }"
        />
        <div :class="$style['about__infocompany-position']">
          <div :class="$style['about__infocompany-img']" v-if="selectedImage">
            <img :src="selectedImage" alt="" />
          </div>
          <div :class="$style['about__infocompany-img']" v-else>
            <img :src="InfoCompany" alt="" />
          </div>

          <div v-if="isEdit" :class="$style['about__infocompany-button-wrapper']">
            <button :class="$style['about__infocompany-button']" @click="handleChangeImage">
              <font-awesome-icon :icon="faRotate" :class="$style['about__infocompany-button-ic']" />
              <span>Đổi ảnh</span>
            </button>

            <input
              type="file"
              style="display: none"
              id="input_file2"
              accept="image/*"
              @change="handleFileInputChange"
            />
          </div>
        </div>
      </div>

      <div :class="$style['about__infocompany-content-item']">
        <div :class="$style['about__infocompany-position']">
          <div :class="$style['about__infocompany-img']" v-if="selectedImage1">
            <img :src="selectedImage1" alt="" />
          </div>
          <div :class="$style['about__infocompany-img']" v-else>
            <img :src="InfoCompany" alt="" />
          </div>

          <div v-if="isEdit" :class="$style['about__infocompany-button-wrapper']">
            <button :class="$style['about__infocompany-button']" @click="handleChangeImage1">
              <font-awesome-icon :icon="faRotate" :class="$style['about__infocompany-button-ic']" />
              <span>Đổi ảnh</span>
            </button>

            <input
              type="file"
              style="display: none"
              id="input_file1"
              accept="image/*"
              @change="handleFileInputChange1"
            />
          </div>
        </div>
        <p v-html="InfoCompanyContent.content2" v-if="!isEdit"></p>
        <editor
          v-if="isEdit"
          allowedEvents="onChange"
          @change="handleChangeContent2"
          api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
          :initial-value="InfoCompanyContent.content2"
          :value="InfoCompanyContent.content2"
          :init="{
            selector: 'textarea',
            placeholder: 'Nhập phương châm',
            height: 370,
            menubar: false,
            plugins: [
              'advlist autolink lists link image charmap print preview anchor',
              'searchreplace visualblocks code fullscreen',
              'insertdatetime media table paste code help wordcount'
            ],
            toolbar:
              'undo redo | formatselect | bold italic backcolor forecolor | \
           alignleft aligncenter alignright alignjustify | \
           bullist numlist outdent indent | removeformat | help'
          }"
        />
      </div>
    </div>

    <button
      :class="$style['about__infocompany-button-first']"
      v-if="!isEdit"
      @click="isEdit = true"
    >
      <font-awesome-icon :icon="faPencil" :class="$style['about__infocompany-button-ic']" />
      <span>Chỉnh sửa</span>
    </button>

    <button :class="$style['about__infocompany-button-left']" v-if="isEdit" @click="isEdit = false">
      <font-awesome-icon :icon="faClose" :class="$style['about__infocompany-button-ic']" />
      <span>Hủy bỏ</span>
    </button>

    <button
      :class="$style['about__infocompany-button-right']"
      v-if="isEdit"
      @click="handleUpdateContent"
    >
      <font-awesome-icon :icon="faCheck" :class="$style['about__infocompany-button-ic']" />
      <span>Xác nhận</span>
    </button>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutInfoCompany.module.scss';
</style>

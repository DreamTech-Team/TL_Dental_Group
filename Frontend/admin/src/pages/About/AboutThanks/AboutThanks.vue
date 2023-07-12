<script setup lang="ts">
// import Intro from '@/assets/imgs/About/Intro.png';
import { ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faComputerMouse, faFloppyDisk } from '@fortawesome/free-solid-svg-icons';
import Editor from '@tinymce/tinymce-vue';
import Swal from 'sweetalert2';

const thanksContent = ref({
  title: 'Tiêu đề/lời nhắn/thư ngỏ của CEO',
  slogan: 'Slogan/tên cty/lời chào',
  content: `Nội dung: Tôi xin gửi lời cảm ơn chân thành đến các công ty hợp tác và những nhà đồng sáng
        lập đã đồng hành cùng chúng tôi trong suốt thời gian qua. Sự hỗ trợ và đóng góp của các bạn
        đã góp phần quan trọng vào thành công và phát triển của chúng tôi.<br /><br />
        Các công ty hợp tác đã đồng hành và chia sẻ tầm nhìn cùng chúng tôi, tạo nên một môi trường
        làm việc chuyên nghiệp và sáng tạo. Chúng tôi đánh giá cao sự tin tưởng và cam kết của các
        bạn, giúp chúng tôi thực hiện những dự án đầy thách thức và mang lại giá trị cho khách
        hàng.<br /><br />
        Những nhà đồng sáng lập đã cùng chúng tôi khởi động và xây dựng nền tảng thành công. Sự tận
        tâm, năng lực và sự đổi mới của các bạn đã trở thành động lực để chúng tôi phát triển và
        vươn lên. Chúng tôi biết ơn những giá trị và tầm nhìn mà các bạn mang đến, và sẽ tiếp tục
        duy trì những giá trị đó trong tất cả các hoạt động của chúng tôi.<br /><br />
        Trân trọng.`
});

const isEdit = ref(false);
const _content = ref(``);

const handleChangeContent = (e) => {
  _content.value = e.target.getContent();
  console.log(_content.value);
};

const handleUpdateContent = () => {
  isEdit.value = false;
  thanksContent.value.content = _content.value;
};
</script>
<template>
  <div :class="$style.about__thanks">
    <div :class="$style['about__thanks-letter']" id="haha">
      <h1>LỜI CẢM ƠN</h1>

      <h2>
        {{ thanksContent.title }}<br />
        {{ thanksContent.slogan }}
      </h2>

      <p v-if="!isEdit" v-html="thanksContent.content"></p>
      <div :class="$style['about__thanks-letter-editor']" v-else>
        <editor
          allowedEvents="onChange"
          @change="handleChangeContent"
          api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
          :initial-value="thanksContent.content"
          :value="thanksContent.content"
          :init="{
            selector: 'textarea',
            placeholder: 'Nhập phương châm',
            height: 500,
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

        <button @click="handleUpdateContent">
          <font-awesome-icon :icon="faFloppyDisk" :class="$style['about__infocompany-button-ic']" />
          <span>Lưu thay đổi</span>
        </button>
      </div>

      <div :class="$style['about__thanks-letter-bg']" @click="isEdit = true" v-if="!isEdit">
        <font-awesome-icon :icon="faComputerMouse" :class="$style['about__thanks-letter-bg-ic']" />
        <span>Click chuột để chỉnh sửa nội dung</span>
      </div>
      <div v-else></div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './AboutThanks.module.scss';
</style>

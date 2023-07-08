<script setup lang="ts">
import Editor from '@tinymce/tinymce-vue';
import { ref, computed } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faComputerMouse, faFloppyDisk } from '@fortawesome/free-solid-svg-icons';

const activityContent = ref({
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
const activityTitle = ref('');
const activitySummary = ref('');

const textareaStyle = {
  resize: 'none',
  height: '100px',
  maxHeight: '120px',
  borderRadius: '10px',
  padding: '10px',
  fontSize: '16px',
  fontFamily: 'Roboto',
  border: '1px solid #ccc',
  marginBottom: '15px'
};

const handleChangeContent = (e) => {
  _content.value = e.target.getContent();
  console.log(_content.value);
};

const handleUpdateContent = () => {
  isEdit.value = false;
  activityContent.value.content = _content.value;
};
</script>
<template>
  <div :class="$style.activity_container">
    <div :class="$style['activity_container--title']">
      <p>CHỈNH SỬA HOẠT ĐỘNG</p>
    </div>
    <div :class="$style['activity_container--wrap']">
      <div :class="$style['activity_container--wrap-left']">
        <div :class="$style.wrap_title">
          <label :class="$style['wrap_title--text']" for="activity-title">Tiêu đề hoạt động</label>
          <input
            id="activity-title"
            v-model="activityTitle"
            placeholder="Nhập tiêu đề hoạt động"
            :class="$style['custom-input']"
          />
        </div>
        <div :class="$style.wrap_sumary">
          <label :class="$style['wrap_title--text']" for="activity-summary">Nội dung tóm tắt</label>
          <textarea
            :style="textareaStyle"
            id="activity-summary"
            v-model="activitySummary"
            placeholder="Nhập nội dung tóm tắt"
            :class="$style['custom-textarea']"
          ></textarea>
        </div>

        <div>
          <p :class="$style['wrap_title--text']">Chọn tag cho hoạt động</p>
        </div>
      </div>

      <div :class="$style['activity_container--wrap-right']">
        <!-- <div>
        <div :class="$style.activity__thanks">
          <div :class="$style['about__thanks-letter']" id="haha">
            <h1>LỜI CẢM ƠN</h1>

            <p v-if="!isEdit" v-html="activityContent.content"></p>
            <div :class="$style['about__thanks-letter-editor']" v-else>
              <editor
                allowedEvents="onChange"
                @change="handleChangeContent"
                api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
                :initial-value="activityContent.content"
                :value="activityContent.content"
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
                    'undo redo | formatselect | bold italic backcolor | \
           alignleft aligncenter alignright alignjustify | \
           bullist numlist outdent indent | removeformat | help'
                }"
              />

              <button @click="handleUpdateContent">
                <font-awesome-icon
                  :icon="faFloppyDisk"
                  :class="$style['about__infocompany-button-ic']"
                />
                <span>Lưu thay đổi</span>
              </button>
            </div>

            <div :class="$style['about__thanks-letter-bg']" @click="isEdit = true" v-if="!isEdit">
              <font-awesome-icon
                :icon="faComputerMouse"
                :class="$style['about__thanks-letter-bg-ic']"
              />
              <span>Click chuột để chỉnh sửa nội dung</span>
            </div>
            <div v-else></div>
          </div>
        </div>
      </div> -->
        <div>
          <p>Cập nhật ảnh bìa</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ActivityModel.module.scss';
</style>

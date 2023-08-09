<script setup lang="ts">
import Editor from '@tinymce/tinymce-vue';
import { ref, watch, type PropType } from 'vue';
import Swal from 'sweetalert2';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import Loading from '@/components/LoadingComponent/LoadingComponent.vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface RecruitmentItem {
  id: string;
  title: string;
  position: string;
  location: string;
  department: string;
  working_form: string;
  description: string;
  requirements: string;
  benefit: string;
  contact: string;
  treatment: string;
  slug: string;
  createAt: string;
}

//Validate TinyMCE
interface TextAreaValue {
  level: {
    content: string;
  };
}

//Dependency
const deps = ref([]);
const isLoading = ref(false);

//Emit
const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', data: { ItemAdd: RecruitmentItem }): void;
}>();

//Define Props
const context = defineProps({
  updateObject: {
    type: Object as PropType<RecruitmentItem | null>,
    required: false
  }
});

//Part 1
const title = ref(context.updateObject?.title);
const address = ref(context.updateObject?.location);
const position = ref(context.updateObject?.position);
const time = ref(context.updateObject?.working_form);

//Part 2
const desSummary = ref<string | undefined>(context.updateObject?.description);
const descriptionInput = ref<TextAreaValue>({
  level: {
    content: context.updateObject?.description || ''
  }
});
const updateDes = (content: TextAreaValue) => {
  descriptionInput.value.level.content = content.level.content;
  desSummary.value = content.level.content;
};

//Part 3
const valueTreat = ref<string | undefined>(context.updateObject?.treatment);
const treatInput = ref<TextAreaValue>({
  level: {
    content: context.updateObject?.treatment || ''
  }
});
const updateTreat = (content: TextAreaValue) => {
  treatInput.value.level.content = content.level.content;
  valueTreat.value = content.level.content;
};

const valueContact = ref<string | undefined>(context.updateObject?.contact);
const contactInput = ref<TextAreaValue>({
  level: {
    content: context.updateObject?.contact || ''
  }
});
const updateContact = (content: TextAreaValue) => {
  contactInput.value.level.content = content.level.content;
  valueContact.value = content.level.content;
};

//Properties
const indexCur = ref(1);

//SWEET ARLERT
const alertDialog = (context: string, page: number) => {
  Swal.fire({
    title: context,
    icon: 'error',
    showCancelButton: false,
    width: '30rem'
  });
  setTimeout(function () {
    Swal.close();
  }, 1200);
  indexCur.value = page;
};

const submitForm = () => {
  if (title.value && title.value.length <= 5) {
    title.value = '';
    alertDialog('Tiêu đề quá ngắn', 1);
    return;
  }
  if (address.value && address.value.length <= 5) {
    address.value = '';
    alertDialog('Địa chỉ không hợp lệ', 1);
    return;
  }
  if (position.value === '') {
    alertDialog('Bạn chưa chọn vị trí', 1);
    return;
  }
  if (time.value === '') {
    alertDialog('Bạn chưa chọn thời gian làm', 1);
    return;
  }
  if (descriptionInput.value.level.content.length <= 5) {
    alertDialog('Mô tả quá ngắn', 2);
    return;
  }
  if (treatInput.value.level.content.length <= 5) {
    alertDialog('Đãi ngộ quá ngắn', 3);
    return;
  }
  if (contactInput.value.level.content.length <= 5) {
    alertDialog('Liên hệ quá ngắn', 3);
    return;
  }

  const object = {
    id: context.updateObject?.id,
    title: title.value,
    position: position.value,
    location: address.value,
    working_form: time.value,
    description: descriptionInput.value.level.content,
    contact: contactInput.value.level.content,
    treatment: treatInput.value.level.content
  };

  isLoading.value = true;

  const updateRecruitment = useAxios<DataResponse>(
    'patch',
    '/recruitment/',
    object,
    {},
    deps.value
  );

  watch(updateRecruitment.response, () => {
    if (updateRecruitment.response.value?.status === 'ok') {
      isLoading.value = false;
      Swal.fire({
        title: 'Cập nhật thành công',
        icon: 'success',
        confirmButtonText: 'Hoàn tất',
        width: '30rem'
      }).then((result) => {
        if (result.isConfirmed) {
          Swal.close();
          emits('update-content', {
            ItemAdd: updateRecruitment.response.value?.data
          });
          emits('close');
        }
      });
    }
  });
};
</script>
<template>
  <div :class="$style.activity_container">
    <div :class="$style['activity_container--title']">
      <p>CẬP NHẬT TUYỂN DỤNG</p>
      <font-awesome-icon @click="$emit('close')" :icon="faXmark" :class="$style.activity_cancel" />
    </div>
    <div :class="$style['activity_container--wrap']">
      <!-- Step 1 -->
      <template v-if="indexCur === 1">
        <div :class="$style['activity_container--wrap-left']">
          <div :class="$style.wrap_title">
            <label :class="$style['wrap_title--text']" for="recruitment-name"
              >Đăng bài tuyển dụng</label
            >
            <input
              id="recruitment-name"
              v-model="title"
              placeholder="Nhập tiêu đề"
              :class="$style['custom-input']"
            />
          </div>
          <div :class="$style.wrap_title">
            <label :class="$style['wrap_title--text']" for="recruitment-address">Địa chỉ</label>
            <input
              id="recruitment-address"
              v-model="address"
              placeholder="Nhập vị trí"
              :class="$style['custom-input']"
            />
          </div>
          <div :class="$style.options_wrap">
            <div :class="$style.options_wrap_item">
              <p :class="$style.options_title">Vị trí</p>
              <input
                v-model="position"
                placeholder="Sale/Marketing"
                :class="$style.options_wrap_btn"
              />
            </div>
            <div :class="$style.options_wrap_item">
              <p :class="$style.options_title">Thời gian làm</p>
              <input
                v-model="time"
                placeholder="Partime/Fulltime"
                :class="$style.options_wrap_btn"
              />
            </div>
          </div>
        </div>
      </template>
      <!-- Step2 -->
      <template v-else-if="indexCur === 2">
        <div :class="$style['activity_container--wrap-left']">
          <div :class="$style.wrap_title">
            <label :class="$style['wrap_title--text']" for="recruitment-description"
              >Mô tả công việc</label
            >
            <editor
              id="uuid1"
              allowedEvents="onChange"
              :onchange="updateDes"
              api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
              :initial-value="descriptionInput.level.content"
              :value="descriptionInput.level.content"
              :init="{
                selector: 'textarea#uuid1',
                placeholder: 'Nhập mô tả công việc',
                height: 300,
                menubar: false,
                plugins: 'advlist lists link image fullscreen',
                toolbar:
                  'undo italic bold | \
                alignleft aligncenter alignright alignjustify | \
                | forecolor bullist fullscreen |',
                images_file_types: 'jpg,svg,webp,png',
                file_picker_types: 'file image media',
                resize_img_proportional: false
              }"
            />
          </div>
        </div>
      </template>
      <!-- Step3 -->
      <template v-else-if="indexCur === 3">
        <div :class="$style['activity_container--wrap-left']">
          <div :class="$style.wrap_title">
            <label :class="$style['wrap_title--text']" for="recruitment-description">Đãi ngộ</label>
            <editor
              id="uuid2"
              allowedEvents="onChange"
              :onchange="updateTreat"
              api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
              :initial-value="treatInput.level.content"
              :value="treatInput.level.content"
              :init="{
                selector: 'textarea#uuid2',
                placeholder: 'Nhập đãi ngộ',
                height: 200,
                menubar: false,
                plugins: 'advlist lists link fullscreen',
                toolbar:
                  'undo italic bold | \
                alignleft aligncenter alignright alignjustify | \
                | forecolor bullist fullscreen |'
              }"
            />
          </div>
          <div :class="$style.wrap_title">
            <label :class="$style['wrap_title--text']" for="recruitment-require">Liên hệ</label>
            <editor
              id="uuid3"
              allowedEvents="onChange"
              :onchange="updateContact"
              api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
              :initial-value="contactInput.level.content"
              :value="contactInput.level.content"
              :init="{
                selector: 'textarea#uuid3',
                placeholder: 'Nhập liên hệ',
                height: 200,
                menubar: false,
                plugins: 'advlist lists link fullscreen',
                toolbar:
                  'undo italic bold | \
                alignleft aligncenter alignright alignjustify | \
                | forecolor bullist fullscreen |'
              }"
            />
          </div>
        </div>
      </template>
      <div :class="$style.adding__modal__line">
        <div
          :class="$style['modal__line-item']"
          v-for="index in 3"
          :key="index"
          :style="{ backgroundColor: index === indexCur ? '#0088CC' : '' }"
        ></div>
      </div>
      <div :class="$style['activity_container--wrap--btn']">
        <button v-if="indexCur === 1" @click="$emit('close')">Hủy</button>
        <button v-if="indexCur > 1" @click="indexCur--">Quay lại</button>
        <button v-if="indexCur < 3" @click="indexCur++">Tiếp tục</button>
        <button v-if="indexCur === 3" @click="submitForm">Cập nhật</button>
      </div>
    </div>
    <div v-show="isLoading" :class="$style.loading__overlay">
      <Loading />
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalRecruitment.module.scss';
</style>

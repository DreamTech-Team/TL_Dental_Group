<script setup lang="ts">
import Editor from '@tinymce/tinymce-vue';
import { ref, computed, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faPlus,
  faMagnifyingGlass,
  faMinus,
  faXmark,
  faDownload
} from '@fortawesome/free-solid-svg-icons';
import useAxios, { type DataResponse } from '@/hooks/useAxios';
import { type PropType } from 'vue';
import Swal from 'sweetalert2';
import CropImage from '@/components/CropImage/CropImage.vue';
import Loading from '@/components/LoadingComponent/LoadingComponent.vue';

interface Tags {
  id: string;
  name: string;
  slug: string;
  createAt: string;
}

interface dataUpdate {
  createAt: string;
  detail: string;
  detailMobile: string;
  highlight: 0;
  id: string;
  img: string;
  slug: string;
  summary: string;
  title: string;
}

interface MyErrorResponse {
  response?: {
    data?: {
      message?: string;
    };
  };
}

interface TextAreaValue {
  level: {
    content: string;
  };
}

const deps = ref([]);
const {
  response: tagsResponsive,
  error: errorTags,
  isLoading: loadingTags
} = useAxios<DataResponse>('get', '/tags', {}, {}, deps.value);

export interface CloseModalFn {
  (...payload: any[]): void;
}

const props = defineProps({
  closeModal: {
    type: Function as PropType<CloseModalFn>,
    required: true
  },
  selectedActivity: {
    type: Object, // Kiểu dữ liệu của prop
    required: true // Bắt buộc phải truyền giá trị cho prop
  }
});

const searchInput = ref('');
const currentStep = ref(1);
const isOpenInput = ref(false);
const indexCur = ref(1);
const listTags = ref<Array<Tags>>([]);
const isCrop = ref(false);
const isLoading = ref(false);

const nextStep = () => {
  currentStep.value = 2;
};

const prevStep = () => {
  currentStep.value = 1;
};

//Step 1
const activityTitle = ref(props.selectedActivity?.title);
const mainFile = ref(props.selectedActivity?.img);
const fileData = ref();
const avatarFile = ref();
const selectedTags = ref<Tags[]>(props.selectedActivity?.tags);
const listIdTags = ref<string[]>(
  props.selectedActivity?.tags.map((item: Tags) => {
    return item.id;
  })
);
const valueSummary = ref<string | undefined>('');
const summaryInput = ref<TextAreaValue>({
  level: {
    content: props.selectedActivity.summary
  }
});

//Handle add step 1
const updateSummary = (content: TextAreaValue) => {
  summaryInput.value.level.content = content.level.content;
  valueSummary.value = content.level.content;
};

watch(tagsResponsive, () => {
  listTags.value = tagsResponsive?.value?.data;
  // console.log(listTags.value);
});

const filteredTags = computed(() => {
  const searchTerm = searchInput.value.toLowerCase();
  return listTags.value.filter((item) => item.name.toLowerCase().includes(searchTerm));
});

const resetMainIMG = () => {
  mainFile.value = '';
};

const base64ToBlob = (base64Data: string) => {
  const byteString = atob(base64Data.split(',')[1]);
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }
  return new Blob([ab], { type: 'image/png' });
};

const handleCroppedImage = (result: string) => {
  if (result) {
    mainFile.value = result;
    fileData.value = base64ToBlob(result);
    avatarFile.value = new File([fileData.value], 'image.png', { type: 'image/png' });
  }
};

//Step 2

const valueDescription = ref<string | undefined>('');
const descriptionInput = ref<TextAreaValue>({
  level: {
    content: props.selectedActivity?.detail
  }
});

//Handle add step 2
const updateDescription = (content: TextAreaValue) => {
  descriptionInput.value.level.content = content.level.content;
  valueDescription.value = content.level.content;
};

//Upload Image TinyMCE
interface BlobInfo {
  blob: () => Blob;
  filename: () => string;
}
// eslint-disable-next-line no-unused-vars
type ProgressFunction = (percentage: number) => void;

const example_image_upload_handler = (blobInfo: BlobInfo, progress: ProgressFunction) =>
  new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = false;
    xhr.open('POST', import.meta.env.VITE_API_ENDPOINT + '/fileUpload');
    const getInforAdmin = localStorage.getItem('infor_admin');
    if (getInforAdmin) {
      const inforAdmin = JSON.parse(getInforAdmin);
      xhr.setRequestHeader('Authorization', 'Bearer ' + inforAdmin.token);
    }
    xhr.upload.onprogress = (e) => {
      progress((e.loaded / e.total) * 100);
    };

    xhr.onload = () => {
      if (xhr.status === 403) {
        reject({ message: 'HTTP Error: ' + xhr.status, remove: true });
        return;
      }

      if (xhr.status < 200 || xhr.status >= 300) {
        reject('HTTP Error: ' + xhr.status);
        return;
      }

      const json = ref<DataResponse>(JSON.parse(xhr.responseText));

      resolve(json.value.data);
    };

    xhr.onerror = () => {
      reject('Image upload failed due to a XHR Transport error. Code: ' + xhr.status);
    };

    const formData = new FormData();
    formData.append('file', blobInfo.blob());

    xhr.send(formData);
  });

//Open file image
const openFileInput = () => {
  console.log('jdsjbjsabdasd');
  isOpenInput.value = !isOpenInput.value;
};

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update', newsAdd: dataUpdate): void;
}>();

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

//Handle change image in step 2
const selectedTag = (tag: Tags) => {
  if (!selectedTags.value.includes(tag)) {
    selectedTags.value.push(tag);
    listIdTags.value.push(tag.id);
  }
};

const removeFromSelected = (index: number) => {
  selectedTags.value.splice(index, 1);
};

const isTagSelected = (tag: Tags) => {
  return selectedTags.value.some((selectedTag) => selectedTag.name === tag.name);
};

//Tạo form submit
const submitForm = () => {
  if (activityTitle.value.length <= 7) {
    activityTitle.value = '';
    alertDialog('Tên không hợp lệ', 1);
    return;
  }

  if (summaryInput.value.level.content.length <= 5) {
    alertDialog('Tóm tắt quá ngắn', 1);
    return;
  }
  if (!mainFile.value) {
    alertDialog('Bạn chưa chọn ảnh bìa', 1);
    return;
  }
  if (listIdTags.value.length === 0) {
    alertDialog('Bạn chưa chọn tags', 1);
    return;
  }
  if (descriptionInput.value.level.content.length <= 5) {
    alertDialog('Mô tả quá ngắn', 2);
    return;
  } else {
    isLoading.value = true;

    const object = {
      id: props.selectedActivity?.id,
      title: activityTitle.value, //step 1
      summary: summaryInput.value.level.content, //step 1
      img: props.selectedActivity?.img,
      slug: props.selectedActivity?.slug,
      createAt: props.selectedActivity?.createAt,
      detail: descriptionInput.value.level.content, //step 2
      highlight: 0,
      detailMobile: '',
      tags: props.selectedActivity?.tags
    };
    const formData = new FormData();
    formData.append('img', avatarFile.value as Blob); // step 1
    listIdTags.value.map((item) => {
      formData.append('tags', item);
    });

    formData.append('data', JSON.stringify(object));
    const updateNews = useAxios<DataResponse>(
      'patch',
      `/news/${props.selectedActivity?.id}`,
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      },
      deps.value
    );
    watch(updateNews.response, () => {
      if (updateNews.response.value?.status === 'ok') {
        isLoading.value = false;

        Swal.fire({
          title: 'Cập nhật thành công',
          icon: 'success',
          confirmButtonText: 'Hoàn tất',
          width: '30rem'
        }).then((result) => {
          if (result.isConfirmed) {
            Swal.close();
            emits('close');
            props.closeModal();
            emits('update', updateNews.response.value?.data);
          }
        });
      }
    });

    watch(updateNews.error, () => {
      const errorValue: MyErrorResponse | null = updateNews.error.value as MyErrorResponse | null;
      if (errorValue !== null) {
        Swal.fire({
          title: 'Tên hoạt động đã tồn tại',
          icon: 'error',
          confirmButtonText: 'Đóng',
          width: '50rem',
          padding: '0 2rem 2rem 2rem'
        });
      }
    });
  }
};
</script>
<template>
  <div :class="$style.activity_container">
    <div :class="$style['activity_container--title']">
      <p>CẬP NHẬT HOẠT ĐỘNG</p>
      <font-awesome-icon @click="closeModal" :icon="faXmark" :class="$style.activity_cancel" />
    </div>
    <!-- Step1 -->
    <div v-if="currentStep === 1" :class="$style.wrap_step1">
      <div :class="$style['wrap_step1--wrap']">
        <div :class="$style['wrap_step1--wrap-left']">
          <div :class="$style.wrap_title">
            <label :class="$style['wrap_title--text']" for="activity-title"
              >Tiêu đề hoạt động</label
            >
            <input
              id="activity-title"
              v-model="activityTitle"
              placeholder="Nhập tiêu đề hoạt động"
              :class="$style['custom-input']"
            />
          </div>
          <div :class="$style.wrap_sumary">
            <label :class="$style['wrap_title--text']" for="activity-summary"
              >Nội dung tóm tắt</label
            >
            <editor
              id="uuid11"
              allowedEvents="onChange"
              :onchange="updateSummary"
              api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
              :initial-value="summaryInput.level.content"
              :value="summaryInput.level.content"
              :init="{
                selector: 'textarea#uuid11',
                placeholder: 'Nhập tóm tắt',
                height: 270,
                menubar: false,
                plugins: 'advlist lists link image fullscreen',
                toolbar:
                  'undo italic bold | \
                alignleft aligncenter alignright alignjustify | \
                | forecolor bullist fullscreen |'
              }"
            />
          </div>

          <div :class="$style.activity_image">
            <p>Cập nhật ảnh bìa</p>
            <div v-if="!mainFile" :class="$style['adding__modal-upload']" @click="openFileInput">
              <font-awesome-icon :icon="faDownload" :class="$style['adding__modal-upload-ic']" />
              <span>Chọn file</span>
            </div>
            <template v-if="mainFile">
              <div :class="$style['adding__item-ctn']">
                <img v-if="mainFile" :src="mainFile" alt="SP" :class="$style['item_img']" />
                <span>Main_photo.png</span>
                <button @click="resetMainIMG">
                  <font-awesome-icon :icon="faXmark" :class="$style['adding__item-ic']" />
                </button>
              </div>
              <div :class="$style['adding__item-button']">
                <button @click="openFileInput">CHỈNH SỬA</button>
              </div>
            </template>
          </div>
        </div>
        <div :class="$style['wrap_step1--wrap-right']">
          <div :class="$style.tag_choose">
            <div :class="$style['modal__search']">
              <font-awesome-icon :icon="faMagnifyingGlass" :class="$style['home__category-ic']" />
              <input
                type="text"
                v-model="searchInput"
                :class="$style['modal__search-input']"
                placeholder="Tìm kiếm tags"
              />
            </div>
            <div :class="$style.container_tags">
              <div :class="$style.wrap_tags_title">
                <p :class="$style.wrap_tags_left">Tags</p>
                <p :class="$style.wrap_tags_right">Các tags đã chọn</p>
              </div>
              <div :class="$style.wrap_tags_ctn">
                <div :class="$style.wrap_tags_ctn_left" style="overflow-y: auto">
                  <div
                    :class="[
                      $style['tag__left-item'],
                      { [$style['selected-tag']]: isTagSelected(item) }
                    ]"
                    v-for="(item, index) in filteredTags"
                    :title="item.name"
                    :key="index"
                    style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden"
                  >
                    {{ item.name }}
                    <font-awesome-icon
                      :icon="faPlus"
                      :class="$style['tag__modal-st']"
                      @click="selectedTag(item)"
                    />
                  </div>
                </div>
                <div :class="$style.wrap_tags_ctn_right" style="overflow-y: auto">
                  <div
                    :class="$style['tag__right-item']"
                    v-for="(item, index) in selectedTags"
                    :key="index"
                    :title="item.name"
                    style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden"
                  >
                    {{ item.name }}
                    <font-awesome-icon
                      :icon="faMinus"
                      :class="$style['tag__modal-st']"
                      @click="removeFromSelected(index)"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div :class="$style['wrap_step1--btn']">
        <button @click="nextStep" :class="$style.button">Qua bước 2</button>
      </div>
    </div>

    <!-- Step 2 -->
    <div v-else-if="currentStep === 2" :class="$style.wrap_step2">
      <div :class="$style.wrap_step2_editer">
        <editor
          allowedEvents="onChange"
          :onchange="updateDescription"
          api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
          :initial-value="descriptionInput.level.content"
          :value="descriptionInput.level.content"
          :init="{
            selector: 'textarea',
            placeholder: 'Nhập mô tả chi tiết',
            height: 'calc(100vh - 200px)',
            menubar: false,
            image_title: true,
            automatic_uploads: true,

            plugins: 'advlist lists link image fullscreen',
            toolbar:
              'undo italic bold image | \
                alignleft aligncenter alignright alignjustify | \
                | forecolor bullist fullscreen |',
            images_file_types: 'jpg,svg,webp,png',
            file_picker_types: 'file image media',
            resize_img_proportional: true,
            block_unsupported_drop: false,
            images_upload_handler: example_image_upload_handler,
            color_map: [
              '000000',
              'Black',
              '808080',
              'Gray',
              'FFFFFF',
              'White',
              'FF0000',
              'Red',
              'FFFF00',
              'Yellow',
              '008000',
              'Green',
              '0000FF',
              'Blue'
            ]
          }"
        />
      </div>
      <div :class="$style.wrap_step2_btn">
        <button :class="$style.wrap_step2_btn_back" @click="prevStep">Quay lại</button>
        <button :class="$style.wrap_step2_btn_done" @click="submitForm">Cập nhật</button>
      </div>
    </div>
    <div v-show="isLoading" :class="$style.loading__overlay">
      <Loading />
    </div>
  </div>
  <crop-image
    :heightCrop="296"
    :widthCrop="367"
    :heightWrap="296"
    :widthWrap="367"
    :check="isOpenInput"
    v-show="isCrop"
    @close="isCrop = false"
    @open="isCrop = true"
    @crop="handleCroppedImage"
  />
</template>

<style module scoped lang="scss">
@import './ActivityModel.module.scss';
</style>

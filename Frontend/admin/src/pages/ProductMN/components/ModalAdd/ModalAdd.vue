<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faDownload } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import CropImage from '@/components/CropImage/CropImage.vue';
import Editor from '@tinymce/tinymce-vue';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface MyErrorResponse {
  response?: {
    data?: {
      message?: string;
    };
  };
}

interface ProductItem {
  id: string;
  name: string;
  slug: string;
  price: number;
  priceSale: number;
  summary: string;
  description: string;
  mainImg: string;
  imgs: string;
  highlight: number;
  createAt: string;
  fkCategory: {
    id: string;
    companyId: {
      id: string;
      name: string;
      logo: string;
      description: string;
      highlight: number;
      slug: string;
      createAt: string;
      outstandingProductId: string;
    };
    cate1Id: {
      id: string;
      title: string;
      img: string;
      highlight: 3;
      slug: string;
      createAt: string;
    };
    cate2Id: {
      id: string;
      title: string;
      slug: string;
      createAt: string;
    };
  };
}

interface DataCompany {
  id: string;
  name: string;
  logo: string;
  description: string;
  highlight: number;
  slug: string;
  createAt: string;
  outstandingProductId: string;
}

interface ItemCategory {
  id: string;
  companyId: {
    id: string;
    name: string;
    logo: string;
    description: string;
    highlight: number;
    slug: string;
    createAt: string;
    outstandingProductId: string;
  };
  cate1Id: {
    id: string;
    title: string;
    img: string;
    highlight: number;
    slug: string;
    createAt: string;
  };
  cate2Id: {
    id: string;
    title: string;
    slug: string;
    createAt: string;
  };
}

interface Company {
  name: string;
  idComp: string;
}

interface Category1 {
  id: string;
  title: string;
}

//Validate TinyMCE
interface TextAreaValue {
  level: {
    content: string;
  };
}

//GET COMPANIES
const deps = ref([]);
const companies = ref<Company[]>([]);
const getCompanies = useAxios<DataResponse>('get', '/company', {}, {}, deps.value);

watch(getCompanies.response, () => {
  companies.value = getCompanies.response.value?.data.map((item: DataCompany) => {
    return {
      name: item.name,
      idComp: item.id
    };
  });
});

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (e: 'update-content', data: { productAdd: ProductItem }): void;
}>();

//Properties
const indexCur = ref(1);

//Part 1
const name = ref('');
const price = ref();
const updateName = (e: Event) => {
  const target = e.target as HTMLInputElement;
  name.value = target.value;
};
const updatePrice = (e: Event) => {
  const target = e.target as HTMLInputElement;
  price.value = target.value;
};

//Companies value
const compID = ref('');
const selectedCompany = computed(() => {
  return companies.value.find((company) => company.idComp === compID.value);
});

//Categories1 value
const selectedCate1 = ref('');
const cate1array = ref<Category1[]>([]);
const cate1ID = ref();

//Categories2 value
const selectedCate2 = ref('');
const cate2array = ref<Category1[]>([]);
const cate2ID = ref();

//Part 2
const valueSummary = ref<string | undefined>('');
const summaryInput = ref<TextAreaValue>({
  level: {
    content: ''
  }
});
const updateSummary = (content: TextAreaValue) => {
  summaryInput.value.level.content = content.level.content;
  valueSummary.value = content.level.content;
};

const valueDescription = ref<string | undefined>('');
const descriptionInput = ref<TextAreaValue>({
  level: {
    content: ''
  }
});
const updateDescription = (content: TextAreaValue) => {
  descriptionInput.value.level.content = content.level.content;
  valueDescription.value = content.level.content;
};

//Part 3
const mainFile = ref();
const isCrop = ref(false);
const isOpenInput = ref(false);
const fileData = ref();
const avatarFile = ref();
const flag = ref(1); //Change status

//Open file image
const openFileInput = () => {
  flag.value = 1;
  isOpenInput.value = !isOpenInput.value;
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

//Function crop Image
const handleCroppedImage = (result: string) => {
  if (result) {
    mainFile.value = result;

    fileData.value = base64ToBlob(result);
    avatarFile.value = new File([fileData.value], 'image.png', { type: 'image/png' });
  }
};

//Calculate List Sub Images
const listHeight = computed(() => {
  if (mainFile.value) {
    return '120px';
  } else {
    return '160px';
  }
});

//Reset main image to null
const resetMainIMG = () => {
  mainFile.value = '';
};

//SUB Images
const subFile = ref();
const subfileData = ref();
const imgsFile = ref();
const subImageSrc = ref<string[]>([]);
const subImagesFile = ref<File[]>([]);

const openFileInput2 = () => {
  flag.value = 2;
  isOpenInput.value = !isOpenInput.value;
};

const deleteImage = (index: number) => {
  subImagesFile.value.splice(index, 1);
  subImageSrc.value.splice(index, 1);
};

//Crop for sub images
const handleCroppedSubImage = (result: string) => {
  if (result) {
    subFile.value = result;
    subImageSrc.value.push(result);
    subfileData.value = base64ToBlob(result);
    imgsFile.value = new File([subfileData.value], 'image.png', { type: 'image/png' });
    subImagesFile.value.push(imgsFile.value);
  }
};

//Switch role crop Image
const finalCrop = (result: string) => {
  if (flag.value === 1) {
    handleCroppedImage(result);
  } else if (flag.value === 2) {
    handleCroppedSubImage(result);
  }
};

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

//Get categories 2 and updateIDCate1
const handleSelectedCate1Change = (event: Event) => {
  cate1ID.value = (event.target as HTMLSelectElement).value;

  const getCategories2 = useAxios<DataResponse>(
    'get',
    `/cate?cate1Id=${cate1ID.value}&companyId=${compID.value}`,
    {},
    {},
    deps.value
  );

  watch(getCategories2.response, () => {
    cate2array.value = getCategories2.response.value?.data.map((item: ItemCategory) => {
      return {
        id: item.cate2Id.id,
        title: item.cate2Id.title
      };
    });
  });
};

//updateIDCate2
const handleSelectedCate2Change = (event: Event) => {
  cate2ID.value = (event.target as HTMLSelectElement).value;
};

const submitForm = () => {
  if (name.value.length <= 7) {
    name.value = '';
    alertDialog('Tên không hợp lệ', 1);
    return;
  }
  if (price.value <= 1000) {
    price.value = '';
    alertDialog('Giá không hợp lệ', 1);
    return;
  }
  if (compID.value === '') {
    alertDialog('Bạn chưa chọn công ty', 1);
    return;
  }
  if (selectedCate1.value === '') {
    alertDialog('Bạn chưa chọn danh mục 1', 1);
    return;
  }
  if (selectedCate2.value === '') {
    alertDialog('Bạn chưa chọn danh mục 2', 1);
    return;
  }
  if (summaryInput.value.level.content.length <= 5) {
    alertDialog('Tóm tắt quá ngắn', 2);
    return;
  }
  if (descriptionInput.value.level.content.length <= 5) {
    alertDialog('Mô tả quá ngắn', 2);
    return;
  }
  if (!mainFile.value) {
    alertDialog('Bạn chưa chọn Avatar', 3);
    return;
  }
  if (subImageSrc.value.length === 0) {
    alertDialog('Bạn chưa thêm ảnh khác', 3);
    return;
  }

  const getFKKey = useAxios<DataResponse>(
    'get',
    // eslint-disable-next-line max-len
    `/cate?cate1Id=${cate1ID.value}&cate2Id=${cate2ID.value}&companyId=${compID.value}`,
    {},
    {},
    deps.value
  );

  watch(getFKKey.response, () => {
    const fkCategory = getFKKey.response.value?.data[0].id;
    const object = {
      name: name.value,
      price: price.value,
      priceSale: 0,
      summary: summaryInput.value.level.content,
      description: descriptionInput.value.level.content,
      fkCategory: fkCategory
    };

    const formData = new FormData();
    formData.append('mainImg', avatarFile.value as Blob);
    subImagesFile.value.forEach((file) => {
      formData.append('imgs', file as Blob);
    });
    formData.append('data', JSON.stringify(object));

    const createProduct = useAxios<DataResponse>(
      'post',
      '/products',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      },
      deps.value
    );

    watch(createProduct.response, () => {
      if (createProduct.response.value?.status === 'ok') {
        Swal.fire({
          title: 'Thêm thành công',
          icon: 'success',
          confirmButtonText: 'Hoàn tất',
          width: '30rem'
        }).then((result) => {
          if (result.isConfirmed) {
            Swal.close();
            emits('update-content', {
              productAdd: createProduct.response.value?.data
            });
            emits('close');
          }
        });
      }
    });

    watch(createProduct.error, () => {
      const errorValue: MyErrorResponse | null = createProduct.error
        .value as MyErrorResponse | null;
      if (errorValue !== null) {
        if (errorValue?.response?.data?.message === 'Product name already taken') {
          alertDialog('Tên sản phẩm đã tồn tại', 1);
          return;
        }
      }
    });
  });
};

watch(selectedCompany, () => {
  const getCategories1 = useAxios<DataResponse>(
    'get',
    `/cate?companyId=${compID.value}`,
    {},
    {},
    deps.value
  );

  watch(getCategories1.response, () => {
    const responseData = getCategories1.response.value?.data;

    if (responseData) {
      const uniqueCate1Ids = new Set<string>(
        responseData.map((item: ItemCategory) => item.cate1Id.id)
      );

      cate1array.value = Array.from(uniqueCate1Ids).map((cate1Id: string) => {
        const category = responseData.find((item: ItemCategory) => item.cate1Id.id === cate1Id);
        return {
          id: cate1Id,
          title: category ? category.cate1Id.title : ''
        };
      });
    }
  });
});
</script>

<template>
  <div :class="$style.adding__overlay">
    <div :class="$style.adding__modal">
      <div :class="$style.adding__modal__heading">
        THÊM SẢN PHẨM
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['adding__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.adding__modal__body">
        <template v-if="indexCur === 1">
          <h4>Tên sản phẩm</h4>
          <input
            type="text"
            placeholder="Nhập vào tên sản phẩm"
            v-model="name"
            @input="updateName"
          />
          <h4>Giá sản phẩm</h4>
          <input
            type="number"
            placeholder="Nhập vào giá sản phẩm"
            v-model="price"
            @input="updatePrice"
          />
          <h4>Tên công ty</h4>
          <select v-model="compID">
            <option disabled value="">Chọn công ty</option>
            <option v-for="(item, index) in companies" :key="index" :value="item.idComp">
              {{ item.name }}
            </option>
          </select>
          <h4>Danh mục 1</h4>
          <select v-model="selectedCate1" @change="handleSelectedCate1Change">
            <option disabled value="">Chọn danh mục</option>
            <option v-for="(item, index) in cate1array" :key="index" :value="item.id">
              {{ item.title }}
            </option>
          </select>
          <h4>Danh mục 2</h4>
          <select v-model="selectedCate2" @change="handleSelectedCate2Change">
            <option disabled value="">Chọn danh mục</option>
            <option v-for="(item, index) in cate2array" :key="index" :value="item.id">
              {{ item.title }}
            </option>
          </select>
        </template>
        <template v-if="indexCur === 2">
          <h4>Tóm tắt</h4>
          <editor
            id="uuid1"
            allowedEvents="onChange"
            :onchange="updateSummary"
            api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
            :initial-value="summaryInput.level.content"
            :value="summaryInput.level.content"
            :init="{
              selector: 'textarea#uuid1',
              placeholder: 'Nhập tóm tắt',
              height: 180,
              menubar: false,
              plugins: 'advlist lists link image fullscreen',
              toolbar:
                'undo italic bold | \
                alignleft aligncenter alignright alignjustify | \
                | forecolor bullist fullscreen |'
            }"
          />
          <h4>Mô tả chi tiết</h4>
          <editor
            id="uuid2"
            allowedEvents="onChange"
            :onchange="updateDescription"
            api-key="y70bvcufdhcs3t72wuylxllnf0jyum0u7nf31vzvgvdliy26"
            :initial-value="descriptionInput.level.content"
            :value="descriptionInput.level.content"
            :init="{
              selector: 'textarea#uuid2',
              placeholder: 'Nhập mô tả chi tiết',
              height: 220,
              menubar: false,
              plugins: 'advlist lists link image fullscreen',
              toolbar:
                'undo italic bold | \
                alignleft aligncenter alignright alignjustify | \
                | forecolor bullist fullscreen |'
            }"
          />
        </template>
        <template v-if="indexCur === 3">
          <h4>Hình ảnh đại diện</h4>
          <div v-if="!mainFile" :class="$style['adding__modal-upload']" @click="openFileInput">
            <font-awesome-icon :icon="faDownload" :class="$style['adding__modal-upload-ic']" />
            <span>Click để chọn file ảnh</span>
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
          <h4>Hình ảnh thêm</h4>
          <div
            v-if="subImagesFile.length === 0"
            :class="$style['adding__modal-upload']"
            @click="openFileInput2"
          >
            <font-awesome-icon :icon="faDownload" :class="$style['adding__modal-upload-ic']" />
            <span>Click để chọn file ảnh</span>
          </div>
          <template v-if="subImageSrc.length > 0">
            <div :class="$style['adding__img-list']" :style="{ maxHeight: listHeight }">
              <div
                :class="$style['adding__item-ctn']"
                v-for="(item, index) in subImageSrc"
                :key="index"
              >
                <img :src="item" alt="SP" :class="$style['item_img']" />
                <span>Sub_photo_{{ index + 1 }}.png</span>
                <button @click="deleteImage(index)">
                  <font-awesome-icon :icon="faXmark" :class="$style['adding__item-ic']" />
                </button>
              </div>
            </div>
          </template>
          <div v-if="subImagesFile.length > 0" :class="$style['adding__item-button']">
            <button @click="openFileInput2">THÊM ẢNH</button>
          </div>
        </template>
      </div>
      <div :class="$style.adding__modal__line">
        <div
          :class="$style['modal__line-item']"
          v-for="index in 3"
          :key="index"
          :style="{ backgroundColor: index === indexCur ? '#0088CC' : '' }"
        ></div>
      </div>
      <div :class="$style.adding__modal__buttons">
        <button v-if="indexCur === 1" @click="$emit('close')">Hủy</button>
        <button v-if="indexCur > 1" @click="indexCur--">Quay lại</button>
        <button v-if="indexCur < 3" @click="indexCur++">Tiếp tục</button>
        <button v-if="indexCur === 3" @click="submitForm">Thêm</button>
      </div>
    </div>
  </div>

  <crop-image
    :heightCrop="350"
    :widthCrop="350"
    :heightWrap="350"
    :widthWrap="350"
    :check="isOpenInput"
    v-show="isCrop"
    @close="isCrop = false"
    @open="isCrop = true"
    @crop="finalCrop"
  />
</template>

<style module scoped lang="scss">
@import './ModalAdd.module.scss';
</style>

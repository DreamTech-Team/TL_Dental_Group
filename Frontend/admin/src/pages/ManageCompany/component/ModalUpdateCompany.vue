<script setup lang="ts">
import { PropType, Ref, ref, watch } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark, faRotate, faPencil } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import styles from './ModalUpdateCompany.module.scss';
import ModalAddProduct from './ModalAddProduct.vue';
import base64ToBlob from '@/utils/base64ToBlob';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface ManageCompany {
  id: string;
  name: string;
  logo: string;
  description: string;
  highlight: number;
  slug: string;
  createAt: string;
  outstandingProductId: string;
}

interface Products {
  id: string;
  name: string;
  description: string;
  mainImg: string;
  fkCategory: {
    companyId: {
      id: string;
    };
  };
}

const context = defineProps({
  item: {
    type: Object,
    required: true
  },
  productOutstanding: {
    type: Object,
    required: true
  },
  products: {
    type: Array,
    required: true
  },
  idCompany: {
    type: String,
    required: true
  },
  change: {
    type: Function as PropType<(newData: ManageCompany, isLoading: boolean) => void>,
    required: true
  },
  changeOutstanding: {
    type: Function as PropType<(newData: ManageCompany) => void>,
    required: true
  }
});

const emit = defineEmits(['close']);

const variableChange = ref([]);
const nameCompanyInput = ref(context.item.name);
const descriptionInput = ref(context.item.description);
const productInput = ref(context.productOutstanding.name);
const productOutstand = ref({ ...context.productOutstanding });
const selectedlogo: Ref<string | null> = ref(context.item.logo);
const productCompany = ref<Products[]>(
  (context.products as Products[]).filter((item) => {
    return item.fkCategory.companyId.id === context.idCompany;
  })
);
const isOpen = ref(false);
const isChange = ref(false);
const isPatchProduct = ref(false);
const idProduct = ref('');

// Các hàm update dữ liệu cho thẻ input
const updateTitle = (e: Event) => {
  const target = e.target as HTMLInputElement;
  nameCompanyInput.value = target.value;
};
const updateContent = (e: Event) => {
  const target = e.target as HTMLInputElement;
  descriptionInput.value = target.value;
};
const updateProduct = (e: Event) => {
  const target = e.target as HTMLInputElement;
  productInput.value = target.value;
};

// Hàm submit dữ liệu, đẩy dữ liệu lên database
const submitForm = () => {
  if (
    nameCompanyInput.value.length < 4 ||
    descriptionInput.value.length < 4 ||
    !selectedlogo.value
  ) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '50rem',
      padding: '0 2rem 2rem 2rem',
      customClass: {
        confirmButton: styles['confirm-button'],
        cancelButton: styles['cancel-button'],
        title: styles['title']
      }
    });
  } else {
    if (isChange.value && selectedlogo.value) {
      // Tạo một đối tượng File từ dữ liệu base64
      const fileData = base64ToBlob.covertBase64ToBlob(selectedlogo.value);
      const image = new File([fileData], 'image.png', { type: 'image/png' });

      const deps = ref([]);

      const object = {
        id: context.item.id,
        name: nameCompanyInput.value,
        description: descriptionInput.value,
        highlight: context.item.highlight,
        outstandingProductId: context.item.outstandingProductId,
        slug: context.item.slug
      };

      const formData = new FormData();
      formData.append('logo', image as Blob);
      formData.append('data', JSON.stringify(object));
      const { response, isLoading } = useAxios<DataResponse>(
        'patch',
        '/company/' + context.item.id,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        },
        deps.value
      );
      context.change({} as ManageCompany, isLoading.value);

      watch(response, () => {
        if (response) {
          if (response.value?.status === 'ok') {
            context.change(response.value?.data, isLoading.value);
          }
        }
      });
    } else {
      const deps = ref([]);

      const object = {
        id: context.item.id,
        name: nameCompanyInput.value,
        description: descriptionInput.value,
        highlight: context.item.highlight,
        outstandingProductId: context.item.outstandingProductId,
        slug: context.item.slug
      };

      const formData = new FormData();
      formData.append('logo', context.item.logo);
      formData.append('data', JSON.stringify(object));
      const { response, isLoading } = useAxios<DataResponse>(
        'patch',
        '/company/' + context.item.id,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        },
        deps.value
      );
      context.change({} as ManageCompany, isLoading.value);

      watch(response, () => {
        if (response) {
          if (response.value?.status === 'ok') {
            context.change(response.value?.data, isLoading.value);
          }
        }
      });
    }

    if (isPatchProduct.value) {
      // Gọi hàm useAxios để lấy response, error, và isLoading
      const formData = new FormData();
      formData.append('idProduct', idProduct.value);
      const { response } = useAxios<DataResponse>(
        'patch',
        '/company/outstanding/' + context.idCompany,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        },
        variableChange.value
      );

      watch(response, () => {
        context.changeOutstanding(response.value?.data);
      });
    }
  }
};

// Hàm chuyển đổi từ ArrayBuffer sang string
const arrayBufferToString = (buffer: ArrayBuffer) => {
  const uintArray = new Uint16Array(buffer);
  const charArray: string[] = [];
  for (let i = 0; i < uintArray.length; i++) {
    charArray.push(String.fromCharCode(uintArray[i]));
  }
  return charArray.join('');
};

// Hàm lấy ảnh từ máy và lưu vào biến selectedImage
const handleFileInputChange = (event: Event) => {
  const target = event.target as HTMLInputElement;

  if (target.files) {
    const file = target.files[0];
    const reader = new FileReader();

    reader.onload = (e: ProgressEvent<FileReader>) => {
      const result = e.target?.result;
      if (result instanceof ArrayBuffer) {
        selectedlogo.value = arrayBufferToString(result);
      } else if (typeof result === 'string') {
        selectedlogo.value = result;
      }

      isChange.value = true;
    };

    reader.readAsDataURL(file);
  }
};

// Hàm kích hoạt thẻ input type file với id là input_file_modal
const handleChangelogo = () => {
  const inputElement = document.getElementById('input_file_modalupdate');
  if (inputElement) {
    inputElement.click();
  }
};

const handleDataProduct = (
  _idProduct: string,
  nameProduct: string,
  descriptionProduct: string,
  image: string
) => {
  isPatchProduct.value = true;

  productOutstand.value.name = nameProduct;
  productOutstand.value.description = descriptionProduct;
  productOutstand.value.mainImg = image;

  idProduct.value = _idProduct;
  productInput.value = nameProduct;
};
</script>

<template>
  <div :class="$style.mncompany__overlay" v-if="!isOpen">
    <div :class="$style.mncompany__modal">
      <div :class="$style.mncompany__modal__heading">
        THÊM CÔNG TY
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['mncompany__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.mncompany__modal__body">
        <h4>Tên công ty</h4>
        <input
          type="text"
          placeholder="Nhập tên công ty..."
          :value="nameCompanyInput"
          @input="updateTitle"
        />

        <h4>Mô tả công ty</h4>
        <textarea
          type="text"
          placeholder="Nhập mô tả..."
          :value="descriptionInput"
          @input="updateContent"
        />

        <h4>Logo</h4>
        <div :class="$style['wrapper1']">
          <div v-if="selectedlogo">
            <img :src="selectedlogo" />
          </div>

          <div :class="$style['about__mottomodal-button-wrapper']">
            <button :class="$style['about__mottomodal-button']" @click="handleChangelogo">
              <font-awesome-icon :icon="faRotate" :class="$style['about__mottomodal-button-ic']" />
              <span>Đổi ảnh</span>
            </button>

            <input
              type="file"
              style="display: none"
              id="input_file_modalupdate"
              accept="logo/*"
              @change="handleFileInputChange"
            />
          </div>
        </div>

        <h4>Sản phẩm nổi bật</h4>
        <div :class="$style['category']">
          <input
            type="text"
            placeholder="Đang trống"
            :value="productInput"
            @change="updateProduct"
            readonly
            :style="{ cursor: 'auto' }"
          />

          <button @click="isOpen = true">
            <font-awesome-icon :icon="faPencil" :class="$style['category-icon']" />
          </button>
        </div>

        <div :class="$style['modal__buttons']">
          <button @click="$emit('close')">Hủy</button>
          <button @click="submitForm">Cập nhật</button>
        </div>
      </div>
    </div>
  </div>

  <modal-add-product
    v-else
    @close="isOpen = false"
    @results="handleDataProduct"
    :product="productOutstand"
    :products="productCompany"
    :id-company="context.idCompany"
  />
</template>

<style module scoped lang="scss">
@import './ModalUpdateCompany.module.scss';
</style>

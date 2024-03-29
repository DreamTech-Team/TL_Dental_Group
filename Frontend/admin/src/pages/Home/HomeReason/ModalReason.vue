<script setup lang="ts">
import { ref, watch, type PropType } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import useAxios, { type DataResponse } from '@/hooks/useAxios';

interface ItemRS {
  id: string;
  image: string;
  title: string;
  description: string;
}

const content = defineProps({
  uuid: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: false
  },
  description: {
    type: String,
    required: false
  },
  listItem: {
    type: Array as PropType<ItemRS[]>,
    required: false
  },
  image: {
    type: String,
    required: false
  }
});

const emits = defineEmits<{
  // eslint-disable-next-line no-unused-vars
  (e: 'close'): void;
  // eslint-disable-next-line no-unused-vars
  (
    // eslint-disable-next-line no-unused-vars
    e: 'update-content',
    // eslint-disable-next-line no-unused-vars
    data: { title: string; description: string; image: string; listrs: ItemRS[] }
  ): void;
}>();

//Validate form
const titleInput = ref(content.title);
const titleSub = ref(content.description);
const name1 = ref(content.listItem?.[0]?.title);
const des1 = ref(content.listItem?.[0]?.description);
const name2 = ref(content.listItem?.[1]?.title);
const des2 = ref(content.listItem?.[1]?.description);
const name3 = ref(content.listItem?.[2]?.title);
const des3 = ref(content.listItem?.[2]?.description);

const updateTitle = (e: Event) => {
  const target = e.target as HTMLInputElement;
  titleInput.value = target.value;
};
const updateTitleSub = (e: Event) => {
  const target = e.target as HTMLInputElement;
  titleSub.value = target.value;
};
const updateName1 = (e: Event) => {
  const target = e.target as HTMLInputElement;
  name1.value = target.value;
};
const updateName2 = (e: Event) => {
  const target = e.target as HTMLInputElement;
  name2.value = target.value;
};
const updateName3 = (e: Event) => {
  const target = e.target as HTMLInputElement;
  name3.value = target.value;
};
const updateDes1 = (e: Event) => {
  const target = e.target as HTMLInputElement;
  des1.value = target.value;
};
const updateDes2 = (e: Event) => {
  const target = e.target as HTMLInputElement;
  des2.value = target.value;
};
const updateDes3 = (e: Event) => {
  const target = e.target as HTMLInputElement;
  des3.value = target.value;
};
const submitForm = () => {
  if (
    (titleInput.value?.length ?? 0) < 10 ||
    (titleSub.value?.length ?? 0) < 10 ||
    (name1.value?.length ?? 0) < 5 ||
    (name2.value?.length ?? 0) < 5 ||
    (name3.value?.length ?? 0) < 5 ||
    (des1.value?.length ?? 0) < 10 ||
    (des2.value?.length ?? 0) < 10 ||
    (des3.value?.length ?? 0) < 10
  ) {
    Swal.fire({
      title: 'Vui lòng điền đủ thông tin',
      icon: 'error',
      confirmButtonText: 'Đóng',
      width: '30rem'
    });
  } else {
    const deps = ref([]);

    const object = {
      heading: {
        id: content.uuid,
        title: titleInput.value,
        content: titleSub.value,
        image: content.image
      },
      subItem1: content.listItem?.[0]
        ? {
            id: content.listItem[0].id,
            title: name1.value,
            content: des1.value,
            image: content.listItem[0].image
          }
        : null,
      subItem2: content.listItem?.[1]
        ? {
            id: content.listItem[1].id,
            title: name2.value,
            content: des2.value,
            image: content.listItem[1].image
          }
        : null,
      subItem3: content.listItem?.[2]
        ? {
            id: content.listItem[2].id,
            title: name3.value,
            content: des3.value,
            image: content.listItem[2].image
          }
        : null
    };

    const tempObject: ItemRS[] = [
      {
        id: content.listItem?.[0]?.id ?? '',
        title: name1.value ?? '',
        description: des1.value ?? '',
        image: content.listItem?.[0]?.image ?? ''
      },
      {
        id: content.listItem?.[1]?.id ?? '',
        title: name2.value ?? '',
        description: des2.value ?? '',
        image: content.listItem?.[1]?.image ?? ''
      },
      {
        id: content.listItem?.[2]?.id ?? '',
        title: name3.value ?? '',
        description: des3.value ?? '',
        image: content.listItem?.[2]?.image ?? ''
      }
    ];

    const formData = new FormData();
    formData.append('data', JSON.stringify(object));
    const { response } = useAxios<DataResponse>(
      'patch',
      '/home/section1',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      },
      deps.value
    );

    watch(response, () => {
      if (response.value?.status === 'ok') {
        Swal.fire({
          title: 'Cập nhật thành công',
          icon: 'success',
          confirmButtonText: 'Hoàn tất',
          width: '30rem'
        }).then((result) => {
          if (result.isConfirmed) {
            Swal.close();
            emits('update-content', {
              title: titleInput.value || '',
              description: titleSub.value || '',
              image: content.image || '',
              listrs: tempObject.filter((item) => item !== null)
            });
            emits('close');
          }
        });
      }
    });
  }
};
</script>

<template>
  <div :class="$style.reason__overlay">
    <div :class="$style.reason__modal">
      <div :class="$style.reason__modal__heading">
        CẬP NHẬT NỘI DUNG
        <font-awesome-icon
          :icon="faXmark"
          :class="$style['reason__modal-ic']"
          @click="$emit('close')"
        />
      </div>
      <div :class="$style.reason__modal__body">
        <h4>Tiêu đề</h4>
        <input type="text" placeholder="Nhập tiêu đề" v-model="titleInput" @input="updateTitle" />
        <h4>Tiêu đề tiêu chí</h4>
        <input
          type="text"
          placeholder="Nhập tiêu đề tiêu chí"
          v-model="titleSub"
          @input="updateTitleSub"
        />
        <h4>Tiêu chí 1</h4>
        <input type="text" placeholder="Nhập tiêu đề 1" v-model="name1" @input="updateName1" />
        <h4>Mô tả chi tiết 1</h4>
        <textarea placeholder="Nhập mô tả" v-model="des1" @input="updateDes1" />
        <h4>Tiêu chí 2</h4>
        <input type="text" placeholder="Nhập tiêu đề 2" v-model="name2" @input="updateName2" />
        <h4>Mô tả chi tiết 2</h4>
        <textarea placeholder="Nhập mô tả" v-model="des2" @input="updateDes2" />
        <h4>Tiêu chí 3</h4>
        <input type="text" placeholder="Nhập tiêu đề 1" v-model="name3" @input="updateName3" />
        <h4>Mô tả chi tiết 3</h4>
        <textarea placeholder="Nhập mô tả" v-model="des3" @input="updateDes3" />
        <div :class="$style['modal__buttons']">
          <button @click="$emit('close')">Hủy</button>
          <button @click="submitForm">Cập nhật</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style module scoped lang="scss">
@import './ModalReason.module.scss';
</style>

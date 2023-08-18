import { ref, computed } from 'vue';
import { defineStore } from 'pinia';

export const useCounterStore = defineStore('counter', () => {
  const count = ref(0);
  const doubleCount = computed(() => count.value * 2);
  function increment() {
    count.value++;
  }

  return { count, doubleCount, increment };
});

interface AccountAdmin {
  id: string;
  email: string;
  fullName: string;
  phoneNumber: string;
  address: string;
  password: string;
  roles: string;
  token: string;
}

export const useInforAdminStore = defineStore('inforAdmin', () => {
  const inforAdmin = ref<AccountAdmin>({
    id: '',
    email: '',
    fullName: '',
    phoneNumber: '',
    address: '',
    password: '',
    roles: '',
    token: ''
  });

  const setInforAdmin = (newInfor: AccountAdmin) => {
    inforAdmin.value = newInfor;
  };

  return { inforAdmin, setInforAdmin };
});

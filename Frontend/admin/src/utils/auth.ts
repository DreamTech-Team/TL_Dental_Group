import { ref, type Ref } from 'vue';

interface UserInfo {
  name: string;
  gender: string;
  dob: string;
  email: string;
}

const auth = {
  login(data: { data: { user: UserInfo }; access_token: string }): void {
    window.localStorage.name = data.data.user.name;
    window.localStorage.email = data.data.user.email;
    window.localStorage.gender = data.data.user.gender;
    window.localStorage.dob = data.data.user.dob;
  },

  setAccessToken(token: string): void {
    window.localStorage.accessToken = token;
  },

  getAccessToken(): string | null {
    return window.localStorage.accessToken || null;
  },

  getID(): string | null {
    return window.localStorage._id || null;
  },

  getPhone(): string | null {
    return window.localStorage.phone || null;
  },

  role(): string | null {
    return window.localStorage.role || null;
  },

  getInfo(): Ref<UserInfo> {
    const name = ref(window.localStorage.name || '');
    const gender = ref(window.localStorage.gender || '');
    const dob = ref(window.localStorage.dob || '');
    const phone = ref(
      `${window.localStorage.phone?.substring(0, 2) || ''}*****${
        window.localStorage.phone?.substring(window.localStorage.phone?.length - 3) || ''
      }`
    );
    const email = ref(window.localStorage.email || '');

    return ref({
      name,
      gender,
      dob,
      phone,
      email
    });
  },

  getAvatar(): string {
    return !window.localStorage.avatar ||
      window.localStorage.avatar === 'undefined' ||
      window.localStorage.avatar === 'null'
      ? ''
      : window.localStorage.avatar;
  },

  updateAvatar(avt: string): void {
    window.localStorage.avatar = avt;
  },

  updateInfo(userInfo: UserInfo): void {
    window.localStorage.name = userInfo.name;
    window.localStorage.gender = userInfo.gender;
    window.localStorage.dob = userInfo.dob;
    window.localStorage.email = userInfo.email;
  },

  logout(): void {
    window.localStorage.clear();
  }
};

export default auth;

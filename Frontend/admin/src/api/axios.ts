import axios, {
  type AxiosInstance,
  type AxiosResponse,
  type InternalAxiosRequestConfig
} from 'axios';
import qs from 'qs';

const getInforAdmin = localStorage.getItem('infor_admin');
const handleSetHeaders = () => {
  if (getInforAdmin) {
    const inforAdmin = JSON.parse(getInforAdmin);

    return inforAdmin.token
      ? {
          'Content-Type': 'application/json',
          Authorization: 'Bearer ' + inforAdmin.token
        }
      : {
          'Content-Type': 'application/json'
        };
  }
};
const axiosClient: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_ENDPOINT,
  headers: handleSetHeaders(),
  paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'brackets' })
});

axiosClient.interceptors.request.use((config: InternalAxiosRequestConfig) => config);

axiosClient.interceptors.response.use(
  (response: AxiosResponse) => {
    if (response && response.data) {
      return response;
    }
    return response;
  },
  (error: AxiosResponse) => {
    if (error && error.data) {
      return Promise.reject(error);
    }
    return Promise.reject(error);
  }
);

const axiosPrivate: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_ENDPOINT,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true,
  paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'brackets' })
});

export { axiosClient, axiosPrivate };

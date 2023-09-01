import axios, {
  type AxiosInstance,
  type AxiosResponse,
  type InternalAxiosRequestConfig
} from 'axios';
import qs from 'qs';

const axiosClient: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_ENDPOINT,
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

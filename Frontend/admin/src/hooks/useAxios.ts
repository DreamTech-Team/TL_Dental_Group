import { ref, type Ref, watch } from 'vue';
import axios, { type AxiosRequestConfig, type AxiosResponse, AxiosError } from 'axios';
import { axiosClient } from '@/api/axios';

export interface DataResponse {
  status: string;
  message: string;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  data: any; // Bổ sung kiểu dữ liệu phù hợp cho data, ví dụ: any[] hoặc object[]
}

export interface UseAxiosResponse<DataResponse> {
  response: Ref<DataResponse | null>;
  error: Ref<AxiosError | null>;
  isLoading: Ref<boolean>;
}

const useAxios = <DataResponse>(
  method: 'get' | 'post' | 'put' | 'delete' | 'patch',
  api: string,
  body: object,
  options: AxiosRequestConfig,
  deps: unknown[]
): UseAxiosResponse<DataResponse> => {
  const isLoading: Ref<boolean> = ref(false);
  const response: Ref<DataResponse | null> = ref(null);
  const error = ref<AxiosError | null>(null);

  const axiosController = axios.CancelToken.source();

  // const handleSetHeaders = () => {
  //   const getInforAdmin = localStorage.getItem('infor_admin');
  //   if (getInforAdmin) {
  //     const inforAdmin = JSON.parse(getInforAdmin);

  //     if (Object.keys(options).length === 0) {
  //       console.log(api, inforAdmin.token);
  //       return inforAdmin.token
  //         ? {
  //             headers: {
  //               'Content-Type': 'application/json',
  //               Authorization: 'Bearer ' + inforAdmin.token
  //             }
  //           }
  //         : {
  //             headers: {
  //               'Content-Type': 'application/json'
  //             }
  //           };
  //     } else {
  //       if (options.headers) {
  //         options.headers.Authorization = 'Bearer ' + inforAdmin.token;
  //       }
  //       return options;
  //     }
  //   }
  // };

  const fetchData = async () => {
    if (!isLoading.value) {
      setTimeout(() => {
        isLoading.value = true;
      }, 0);
      try {
        const res: AxiosResponse<DataResponse> = await axiosClient[method](api, body, {
          ...options,
          cancelToken: axiosController.token
        });

        if (res && res.data) {
          response.value = res.data;
        }
      } catch (err: unknown) {
        if (err instanceof AxiosError) {
          error.value = err;
        }
      } finally {
        isLoading.value = false;
      }
    }
  };

  watch(
    deps,
    () => {
      fetchData();
    },
    { immediate: true }
  );

  return { response, error, isLoading };
};

export default useAxios;

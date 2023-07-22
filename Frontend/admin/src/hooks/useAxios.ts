import { ref, type Ref, watch } from 'vue';
import axios, { type AxiosRequestConfig, type AxiosResponse, AxiosError } from 'axios';
import { axiosClient } from '@/api/axios';

export interface DataResponse {
  status: string;
  message: string;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  data: any; // Bổ sung kiểu dữ liệu phù hợp cho data, ví dụ: any[] hoặc object[]
}

interface UseAxiosResponse<DataResponse> {
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

  const fetchData = async () => {
    if (!isLoading.value) {
      isLoading.value = true;
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
          console.log(err);
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

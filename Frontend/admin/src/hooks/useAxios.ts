import { ref, type Ref, watch } from 'vue';
import axios, { type AxiosRequestConfig, type AxiosResponse, AxiosError } from 'axios';
import { axiosClient } from '@/api/axios';

interface UseAxiosResponse<T> {
  response: Ref<T | null>;
  error: Ref<AxiosError | null>;
  isLoading: Ref<boolean>;
}

const useAxios = <T>(
  method: 'get' | 'post' | 'put' | 'delete' | 'patch',
  api: string,
  body: object,
  options: AxiosRequestConfig,
  deps: unknown[]
): UseAxiosResponse<T> => {
  const isLoading: Ref<boolean> = ref(false);
  const response: Ref<T | null> = ref(null);
  const error = ref<AxiosError | null>(null);

  const axiosController = axios.CancelToken.source();

  const fetchData = async () => {
    if (!isLoading.value) {
      isLoading.value = true;
      try {
        const res: AxiosResponse<T> = await axiosClient[method](api, body, {
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

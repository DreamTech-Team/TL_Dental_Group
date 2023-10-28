/* eslint-disable @typescript-eslint/no-explicit-any */
import { ref, type Ref, watch } from 'vue';
import axios, { type AxiosRequestConfig, type AxiosResponse, AxiosError } from 'axios';
import { axiosClient } from '@/api/axios';
import { getCache, setCache } from '@/utils/sessionStorage';

export interface DataResponse {
  status: string;
  message: string;
  data: any;
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
      setTimeout(() => {
        isLoading.value = true;
      }, 0);
      try {
        // Check Cache
        const cachedData = getCache(api);
        if (
          cachedData !== null &&
          new Date().getTime() - cachedData.cacheTimestamp < 5 * 60 * 1000
        ) {
          setTimeout(() => {
            response.value = cachedData.data;
            isLoading.value = false;
          }, 0);
          return;
        }

        const res: AxiosResponse<DataResponse> = await axiosClient[method](api, body, {
          ...options,
          cancelToken: axiosController.token
        });

        if (res && res.data) {
          response.value = res.data;
          // Save to Cache
          setCache(api, res.data);
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

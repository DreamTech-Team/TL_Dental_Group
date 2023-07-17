import { ref, type Ref, onBeforeUnmount } from 'vue';
import { axiosPrivate } from '@/api/axios';
import auth from '@/utils/auth';
import useRefreshToken from './useRefreshToken';

const useAxiosPrivate = (): Ref<unknown> => {
  const refresh = useRefreshToken();
  const axiosInstance = ref(axiosPrivate);

  const requestIntercept = axiosInstance.value.interceptors.request.use((config) => {
    if (!config.headers.authorization) {
      config.headers.authorization = `Bearer ${auth.getAccessToken()}`;
    }
    return config;
  });

  const responseIntercept = axiosInstance.value.interceptors.response.use(
    (response) => response,
    async (error) => {
      const prevRequest = error.config;
      if (error.response.status === 403) {
        const newAccessToken = await refresh();
        prevRequest.headers.authorization = `Bearer ${newAccessToken}`;
        return axiosInstance.value(prevRequest);
      }
      return Promise.reject(error);
    }
  );

  onBeforeUnmount(() => {
    axiosInstance.value.interceptors.request.eject(requestIntercept);
    axiosInstance.value.interceptors.response.eject(responseIntercept);
  });

  return axiosInstance;
};

export default useAxiosPrivate;

import { type AxiosResponse } from 'axios';
import { axiosPrivate } from '../api/axios';
import auth from '../utils/auth';

const useRefreshToken = (): (() => Promise<string>) => {
  const refresh: () => Promise<string> = async () => {
    const response: AxiosResponse<{ access_token: string }> = await axiosPrivate.get(
      '/auth/refresh'
    );

    auth.setAccessToken(response.data.access_token);
    return response.data.access_token;
  };

  return refresh;
};

export default useRefreshToken;

import axios from "axios";
import api from "./axiosApi";

axios.interceptors.response.use(
    function (response) {
      return response;
    },
    async function (error) {
      const originalRequest = error.config;
      if ((error.response.status == 401 || error.response.status == 403) && !originalRequest._retry) {
        originalRequest._retry = true;

        try {
          const id = localStorage.getItem("id");
          if (id === null || id === '') {
            console.log('ID가 저장되어 있지 않습니다.');
            localStorage.clear();
            window.location.href = '/login';
            return Promise.reject(error);
          }

          const tokenResponse = await api.get('hoopi/refresh-token', { params: { id: id } });
          if (tokenResponse.status == 200) {
            return axios(originalRequest);
          } else {
            console.error('리프레시 토큰 갱신 실패:', tokenResponse.statusText);
            localStorage.clear();
            window.location.href = '/login';
            return Promise.reject(error);
          }
        } catch (refreshError) {
          console.error('리프레시 시도 중 에러 발생:', refreshError);
          localStorage.clear();
          window.location.href = '/login';
          return Promise.reject(refreshError);
        }
      }
      return Promise.reject(error);
    }
);

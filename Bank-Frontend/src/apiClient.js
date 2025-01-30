import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8070', // Replace with your Spring Boot backend URL
  headers: {
    'Content-Type': 'application/json',
  }
});

// Request Interceptor to add JWT token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor to handle expired JWT and refresh token logic
apiClient.interceptors.response.use(
  response => response, // If request is successful, simply return response
  async error => {
    const originalRequest = error.config;
    console.log(error.response.status)
    
    if ((error.response.status === 401 || error.response.status === 500) && !originalRequest._retry) {
      originalRequest._retry = true;
      
      const refreshToken = localStorage.getItem('refresh_token');
      console.log(refreshToken)
      if (refreshToken) {
        try {
          // Send the refresh token as a path variable in the URL
          const refreshResponse = await axios.post(
            'http://localhost:8070/refresh', 
            { token: refreshToken }
          );
                console.log(refreshResponse.data);
          const newAccessToken = refreshResponse.data.token;
          localStorage.setItem('access_token', newAccessToken);
          
          // Add the new token to the original request
          originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
          return axios(originalRequest);
        } catch (refreshError) {
          console.error('Refresh token failed', refreshError);
          // Handle failure (clear tokens, redirect to login, etc.)
        }
      }
    }
    
    // If the error is not a 401 or something else goes wrong, reject the error
    return Promise.reject(error);
  }
);

export default apiClient;

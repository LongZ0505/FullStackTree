import React, { createContext, useState, useEffect } from 'react';
import { register as apiRegister } from '../api/apiConfig';
import {login as apiLogin} from '../api/apiConfig';
const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);  // user mặc định null
  const [loading, setLoading] = useState(true); // loading true lúc đầu

  // Load user từ localStorage khi app khởi động
  useEffect(() => {
    try {
   //  localStorage.clear();
      const storedUser = localStorage.getItem('user');
      console.log("stored")
      if (storedUser) {
        console.log("stored2",JSON.parse(storedUser))
        setUser(JSON.parse(storedUser));
      }
    } catch (error) {
      console.error("Failed to load user from storage", error);
    } finally {
      setLoading(false);
    }
  }, []);

  // Hàm login thực sự
  const login = async (formData) => {
    setLoading(true);
    try {
      console.log(formData)
      const response = await apiLogin(formData); // gọi API login
      console.log(response)
      localStorage.setItem("token",response.result.token);
       // Fake user
      setUser(response.result.user)
      console.log("user",response.result.user)
      // có thể PrivateRoute kiểm tra toke thay vì user để introspect token theo đúng bussniness
      localStorage.setItem('user', JSON.stringify(response.result.user));
    } catch (err) {
      console.error("Login failed", err);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // Hàm register thực sự
  const register = async (formData) => {
    setLoading(true);
    try {
      console.log("register")
      const response = await apiRegister(formData); // gọi API register
      // Fake user
      setUser(response.result);
      localStorage.setItem('user', JSON.stringify(response.user));
    } catch (err) {
      console.error("Register failed", err);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // Logout thật
  const logout = () => {
    setUser(null);
    localStorage.removeItem('user');
  };

  const authValue = {
    user,
    loading,
    login,
    register,
    logout,
  };

  return (
    <AuthContext.Provider value={authValue}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthContext;

import React from 'react';
import { createBrowserRouter, Navigate } from 'react-router-dom';
import Graficos from './pages/graficos/graficos';
import Auth from './pages/auth/Auth';
import App from './App';
import User from './pages/user/User';

const router = createBrowserRouter([
    {
      path: '/',
      element: <App />,
      children: [
        { index: true, element: <Navigate to="/auth" replace /> },
  
        { path: 'auth', element: <Auth /> },
        { path: 'graficos', element: <Graficos/> },
        { path: 'user', element: <User /> },
  
        { path: '*', element: <Navigate to="/auth" replace /> },
      ]
    }
  ]);

export default router;

import React from 'react';
import { createBrowserRouter, Navigate } from 'react-router-dom';
import Graficos from './pages/graficos/graficos';
import Auth from './pages/auth/Auth';
import App from './App';
import User from './pages/user/User';
import { decodeJwt } from './api/decodeJwt';

export async function getRouter() {
  let username = null;
  try {
    username = await decodeJwt();
  } catch (error) {
    console.error("Erro ao decodificar o token:", error.message);
  }

  const routes = [
    {
      path: '/',
      element: <App />,
      children: [
        { index: true, element: <Navigate to="/auth" replace /> },
        { path: 'auth', element: <Auth /> },
        { path: 'user', element: <User /> },
        ...(username === 'admin'
          ? [{ path: 'graficos', element: <Graficos /> }]
          : []),
        { path: '*', element: <Navigate to="/auth" replace /> },
      ],
    },
  ];

  return createBrowserRouter(routes);
}

import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import { Bounce, Flip, ToastContainer } from 'react-toastify';

import { AuthProvider } from './hooks/useAuth';
import CartProvider from './hooks/useCart';
import './index.css';
import './api/axiosConfiguration.js';
import 'react-toastify/dist/ReactToastify.css';
import { LoadingProvider } from './hooks/useLoading.js';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <BrowserRouter>
      <LoadingProvider>
        <AuthProvider>
            <CartProvider>
              <App />
              <ToastContainer
                position="top-center"
                autoClose={7500}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme="dark"
                transition={Flip}
              />
            </CartProvider>
          </AuthProvider>
      </LoadingProvider>
    </BrowserRouter>
);
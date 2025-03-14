import React, { useRef } from 'react';
import { Route, Routes } from 'react-router-dom';
import { AuthRoute } from './components/AuthRoute/AuthRoute';
import AdminRoute from './components/AdminRoute/AdminRoute';
import { HomePage } from './pages/Home/HomePage';
import { ProductsPage } from './pages/Products/ProductsPage';
import { ProductPage } from './pages/Product/ProductPage';
import { CartPage } from './pages/Cart/CartPage';
import { LoginPage } from './pages/Login/LoginPage';
import { RegisterPage } from './pages/Register/RegisterPage';
import { VerificationPage } from './pages/Verification/VerificationPage';
import { ForgotPasswordPage } from './pages/ForgotPassword/ForgotPasswordPage';
import { ResetPasswordPage } from './pages/ResetPassword/ResetPasswordPage';
import { CheckoutPage } from './pages/Checkout/CheckoutPage';
import { PaymentPage } from './pages/Payment/PaymentPage';
import { TrackOrdersPage } from './pages/TrackOrders/TrackOrdersPage';
import { ProfilePage } from './pages/Profile/ProfilePage';
import { ManagmentDashboardPage } from './pages/ManagmentDashboard/ManagmentDashboardPage';
import { ManagmentOrdersPage } from './pages/ManagmentOrders/ManagmentOrdersPage';
import { ManagmentProductsPage } from './pages/ManagmentProducts/ManagmentProductsPage';
import { ManagmentNewsPage } from './pages/ManagmentNews/ManagmentNewsPage';


export default function AppRoutes(props) {
  return (
    <>
      <Routes>
        <Route path="/" element={<HomePage homeRef={props.homeRef} aboutRef={props.aboutRef} contactRef={props.contactRef} />} />
        <Route path="/products" element={<ProductsPage />} />
        <Route path="/products/search/:searchTerm" element={<ProductsPage />} />
        <Route path="/products/:name" element={<ProductPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/registration" element={<RegisterPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
        <Route path="/verify/account" element={<VerificationPage />} />
        <Route path="/verify/password" element={<ResetPasswordPage />} />

        <Route path="/cart" element={<AuthRoute><CartPage /></AuthRoute>} />
        <Route path="/checkout" element={<AuthRoute><CheckoutPage /></AuthRoute>} />
        <Route path="/payment" element={<AuthRoute><PaymentPage /></AuthRoute>} />
        <Route path="/track/orders" element={<AuthRoute><TrackOrdersPage /></AuthRoute>} />
        <Route path="/profile" element={<AuthRoute><ProfilePage /></AuthRoute>} />
        
        <Route path="/managment" element={<AdminRoute><ManagmentDashboardPage /></AdminRoute>} />
        <Route path="/managment/orders/:orderId?" element={<AdminRoute><ManagmentOrdersPage /></AdminRoute>} />
        <Route path="/managment/products/:searchTerm?" element={<AdminRoute><ManagmentProductsPage /></AdminRoute>} />
        <Route path="/managment/news/:searchTerm?" element={<AdminRoute><ManagmentNewsPage /></AdminRoute>} />
      </Routes>
    </>
  );
}

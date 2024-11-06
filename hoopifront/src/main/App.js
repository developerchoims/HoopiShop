import React, { useContext, createContext, useState, useEffect } from 'react';
import {BrowserRouter as Router, Route, Routes, Link, useLocation} from 'react-router-dom';
import Login from '../auth/Login';
import Join from '../auth/Join';
import './App.css';
import Main from "./Main";
import './axios/axiosInterceptor.js';
import AdminMain from "../admin/adminMain";
import {SearchProvider} from "../searchMenu/SearchContext";
import Menu from "../searchMenu/Menu";
import Product from "../product/Product";
import {ProductDetail} from "../product/ProductDetail";
import UserBody from "../admin/user/userBody";
import Board from "../admin/board/Board";
import Notice from "../notice/Notice";
import NoticeDetail from "../notice/NoticeDetail";
import Cart from "../cart/Cart";
import api from "./axios/axiosApi";
import Order from "../order/Order";
import MyPage from "../myPage/MyPage";
import PersonalInfo from "../myPage/PersonalInfo";
import HelpDesk from "../myPage/HelpDesk";
import AdminOrder from "../admin/order/AdminOrder";

// Context 생성
const UserContext = createContext(null);

// App 컴포넌트
function App() {

    const id = localStorage.getItem("id");
    const role = localStorage.getItem("role");


    const handleLogout =  () => {
         api.delete('hoopi/logout', { params: { id: id } })
            .then(response => {
                localStorage.clear();
                alert(response.data);
                window.location.href = '/';
            })
            .catch(error => {
                alert(error.response.data);
            });
    }

    return (
            <Router>
                <div className='mainNav-container'>
                    <div className='mainNav-sns-box'>
                        {role == null || role === '' || id == null || id === '' ? (
                            <></>
                        ) : (
                            <div className='mainNav-nav-box'>
                                <Link to="/cart"><p>장바구니</p></Link>
                                <Link to="/my-page"><p>마이페이지</p></Link>
                            </div>
                        )}
                    </div>
                    <div className='mainNav-img-box'>
                        <Link to='/'><img src='/nata_logo.png' /></Link>
                    </div>
                    <div className='mainNav-link-box'>
                        {role == null || role === '' || id == null || id === '' ? (
                            <>
                                <Link to="/join">회원가입</Link>
                                <Link to="/login">로그인</Link>
                            </>
                        ) : (
                            <span>
                                {id} 님 환영합니다 <Link to="/" onClick={handleLogout}>로그아웃</Link>
                            </span>
                        )}
                    </div>
                </div>
                <div>
                    <SearchProvider>
                        <Menu/>
                        <Routes>
                            <Route path='/' element={<Main/>}/>
                            <Route path='/join' element={<Join />}/>
                            <Route path='/login' element={<Login />}/>
                            <Route path='/admin' element={<AdminMain/>}>
                                <Route path='user' element={<UserBody/>}/>
                                <Route path='product' element={<Product/>}/>
                                <Route path='product/:productCode/:name' element={<ProductDetail/>}/>
                                <Route path='notice' element={<Notice/>}/>
                                <Route path='notice/:articleCode/:articleTitle' element={<NoticeDetail/>}/>
                                <Route path=':boardId/write' element={<Board/>}/>
                                <Route path='order' element={<AdminOrder/>}/>
                            </Route>
                            <Route path='/product' element={<Product/>}/>
                            <Route path='/product/:productCode/:name' element={<ProductDetail/>}/>
                            <Route path="/notice" element={<Notice/>}/>
                            <Route path='/notice/:articleCode/:articleTitle' element={<NoticeDetail/>}/>
                            <Route path='/cart' element={<Cart/>}/>
                            <Route path='/order' element={<Order/>}/>
                            <Route path='/my-page' element={<MyPage/>}/>
                            <Route path='/personal-info' element={<PersonalInfo/>}/>
                            <Route path='/help-desk' element={<HelpDesk/>}/>
                        </Routes>
                    </SearchProvider>
                </div>
            </Router>
    );
}

export default App;
export {UserContext};
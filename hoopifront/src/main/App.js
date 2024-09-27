import React, { useContext, createContext, useState, useEffect } from 'react';
import axios from 'axios';
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

// Context 생성
const UserContext = createContext(null);

// App 컴포넌트
function App() {

    const id = localStorage.getItem("id");
    const role = localStorage.getItem("role");


    const handleLogout =  () => {
         axios.delete('http://hoopi.p-e.kr/api/hoopi/logout', { params: { id: id } })
            .then(response => {
                localStorage.removeItem('id');
                localStorage.removeItem('role');
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
                            <>
                                <Link to="/cart"><p>장바구니</p></Link>
                                <Link to="/my-page"><p>마이페이지</p></Link>
                            </>
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
                            </Route>
                            <Route path='/product' element={<Product/>}/>
                            <Route path='/product/:productCode/:name' element={<ProductDetail/>}/>
                            <Route path="/notice" element={<Notice/>}/>
                            <Route path='/notice/:articleCode/:articleTitle' element={<NoticeDetail/>}/>
                            <Route path='/cart' element={<Cart/>}/>
                        </Routes>
                    </SearchProvider>
                </div>
            </Router>
    );
}

export default App;
export {UserContext};
import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import api from "../main/axios/axiosApi";
import {UserContext} from "../main/App";
import './Login.css';

const Login = () => {
    const [user, setUser] = useState({ 'id': '', 'pwd': '' });
    const navigate = useNavigate();

    const handleUsers = (e) => {
        const { id, value } = e.target;
        setUser(prevState => ({
            ...prevState,
            [id]: value
        }));
    }


    const handleLogin = async () => {
        if (user.id === '' || user.pwd === '') {
            alert('아이디와 비밀번호를 모두 입력해주세요.');
            return;
        }

        try {
            const response = await api.post("hoopi/login", user);
            console.log('user:'+user);
            localStorage.setItem("id", response.data.id);
            localStorage.setItem("role", response.data.role);
            alert(response.data.msg);
            window.location.href = '/';
        } catch (error) {
            console.error('로그인 요청 오류:', error);
            alert(error.response.data);
        }
    }


    return (
        <div className='login-container'>
            <table className='login-table'>
                <thead>
                    <tr>
                        <th colSpan={6}>로그인</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>아이디</td>
                        <td colSpan={3}>
                            <input id="id" onChange={handleUsers} value={user.userId} />
                        </td>
                        <td rowSpan={2}>
                            <button onClick={handleLogin}>로그인</button>
                        </td>
                    </tr>
                    <tr>
                        <td>비밀번호</td>
                        <td colSpan={3}>
                            <input type="password" id="pwd" onChange={handleUsers} value={user.userPw} />
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    )
}

export default Login;
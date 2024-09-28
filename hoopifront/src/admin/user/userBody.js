import { useEffect, useState } from "react";
import axios from "axios";
import Pagination from "@mui/material/Pagination";
import Stack from "@mui/material/Stack";
import './userBody.css'
import { useSearch } from '../../searchMenu/SearchContext';
import api from "../../main/axios/axiosApi";

const UserBody = () => {
    const id = localStorage.getItem("id");
    const role = localStorage.getItem("role");

    const { keyword, searchCate } = useSearch();

    const [userPage, setUserPage] = useState({ content: [], totalPages: 0 });
    const [currentPage, setCurrentPage] = useState(1);
    const [userDetail, setUserDetail] = useState({id: ''
                                                                , name:''
                                                                , phone:''
                                                                , birth: ''
                                                                , email: ''
                                                                , quitYn: ''
                                                                , quitDate: ''
                                                                , joinDate: ''});
    const [detailVisible, setDetailVisible] = useState(false);

    useEffect(() => {
        fetchUsers(currentPage);
    }, [currentPage, keyword, searchCate]); // 페이지 변경시 자동으로 데이터 로딩

    const fetchUsers = async (page) => {
        try {
            const response = await api.get("hoopi/admin/user", {
                params: { searchCate: searchCate, keyword: keyword, page: page - 1, size: 10 }
            });
            console.log("response.data : ", response.data.content);
            setUserPage(response.data);
        } catch (error) {
            console.error("Failed to fetch data:", error);
        }
    };

    const handleUserDetail = async (code) => {
        try {
            const response = await api.get('hoopi/admin/user-detail', {params: {code: code}});
            setUserDetail(response.data);
            setDetailVisible(true);
        } catch (error) {
            alert(error.response.data);
        }
    };

    const handleUserQuit = async () => {
        try {
            const response = await api.put('hoopi/admin/user-quit', null,{params:{id:userDetail.id}});
            alert("사용자 탈퇴 처리가 완료되었습니다.");
            fetchUsers(currentPage); // 상태 업데이트 후 목록 새로고침
            setDetailVisible(false);
        } catch (error) {
            console.log(error);
            alert(error.response.data);
        }
    };

    const handleClose = () => {
        setDetailVisible(false);
    };

    const handlePageChange = (event, page) => {
        setCurrentPage(page);
    };

    return (
        <div className='user-body-container'>
            <table>
                <thead>
                <tr>
                    <th>순서</th>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>핸드폰 번호</th>
                </tr>
                </thead>
                <tbody>
                {userPage?.content?.map((item, index) => (
                    <tr key={item.code} id={item.userId} onClick={() => handleUserDetail(item.code)}>
                        <td>{index + 1}</td>
                        <td>{item.userId}</td>
                        <td>{item.userName}</td>
                        <td>{item.email}</td>
                        <td>{item.phone}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <div className='admin-user-detail-container' style={{display: detailVisible?"block" : "none"}}>
                <div className='admin-user-detail-box'>
                    <table>
                        <thead>
                        <th colSpan="2">
                            {userDetail.id}님의 상세 정보
                        </th>
                        </thead>
                        <tbody>
                        <tr>
                            <td>id</td>
                            <td>{userDetail.id}</td>
                        </tr>
                        <tr>
                            <td>name</td>
                            <td>{userDetail.name}</td>
                        </tr>
                        <tr>
                            <td>phone</td>
                            <td>{userDetail.phone}</td>
                        </tr>
                        <tr>
                            <td>email</td>
                            <td>{userDetail.email}</td>
                        </tr>
                        <tr>
                            <td>birth</td>
                            <td>{userDetail.birth}</td>
                        </tr>
                        <tr>
                            <td>joinDate</td>
                            <td>{userDetail.joinDate}</td>
                        </tr>
                        <tr>
                            <td>quitDate</td>
                            <td>{userDetail.quitDate}</td>
                        </tr>
                        <tr>
                            <td>quitYn</td>
                            <td>{userDetail.quitYn}</td>
                        </tr>
                        <tr>
                            <td>Address</td>
                            <td>
                                {userDetail?.addressDto?.map((item, index) => (
                                    <div key={item.addressCode}>
                                        <span>{item.main === 'Y' ? '메인 주소' : index}</span>: {item.address}
                                    </div>
                                ))}
                            </td>
                        </tr>
                        </tbody>
                        <tfoot>
                        <tr>
                            <td colSpan={2}>
                                <button id={userDetail.id} onClick={handleUserQuit}>탈퇴</button>
                                <button onClick={handleClose}>닫기</button>
                            </td>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
            <div className='user-body-pagination'>
                <Stack spacing={2}>
                    <Pagination count={userPage.totalPages} page={currentPage} onChange={handlePageChange}
                                variant="outlined" color="primary"/>
                </Stack>
            </div>
        </div>
    );
};

export default UserBody;

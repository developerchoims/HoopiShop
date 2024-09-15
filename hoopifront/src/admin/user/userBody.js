import {useEffect, useState} from "react";
import axios from "axios";

const UserBody = () => {

    const id = localStorage.getItem("id");

    const[userPage, setUserPage] = useState([]);
    const[userDetail, setUserDetail] = useState({'': ''});
    const[detailVisible, setDetailVisible] = useState(false);

    useEffect(() => {
        const handleUserPage = async function(id){
            const response = await axios.get("http://hoopi.p-e.kr/hoopi/admin/user");
            if(response.data == null){
                return false;
            }
            setUserPage(response.data);
        }
        handleUserPage();
    }, [])

    // user detail 정보 불러오기
    const handleUserDetail = () => {
        axios.get("http://hoopi.p-e.kr/hoopi/admin/user-detail")
            .then(response => {
                setUserDetail(response.data);
            })
            .catch(error => {
                alert(error.response.data);
            })
    }

    // user quit
    const handleUserQuit = () => {
        axios.get("http://hoopi.p-e.kr/hoopi/admin/user-quit")
            .then(response => {
                alert(response.data);
            })
            .catch(error => {
                alert(error.response.data);
            })
    }

    // user detail 정보 닫기
    const handleClose = () => {
        setDetailVisible(false);
    }

    return (
        <div>
            <table>
                <thead>
                <tr>
                    <th>순서</th>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>핸드폰 번호</th>
                </tr>
                </thead>
                <tbody>
                {userPage.map((item, index) => {
                    return (
                        <tr key={item.code} id={item.id} onClick={() => handleUserDetail(item.id)}>
                            <td>{index + 1}</td>
                            <td>{item.id}</td>
                            <td>{item.name}</td>
                            <td>{item.phone}</td>
                        </tr>
                    )
                })}
                </tbody>
            </table>
            <div className='admin-user-detail-container' style={{display: detailVisible?"block" : "none"}}>
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
);
}

export default UserBody;
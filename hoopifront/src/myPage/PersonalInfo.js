import { useEffect, useState } from "react";
import axios from "axios";
import './personalInfo.css'

const PersonalInfo = () => {
    const id = localStorage.getItem("id");
    const [userInfo, setUserInfo] = useState({
        id: '',
        name: '',
        phone: '',
        email: '',
        addresses: []
    });

    const handlePersonalInfo = async () => {
        try {
            const response = await axios.get('https://hoopi.co.kr/api/hoopi/personal-info', {
                params: {
                    id: id,
                }
            });
            setUserInfo(response.data);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        handlePersonalInfo();
    }, []);

    const handleDeleteAddress = (addressCode) => {
        axios.post('https://hoopi.co.kr/api/hoopi/personal-info', addressCode)
            .then(response => {
                alert(response.data);
            })
            .catch(error => {
                console.log(error);
            })
    }

    return (
        <div className='personal-info-container'>
            <div className='personal-info-box'>
                <table>
                    <thead>
                    <tr>
                        <th colSpan={2}>개인 정보 확인</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>아이디</td>
                        <td>{userInfo?.id}</td>
                    </tr>
                    <tr>
                        <td>이름</td>
                        <td>{userInfo?.name}</td>
                    </tr>
                    <tr>
                        <td>핸드폰 번호</td>
                        <td>{userInfo?.phone}</td>
                    </tr>
                    <tr>
                        <td>이메일</td>
                        <td>{userInfo?.email}</td>
                    </tr>
                    <tr>
                        <td>주소</td>
                        <td>
                            <table>
                                {userInfo?.addresses?.map((address, index) => (
                                    <>
                                        <tbody>
                                        <tr>
                                            <th rowSpan={4}>주소 {index + 1}</th>
                                        </tr>
                                        <tr>
                                            <td>{address.addressName}</td>
                                        </tr>
                                        <tr>
                                            <td>{address.addressPhone}</td>
                                        </tr>
                                        <tr>
                                            <td>{address.postCode}</td>
                                        </tr>
                                        <tr>
                                            <td>{address.address}</td>
                                        </tr>
                                        <tr>
                                            <td rowSpan={4}><button
                                                                    id={address.addressCode}
                                                                    onClick={()=>handleDeleteAddress(address.addressCode)}>주소 삭제</button></td>
                                        </tr>
                                        </tbody>
                                    </>
                                ))}
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default PersonalInfo;

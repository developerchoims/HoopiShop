import { useEffect, useState } from "react";
import axios from "axios";
import './personalInfo.css'
import DaumPostCode from "../auth/DaumPostCode";

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
        axios.delete('https://hoopi.co.kr/api/hoopi/personal-info', addressCode)
            .then(response => {
                alert(response.data);
            })
            .catch(error => {
                console.log(error);
            })
    }


    const [showPostCode, setShowPostCode] = useState(false);
    const [address, setAddress] = useState({});
    const handleCompleteAddress = (data) => {
        setAddress(prevState => ({
            ...prevState,
            id,
            address: data.address + ' ' + data.extraAddress,
            postCode: data.zoneCode
        }));
        setShowPostCode(false);  // 팝업 닫기
    };
    const handleAddressPhone = (e) => {
        const {id, val} = e.target;
        setAddress( prevState=> ({
            ...prevState,
            [id]: val
        }));
    };
    const handleAddAddress = (address) => {
        axios.post('https://hoopi.co.kr/api/hoopi/personal-info', address)
            .then(response => {
                alert(response.data);
            })
            .catch(error => {
                console.log(error);
            })
    }
    const handleShowPostcode = () => {
        setShowPostCode(false);
        setAddress({});
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
                        <td>주소 <button onClick={setShowPostCode(true)}>주소찾기</button></td>
                        <td>
                            <table>
                                {userInfo?.addresses?.map((address, index) => (
                                    <>
                                        <tbody>
                                        <tr>
                                            <th rowSpan={5}>주소 {index + 1}</th>
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
                                            <td rowSpan={5}>
                                                <button
                                                    id={address.addressCode}
                                                    onClick={() => handleDeleteAddress(address.addressCode)}>주소 삭제
                                                </button>
                                            </td>
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
            <div>
                {showPostCode && (
                    <div style={{
                        position: 'fixed',
                        top: '50%',
                        left: '50%',
                        width: '50%',
                        transform: 'translate(-50%, -50%)',
                        zIndex: 100,
                        padding: '10px',
                        backgroundColor: '#fff',
                        boxShadow: '0px 0px 10px rgba(0,0,0,0.3)'
                    }}>
                        <DaumPostCode onComplete={handleCompleteAddress}/>
                        <input id='addressPhone'
                               value={address?.addressPhone}
                               type="text"
                               onChange={handleAddressPhone}/>
                        <input id='addressName'
                               value={address?.addressPhone}
                               type="text"
                               onChange={handleAddressPhone}/>
                        <button onClick={() => handleAddAddress(address)}>추가</button>
                        <button onClick={handleShowPostcode}>닫기</button>
                    </div>
                )}
            </div>
        </div>
    );
}

export default PersonalInfo;

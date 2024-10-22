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
        axios.delete(`https://hoopi.co.kr/api/hoopi/personal-info/${addressCode}`)
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
            "address": data.address,
            "postCode": data.zoneCode
        }));
        console.log(address);
    };
    const handleAddress = (e) => {
        const {id, value} = e.target;
        setAddress( prevState=> ({
            ...prevState,
            [id]: value
        }));
    };
    const handleAddAddress = (address) => {
        axios.post('https://hoopi.co.kr/api/hoopi/personal-info', address)
            .then(response => {
                alert(response.data);
                setShowPostCode(false);
            })
            .catch(error => {
                console.log(error);
            })
    }
    const handleShutPostcode = () => {
        setShowPostCode(false);
        setAddress({});
    }
    const handleShowPostcode = () => {
        setShowPostCode(true);
    }

    return (
        <div className='personal-info-container'>
            <div className='personal-info-box'>
                <table>
                    <thead>
                    <tr>
                        <th></th>
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
                        <td><p>주소</p> <p><button onClick={handleShowPostcode}>주소 추가</button></p></td>
                        <td>
                            <table>
                                {userInfo?.addresses?.map((address, index) => (
                                    <>
                                        <tbody>
                                        <tr>
                                            <th rowSpan={4}>주소 {index + 1}</th>
                                            <td>{address.addressName}</td>
                                            <td rowSpan={4}>
                                                <button
                                                    id={address.addressCode}
                                                    onClick={() => handleDeleteAddress(address.addressCode)}>주소 삭제
                                                </button>
                                            </td>

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
                        boxShadow: '0px 0px 10px rgba(0,0,0,0.3)',
                        display: flex,
                        justifyContent: center,
                        alignContent: center
                    }}>
                        <p>수취인 번호 : <input id='addressPhone'
                                           value={address?.addressPhone}
                                           type="text"
                                           onChange={handleAddress}/>
                        </p>
                        <p>수취인 성함 : <input id='addressName'
                                           value={address?.addressName}
                                           type="text"
                                           onChange={handleAddress}/>
                        </p>
                        <DaumPostCode onComplete={handleCompleteAddress}/>
                        <p>우편  번호 : <input id='postCode'
                                          value={address?.postCode}
                                          type="text"/>
                        </p>
                        <p>수취인 주소 : <input id='address'
                                           value={address?.address}
                                           type="text"/>
                        </p>
                        <p>세부  주소 : <input id='extraAddress'
                                           value={address?.extraAddress}
                                           type="text"
                                           onChange={handleAddress}/>
                        </p>
                        <button id="addAddressBtn" onClick={() => handleAddAddress(address)}>추가</button>
                        <button id="cancleAddressBtn" onClick={handleShutPostcode}>닫기</button>
                    </div>
                )}
            </div>
        </div>
    );
}

export default PersonalInfo;

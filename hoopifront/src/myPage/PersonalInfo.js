import {useEffect, useState} from "react";
import axios from "axios";

const PersonalInfo = () => {

    const id = localStorage.getItem("id");

    const [userInfo, setUserInfo] = useState({});
    const handlePersonalInfo = async () => {
        try{
            const response = await axios.get('https://hoopi.co.kr/api/hoopi/personal-info', {params:{
                id: id,
                }});
            setUserInfo(response.data);
        } catch (error) {
            console.error(error);
        }
    }
    useEffect(() => {
        handlePersonalInfo();
    }, []);

    return(
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
                        <td>
                            아이디
                        </td>
                        <td>
                            {userInfo?.id}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            이름
                        </td>
                        <td>
                            {userInfo?.name}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            핸드폰 번호
                        </td>
                        <td>
                            {userInfo?.phone}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            이메일
                        </td>
                        <td>
                            {userInfo?.address}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            주소
                        </td>
                        <td>
                            <table>
                                {userInfo?.addresses?.map(address=> {
                                    <thead>
                                    <tr>
                                        <th>{address.addressName}</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>{address.addressPhone}</td>
                                    </tr>
                                    <tr>
                                        <td>{address.address}</td>
                                    </tr>
                                    </tbody>
                                })}
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    )
}

export default PersonalInfo;
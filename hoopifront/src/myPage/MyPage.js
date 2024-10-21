import {useNavigate} from "react-router-dom";
import './myPage.css';

const MyPage = () => {

    const navigate = useNavigate();
    const handleRoute = (e) => {
        const id = e.target.id;
        navigate(`/${id}`);
    }

    return (
        <div className="myPage-container">
            <div className="myPage-box">
                <h4>마이 페이지</h4>
                <table>
                    <tbody>
                    <tr>
                        <td>
                            <button id='personal-info' onClick={handleRoute}>개인 정보 확인</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id='help-desk' onClick={handleRoute}>고객 센터</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id='quit'>탈퇴</button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    )
}

export default MyPage;
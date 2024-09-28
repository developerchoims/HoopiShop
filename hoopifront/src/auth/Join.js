import { useState } from "react";
import "./Join.css";
import DaumPostCode from "./DaumPostCode";
import api from "../main/axios/axiosApi";

function Join() {

    // 값 저장
    const [chkPw, setChkPw] = useState("");
    const [add1, setAdd1] = useState("");
    const [add2, setAdd2] = useState("");
    const [add3, setAdd3] = useState("");
    const [showPostCode, setShowPostCode] = useState(false);
    const [emailCd, setEmailCd] = useState("");
    const [chkEmailCd, setChkEmailCd] = useState('');
    const [phoneCd, setPhoneCd] = useState('');
    const [chkPhoneCd, setChkPhoneCd] = useState('');
    const [emailState, setEmailState] = useState(false);
    const [phoneState, setPhoneState] = useState(false);
    const [user, setUser] = useState({});

    const handleChange = (e) => {
        const { id, value } = e.target;
        setUser(prevState => ({
            ...prevState,
            [id]: value
        }));
    }

    const handleChkPw = (e) => {
        setChkPw(e.target.value);
    }

    const handleAddress = (e) => {
        switch (e.target.id) {
            case "add1":
                setAdd1(e.target.value);
                break;
            case "add2":
                setAdd2(e.target.value);
                break;
            case "add3":
                setAdd3(e.target.value);
                break;
            default:
                break;
        }
        setUser(prevState => ({
            ...prevState,
            address: add1 + ' ' + add2 + ' ' + add3
        }));
    }

    const handleCompleteAddress = (data) => {
        setAdd1(data.address);
        setAdd2(data.extraAddress);
        setUser(prevState => ({
            ...prevState,
            address: data.address + ' ' + data.extraAddress,
            zoneCode: data.zoneCode
        }));
        setShowPostCode(false);  // 팝업 닫기
    };

    const handleChkEmailCd = (e) => {
        setChkEmailCd(e.target.value);
    }

    // 인증 버튼 눌러 본인인증 요청하기
    const handleEmail = () => {
        if(user.email === "" || user.email == null) {
            alert("이메일을 입력해주세요");
            return false;
        }

        api.post("hoopi/email", null, {
            params: {
                email: user.email
            }
        })
            .then(response => {
                setEmailCd(response.data);
                alert("이메일을 확인하여 본인인증을 완료해주세요.");
            })
            .catch(error => {
                alert(error);
                setEmailCd('');
            })

    }

    // 인증 완료하기
    const chkEmail = () => {
        if(emailCd == chkEmailCd){
            setEmailState(true);
            console.log("돼라...");
            console.log("이메일 Cd",emailCd);
            console.log(chkEmailCd);
            alert("이메일 인증에 성공하셨습니다.");
        } else {
            console.log(emailCd);
            console.log(chkEmailCd);
            alert("다시 인증해주세요.");
            setEmailCd('');
            setChkEmailCd('');
        }
    }

    // 핸드폰 인증하기
    const handlePhone = () => {
        api.post("hoopi/phone", null ,{
            params: {
                phone: user.phone
            }
        })
            .then(response => {
                setPhoneCd(response.data);
                alert("핸드폰을 확인하여 본인인증을 완료해주세요.");
            })
            .catch(error => {
                alert(error);
                setPhoneCd('');
                setChkPhoneCd('');
            })
    }

    // 핸드폰 인증 완료하기
    const chkPhone = () => {
        if(phoneCd == chkPhoneCd){
            setPhoneState(true);
            console.log(phoneCd);
            console.log(chkPhoneCd);
            alert("핸드폰 인증에 성공하셨습니다.");
        } else {
            console.log(phoneCd);
            console.log(chkPhoneCd);
            alert("다시 인증해주세요.");
            setPhoneCd('');
            setChkPhoneCd('');
        }
    }

    const handleChkPhoneCd = (e) => {
        setChkPhoneCd(e.target.value);
    }

    // 회원가입 신청
    const handleJoin = () => {
        if(user.id === "" || user.pwd === "" || user.name === "" ||
            user.address === "" || user.email === "" || user.phone === "") {
            alert("기본 입력 사항을 모두 입력해주세요.");
            return false;
        }
        if(chkPw !== user.pwd){
            alert("비밀번호를 다시 입력해주세요.");
            return false;
        }
        if(!emailState || !phoneState){
            alert("이메일과 핸드폰 인증을 마무리해주세요.");
            return false;
        }
        api.post("hoopi/join", user)
            .then(response => {
                alert("회원가입이 성공적으로 이루어졌습니다.");
                window.location.reload('/');
            })
            .catch(error => {
                if (error.response) {
                    alert(error.response.data);
                } else if (error.request) {
                    // 요청이 만들어졌지만 서버에서 응답이 없을 때
                    console.error('요청 데이터:', error.request);
                    alert('서버로부터 응답이 없습니다.');
                }
            })

    }

    return (
        <div className="join-container">
            <table className="join-table">
                <thead>
                <tr>
                    <td colSpan={6}>회원 가입</td>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>ID</td>
                    <td colSpan={5}><input id="id" value={user.id} onChange={handleChange}></input></td>
                </tr>
                <tr>
                    <td>password</td>
                    <td colSpan={5}><input id="pwd" value={user.pwd} onChange={handleChange}></input></td>
                </tr>
                <tr>
                    <td>비밀번호 확인</td>
                    <td colSpan={5}><input id="chkPw" value={chkPw} onChange={handleChkPw}></input></td>
                </tr>
                <tr>
                    <td>이름</td>
                    <td colSpan={5}><input id="name" value={user.name} onChange={handleChange}></input></td>
                </tr>
                <tr>
                    <td>생일</td>
                    <td colSpan={5}><input id="birth" type="date" value={user.birth} onChange={handleChange}/></td>
                </tr>
                <tr>
                    <td>주소</td>
                    <td colSpan={5}><input disabled={true} id="add1" value={add1} onChange={handleAddress}/></td>
                </tr>
                <tr>
                    <td><button onClick={() => setShowPostCode(true)}>주소 찾기</button></td>
                    <td colSpan={1}><input disabled={true} id="add2" value={add2} onChange={handleAddress}/></td>
                    <td colSpan={4}><input id="add3" value={add3} onChange={handleAddress}/></td>
                </tr>
                <tr>
                    <td>이메일</td>
                    <td colSpan={3}><input disabled={emailState} id="email" value={user.email} onChange={handleChange}></input></td>
                    <td colSpan={2}>
                        <button disabled={emailState} id="emailChkBtn" onClick={handleEmail}>인증</button>
                    </td>
                </tr>
                <tr>
                    <td>이메일 확인</td>
                    <td colSpan={3}><input disabled={emailState} id="chkEmailCd" value={chkEmailCd} onChange={handleChkEmailCd}></input></td>
                    <td colSpan={2}>
                        <button disabled={emailState} onClick={chkEmail}>이메일 인증</button>
                    </td>
                </tr>
                <tr>
                    <td>핸드폰</td>
                    <td colSpan={3}><input disabled={phoneState} id="phone" value={user.phone} onChange={handleChange}></input></td>
                    <td colSpan={2}>
                        <button disabled={phoneState} id="phoneChkBtn" onClick={handlePhone}>인증</button>
                    </td>
                </tr>
                <tr>
                    <td>핸드폰 확인</td>
                    <td colSpan={3}><input disabled={phoneState} id="chkPhoneCd" value={chkPhoneCd} onChange={handleChkPhoneCd}></input></td>
                    <td colSpan={2}>
                        <button disabled={phoneState} onClick={chkPhone}>핸드폰 인증</button>
                    </td>
                </tr>
                <tr>
                    <td colSpan={6}>
                        <button onClick={handleJoin}>회원가입</button>
                    </td>
                </tr>
                </tbody>
            </table>
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
                        <DaumPostCode onComplete={handleCompleteAddress} />
                        <button onClick={() => setShowPostCode(false)}>닫기</button>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Join;

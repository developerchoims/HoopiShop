import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import './noticeDetail.css'

const NoticeDetail = () => {
    // 게시글 정보
    const {articleCode, articleName} = useParams();

    // 게시글 정보 설정
    const[noticeDetail, setNoticeDetail] = useState([]);

    useEffect(() => {
        fetchNoticeDetail();
    }, []);

    const fetchNoticeDetail = async () => {
        try{
            const response = await axios.get(`http://hoopi.p-e.kr/api/hoopi/notice-detail/${articleCode}`, {});
            setNoticeDetail(response.data);
            console.log(response.data);
        } catch (e) {
            console.log(e);
        }
    }

    // 날짜 형식 지정
    const modifiedDateTime = (time) => {
        return time ? time.split(".")[0] : '';
    }

    return(
        <div className="notice-detail-container">
            <div className="notice-detail-box">
                <div className="notice-detail-title-box">
                    <div>{noticeDetail?.articleTitle}</div>
                    <div>{modifiedDateTime(noticeDetail?.articleDate)}</div>
                </div>
                <div className="notice-detail-content-box">
                    <img src={noticeDetail?.imgUrl} alt={noticeDetail?.articleTitle}/>
                    <br/>
                    {noticeDetail?.boardContent}
                </div>
            </div>
        </div>
    );
}
export default NoticeDetail;
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
            const response = await axios.get(`http://hoopi.p-e.kr/hoopi/notice-detail/${articleCode}`, {});
            setNoticeDetail(response.data);
        } catch (e) {
            console.log(e);
        }
    }

    return(
        <div className="notice-detail-container">
            <div className="notice-detail-box">
                <div className="notice-detail-title-box">
                    <div>{noticeDetail?.articleTitle}</div>
                    <div>{noticeDetail?.articleDate}</div>
                </div>
                <div className="notice-detail-content-box">
                    <img src={noticeDetail?.imgUrl} alt={noticeDetail?.articleTitle}/>
                    {noticeDetail?.boardContent}
                </div>
            </div>
        </div>
    );
}
export default NoticeDetail;
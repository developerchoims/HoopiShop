import {useSearch} from "../searchMenu/SearchContext";
import Pagination from "@mui/material/Pagination";
import {useEffect, useState} from "react";
import api from "../main/axios/axiosApi";
import './notice.css';
import {Link, useNavigate} from "react-router-dom";

const Notice = () => {
    const{searchCate, setSearchCate, keyword, setKeyword} = useSearch();

    // 페이지네이션
    const [currentPage, setCurrentPage] = useState(1);
    const handlePageChange = (event, page) => {
        setCurrentPage(page);
    };

    // notice정보
    const [notice, setNotice] = useState();
    useEffect(() => {
        getNotice(currentPage);
    }, [currentPage, keyword, searchCate]);

    const getNotice = async(page) => {
        try{
            const response = await api.get('hoopi/notice', {
                params:{page: page-1, size: 10, searchCate: searchCate, keyword: keyword}
            });
            console.log(response.data);
            setNotice(response.data);

        } catch (e){
            console.log(e);
        }
    }

    // noticeDetail페이지로 이동
    const navigate = useNavigate();
    const handleNoticeDetail = (n) => {
        navigate(`/notice/${n.articleCode}/${n.articleTitle}`);
    }

    // 날짜 형식 지정
    const modifiedDateTime = (time) => {
        return time ? time.split(".")[0].replace("T", " ") : '';
    }

    return(
        <div className="notice-container">
            <div className='notice-box'>
                <table>
                    <thead>
                    <tr>
                        <th>순번</th>
                        <th>제목</th>
                        <th>날짜</th>
                    </tr>
                    </thead>
                    <tbody>
                    {notice?.content?.map((n, index) => (
                        <tr onClick = {() => handleNoticeDetail(n)} key={index}>
                            <td>{index+1}</td>
                            <td>{n.articleTitle}</td>
                            <td>{modifiedDateTime(n.articleDate)}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>

                <div className="notice-pagination-box">
                    <Pagination count={notice?.totalPages} page={currentPage} onChange={handlePageChange}
                                variant="outlined" color="primary"/>
                </div>
            </div>
        </div>

    );
}
export default Notice;
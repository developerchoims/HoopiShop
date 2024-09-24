import {useSearch} from "../searchMenu/SearchContext";
import Pagination from "@mui/material/Pagination";
import {useEffect, useState} from "react";
import axios from "axios";
import './notice.css';
import {Link} from "react-router-dom";

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
        getNotice();
    }, [currentPage, keyword, searchCate]);

    const getNotice = async() => {
        try{
            const response = await axios.get('http://hoopi.p-e.kr/api/hoopi/notice', {
                params:{page: currentPage, size: 10, searchCate: searchCate, keyword: keyword}
            });
            console.log(response.data);
            setNotice(response.data);

        } catch (e){
            console.log(e);
        }
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
                        <tr>
                            <Link to={`/admin/notice/${n.articleCode}/${n.articleTitle}`} key={n.articleCode}>
                            <td>{index}</td>
                            <td>{n.articleTitle}</td>
                            <td>{n.articleDate}</td>
                            </Link>
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
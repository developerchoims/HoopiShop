import {useSearch} from "../searchMenu/SearchContext";
import Pagination from "@mui/material/Pagination";
import {useEffect, useState} from "react";
import axios from "axios";

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
            setNotice(response.data);
        } catch (e){
            console.log(e);
        }
    }

    return(
        <div className="notice-container">
            <table>
                <thead>
                <tr>
                    <th>순번</th>
                    <th>제목</th>
                    <th>날짜</th>
                </tr>
                </thead>
                <tbody>
                {notice?.map((n, index)=> (
                    <tr key={n.articleCode}>
                        <td>{index}</td>
                        <td>{n.articleTitle}</td>
                        <td>{n.articleDate}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <Pagination count={notice.totalPages} page={currentPage} onChange={handlePageChange}
                        variant="outlined" color="primary"/>
        </div>
    );
}
export default Notice;
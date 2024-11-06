import {useEffect, useState} from "react";
import api from "../../main/axios/axiosApi"
import Pagination from "@mui/material/Pagination";
import {useSearch} from "../../searchMenu/SearchContext";

const AdminOrder = () => {

    const {keyword, setKeyword, searchCate, setSearchCate} = useSearch();

    const [currentPage, setCurrentPage] = useState(1);

    const [orderList, setOrderList] = useState([]);

    useEffect(() => {
        fetchOrderList();
    }, [currentPage, keyword]);

    const fetchOrderList = async () => {
        try {
            const response = await api.get("hoopi/admin/order");
            setOrderList(response.data);
        } catch (error) {
            console.log(error);
        }
    }

    // 주문 디테일 보기 버튼 클릭 시
    const handleOrderDetail = (orderCode) => {
        try{
            const response = api.get('hoopi/admin/order/detail');
        } catch (error){
            console.log(error);
        }
    }

    const handlePageChange = (event, page) => {
        setCurrentPage(page);
    };

    const handleDate = (e)=>{
        return e.replace('T', '  ').slice(0, 20);
    }

    return(
        <div className="adminOrder-container">
            <div className="adminOrder-box">
                <div className="adminOrder-box-title">
                    <h3>관리자 주문</h3>
                </div>
                <div className="adminOrder-box-table">
                    <table>
                        <thead>
                        <tr>
                            <th>회원 아이디</th>
                            <th>주문 날짜</th>
                            <th>주문 번호</th>
                            <th>주문 상태</th>
                            <th>자세히 보기</th>
                        </tr>
                        </thead>
                        <tbody>
                        {orderList?.map(order => (
                            <tr>
                                <td>{order.id}</td>
                                <td>{handleDate(order.orderDate)}</td>
                                <td>{order.orderCode}</td>
                                <td>{order.status}</td>
                                <td>
                                    <button
                                        onClick= {() => handleOrderDetail(order.orderCode)}>
                                        자세히 보기
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
                <Pagination count={orderList?.totalPages} page={currentPage} onChange={handlePageChange}
                            variant="outlined" color="primary"/>
            </div>
        </div>
    )
}
export default AdminOrder;
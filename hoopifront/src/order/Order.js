import {useEffect, useState} from "react";
import axios from "axios";
import './order.css';
import Pagination from "@mui/material/Pagination";

const Order = () => {

    const id = localStorage.getItem("id");


    const [currentPage, setCurrentPage] = useState(1);

    useEffect(() => {
        fetchOrders(currentPage);
    }, [currentPage]);

    const [orders, setOrders] = useState({ content: [], totalPages: 0 });
    const fetchOrders = async (page) => {
        try{
            const response = await axios.get('https://hoopi.co.kr/api/hoopi/order', {params: {
                id: id,
                page: page - 1,
                size: 10
                }});
            setOrders(response.data);
        } catch (error){
            console.log(error);
        }
    }

    const handlePageChange = (event, page) => {
        setCurrentPage(page);
    };

    return(
        <div className="order-container">
            <h3>주문 내역</h3>
            <div className="order-box">
                {orders?? <h4>주문 내역이 존재하지 않습니다.</h4>.content?.map(order => (
                    <>
                        <div className="order-user">
                            <h4>{}</h4>
                            <h4>배송지</h4>
                            <table>
                                <tbody>
                                <tr>
                                    <td>이름</td>
                                    <td>
                                        <button>배송지 변경</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>핸드폰 번호</td>
                                </tr>
                                <tr>
                                    <td>주소지</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div className="order-product">
                            <table>
                                <thead>
                                <tr>
                                    <th>상품명</th>
                                    <th>수량</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td>
                                        <div>

                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </>
                ))}
                <Pagination count={orders.totalPages} page={currentPage} onChange={handlePageChange}
                            variant="outlined" color="primary"/>/>
            </div>
        </div>
    )
}

export default Order;
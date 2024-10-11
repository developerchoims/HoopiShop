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
            console.log(response.data.content);
        } catch (error){
            console.log(error);
        }
    }

    const handlePageChange = (event, page) => {
        setCurrentPage(page);
    };

    const handleDate = (e)=>{
        return e.replace('T', ' ').slice(0, 19);
    }

    return(
        <div className="order-container">
            <h3>주문 내역</h3>
            <div className="order-box">
                {orders.content?.map(order => (
                        <>
                            <div className="order-user">
                                <h4>{handleDate(order.orderDate)}</h4>
                                <h4>배송지</h4>
                                <table>
                                    <tbody>
                                    <tr>
                                        <td>{order.address.addressName}</td>
                                        <td>
                                            <button>배송지 변경</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>{order.address.addressPhone}</td>
                                    </tr>
                                    <tr>
                                        <td>{order.address.address}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            {order.orderDetails?.map(od => (
                                <div className="order-product">
                                    <table>
                                        <thead>
                                        <tr>
                                            <th>상품명</th>
                                            <th>수량</th>
                                            <th>가격</th>
                                            <th>총 가격</th>
                                            <th></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>{od.productName}</td>
                                            <td>{od.quantity}</td>
                                            <td>{od.orderAmount}</td>
                                            <td>{od.totalPrice}</td>
                                            <td>
                                                <div>
                                                    <img src={od.productImg}/>
                                                </div>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            ))}
                        </>
                        )
                    )}
                <Pagination count={orders.totalPages} page={currentPage} onChange={handlePageChange}
                            variant="outlined" color="primary"/>
            </div>
        </div>
    )
}

export default Order;
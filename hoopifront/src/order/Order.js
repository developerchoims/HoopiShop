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
        return e.replace('T', '  ').slice(0, 20);
    }

    return(
        <div className="order-container">
            <div className= "order-semi-container">

            <div className="order-box">
                {orders.content?.map(order => (
                        <>
                            <div className="order-title">
                                <h3>주문 내역</h3>
                                <button>주문 취소</button>
                            </div>
                            <div className="order-user">
                                <div className="order-date">
                                    <h5>{handleDate(order.orderDate)}</h5>
                                    <h5>{order.orderStatus}</h5>
                                </div>
                                <h5>배송지</h5>
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
                            <br/>
                            <div className="order-product">
                                {order.orderDetails?.map(od => (
                                    <table>
                                        <thead>
                                        <tr>
                                            <th>상품명</th>
                                            <th>수량</th>
                                            <th>가격</th>
                                            <th>총 가격</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>{od.productName}</td>
                                            <td>{od.quantity}</td>
                                            <td>{od.orderAmount}</td>
                                            <td>{od.totalPrice}</td>
                                            <td rowSpan={2}>
                                                <div>
                                                    <img src={od.productImg}/>
                                                </div>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                ))}
                                <br/>
                            </div>
                        </>
                    )
                    )}
                    </div>

                    <Pagination count={orders.totalPages} page={currentPage} onChange={handlePageChange}
                            variant="outlined" color="primary"/>
                    </div>
                    </div>
                    )

                }

export default Order;
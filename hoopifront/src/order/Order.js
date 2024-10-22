import {useEffect, useState} from "react";
import axios from "axios";
import './order.css';
import Pagination from "@mui/material/Pagination";
import {useSearch} from "../searchMenu/SearchContext";

const Order = () => {

    const id = localStorage.getItem("id");

    const {keyword, setKeyword, searchCate, setSearchCate} = useSearch();

    const [currentPage, setCurrentPage] = useState(1);

    useEffect(() => {
        fetchOrders(currentPage);
    }, [currentPage, keyword]);

    const [orders, setOrders] = useState({ content: [], totalPages: 0 });
    const fetchOrders = async (page) => {
        try{
            const response = await axios.get('https://hoopi.co.kr/api/hoopi/order', {params: {
                id: id,
                page: page - 1,
                size: 10,
                searchCate: searchCate,
                keyword: keyword,
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

    const [refundDisplay, setRefundDisplay] = useState('none');
    const [orderCode, setOrderCode] = useState('');
    const [reason, setReason] = useState('단순 변심');

    const handleRefundDisplay = (oc) => {
        setRefundDisplay('block');
        setOrderCode(oc);
    }

    const handleRefundReason = (e) => {
        setReason(e.target.value);
    }

    const handleRefundCancel = () => {
        setReason('');
        setOrderCode('');
        setRefundDisplay('none');
    }

    const handleRequestRefund = () => {
        axios.put('https://hoopi.co.kr/api/hoopi/order', {
            orderCode,
            reason
        })
            .then(response => {
                alert(response.data);
                setReason('');
                setOrderCode('');
                setRefundDisplay('none');
        })
        .catch(error => {
            console.log(error);
        });

    }

    return(
        <div className="order-container">
            <div className="order-semi-container">

                <div className="order-box">
                    {orders.content?.map(order => (
                            <>
                                <div className="order-title">
                                    <h3>주문 내역</h3>
                                    <h5>{handleDate(order.orderDate)}</h5>
                                    <h5>{order.orderStatus}</h5>
                                    <button onClick={()=>handleRefundDisplay(order.orderCode)}>주문 취소</button>
                                </div>
                                <div className="order-user">
                                    <div className="order-address">
                                        <h3>배송지</h3>
                                        <table>
                                            <tbody>
                                            <tr>
                                                <td>수취인 성함 : </td>
                                                <td>{order.address.addressName}</td>
                                            </tr>
                                            <tr>
                                                <td>수취인 연락처 : </td>
                                                <td>{order.address.addressPhone}</td>
                                            </tr>
                                            <tr>
                                                <td>수취인 주소지 : </td>
                                                <td>{order.address.address}</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                        <button>배송지 변경</button>
                                    </div>
                                </div>
                                <br/>
                                <div className="order-product">
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
                                        {order.orderDetails?.map(od => (
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
                                            ))}
                                        </tbody>
                                    </table>
                                        <br/>
                                </div>
                            </>
                        )
                    )}


                    <div className="order-pagination-box">
                        <Pagination count={orders.totalPages} page={currentPage} onChange={handlePageChange}
                                    variant="outlined" color="primary"/>
                    </div>
                </div>
            </div>
            <div className='order-refund-container' style={{display:refundDisplay}}>
                <div className='order-refund-box'>
                    <table>
                        <thead>
                        <tr>
                            <th>환불 신청</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>환불 이유</td>
                        </tr>
                        <tr>
                            <td>
                                <select value={reason} onChange={handleRefundReason}>
                                    <option>단순 변심</option>
                                    <option>수량 변경 후 재주문</option>
                                    <option>품목 변경 후 재주문</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <button onClick={handleRefundCancel}>취소</button>
                                <button onClick={handleRequestRefund}>환불</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    )

}

export default Order;
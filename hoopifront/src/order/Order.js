const Order = ({cartdetail}) => {

    const id = localStorage.getItem("id");

    return(
        <div className="order-container">
            <h3>주문하기</h3>
            <div className="order-box">
                <div className="order-user">
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

                </div>
            </div>
        </div>
    )
}

export default Order
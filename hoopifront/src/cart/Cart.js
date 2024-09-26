const Cart = () => {

    return(
        <div className="cart-container">
            <div className="cart-box">
                <table>
                    <thead>
                    <tr>
                        <th><input type="checkbox" className="cart-checkbox"/></th>
                        <th>
                            <button>상품 삭제</button>
                            <button>전체 삭제</button>
                        </th>
                        <th>수량</th>
                        <th>가격</th>
                        <th>주문</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><input type="checkbox" className="cart-checkbox"/></td>
                        <td><img/></td>
                        <td>수량</td>
                        <td>가격</td>
                        <td><button>주문</button></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    );
}
export default Cart;
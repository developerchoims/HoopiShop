const Cart = () => {

    return(
        <div className="cart-container">
            <div className="cart-box">
                <table>
                    <thead>
                    <tr>
                        <th><input type="checkbox" className="cart-checkbox"/></th>
                        <th><button>상품 삭제</button></th>
                        <th><button>전체 삭제</button></th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><input type="checkbox" className="cart-checkbox"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    );
}
export default Cart;
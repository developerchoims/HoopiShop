import {useState} from "react";
import axios from "axios";

const Cart = () => {

    const id = localStorage.getItem("id");
    const [cartdetail, setCartdetail] = useState(null);

    useEffect(() => {
        fetchCart();
    }, [])

    const fetchCart = async () => {
        try{
            const response = await axios.get(`http://hoopi.p-e.kr/api/hoopi/cart`,{params:{id:id}});
            console.log(response.data);
            setCartdetail(response.data);
        }catch(e){
            console.log(e);
        }
    }

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
                    {
                        cartdetail?.map((product, index) => (
                            <tr key={product.productCode}>
                                <td><input type="checkbox" className="cart-checkbox"/></td>
                                <td><img src={product.imgUrl} alt={product.imgUrl}/></td>
                                <td>{product.quantity}</td>
                                <td>{product.cartAmount}</td>
                                <td>
                                    <button>주문</button>
                                </td>
                            </tr>
                        ))
                    }
                    </tbody>
                </table>
            </div>
        </div>
    );
}
export default Cart;
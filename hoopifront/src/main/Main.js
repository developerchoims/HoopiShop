import './Main.css';
import Series from "./Series";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link, useLocation} from "react-router-dom";
const Main = () => {

    const role = localStorage.getItem("role");

    const location = useLocation();
    const path = location.pathname;
    useEffect(() => {
            productNew();
    }, []);

    const[newProduct, setNewProduct] = useState([]);
    const productNew = async () => {
        try{
            const response = await axios.get('http://hoopi.p-e.kr/api/hoopi/product-new');
            setNewProduct(response.data);
            console.log(response.data);
        } catch (e){
            console.log(e);
        }
    }


    return (
        <div className="main-container">
            <Series/>
            <div className= "main-letter-container">신상품</div>
            <div className="main-new-product-container">
                {newProduct?.map(np=> (
                    <Link to={`/product/${np.product.productCode}/${np.product.name}`}>
                        <div className="main-new-product-box" key={np.product.productCode} id={np.product.productCode}>
                            <table>
                                <thead>
                                <tr>
                                    <td colSpan={2}>
                                        <div>
                                            <img src={np.imgUrl} alt={np.product.name}/>
                                        </div>
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <th colSpan={2}>{np.product.name}</th>
                                </tr>
                                <tr>
                                    <td>{role === 'user' ? np.product.price : role === 'admin' ? 0 : '로그인 후 가격 확인'}</td>
                                    <td>{np.product.name}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </Link>
                ))}
            </div>
        </div>

    );
}

export default Main;
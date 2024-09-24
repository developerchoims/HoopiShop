import './Series.css'
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";

const Series = () => {

    const role = localStorage.getItem('role');

    useEffect(() => {
        productPopular();
    }, []);

    const[popularProduct, setPopularProduct] = useState([]);
    const productPopular = async () => {
        try{
            const response = await axios.get('http://hoopi.p-e.kr/api/hoopi/product-popular');
            setPopularProduct(response.data);
            console.log(response.data);
        } catch (e){
            console.log(e);
        }
    }

    return(

        <div className="series-container">
            <div className="series-letter-container">인기 상품</div>
            <div className="series-img-container">
                {popularProduct.map(pp => (
                    <Link key= {pp.product.productCode} to={`/product/${pp.product.productCode}/${pp.product.name}`}>
                        <div className="series-img-box">
                            <table>
                                <thead>
                                <tr>
                                    <td colSpan={2}>
                                        <div>
                                            <img src={pp.imgUrl} alt={pp.product.name}/>
                                        </div>
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <th colSpan={2}>{pp.product.name}</th>
                                </tr>
                                <tr>
                                    <td>{role === 'user' ? pp.product.price : role === 'admin' ? 0 : '로그인 후 가격 확인'}</td>
                                    <td>{pp.product.name}</td>
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
export default Series;
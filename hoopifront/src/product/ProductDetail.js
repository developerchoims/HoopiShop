import './productDetail.css'
import {useParams} from "react-router-dom";
import {useState} from "react";
const ProductDetail = () => {

    const role = localStorage.getItem("role");
    const id = localStorage.getItem("id");

    useEffect(() => {

    }, [])

    //productCode값 가져와서 productDetail값 가져오기
    const {productCode, name} = useParams();
    const [productDetail, setProductDetail] = useState();
    const fetchProductDetail = async () => {
        try{
            const response = await axios.get(`http://hoopi.p-e.kr/api/hoopi/product/${productCode}`, {});
            setProductDetail(response.data);
        } catch (error) {
            console.log(error);
        }
    }

    return(
        <div className='productDetail-container'>
            <div className='productDetail-box'>
                <div className='productDetail-header'>
                    <div className='productDetail-img'>
                        <img src={productDetail.imgUrl} alt='logo'/>
                    </div>
                    <div className='productDetail-info'>
                        <h4>{productDetail.product.name}</h4>
                        <p>{productDetail.product.price}</p>
                        <div className='productDetail-price'>
                            <input type='number' min='1' max='5' step='1' />
                            총 금액 : 00000원
                        </div>
                        <div className='productDetail-info-button'>
                            <button>장바구니</button>
                            <button>결제하기</button>
                        </div>
                    </div>
                </div>
                <div className='productDetail-body'>
                    <div className='productDetail-body-img'>
                        <img src='/logo192.png' alt='logo'/>
                    </div>
                    <div className='productDetail-body-info'>
                        배송비 별도 : 3,500원
                    </div>
                </div>
            </div>
        </div>
    );
}

export {ProductDetail}
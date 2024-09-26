import './productDetail.css'
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
const ProductDetail = () => {

    const role = localStorage.getItem("role");
    const id = localStorage.getItem("id");

    useEffect(() => {
        fetchProductDetail();
    }, [])

    //productCode값 가져와서 productDetail값 가져오기
    const {productCode, name} = useParams();
    const [productDetail, setProductDetail] = useState();
    const fetchProductDetail = async () => {
        try{
            const response = await axios.get(`http://hoopi.p-e.kr/api/hoopi/product/${productCode}`, {});
            setProductDetail(response.data);
            console.log(response.data);
        } catch (error) {
            console.log(error);
        }
    }

    // 장바구니에 담길 객체 설정
    const [cartRequestDto, setCartRequestDto] = useState({'id':id, 'productCode':productCode });
    const [totalPrice, setTotalPrice] = useState(productDetail?.product?.price);
    const handleTotalPrice = (e) => {
        let quantity = e.target.value;
        let price = productDetail?.product?.price
        setTotalPrice(price * quantity);
        setCartRequestDto((...prevState) =>({
            ...prevState,
            "quantity" : quantity,
            "totalPrice": price
        }));
    }
    // 장바구니 담기 구현
    const handleCart = () => {
        axios.post(`http://hoopi.p-e.kr/api/hoopi/cart`, cartRequestDto)
            .then(response => {
                alert(response.data);
            })
            .catch(error => {
                console.log(error);
            })
    }


    return(
        <div className='productDetail-container'>
            <div className='productDetail-box'>
                <div className='productDetail-header'>
                    <div className='productDetail-img'>
                        <img src={productDetail?.imgUrl} alt='logo'/>
                    </div>
                    <div className='productDetail-info'>
                        <h4>{productDetail?.product.name}</h4>
                        <p>{productDetail?.product.price}</p>
                        <div className='productDetail-price'>
                            <input type='number' min='1' max='5' step='1' defaultValue='1' id='quantity' onChange={handleTotalPrice}/>
                            총 금액 : {totalPrice?? productDetail?.product?.price}원
                        </div>
                        <div className='productDetail-info-button'>
                            <button onClick={handleCart}>장바구니</button>
                            <button>결제하기</button>
                        </div>
                    </div>
                </div>
                <div className='productDetail-body'>
                    <div className='productDetail-body-img'>
                        <img src={productDetail?.boardImgUrl ?? productDetail?.imgUrl} alt='logo'/>
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
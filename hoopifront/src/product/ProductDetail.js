import './productDetail.css'
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import api from "../main/axios/axiosApi";
const ProductDetail = () => {

    const role = localStorage.getItem("role");
    const id = localStorage.getItem("id");

    const { productCode, name } = useParams();
    const [productDetail, setProductDetail] = useState();
    const [totalPrice, setTotalPrice] = useState();
    const [cartRequestDto, setCartRequestDto] = useState({
        id: id,
        productCode: productCode,
        quantity: 1,
        cartAmount: 0, // 초기값으로 0 설정
    });

// productDetail이 변경될 때 cartAmount 업데이트
    useEffect(() => {
        if (productDetail && productDetail.product?.price) {
            setTotalPrice(productDetail.product.price);
            setCartRequestDto((prevState) => ({
                ...prevState,
                cartAmount: productDetail.product.price, // price로 초기값 설정
            }));
        }
    }, [productDetail]);

// productCode값 가져와서 productDetail값 가져오기
    useEffect(() => {
        fetchProductDetail();
    }, []);

    const fetchProductDetail = async () => {
        try {
            const response = await api.get(
                `hoopi/product/${productCode}`
            );
            setProductDetail(response.data);
            console.log(response.data);
        } catch (error) {
            console.log(error);
        }
    };

// handleTotalPrice 함수
    const handleTotalPrice = (e) => {
        let quantity = e.target.value;
        let price = productDetail?.product?.price;
        setTotalPrice(price * quantity);
        setCartRequestDto((prevState) => ({
            ...prevState,
            quantity: quantity,
            cartAmount: price * quantity, // cartAmount 업데이트
        }));
    };

// 장바구니 담기 구현
    const handleCart = () => {
        api.post(`http://hoopi.p-e.kr/api/hoopi/cart`, cartRequestDto)
            .then((response) => {
                alert(response.data);
            })
            .catch((error) => {
                console.log(error);
            });
    };



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
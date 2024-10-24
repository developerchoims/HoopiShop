import {useEffect, useState} from "react";
import './cart.css';
import api from "../main/axios/axiosApi";
import IamPort from "./IamPort";

const Cart = () => {

    const id = localStorage.getItem("id");
    const [cartdetail, setCartdetail] = useState([]);
    const [addresses, setAddresses] = useState([]);
    const [addressDisplay, setAddressDisplay] = useState('none')
    const[selectedAddress, setSelectedAddress] = useState(null);

    useEffect(() => {
        fetchCart();
    }, [])

    const fetchCart = async () => {
        try{
            const response = await api.get(`hoopi/cart`,{params:{id:id}});
            console.log(response.data);
            setCartdetail(response.data.carts);
            setAddresses(response.data.addresses);
            console.log(response.data.addresses);
        }catch(e){
            console.log(e);
        }
    }

    // 주소창 보이기
    const handleAddressDisplay = () => {
        if(addressDisplay === 'block') {
            setAddressDisplay('none');
        } else {
            setAddressDisplay('block');
        }
        setSelectedAddress(null);
    }
    //주소지 선택
    const handleSelectAddress = (addressCode) => {
        if(selectedAddress === null) {
            setSelectedAddress(addressCode);
        } else {
            setSelectedAddress(null);
        }
    }

    // 수량 변경, 가격 변경 시 DB 수정
    const handleUpdate = (e, productCode, quantity, cartAmount) => {
        const newQuantity = handleQuantityChange(e.target.value);
        const rawPrice = cartAmount/quantity;
        api.put(`hoopi/cart`, {
            cartCode: cartdetail[0].cartCode
            , productCode: productCode
            , quantity: newQuantity
            , cartAmount: rawPrice * newQuantity})
            .then(response => {
                console.log(response.data);
                fetchCart();
            })
            .catch(error => {
                console.log(error);
            });
    }

    // 0보다 작아지는 걸 막기
    function handleQuantityChange(quantity) {
        const minValue = 1;
        if (quantity < minValue) {
            return minValue;
        } else {
            return quantity;
        }
    }
    // 전체 선택/해제 핸들러
    const [allChecked, setAllChecked] = useState(false);
    const handleSelectAll = (event) => {
        const { checked } = event.target;
        if (checked) {
            const allIds = cartdetail.map(product => product.productCode);
            setSelectedIds(allIds);
        } else {
            setSelectedIds([]);
        }
        setAllChecked(checked);
    };

    // 선택된 체크 박스의 아이디 불러오기
    const [selectedIds, setSelectedIds] = useState([]);
    const handleSelectPart = (event) => {
        const { id, checked } = event.target;
        if (checked) {
            // 체크된 경우, id 추가
            setSelectedIds(prev => {
                const newSelectedIds = [...prev, id];
                // 모든 아이템이 선택되었는지 확인
                if (newSelectedIds.length === cartdetail.length) {
                    setAllChecked(true);
                }
                return newSelectedIds;
            });
        } else {
            // 체크 해제된 경우, id 제거
            setSelectedIds(prev => {
                const newSelectedIds = prev.filter(x => x !== id);
                // 하나라도 해제되면 전체 선택 체크 해제
                setAllChecked(false);
                return newSelectedIds;
            });
        }
        handleCheckedCartDetail();
    };

    // 상품 부분 삭제 시 DB 수정
    const handleDeletePart = () => {
        console.log("selectedIds확인",selectedIds);
        if(selectedIds.length === 0){
            alert("삭제할 상품을 선택해주세요.");
            return;
        }
        api.delete('hoopi/cart-part', {params:{
            cartCode: cartdetail[0].cartCode,
            productCodes: selectedIds.join(',')
            }})
            .then(response => {
                alert(response.data);
                fetchCart();
            })
            .catch(error=>{
                console.log(error);
            });
    }

    // 상품 전체 삭제 시 DB 수정
    const handleDeleteAll = () => {
        api.delete('hoopi/cart-all', {params:{cartCode: cartdetail[0].cartCode}})
            .then(response =>{
                alert(response.data);
                fetchCart();
            })
            .catch(error=>{
                console.log(error);
            });
    }

    // 선택된 id와 비교해서 cartDetail 가져오기
    const handleCheckedCartDetail = () => {
        if( selectedIds.length > 0){
            return cartdetail?.filter(x => selectedIds.includes(x.productCode));
        }
    }


    return(
        <>
            <div className='cart-address-container'>
                <div className='cart-address-box'>
                    <h4>배송지 지정</h4>
                    <div className='cart-address-select' value={selectedAddress}>
                        <button onClick={handleAddressDisplay}>{addressDisplay === 'block'?<h3>배송지를 지정하세요. ▼ </h3>:<h3>배송지를 지정하세요. ▲</h3> }</button>
                        {addresses?.length === 0
                            ? <h3 style={{display: addressDisplay}}>"주소를 불러올 수 없습니다."</h3>
                            : addresses?.map((address, index) => (
                                <div id={address.addressCode} value={address.addressCode} key={address.addressCode}
                                     style={{
                                            display: selectedAddress === address.addressCode || addressDisplay === 'block' ? 'block' : 'none',
                                            backgroundColor: selectedAddress === address.addressCode ? '#c9dcf9' : 'white'
                                            }}
                                     onClick={() => handleSelectAddress(address.addressCode)}>
                                    <table>
                                        <thead>
                                        <tr>
                                            <th colSpan={2}> 주소 {index + 1}</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>수취인 성함 :</td>
                                            <td>{address.addressName}</td>
                                        </tr>
                                        <tr>
                                            <td>수취인 연락처 :</td>
                                            <td>{address.addressPhone}</td>
                                        </tr>
                                        <tr>
                                            <td>수취인 우편번호 :</td>
                                            <td>{address.postcode}</td>
                                        </tr>
                                        <tr>
                                            <td>수취인 주소지 :</td>
                                            <td>{address.address}</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            ))}
                    </div>
                </div>
            </div>
            <div className="cart-container">
                <div className="cart-box">
                    <table>
                        <thead>
                        <tr>
                            <th><input type="checkbox" className="cart-checkbox checkbox-parents"
                                       checked={allChecked} onChange={handleSelectAll}/></th>
                            <th colSpan={2}>
                                <button onClick={handleDeletePart}>선택 삭제</button>
                                <button onClick={handleDeleteAll}>전체 삭제</button>
                            </th>
                            <th>수량</th>
                            <th>가격</th>
                            <th>
                                <IamPort cartdetail={handleCheckedCartDetail()} buttonName='선택 주문' address={selectedAddress}/>
                                <IamPort cartdetail={cartdetail} buttonName='전체 주문' address={selectedAddress}/>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        {
                            !Array.isArray(cartdetail) || cartdetail.length === 0 ?
                                <tr>
                                    <td colSpan={5}>"장바구니에 담긴 상품이 없습니다."</td>
                                </tr>
                                : cartdetail?.map((product, index) => (
                                    <tr key={product.productCode}>
                                        <td><input type="checkbox" className="cart-checkbox checkbox-child"
                                                   id={product.productCode}
                                                   checked={selectedIds.includes(product.productCode)}
                                                   onChange={handleSelectPart}/></td>
                                            <td colSpan={2}><img src={product.imgUrl} alt={product.imgUrl}/></td>
                                            <td>
                                                <input type='number' value={product.quantity} min='1'
                                                       onChange={(e) => handleUpdate(e, product.productCode, product.quantity, product.cartAmount)}/>
                                            </td>
                                            <td><p defaultValue={product.cartAmount}>{product.cartAmount}</p></td>
                                        </tr>
                                    ))
                            }
                            </tbody>
                        </table>
                    </div>
                </div>

        </>
    );
}
export default Cart;
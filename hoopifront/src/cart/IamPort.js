
import api from "../main/axios/axiosApi";
import PortOne from "@portone/browser-sdk/v2";

const IamPort = ({ cartdetail, buttonName, address }) => {
    const handlePayment = async () => {
        const orderName = cartdetail.length === 1 ? cartdetail[0].name : `${cartdetail[0].name} 외 ${cartdetail.length - 1}건`;
        const totalAmount = cartdetail.reduce((total, p) => total + p.cartAmount, 0);
        const paymentId = crypto.randomUUID();
        const method = "CARD";
        const storeId = "store-d9a9dcb7-489f-4574-9652-8920ac17fe37";

        try {
            const response = await PortOne.requestPayment({
                storeId,
                channelKey: "channel-key-237352d9-f99e-44e9-80fe-8b5a016a0581",
                paymentId: `payment-${paymentId}`,
                orderName,
                totalAmount,
                currency: "CURRENCY_KRW",
                payMethod: method,
                windowType: {
                    pc: 'IFRAME',
                }
        });

            if (response.code != null) {
                return alert(response.message);
            }

            let productCodes = cartdetail.map(p => p.productCode);
            let paymentAmount = cartdetail.reduce((sum, p) => sum + p.cartAmount, 0);

            const notified = await api.post(`hoopi/order`, {
                cartCode: cartdetail[0].cartCode,
                productCode: productCodes,
                storeId,
                address,
                paymentRequestDto : {
                    paymentCode: paymentId,
                    method,
                    bank: '나이스페이먼츠',
                    paymentAmount
                }
            });

            alert(notified.data);
            window.location.href = '/order';
        } catch (error) {
            console.error(error);
            alert("결제 처리 중 오류가 발생했습니다.");
        }
    };

    return (
        <>
            <button onClick={handlePayment}>{buttonName}</button>
        </>
    );
};

export default IamPort;

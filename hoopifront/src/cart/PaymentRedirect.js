import React from 'react';
import { useParams, useLocation } from 'react-router-dom';

const PaymentRedirect = () => {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const status = searchParams.get('status');

    useEffect(() => {
        if (status === 'success') {
            alert('결제가 성공적으로 처리되었습니다!');
            // 여기서 추가 로직 수행 (예: 데이터베이스 업데이트, 로컬 스토리지 클리어 등)
            //window.location.href = '/order-success'; // 성공 페이지로 리디렉션
        } else {
            alert('결제에 실패했습니다. 다시 시도해주세요.');
            //window.location.href = '/payment-retry'; // 결제 재시도 페이지로 리디렉션
        }
    }, [status]);

    return (
        <div>Loading...</div>
    );
};

export default PaymentRedirect;

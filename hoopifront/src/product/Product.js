import {useEffect, useState} from "react";
import axios from "axios";
import {useSearch} from "../searchMenu/SearchContext";
import Pagination from "@mui/material/Pagination";
import './product.css';
import {Link} from "react-router-dom";

const Product = () => {

    const role = localStorage.getItem("role");

    const {keyword, setKeyword, searchCate, setSearchCate} = useSearch();

    // 페이지네이션
    const [currentPage, setCurrentPage] = useState(1);
    const handlePageChange = (event, page) => {
        setCurrentPage(page);
    };

    // products 불러오기
    const [products, setProducts] = useState([]);

    useEffect(() => {
        getProduct(role, currentPage);
    }, [currentPage, keyword, searchCate]);

    const getProduct = async (role, page) => {
        const response = await axios.get('http://hoopi.p-e.kr/api/hoopi/product', {
            params:{role: role, keyword: keyword, searchCate: searchCate, page: page-1, size: 10}
        });
        setProducts(response.data);
    }



    return(
        <>
            <div className='product-container'>
                {products.content.map((product, index) => (
                    <Link to={`/product/${product.name}`} key={index}>
                        <div className="product-box">
                            <img src={product.imgUrl} alt={product.name} className="product-img"/>
                            <div className="product-content">
                                <h4>{product.name}</h4>
                                <p>{product.price}원</p>
                            </div>
                        </div>
                    </Link>
                ))}
            </div>
            <Pagination count={products.totalPages} page={currentPage} onChange={handlePageChange}
                        variant="outlined" color="primary"/>
        </>
    )
}
export default Product;
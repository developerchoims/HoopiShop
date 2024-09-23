import Header from "./header";
import UserBody from "./user/userBody";
import {useEffect} from "react";
import {Outlet, useLocation} from "react-router-dom";
import Board from "./board/Board";
import Product from "../product/Product";
import {ProductDetail} from "../product/ProductDetail";

const AdminMain = () => {
    const id = localStorage.getItem("id");
    const role = localStorage.getItem("role");

    const location = useLocation();
    const path = location.pathname;

    useEffect(() => {
        fetchAdmin();
    }, []);

    const fetchAdmin = () => {
        if(id == null || id == '' || role == null || role == '' || role == 'user'){
            window.location.href = '/';
            alert("관리자만 접근가능합니다.");
        }
    }

    return(
        <div>
            <Header/>
            <Outlet/>
        </div>
    );
}

export default AdminMain
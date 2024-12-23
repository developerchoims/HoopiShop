import api from "../main/axios/axiosApi";
import {useEffect, useState} from "react";
import {Link, useLocation} from "react-router-dom";
import {useSearch} from "./SearchContext";

import './menu.css';
import './Search.css';


const Menu = () => {
    const { keyword, setKeyword, searchCate, setSearchCate } = useSearch();

    const location = useLocation();
    const path = location.pathname;

    const role = localStorage.getItem("role");
    const id = localStorage.getItem("id");

    const [boardId, setBoardId] = useState('');
    const [board, setBoard] = useState({});
    const [category, setCategory] = useState([]);
    const[visible, setVisible] = useState(false);
    const[categoryVisible, setCategoryVisible] = useState('flex');
    const[menu, setMenu] = useState();

    useEffect(() => {
        fetchMenuCategory();
        setKeyword('');
    }, [path]);

    const fetchMenuCategory = async () => {
        try {
            // 메뉴 설정
            const response = await api.get('hoopi/menu');
            setMenu(response.data);
            if (role === 'admin') {
                setVisible(true);
            } else {
                setVisible(false);
            }
            // board 설정
            let tempBoardId = '';
            if (path.includes('user')) {
                setCategoryVisible('flex');
                tempBoardId = 'user';
            } else if (!path.includes('admin') && path.includes('order')) {
                setCategoryVisible('flex');
                tempBoardId = 'order';
            } else if (path.includes('product')) {
                setCategoryVisible('flex');
                tempBoardId = 'product';
            } else if (path.includes('notice')) {
                setCategoryVisible('flex');
                tempBoardId = 'notice';
            } else if (path.includes('admin/main')) {
                setCategoryVisible('flex');
                tempBoardId = 'user';
            } else if(path.includes('admin/adminOrder')){
                setCategoryVisible('flex');
                tempBoardId = 'adminOrder';
            } else {
                setCategoryVisible('none');
            }
            console.log(tempBoardId);
            const boardResponse = await api.get('hoopi/board', { params: { boardId: tempBoardId } });
            setBoardId(tempBoardId); // boardId 업데이트
            setBoard(boardResponse.data);

            const categoryResponse = await api.get('hoopi/category', {params: {boardCode: boardResponse.data.boardCode}});
            setCategory(categoryResponse.data);
            // 첫 번째 카테고리의 ID를 searchCate 상태에 설정
            if (categoryResponse.data && categoryResponse.data.length > 0) {
                setSearchCate(categoryResponse.data[0].categoryId);
            }
            console.log(boardResponse.data.boardCode);
        } catch (error){
            console.error(error);
        }
    }

    const handleKeyword = (e) => {
        setKeyword(e.target.value);
    }

    const handleSearchCate = (e) => {
        setSearchCate(e.target.value);
    }
    return(
        <>
            <div className='menu-container'>
                <div className='menu-box'>
                    <table>
                        <tbody>
                        <tr>
                            {menu?.map(m => (
                                <td style={{
                                    display: visible && m.name === '회원' ? 'display'
                                           : visible && m.name === '관리자주문' ? 'display'
                                            :!visible && m.name !== '회원' && m.name !== '관리자주문' ? 'display'
                                            : visible && m.name !== '주문' ? 'display'
                                            :'none'
                                }} key={m.boardCode}>
                                    <Link to={`${visible ? 'admin/' : ''}${m.boardId}`}>{m.name}</Link>
                                </td>
                            ))}
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div className='search-container' style={{display: categoryVisible}}>
                <div className='search-input-box'>
                    <select className='search-input-box' value={searchCate} onChange={handleSearchCate}>
                        {category.map(m => (
                            <option key={m.id} value={m.categoryId}>
                                {m.name}
                            </option>
                        ))}
                    </select>
                    <input className='keyword' value={keyword} onChange={handleKeyword} placeholder='검색어를 입력하세요'/>
                    <button>검색</button>
                </div>
            </div>
        </>
    );
}

export default Menu;
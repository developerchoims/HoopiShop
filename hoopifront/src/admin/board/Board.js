import {useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import './board.css';

const Board = () => {

    const id = localStorage.getItem("id");

    const location = useLocation();
    const path = location.pathname;

    const [board, setBoard] = useState();
    const [visible, setVisible] = useState(false);
    useEffect(() => {
        fetchBoard();
    }, [path]);

    const fetchBoard = async () => {
        let tempBoardId = '';
        if (path.includes('user')) {
            tempBoardId = 'user';
        } else if (path.includes('order')) {
            tempBoardId = 'order';
        } else if (path.includes('product')) {
            tempBoardId = 'product';
        } else if (path.includes('notice')) {
            tempBoardId = 'notice';
        } else if (path.includes('admin/main')){
            tempBoardId = 'user';
        } else {
            tempBoardId = 'product';
        }
        setVisible(path.includes('product'));
        try {
            console.log(tempBoardId);
            console.log(path);
            const response = await axios.get('http://hoopi.p-e.kr/api/hoopi/board', { params: { boardId: tempBoardId } });
            setBoard(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    // 이미지 미리보기
    const [files, setFiles] = useState([]);
    const [imagePreviews, setImagePreviews] = useState([]);

    const handleFileChange = (event) => {
        const newFiles = Array.from(event.target.files);
        setFiles(newFiles);

        const newImagePreviews = newFiles.map(file => {
            const reader = new FileReader();
            reader.onload = (e) => {
                setImagePreviews(prev => [...prev, e.target.result]);
            };
            reader.readAsDataURL(file);
            return reader;
        });
    };

    // article 설정
    const [article, setArticle] = useState({"id": id, "boardCode": board?.boardCode});
    const handleArticle = (e) => {
        const{id, value} = e.target;
        setArticle((prevState)=> ({
            ...prevState,
            [id] : value
        }))
    }

    // product 설정
    const [product, setProduct] = useState({"name":"", "price":0, "stock":0});
    const handleProduct = (e) => {
        const{id, value} = e.target;
        setProduct((prevState)=> ({
            ...prevState,
            [id] : value
        }))
    }

    const handleUpload = async () => {
        const formData = new FormData();
        const combinedData = {
            product: product,
            article: article
        };
        formData.append('data', new Blob([JSON.stringify(combinedData)], {type: 'application/json'}));

        Array.from(files).forEach(file => {
            formData.append('imgs', file);
        });

        try {
                const response = await axios.post('http://hoopi.p-e.kr/api/hoopi/article', formData);
            alert(response.data);
        } catch (error) {
            console.error(error);
        }
    };




    return(
        <div className='admin-article-container'>
            <div className='admin-product-container' style={{display: visible?'block':'none'}}>
                <div className='admin-product-box'>
                    상품명 : <input type='text' id='name' value={product.name} onChange={handleProduct}/> <br/>
                    가 격 : <input type='text' id='price' value={product.price} onChange={handleProduct}/> <br/>
                    재 고 : <input type='text' id='stock' value={product.stock} onChange={handleProduct}/> <br/>
                </div>
            </div>
            <div className='admin-article-box'>
                <div className='admin-article-title'>
                    <select  disabled={true} defaultValue={board?.boardCode}>
                        <option key={board?.boardCode} id='boardCode' value={board?.boardCode}>
                            {board?.name}
                        </option>
                    </select>
                    <input type='text' id='articleTitle' value={article.articleTitle} onChange={handleArticle}/>
                    <input type='file' onChange={handleFileChange} multiple={true}/>
                </div>
                <div className='admin-article-content'>
                    <textarea id='boardContent' value={article.boardContent} onChange={handleArticle}/>
                </div>
                <div className='admin-article-img'>
                    {imagePreviews?.map((image, index) => (
                        <img key={index} src={image} alt={`preview ${index}`}
                             style={{width: '100px'}}/>
                    ))}
                </div>
                <div className='admin-article-footer'>
                    <button onClick={handleUpload}>완료</button>
                    <button>취소</button>
                </div>
            </div>
        </div>
    );
}
export default Board;
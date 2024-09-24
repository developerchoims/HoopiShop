package com.ms.hoopi.repository;

import com.ms.hoopi.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query("SELECT a.boardContent FROM Article a WHERE a.productCode.productCode = :productCode")
    String findByProductCode(String productCode);

    @Query("SELECT a FROM Article a WHERE a.boardCode.boardId = 'notice'")
    List<Article> findNotice();

    @Query("SELECT a FROM Article a WHERE a.boardCode.boardId = 'notice' AND a.articleTitle LIKE CONCAT('%', :keyword, '%')")
    List<Article> findNoticeByName(String keyword);

    @Query("SELECT a FROM Article a WHERE a.boardCode.boardId = 'notice' AND a.boardContent LIKE CONCAT('%', :keyword, '%')")
    List<Article> findNoticeByContent(String keyword);

    @Query("SELECT a FROM Article a WHERE a.articleCode = :articleCode")
    Optional<Article> findByArticleCode(String articleCode);
}

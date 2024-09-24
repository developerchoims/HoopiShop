package com.ms.hoopi.admin.notice.service.serviceimpl;

import com.ms.hoopi.admin.notice.model.NoticeResponseDto;
import com.ms.hoopi.admin.notice.service.NoticeService;
import com.ms.hoopi.common.service.FileUploadService;
import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.model.entity.Article;
import com.ms.hoopi.repository.ArticleImgRepository;
import com.ms.hoopi.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final ArticleRepository articleRepository;
    private final FileUploadService fileUploadService;

    @Override
    public Page<NoticeResponseDto> getNotice(int page, int size, String searchCate, String keyword) {
        try{
            // notice 모음 가져오기
            List<Article> article = new ArrayList<>();
            if(keyword == null || keyword.isEmpty()){
                article = articleRepository.findNotice();
            } else {
                switch(searchCate){
                    case "noticeName": article = articleRepository.findNoticeByName(keyword); break;
                    case "noticeContent": article = articleRepository.findNoticeByContent(keyword); break;
                }
            }
            List<NoticeResponseDto> notices = new ArrayList<>();
            for(Article a : article){
                NoticeResponseDto noticeResponseDto = NoticeResponseDto.builder()
                        .articleCode(a.getArticleCode())
                        .articleTitle(a.getArticleTitle())
                        .boardContent(a.getBoardContent())
                        .articleDate(a.getArticleDate())
                        .imgUrl(fileUploadService.getS3(a.getArticleImgs().stream().map(m->m.getImgKey()).toString()))
                        .build();
                notices.add(noticeResponseDto);
            }
            Pageable pageable = PageRequest.of(page, size);
            return new PageImpl<>(notices, pageable, notices.size());

        } catch (Exception e){
            log.error(Constants.NONE_ARTICLE, e);
            return null;
        }
    }

    @Override
    public NoticeResponseDto getNoticeDetail(String articleCode) {
        Article article = articleRepository.findByArticleCode(articleCode)
                                            .orElseThrow(() -> new EntityNotFoundException(Constants.NONE_ARTICLE));
        try{
            NoticeResponseDto noticeDetail = NoticeResponseDto.builder()
                    .articleCode(article.getArticleCode())
                    .articleTitle(article.getArticleTitle())
                    .boardContent(article.getBoardContent())
                    .imgUrl(fileUploadService.getS3(article.getArticleImgs().stream().map(m->m.getImgKey()).toString()))
                    .articleDate(article.getArticleDate())
                    .build();
            return noticeDetail;
        }catch (Exception e){
            log.error(Constants.NONE_ARTICLE, e);
            return null;
        }
    }
}

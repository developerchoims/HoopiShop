package com.ms.hoopi.admin.notice.service;

import com.ms.hoopi.admin.notice.model.NoticeResponseDto;
import org.springframework.data.domain.Page;

public interface NoticeService {
    Page<NoticeResponseDto> getNotice(int page, int size, String searchCate, String keyword);

    NoticeResponseDto getNoticeDetail(String articleCode);
}

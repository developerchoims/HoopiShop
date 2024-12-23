package com.ms.hoopi.admin.notice.controller;

import com.ms.hoopi.admin.notice.model.NoticeResponseDto;
import com.ms.hoopi.admin.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hoopi")
@RequiredArgsConstructor
@Slf4j
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/notice")
    public Page<NoticeResponseDto> getNotice(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(required = false) String searchCate,
                                             @RequestParam(required = false) String keyword){
        return noticeService.getNotice(page, size, searchCate, keyword);
    }

    @GetMapping("/notice-detail/{articleCode}")
    public NoticeResponseDto getNoticeDetail(@PathVariable String articleCode){
        return noticeService.getNoticeDetail(articleCode);
    }
}

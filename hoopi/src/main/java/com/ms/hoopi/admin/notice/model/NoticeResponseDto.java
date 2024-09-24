package com.ms.hoopi.admin.notice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ms.hoopi.model.entity.Board;
import com.ms.hoopi.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class NoticeResponseDto {
    private String articleCode;
    private String boardContent;
    private LocalDateTime articleDate;
    private String articleTitle;
    private String imgUrl;
}

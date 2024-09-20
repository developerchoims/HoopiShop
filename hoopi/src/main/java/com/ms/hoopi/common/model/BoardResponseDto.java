package com.ms.hoopi.common.model;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class BoardResponseDto {
    private String boardCode;
    private String name;
    private String depth;
    private String superId;
    private String boardId;

}

package com.fufunode.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedingTab {
    private Long Id;
    private String name;
    private String ban_reason;
    private Long typeId;
    private String type;
    private String description;
    private String imgUrl;
    private Boolean status;
    private Long userId;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}

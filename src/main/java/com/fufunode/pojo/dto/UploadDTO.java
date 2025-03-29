package com.fufunode.pojo.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class UploadDTO {
    private Long userId;
    private MultipartFile file;
}

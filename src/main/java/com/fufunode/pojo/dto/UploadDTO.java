package com.fufunode.pojo.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@Builder
public class UploadDTO implements Serializable {
    private Long id;
    private MultipartFile file;
}

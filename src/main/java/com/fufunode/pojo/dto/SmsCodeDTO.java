package com.fufunode.pojo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmsCodeDTO {
    private String phone;
}

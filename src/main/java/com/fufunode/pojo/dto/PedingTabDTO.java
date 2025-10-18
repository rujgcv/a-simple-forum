package com.fufunode.pojo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PedingTabDTO {
    private String name;
    private Long typeId;
    private String description;
}

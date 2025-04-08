package com.fufunode.pojo.dto;

import com.fufunode.enums.Role;
import lombok.Data;

@Data
public class UserLoginDTO {
    private String name;
    private String password;
    private Role role;
}

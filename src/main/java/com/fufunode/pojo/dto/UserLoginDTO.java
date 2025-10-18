package com.fufunode.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fufunode.enums.Role;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {
    private String name;
    private String phone;
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Role role;
}

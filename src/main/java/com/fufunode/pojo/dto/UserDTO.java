package com.fufunode.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fufunode.enums.Role;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserDTO implements Serializable {
    private Long id;
    private String name;
    private String phone;
    private String password;
}

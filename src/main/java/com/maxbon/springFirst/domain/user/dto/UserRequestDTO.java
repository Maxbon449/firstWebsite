package com.maxbon.springFirst.domain.user.dto;

import com.maxbon.springFirst.domain.user.entity.UserRoleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    private String username;
    private String nickname;
    private String password;
}

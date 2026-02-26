package com.maxbon.springFirst.domain.user.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private String username;
    private String nickname;
    private String role;
}

package com.encore.board.author.dto;

import lombok.Data;

@Data
public class AuthorSaveReqDto {
    private String name;
    private String email;
    private String password;
    private String role; //enum으로 형변환이 안됨 String으로만 받아짐
}

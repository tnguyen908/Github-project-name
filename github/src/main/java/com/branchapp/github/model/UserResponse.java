package com.branchapp.github.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private String login;
    private String name;
    private String avatar_url;
    private String location;
    private String email;
    private String html_url;
    private String created_at;
}



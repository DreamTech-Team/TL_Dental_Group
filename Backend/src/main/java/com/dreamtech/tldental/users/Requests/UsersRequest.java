package com.dreamtech.tldental.users.Requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsersRequest {
    private String email;
    private String password;
    private String roles;
    private String fullname;
    private String phonenumber;
    private String address;
}

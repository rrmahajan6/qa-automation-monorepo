package com.framework.models.request;

import lombok.Data;

@Data
public class ShopLogin {
    private String userEmail;
    private String userPassword;
}
/*
{
    "userEmail": "rrmahajan6@gmail.com",
    "userPassword": "Test@1234"
}
*/
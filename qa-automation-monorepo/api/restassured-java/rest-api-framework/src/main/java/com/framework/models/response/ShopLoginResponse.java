package com.framework.models.response;

import lombok.Data;

@Data
public class ShopLoginResponse {
    private String token;
    private String userId;
    private String message;
}
/*
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2OGQ5MDgwYmY2NjlkNmNiMGFlZWZjZjkiLCJ1c2VyRW1haWwiOiJycm1haGFqYW42QGdtYWlsLmNvbSIsInVzZXJNb2JpbGUiOjc3NTU5MDYxMjIsInVzZXJSb2xlIjoiY3VzdG9tZXIiLCJpYXQiOjE3NzQ3NzMxNTYsImV4cCI6MTgwNjMzMDc1Nn0.Rnb8GmRnY_zA3WfhjPW_2lh-931hnYUiv1lB2P_GsNc",
    "userId": "68d9080bf669d6cb0aeefcf9",
    "message": "Login Successfully"
}
*/
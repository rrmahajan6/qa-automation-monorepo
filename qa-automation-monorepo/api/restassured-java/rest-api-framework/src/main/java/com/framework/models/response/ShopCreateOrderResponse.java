package com.framework.models.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopCreateOrderResponse {
    private List<String> orders;
    private List<String> productOrderId;
    private String message;
}
/*
{
    "orders": [
        "69c8e7aef86ba51a65332bf0"
    ],
    "productOrderId": [
        "69c8e63cf86ba51a65332865"
    ],
    "message": "Order Placed Successfully"
}
*/
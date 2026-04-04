package com.framework.models.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopCreateOrder {

    private List<Order> orders;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order {
        private String country;
        private String productOrderedId;
    }
}
/*
{
    "orders":[
        {
            "country": "India",
            "productOrderedId": "69c8e63cf86ba51a65332865"
        }
    ]
}
*/
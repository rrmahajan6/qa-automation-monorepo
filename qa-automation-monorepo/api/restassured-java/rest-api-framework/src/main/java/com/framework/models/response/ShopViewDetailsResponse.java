package com.framework.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopViewDetailsResponse {
    private String message;
    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {    
    @JsonProperty("_id")
    private String id;
    private String orderById;
    private String orderBy;
    private String productOrderedId;
    private String productName;
    private String country;
    private String productDescription;
    private String productImage;
    private String orderPrice;
    @JsonProperty("__v")
    private int version;
    }
}
/**
 * {
    "data": {
        "_id": "69c8e7aef86ba51a65332bf0",
        "orderById": "68d9080bf669d6cb0aeefcf9",
        "orderBy": "rrmahajan6@gmail.com",
        "productOrderedId": "69c8e63cf86ba51a65332865",
        "productName": "ProductName 1",
        "country": "India",
        "productDescription": " Adidas Originals",
        "productImage": "https://rahulshettyacademy.com/api/ecom/uploads/productImage_1774773820499.jpg",
        "orderPrice": "11500",
        "__v": 0
    },
    "message": "Orders fetched for customer Successfully"
}
 */
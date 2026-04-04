package com.framework.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopAddProductResponse {
    private String message;
    private String productId;
}
/*
{
    "message": "Product added successfully",
    "productId": "69c8e63cf86ba51a65332865"
}
*/
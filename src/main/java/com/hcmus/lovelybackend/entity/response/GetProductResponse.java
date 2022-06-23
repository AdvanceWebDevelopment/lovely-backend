package com.hcmus.lovelybackend.entity.response;

import com.hcmus.lovelybackend.entity.common.ResponseHeader;
import com.hcmus.lovelybackend.entity.dao.Product;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class GetProductResponse {
    private Product product;
    private Page<Product> products;

    public GetProductResponse(Product product, Page<Product> products) {
        this.product = product;
        this.products = products;
    }
}

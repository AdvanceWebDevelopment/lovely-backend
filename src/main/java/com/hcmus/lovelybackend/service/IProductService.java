package com.hcmus.lovelybackend.service;

import com.hcmus.lovelybackend.constant.FilterSearchConstant;
import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.dao.Product;
import com.hcmus.lovelybackend.entity.response.GetProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    List<Product> getProducts(String top);
    GetProductResponse getProductById(Integer id);
    Page<Product> searchProduct(Integer categoryId, Integer subcategoryId, String text, FilterSearchConstant sortBy,Integer page, Integer size);
    AuMessageCommonResponse deleteProduct(String token, Integer productId);
}

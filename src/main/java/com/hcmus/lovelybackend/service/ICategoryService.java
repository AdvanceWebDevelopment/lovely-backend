package com.hcmus.lovelybackend.service;

import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.dao.Category;
import com.hcmus.lovelybackend.entity.dao.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    List<Category> getAll();
    Page<Product> getProductCategory(Integer id, Integer page, Integer size);
    AuMessageCommonResponse createCategory(String token, Category category);
    AuMessageCommonResponse updateCategory(String token, Category category, Integer categoryId);
    AuMessageCommonResponse deleteCategory(String token, Integer categoryId);
}

package com.hcmus.lovelybackend.service;

import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.dao.Product;
import com.hcmus.lovelybackend.entity.dao.SubCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISubCategoryService {
    List<SubCategory> getAll();
    Page<Product> getProductSubCategory(Integer id, Integer page, Integer size);
    AuMessageCommonResponse createSubCategory(String token, SubCategory subCategory, Integer categoryId);
    AuMessageCommonResponse updateSubCategory(String token, SubCategory subCategory, Integer categoryId, Integer subCategoryId);
    AuMessageCommonResponse deleteSubCategory(String token, Integer subCategoryId);
}

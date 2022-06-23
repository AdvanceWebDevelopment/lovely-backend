package com.hcmus.lovelybackend.service.impl;

import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.common.TokenUser;
import com.hcmus.lovelybackend.entity.dao.Category;
import com.hcmus.lovelybackend.entity.dao.Product;
import com.hcmus.lovelybackend.entity.dao.SubCategory;
import com.hcmus.lovelybackend.exception.runtime.BadRequestException;
import com.hcmus.lovelybackend.exception.runtime.NotFoundException;
import com.hcmus.lovelybackend.repository.CategoryRepository;
import com.hcmus.lovelybackend.repository.ProductRepository;
import com.hcmus.lovelybackend.repository.SubCategoryRepository;
import com.hcmus.lovelybackend.service.ISubCategoryService;
import com.hcmus.lovelybackend.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService implements ISubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AppUtils appUtils;

    @Override
    public List<SubCategory> getAll() {
        return subCategoryRepository.findAll();
    }

    @Override
    public Page<Product> getProductSubCategory(Integer id, Integer page, Integer size) {
        Page<Product> products = productRepository.findAllBySubCategoryId(id, PageRequest.of(page, size));
        products.forEach(p -> {
            p.getImages().removeIf(image -> !image.getIsMain());
            p.setImages(p.getImages());
        });
        return products;
    }

    @Override
    public AuMessageCommonResponse createSubCategory(String token, SubCategory subCategory, Integer categoryId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            throw new NotFoundException("Category not found !!!");
        });
        subCategory.setCategory(category);
        subCategoryRepository.save(subCategory);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Create subcategory success !!!");
    }

    @Override
    public AuMessageCommonResponse updateSubCategory(String token, SubCategory subCategory, Integer categoryId, Integer subCategoryId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            throw new NotFoundException("Category not found !!!");
        });
        SubCategory oldSubCategory = subCategoryRepository.findById(subCategoryId).orElseThrow(() -> {
            throw new NotFoundException("Subcategory not found !!!");
        });
        oldSubCategory.setCategory(category);
        oldSubCategory.setName(subCategory.getName());
        subCategoryRepository.save(oldSubCategory);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Update subcategory success !!!");
    }

    @Override
    public AuMessageCommonResponse deleteSubCategory(String token, Integer subCategoryId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        subCategoryRepository.findById(subCategoryId).orElseThrow(() -> {
            throw new NotFoundException("Subcategory not found !!!");
        });
        Page<Product> products = getProductSubCategory(subCategoryId, 0, 3);
        if(!products.isEmpty()){
            throw new BadRequestException("Cannot delete Subcategory had products !!");
        }
        subCategoryRepository.deleteById(subCategoryId);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Delete subcategory success !!!");
    }
}

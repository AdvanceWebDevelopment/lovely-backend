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
import com.hcmus.lovelybackend.service.ICategoryService;
import com.hcmus.lovelybackend.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AppUtils appUtils;

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Page<Product> getProductCategory(Integer id, Integer page, Integer size) {
        if(id == 1){
            return productRepository.findAll(PageRequest.of(page, size));
        }
        return productRepository.findAllByCategoryId(id, PageRequest.of(page, size));
    }

    @Override
    public AuMessageCommonResponse createCategory(String token, Category category) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        List<SubCategory> subCategories = category.getSubCategories();
        subCategories.forEach(s -> {
            s.setCategory(category);
        });
        category.setSubCategories(subCategories);
        categoryRepository.save(category);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Create category success !!!");
    }

    @Override
    public AuMessageCommonResponse updateCategory(String token, Category category, Integer categoryId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Category oldCategory = categoryRepository.findById(categoryId).orElseThrow(()->{
            throw new NotFoundException("Category not found !!!");
        });
        if(StringUtils.isNotBlank(category.getName())){
            oldCategory.setName(category.getName());
        }
        if(category.getSubCategories() != null){
            List<SubCategory> subCategory = category.getSubCategories();
            subCategory.forEach(s -> {
                s.setCategory(oldCategory);
            });
            oldCategory.setSubCategories(subCategory);
        }
        categoryRepository.save(oldCategory);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Update category success !!!");
    }

    @Override
    public AuMessageCommonResponse deleteCategory(String token, Integer categoryId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Category oldCategory = categoryRepository.findById(categoryId).orElseThrow(()->{
            throw new NotFoundException("Category not found !!!");
        });
        Page<Product> products = getProductCategory(categoryId, 0, 3);
        if(!products.isEmpty()){
            throw new BadRequestException("Cannot delete Category had products !!");
        }
        categoryRepository.deleteById(oldCategory.getId());
        return new AuMessageCommonResponse(tokenUser.getToken(), "Delete category success !!!");
    }
}

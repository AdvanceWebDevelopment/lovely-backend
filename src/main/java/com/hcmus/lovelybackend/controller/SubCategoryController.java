package com.hcmus.lovelybackend.controller;

import com.hcmus.lovelybackend.service.ISubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subcategory")
public class SubCategoryController {

    @Autowired
    private ISubCategoryService subCategoryService;

    @GetMapping()
    public ResponseEntity<?> getSubCategory() {
        return ResponseEntity.ok(subCategoryService.getAll());
    }

    @GetMapping(value = "/{id}/products")
    public ResponseEntity<?> getProductSubCategory(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @PathVariable Integer id){
        return ResponseEntity.ok(subCategoryService.getProductSubCategory(id, page, size));
    }
}

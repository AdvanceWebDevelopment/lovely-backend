package com.hcmus.lovelybackend.controller;

import com.hcmus.lovelybackend.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping()
    public ResponseEntity<?> getCategory() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping(value = "/{id}/products")
    public ResponseEntity<?> getProductCategory(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @PathVariable Integer id){
        return ResponseEntity.ok(categoryService.getProductCategory(id, page, size));
    }
}

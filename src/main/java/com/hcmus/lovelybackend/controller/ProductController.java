package com.hcmus.lovelybackend.controller;

import com.hcmus.lovelybackend.constant.FilterSearchConstant;
import com.hcmus.lovelybackend.service.IProductBidderService;
import com.hcmus.lovelybackend.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductBidderService productBidderService;

    @GetMapping("/products")
    public ResponseEntity<?> getProducts(@RequestParam("top") String top) {
        return ResponseEntity.ok(productService.getProducts(top));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping(value = "/product")
    public ResponseEntity<?> searchProductWithCategory(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                                       @RequestParam(value = "subcategoryId", required = false) Integer subcategoryId,
                                                       @RequestParam("text") String text,
                                                       @RequestParam(value = "sortBy", required = false) FilterSearchConstant sortBy,
                                                       @RequestParam("page") Integer page,
                                                       @RequestParam("size") Integer size){
        return ResponseEntity.ok(productService.searchProduct(categoryId, subcategoryId, text, sortBy, page, size));
    }

    @GetMapping("/product/{productId}/history")
    public ResponseEntity<?> getHistory(@PathVariable Integer productId,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size) {
        return new ResponseEntity<>(productBidderService.getListBidOfProduct(productId, page, size), HttpStatus.OK);
    }
}

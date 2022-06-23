package com.hcmus.lovelybackend.controller;

import com.hcmus.lovelybackend.entity.dao.Category;
import com.hcmus.lovelybackend.entity.dao.SubCategory;
import com.hcmus.lovelybackend.entity.request.RegisterRequest;
import com.hcmus.lovelybackend.entity.request.UpdateUserProfileRequest;
import com.hcmus.lovelybackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ISubCategoryService subCategoryService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUpgradeListService upgradeListService;

    @PostMapping("/category")
    public ResponseEntity<?> createCategory(@RequestHeader("Authorization") String token,
                                            @RequestBody Category category){
        return ResponseEntity.ok(categoryService.createCategory(token, category));
    }

    @PutMapping("/category/{categoryId}")
    public ResponseEntity<?> updateCategory(@RequestHeader("Authorization") String token,
                                            @RequestBody Category category,
                                            @PathVariable Integer categoryId){
        return ResponseEntity.ok(categoryService.updateCategory(token,category, categoryId));
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<?> deleteCategory(@RequestHeader("Authorization") String token,
                                            @PathVariable Integer categoryId){
        return ResponseEntity.ok(categoryService.deleteCategory(token, categoryId));
    }

    @PostMapping("/category/{categoryId}/subcategory")
    public ResponseEntity<?> createSubCategory(@RequestHeader("Authorization") String token,
                                            @RequestBody SubCategory subCategory,
                                               @PathVariable Integer categoryId){
        return ResponseEntity.ok(subCategoryService.createSubCategory(token, subCategory, categoryId));
    }

    @PutMapping("/category/{categoryId}/subcategory/{subcategoryId}")
    public ResponseEntity<?> updateSubCategory(@RequestHeader("Authorization") String token,
                                            @RequestBody SubCategory subCategory,
                                               @PathVariable Integer categoryId,
                                               @PathVariable Integer subcategoryId){
        return ResponseEntity.ok(subCategoryService.updateSubCategory(token, subCategory, categoryId, subcategoryId));
    }

    @DeleteMapping("/subcategory/{subcategoryId}")
    public ResponseEntity<?> deleteSubCategory(@RequestHeader("Authorization") String token,
                                               @PathVariable Integer subcategoryId){
        return ResponseEntity.ok(subCategoryService.deleteSubCategory(token, subcategoryId));
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<?> deleteProduct(@RequestHeader("Authorization") String token,
                                               @PathVariable Integer productId){
        return ResponseEntity.ok(productService.deleteProduct(token, productId));
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestHeader("Authorization") String token,
                                               @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.createUser(token, request));
    }

    @PatchMapping("/user/{userId}")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token,
                                           @RequestBody UpdateUserProfileRequest request,
                                           @PathVariable Integer userId){
        return new ResponseEntity<>(userService.updateUserProfileByAdmin(token, request, userId), HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token,
                                        @PathVariable Integer userId)  {
        return new ResponseEntity<>(userService.deleteUser(token, userId), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String token,
                                      @RequestParam("page") Integer page,
                                      @RequestParam("size") Integer size){
        return ResponseEntity.ok(userService.getListUsers(token, page, size));
    }

    @GetMapping("/bidder")
    public ResponseEntity<?> getBidders(@RequestHeader("Authorization") String token,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size){
        return ResponseEntity.ok(upgradeListService.getUpgradeList(token, page, size));
    }

    @GetMapping("/seller")
    public ResponseEntity<?> getSellers(@RequestHeader("Authorization") String token,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size){
        return ResponseEntity.ok(upgradeListService.getListSeller(token, page, size));
    }

    @PutMapping("/bidder/{bidderId}")
    public ResponseEntity<?> upgradeUser(@RequestHeader("Authorization") String token,
                                         @PathVariable Integer bidderId){
        return ResponseEntity.ok(upgradeListService.acceptUpgradeUser(token, bidderId));
    }

    @PutMapping("/seller/{sellerId}")
    public ResponseEntity<?> downgradeUser(@RequestHeader("Authorization") String token,
                                         @PathVariable Integer sellerId){
        return ResponseEntity.ok(upgradeListService.downgradeUser(token, sellerId));
    }
}

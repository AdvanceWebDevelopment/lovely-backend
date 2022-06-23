package com.hcmus.lovelybackend.controller;

import com.hcmus.lovelybackend.entity.request.ChangePasswordRequest;
import com.hcmus.lovelybackend.entity.request.UpdateUserProfileRequest;
import com.hcmus.lovelybackend.service.IBidderService;
import com.hcmus.lovelybackend.service.IProductService;
import com.hcmus.lovelybackend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IUserService userService;

    @PostMapping(value = "/watch-list/product/{id}")
    public ResponseEntity<?> addProductToWatchList(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        return new ResponseEntity<>(bidderService.saveProductToWatchList(token, id), HttpStatus.CREATED);
    }

    @GetMapping(value = "/watch-list")
    public ResponseEntity<?> getWatchList(@RequestHeader("Authorization") String token,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size){
        return new ResponseEntity<>(bidderService.getWatchList(token, page, size), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(userService.getCurrentUser(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(userService.getUserProfile(token), HttpStatus.OK);
    }

    @PatchMapping()
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token, @RequestBody UpdateUserProfileRequest request){
        return new ResponseEntity<>(userService.updateUserProfile(token, request), HttpStatus.OK);
    }

    @PutMapping(value = "/change-password")
    public ResponseEntity<?> handleChangePassword(@RequestHeader("Authorization") String token,
                                                  @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(token, request));
    }

    @PatchMapping(value = "/product/{productId}")
    public ResponseEntity<?> requestBidProduct(@RequestHeader("Authorization") String token,
                                               @PathVariable Integer productId) {
        return ResponseEntity.ok(bidderService.requestBidProduct(token, productId));
    }
}

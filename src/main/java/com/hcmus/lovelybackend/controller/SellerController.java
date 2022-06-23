package com.hcmus.lovelybackend.controller;

import com.hcmus.lovelybackend.entity.dao.Product;
import com.hcmus.lovelybackend.entity.request.RejectProductRequest;
import com.hcmus.lovelybackend.entity.request.ReviewWinnerRequest;
import com.hcmus.lovelybackend.entity.request.UpdateProductRequest;
import com.hcmus.lovelybackend.service.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    @Autowired
    private ISellerService sellerService;

    @PostMapping(value = "/product")
    public ResponseEntity<?> createProduct(@RequestHeader("Authorization") String token,
                                           @RequestBody Product product) {
        return new ResponseEntity<>(sellerService.createProduct(token, product), HttpStatus.CREATED);
    }

    @PutMapping(value = "/product/{id}")
    public ResponseEntity<?> updateDescriptionProduct(@RequestHeader("Authorization") String token,
                                                      @RequestBody UpdateProductRequest updateProductRequest,
                                                      @PathVariable Integer id) {
        return new ResponseEntity<>(sellerService.updateDescriptionProduct(token, updateProductRequest, id), HttpStatus.OK);
    }

    @PatchMapping(value = "/product/{productId}/bidder/{bidderId}")
    public ResponseEntity<?> rejectBidderBidProduct(@RequestHeader("Authorization") String token,
                                                    @RequestBody RejectProductRequest request,
                                                    @PathVariable Integer bidderId,
                                                    @PathVariable Integer productId) {
        return ResponseEntity.ok(sellerService.rejectBidderBidProduct(token, request.getId(), productId, bidderId));
    }

    @GetMapping(value = "/products")
    public ResponseEntity<?> getProductBySeller(@RequestHeader("Authorization") String token,
                                                @RequestParam(value = "page") Integer page,
                                                @RequestParam(value = "size") Integer size) {
        return ResponseEntity.ok(sellerService.getListProductBySeller(token, page, size));
    }

    @GetMapping(value = "/products/win")
    public ResponseEntity<?> getProductsHaveWinner(@RequestHeader("Authorization") String token,
                                                   @RequestParam(value = "page") Integer page,
                                                   @RequestParam(value = "size") Integer size) {
        return ResponseEntity.ok(sellerService.getListProductHaveWinner(token, page, size));
    }

    @PostMapping(value = "/winner/{winnerId}/product/{productId}")
    public ResponseEntity<?> reviewWinnerInProduct(@RequestHeader("Authorization") String token,
                                                   @RequestBody ReviewWinnerRequest request,
                                                   @PathVariable Integer winnerId,
                                                   @PathVariable Integer productId) {
        return ResponseEntity.ok(sellerService.reviewWinner(token, winnerId, productId, request));
    }

    @PatchMapping(value = "/winner/{winnerId}/product/{productId}")
    public ResponseEntity<?> rejectWinnerInProduct(@RequestHeader("Authorization") String token,
                                                   @PathVariable Integer winnerId,
                                                   @PathVariable Integer productId) {
        return ResponseEntity.ok(sellerService.rejectWinner(token, winnerId, productId));
    }

    @GetMapping(value = "/product/{productId}")
    public ResponseEntity<?> getListBidderTemporary(@RequestHeader("Authorization") String token,
                                                    @RequestParam(value = "page") Integer page,
                                                    @RequestParam(value = "size") Integer size,
                                                    @PathVariable Integer productId){
        return ResponseEntity.ok(sellerService.getListBidderTemporary(token, productId, page, size));
    }

    @PutMapping(value = "/bidder/{bidderId}/product/{productId}")
    public ResponseEntity<?> acceptBidderTemporary(@RequestHeader("Authorization") String token,
                                                   @PathVariable Integer bidderId,
                                                    @PathVariable Integer productId){
        return ResponseEntity.ok(sellerService.acceptBidProduct(token, bidderId, productId));
    }

    @DeleteMapping(value = "/bidder/{bidderId}/product/{productId}")
    public ResponseEntity<?> rejectBidderTemporary(@RequestHeader("Authorization") String token,
                                                   @PathVariable Integer bidderId,
                                                   @PathVariable Integer productId){
        return ResponseEntity.ok(sellerService.rejectBidProduct(token, bidderId, productId));
    }
}

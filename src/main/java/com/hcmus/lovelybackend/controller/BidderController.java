package com.hcmus.lovelybackend.controller;

import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.common.AuObjectCommonResponse;
import com.hcmus.lovelybackend.entity.dao.ProductBidder;
import com.hcmus.lovelybackend.entity.dao.TextMessageDTO;
import com.hcmus.lovelybackend.entity.request.BidProductRequest;
import com.hcmus.lovelybackend.entity.request.ReviewWinnerRequest;
import com.hcmus.lovelybackend.service.IProductBidderService;
import com.hcmus.lovelybackend.service.IUpgradeListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bidder")
@Slf4j
public class BidderController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private IProductBidderService productBidderService;

    @Autowired
    private IUpgradeListService upgradeListService;

    @PutMapping("/product/{productId}")
    public ResponseEntity<?> bidProduct(@RequestHeader("Authorization") String token,
                                        @PathVariable Integer productId,
                                        @RequestBody BidProductRequest request) {
        AuObjectCommonResponse auObjectCommonResponse = productBidderService.saveBidOfBidder(token, productId, request);
        TextMessageDTO textMessageDTO = new TextMessageDTO();
        textMessageDTO.setMessage((ProductBidder) auObjectCommonResponse.getObject());
        template.convertAndSend("/topic/history", textMessageDTO);
        return new ResponseEntity<>(auObjectCommonResponse, HttpStatus.OK);
    }

    @PutMapping("/product/{productId}/buy")
    public ResponseEntity<?> payNow(@RequestHeader("Authorization") String token,
                                    @PathVariable Integer productId) {
        AuObjectCommonResponse auObjectCommonResponse = productBidderService.payNowProduct(token, productId);
        TextMessageDTO textMessageDTO = new TextMessageDTO();
        textMessageDTO.setMessage((ProductBidder) auObjectCommonResponse.getObject());
        template.convertAndSend("/topic/history", textMessageDTO);
        return new ResponseEntity<>(auObjectCommonResponse, HttpStatus.OK);
    }

    @SendTo("/topic/history")
    public TextMessageDTO broadcastMessage(@Payload TextMessageDTO textMessageDTO) {
        log.info("New product in history");
        return textMessageDTO;
    }

    @GetMapping("/product")
    public ResponseEntity<?> getListBidByBidder(@RequestHeader("Authorization") String token,
                                                @RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size) {
        return new ResponseEntity<>(productBidderService.getListBidByBidder(token, page, size), HttpStatus.OK);
    }

    @GetMapping("/evaluation")
    public ResponseEntity<?> getEvaluation(@RequestHeader("Authorization") String token,
                                           @RequestParam("page") Integer page,
                                           @RequestParam("size") Integer size) {
        return new ResponseEntity<>(productBidderService.getEvaluationDetails(token, page, size), HttpStatus.OK);
    }

    @GetMapping("/product/win")
    public ResponseEntity<?> getProductsWin(@RequestHeader("Authorization") String token,
                                            @RequestParam("page") Integer page,
                                            @RequestParam("size") Integer size) {
        return new ResponseEntity<>(productBidderService.getProductsWin(token, page, size), HttpStatus.OK);
    }

    @PostMapping(value = "/seller/{sellerId}/product/{productId}")
    public ResponseEntity<?> reviewSellerInProduct(@RequestHeader("Authorization") String token,
                                                   @RequestBody ReviewWinnerRequest request,
                                                   @PathVariable Integer sellerId,
                                                   @PathVariable Integer productId) {
        return ResponseEntity.ok(productBidderService.reviewSeller(token, sellerId, productId, request));
    }

    @PutMapping("/product/{productId}/auto")
    public ResponseEntity<?> autoBidProduct(@RequestHeader("Authorization") String token,
                                            @PathVariable Integer productId,
                                            @RequestBody BidProductRequest request) {
        AuObjectCommonResponse auObjectCommonResponse = productBidderService.autoBid(token, productId, request);
        TextMessageDTO textMessageDTO = new TextMessageDTO();
        textMessageDTO.setMessage((ProductBidder) auObjectCommonResponse.getObject());
        template.convertAndSend("/topic/history", textMessageDTO);
        return new ResponseEntity<>(auObjectCommonResponse, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> requestUpgrade(@RequestHeader("Authorization") String token) {
        AuMessageCommonResponse auObjectCommonResponse = upgradeListService.requestUpgrade(token);
        return new ResponseEntity<>(auObjectCommonResponse, HttpStatus.OK);
    }
}
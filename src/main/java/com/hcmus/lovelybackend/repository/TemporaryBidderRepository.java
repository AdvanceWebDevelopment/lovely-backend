package com.hcmus.lovelybackend.repository;

import com.hcmus.lovelybackend.entity.dao.TemporaryBidder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TemporaryBidderRepository extends JpaRepository<TemporaryBidder, Integer> {

    @Query(value = "SELECT t FROM TemporaryBidder t WHERE t.product.id = :productId AND t.product.seller.id = :sellerId")
    Page<TemporaryBidder> findAllByProductIdAndSellerId(Integer productId, Integer sellerId, Pageable pageable);
    Optional<TemporaryBidder> findAllByProductIdAndBidderId(Integer productId, Integer bidderId);
}

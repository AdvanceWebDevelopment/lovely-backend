package com.hcmus.lovelybackend.repository;

import com.hcmus.lovelybackend.entity.dao.Product;
import com.hcmus.lovelybackend.entity.dao.ProductBidder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductBidderRepository extends CrudRepository<ProductBidder, Integer> {
    Set<ProductBidder> findAllByProductIdAndBidderIdAndReject(Integer productId, Integer bidderId, Boolean reject);
    List<ProductBidder> findAllByProductIdAndBidderIdNotOrderByPriceDesc (Integer productId, Integer bidderId);
    Page<ProductBidder> findAllByBidderId(Integer bidderId, Pageable pageable);
    Page<ProductBidder> findAllByProductIdOrderByPriceDesc(Integer productId, Pageable pageable);
    List<ProductBidder> findAll();
}

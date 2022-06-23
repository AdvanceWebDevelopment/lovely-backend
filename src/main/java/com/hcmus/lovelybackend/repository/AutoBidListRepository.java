package com.hcmus.lovelybackend.repository;

import com.hcmus.lovelybackend.entity.dao.AutoBidList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoBidListRepository extends JpaRepository<AutoBidList, Integer> {
    Optional<AutoBidList> findAllByBidderIdAndProductId(Integer bidderId, Integer productId);
}

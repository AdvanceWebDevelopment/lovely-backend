package com.hcmus.lovelybackend.repository;

import com.hcmus.lovelybackend.entity.dao.UpgradeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UpgradeListRepository extends JpaRepository<UpgradeList, Integer> {
    Optional<UpgradeList> findAllByBidderId (Integer bidderId);
}

package com.hcmus.lovelybackend.repository;

import com.hcmus.lovelybackend.entity.dao.WatchList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList, Integer> {
    Page<WatchList> findAllByBidderId(Integer id, Pageable pageable);
}

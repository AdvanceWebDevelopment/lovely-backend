package com.hcmus.lovelybackend.repository;

import com.hcmus.lovelybackend.entity.dao.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {
}

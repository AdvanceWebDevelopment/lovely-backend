package com.hcmus.lovelybackend.repository;

import com.hcmus.lovelybackend.entity.dao.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAll();
    List<Category> findAll(Sort sort);
}

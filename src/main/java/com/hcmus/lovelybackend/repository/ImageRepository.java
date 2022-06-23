package com.hcmus.lovelybackend.repository;

import com.hcmus.lovelybackend.entity.dao.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

}

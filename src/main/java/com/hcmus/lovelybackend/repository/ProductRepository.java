package com.hcmus.lovelybackend.repository;

import com.hcmus.lovelybackend.entity.dao.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT p FROM Product p WHERE p.endAt >= :now ORDER BY p.productBidders.size DESC")
    List<Product> findTop5MostBids(@Param("now") LocalDateTime now, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.endAt >= :now ORDER BY p.endAt ASC")
    List<Product> findTop5toEnd(@Param("now") LocalDateTime now, Pageable pageable);

    @Query(value = "SELECT p FROM Product p JOIN SubCategory s ON p.subCategory.id = s.id JOIN Category c ON s.category.id = c.id WHERE c.id = :id")
    Page<Product> findAllByCategoryId(@Param("id") Integer id, Pageable pageable);

    @Query(value = "SELECT p FROM Product p JOIN SubCategory s ON p.subCategory.id = s.id JOIN Category c ON s.category.id = c.id WHERE c.id = :id AND p.name LIKE %:name%")
    Page<Product> findAllByCategoryIdAndName(@Param("id") Integer id, @Param("name") String name, Pageable pageable);
//    Page<Product> findAllByCategoryIdAndNameContaining(Integer id, String name, Pageable pageable);

    Page<Product> findAllBySubCategoryId(Integer id, Pageable pageable);

    Page<Product> findAllBySubCategoryIdAndIdNot(Integer id, Pageable pageable, Integer productId);

    //    @Query(value = "SELECT p FROM Product p WHERE p.endAt >= CURRENT_DATE AND ")
    Page<Product> findAllByNameContaining(String name, Pageable pageable);

    Page<Product> findAllBySubCategoryIdAndNameContaining(Integer id, String name, Pageable pageable);

    Optional<Product> findAllById(Integer id);

    @Query(value = "SELECT p FROM Product p WHERE p.endAt < :now AND p.bidderHighest.id = :id ORDER BY p.endAt ASC")
    Page<Product> findAllByBidderHighestIdAndHaveWinner(@Param("id") Integer id, @Param("now") LocalDateTime now, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.endAt >= :now AND p.seller.id = :id ORDER BY p.endAt ASC")
    Page<Product> findAllBySellerId(@Param("id") Integer id, @Param("now") LocalDateTime now, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.endAt < :now AND p.seller.id = :id AND p.bidderHighest.id IS NOT NULL ORDER BY p.endAt ASC")
    Page<Product> findAllBySellerIdAndHaveWinner(@Param("id") Integer id, @Param("now") LocalDateTime now, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.endAt < :now")
    List<Product> findAllByEndAtExpired(@Param("now") LocalDateTime now);
}

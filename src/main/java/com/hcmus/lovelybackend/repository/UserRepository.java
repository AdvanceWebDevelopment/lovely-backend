package com.hcmus.lovelybackend.repository;

import com.hcmus.lovelybackend.entity.dao.UserDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserDAO, Integer> {
    UserDAO findByEmail(String email);

    Boolean existsByEmail(String email);

    UserDAO save(UserDAO userDAO);

    UserDAO findByVerificationCode(String code);

    Optional<UserDAO> findById(Integer id);

    Page<UserDAO> findAllByRole(Integer role, Pageable pageable);
}
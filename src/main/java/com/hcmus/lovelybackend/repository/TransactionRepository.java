package com.hcmus.lovelybackend.repository;

import com.hcmus.lovelybackend.entity.dao.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<Transaction> findAllByProductIdAndAssessorIdAndRecipientId(Integer productId, Integer assessorId, Integer recipientId);
    Page<Transaction> findAllByRecipientId(Integer recipientId, Pageable pageable);
    List<Transaction> findAllByRecipientId(Integer recipientId);
}

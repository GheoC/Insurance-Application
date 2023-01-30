package com.pot.benefits.repository;

import com.pot.benefits.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByEnrolmentId(String enrolmentId);
}

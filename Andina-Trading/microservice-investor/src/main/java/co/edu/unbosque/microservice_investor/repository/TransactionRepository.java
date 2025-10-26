package co.edu.unbosque.microservice_investor.repository;

import co.edu.unbosque.microservice_investor.model.entity.Transaction;
import co.edu.unbosque.microservice_investor.model.enums.TransactionStatus;
import co.edu.unbosque.microservice_investor.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByUserId(Integer userId);

    List<Transaction> findByOrderId(Integer orderId);

    List<Transaction> findByUserIdOrderByCreatedAtDesc(Integer userId);

    List<Transaction> findByUserIdAndTypeAndStatus(Integer userId, TransactionType type, TransactionStatus status);

    List<Transaction> findByUserIdAndType(Integer userId, TransactionType type);

    Optional<Transaction> findByOrderIdAndType(Integer orderId, TransactionType type);

}
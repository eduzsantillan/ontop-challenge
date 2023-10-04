package com.ontop.challenge.repository;

import com.ontop.challenge.model.entity.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, ObjectId> {
    Optional<Transaction> findByProviderTransactionId(String providerTransactionId);
    List<Transaction> findAllByUserId(String userId);
}

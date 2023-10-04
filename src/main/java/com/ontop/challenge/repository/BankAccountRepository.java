package com.ontop.challenge.repository;

import com.ontop.challenge.model.entity.BankAccount;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends MongoRepository<BankAccount, String> {
    Optional<BankAccount> findByUserId(String userId);
}

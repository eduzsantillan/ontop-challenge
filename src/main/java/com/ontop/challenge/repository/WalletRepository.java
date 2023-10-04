package com.ontop.challenge.repository;

import com.ontop.challenge.model.entity.Wallet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends MongoRepository<Wallet, ObjectId> {
    Optional<Wallet> findByUserId(String userId);
}

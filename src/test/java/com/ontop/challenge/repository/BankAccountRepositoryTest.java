package com.ontop.challenge.repository;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.ontop.challenge.model.entity.BankAccount;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataMongoTest
public class BankAccountRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BankAccountRepository bankAccountRepository;


    @Test
    void testFindByUserIdNotFound() {
        BankAccount bankAccount = bankAccountRepository.findByBankAccountId("NotRegisteredId").orElse(null);
        assertNull(bankAccount);
    }



}

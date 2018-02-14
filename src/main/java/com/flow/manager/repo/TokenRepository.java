package com.flow.manager.repo;

import com.flow.manager.repo.entity.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {

    Token findByUserId(String userId);

}

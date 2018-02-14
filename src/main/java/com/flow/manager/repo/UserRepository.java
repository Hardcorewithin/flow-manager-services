package com.flow.manager.repo;

import com.flow.manager.repo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    List<User> findByChatId(String chatId);

    boolean findByRandomId(String randomId);
}

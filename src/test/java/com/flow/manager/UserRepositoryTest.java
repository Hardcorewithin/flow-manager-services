package com.flow.manager;

import com.flow.manager.repo.entity.User;
import com.flow.manager.repo.UserRepository;
import com.flow.manager.utils.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userMongoRepository;


    @Before
    public void setUp() throws Exception {
        User user1= new User("Alice", "23");
        User user2= new User("Bob", "33");
        //save product, verify has ID value after save
        assertNull(user1.getId());
        assertNull(user2.getId());//null before save
        this.userMongoRepository.save(user1);
        this.userMongoRepository.save(user2);
        assertNotNull(user1.getId());
        assertNotNull(user2.getId());
    }

    @Test
    public void testFetchData(){
        /*Test data retrieval*/
        User userA = userMongoRepository.findByUsername("Bob");
        assertNotNull(userA);
        assertEquals("33", userA.getChatId());
        /*Get all products, list should only have two*/
        Iterable<User> users = userMongoRepository.findAll();
        int count = 0;
        for(User p : users){
            count++;
        }
        assertEquals(count, 2);
    }

    @Test
    public void testRandomId(){
        /*Test data retrieval*/
        User userA = userMongoRepository.findByUsername("Bob");
        assertNotNull(userA);
        userA.setRandomId(Utils.generateRandomString());
        userMongoRepository.save(userA);
        assertNotNull(userA.getRandomId());
    }

    @Test
    public void testDataUpdate(){
        /*Test update*/
        User userB = userMongoRepository.findByUsername("Bob");
        userB.setChatId("40");
        userMongoRepository.save(userB);
        User userC= userMongoRepository.findByUsername("Bob");
        assertNotNull(userC);
        assertEquals("40", userC.getChatId());
    }

    @After
    public void tearDown() throws Exception {
        this.userMongoRepository.deleteAll();
    }

}

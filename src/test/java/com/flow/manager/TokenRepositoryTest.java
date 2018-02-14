package com.flow.manager;

import com.flow.manager.repo.entity.Token;
import com.flow.manager.repo.TokenRepository;
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
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Before
    public void setUp() throws Exception {
        Token token1= new Token("refresh111","access111","Bob");
        Token token2= new Token("refresh222","access222","Alice");
        //save product, verify has ID value after save
        //assertNull(token1.getId());
        //assertNull(token2.getId());//null before save
        this.tokenRepository.save(token1);
        this.tokenRepository.save(token2);
        //assertNotNull(token1.getId());
        //assertNotNull(token2.getId());
    }

    @Test
    public void testFetchData(){
        /*Test data retrieval*/
        Token token1 = tokenRepository.findByUserId("Bob");
        assertNotNull(token1);
        assertEquals("access111", token1.getAccessToken());
        assertEquals("refresh111", token1.getRefreshToken());
        /*Get all products, list should only have two*/
        Iterable<Token> tokens = tokenRepository.findAll();
        int count = 0;
        for(Token t : tokens){
            count++;
        }
        assertEquals(count, 2);
    }

    @Test
    public void testDataUpdate(){
        /*Test update*/
        Token token2 = tokenRepository.findByUserId("Bob");
        token2.setAccessToken("access444");
        token2.setRefreshToken("refresh444");

        tokenRepository.save(token2);
        Token token3= tokenRepository.findByUserId("Bob");
        assertNotNull(token3);
        assertEquals("access444", token3.getAccessToken());
        assertEquals("refresh444", token3.getRefreshToken());
    }

    @After
    public void tearDown() throws Exception {
        this.tokenRepository.deleteAll();
    }
}

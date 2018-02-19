package com.flow.manager.repo;

import com.flow.manager.repo.entity.Token;
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

    private Token token1;
    private Token token2;

    @Before
    public void setUp() throws Exception {
        token1 = new Token("refresh111","access111","Bob", 1L);
        token2 = new Token("refresh222","access222","Alice",2L);
        this.tokenRepository.save(token1);
        this.tokenRepository.save(token2);
        assertEquals(2,tokenRepository.findAll().size());
    }

    @Test
    public void testFetchData(){
        /*Test data retrieval*/
        Token token1 = tokenRepository.findByUserId("Bob");
        assertNotNull(token1);
        assertEquals("refresh111", token1.getRefreshToken());
        assertEquals("access111", token1.getAccessToken());
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
        this.tokenRepository.delete(token1);
        this.tokenRepository.delete(token2);
    }
}

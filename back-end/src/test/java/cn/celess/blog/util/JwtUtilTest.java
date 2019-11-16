package cn.celess.blog.util;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class JwtUtilTest extends BaseTest {

    @Autowired
    JwtUtil jwtUtil;
    private String jwtExp = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhQGNlbGVzcy5jbiIsImV4cCI6MTU3NDMwNzIyNH0.kIyJPLqnzmQhh0NPXOsj6hdgbbqyfFBcD2nOUU2EiEyqPWAKao_hxSHAZQM_8FbXvvElgioebT8oj9jc2UYEdQ";


    @Test
    public void generateToken() {
        User user = new User();
        user.setEmail("a@celess.cn");
        String s = jwtUtil.generateToken(user);
        System.out.println(s);
        assertNotNull(s);
        jwtExp = s;
    }

    @Test
    public void validateToken() {
        User user = new User();
        user.setEmail("a@celess.cn");
        assertTrue(jwtUtil.validateToken(jwtExp, user));
    }

    @Test
    public void isTokenExpired() {
        assertFalse(jwtUtil.isTokenExpired(jwtExp));
    }

    @Test
    public void getUsernameFromToken() {
        assertEquals("a@celess.cn", jwtUtil.getUsernameFromToken(jwtExp));
    }

    @Test
    public void getExpirationDateFromToken() {
        assertNotNull(jwtUtil.getExpirationDateFromToken(jwtExp));
    }
}
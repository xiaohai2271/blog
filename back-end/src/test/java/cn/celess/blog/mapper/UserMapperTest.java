package cn.celess.blog.mapper;

import cn.celess.blog.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void addUser() {
        userMapper.addUser("a@celess.cn", "123");
    }

    @Test
    public void updateInfo() {
    }

    @Test
    public void getPwd() {
    }

    @Test
    public void existsByEmail() {
    }

    @Test
    public void findByEmail() {
        User byEmail = userMapper.findByEmail("a@celess.cn");
        System.out.println(byEmail);
    }

    @Test
    public void findById() {
        User byId = userMapper.findById(2);
        System.out.println(byId);
    }

    @Test
    public void getAvatarImgUrlById() {
    }

    @Test
    public void getEmail() {
    }

    @Test
    public void getDisPlayName() {
    }
}
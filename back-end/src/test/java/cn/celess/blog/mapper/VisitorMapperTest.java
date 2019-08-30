package cn.celess.blog.mapper;

import cn.celess.blog.entity.Visitor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VisitorMapperTest {

    @Autowired
    VisitorMapper visitorMapper;

    @Test
    public void save() {
        Visitor visitor = new Visitor();
        visitor.setDate(new Date());
        visitor.setIp("127.0.0.1");
        visitor.setUa("ua....");
        Assert.assertEquals(1, visitorMapper.insert(visitor));
    }

    @Test
    public void delete() {
        Assert.assertEquals(1,visitorMapper.delete(1));
    }

    @Test
    public void findAll() {
        Assert.assertNotEquals(0,visitorMapper.findAll().size());
    }
}
package cn.celess.blog.mapper;

import cn.celess.blog.entity.WebUpdate;
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
public class WebUpdateInfoMapperTest {

    @Autowired
    WebUpdateInfoMapper webUpdateInfoMapper;

    @Test
    public void save() {
        WebUpdate webUpdate = new WebUpdate();
        webUpdate.setUpdateInfo("testUpdate");
        webUpdate.setUpdateTime(new Date());
        int save = webUpdateInfoMapper.insert(webUpdate);

        Assert.assertEquals(1, save);
    }

    @Test
    public void delete() {
        Assert.assertEquals(1, webUpdateInfoMapper.delete(1));
        Assert.assertEquals(0, webUpdateInfoMapper.delete(1));
    }

    @Test
    public void update() {
        Assert.assertEquals(1, webUpdateInfoMapper.update(2, "test"));
    }

    @Test
    public void existsById() {
        Assert.assertEquals(true, webUpdateInfoMapper.existsById(2));
        Assert.assertEquals(false, webUpdateInfoMapper.existsById(1));
    }

    @Test
    public void findById() {
        Assert.assertNotNull(webUpdateInfoMapper.findById(2));
        Assert.assertNull(webUpdateInfoMapper.findById(3));
    }

    @Test
    public void findAll() {
        Assert.assertNotEquals(0,webUpdateInfoMapper.findAll().size());
    }
}
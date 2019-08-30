package cn.celess.blog.mapper;

import cn.celess.blog.entity.Tag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagMapperTest {

    @Autowired
    TagMapper tagMapper;

    @Test
    public void save() {
        Tag tag = new Tag();
        tag.setName("hhh");
        tag.setArticles("1,");
        Assert.assertEquals(1, tagMapper.insert(tag));
    }

    @Test
    public void update() {
        Tag tag = tagMapper.findTagById(1);
        tag.setName("test");
        Assert.assertEquals(1, tagMapper.update(tag));
        Tag tag1 = new Tag();
        Assert.assertEquals(0,tagMapper.update(tag1));

    }

    @Test
    public void delete() {
        Assert.assertEquals(0,tagMapper.delete(1));
    }

    @Test
    public void findTagById() {
        Assert.assertNotNull(tagMapper.findTagById(1));
        Assert.assertNull(tagMapper.findTagById(2));
    }

    @Test
    public void findTagByName() {
        Assert.assertNotNull(tagMapper.findTagByName("test"));
        Assert.assertNull(tagMapper.findTagByName("aasd"));
    }

    @Test
    public void existsByName() {
        Assert.assertEquals(true,tagMapper.existsByName("test"));
        Assert.assertEquals(false,tagMapper.existsByName("tesdasdsat"));
    }

    @Test
    public void getIDByName() {
        Assert.assertEquals(1,(long)tagMapper.getIDByName("test"));
        Assert.assertNull(tagMapper.getIDByName("testaa"));
    }

    @Test
    public void getNameById() {
        Assert.assertEquals("test",tagMapper.getNameById(1));
        Assert.assertNull(tagMapper.getNameById(2));
    }
}
package cn.celess.blog.mapper;

import cn.celess.blog.entity.Category;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryMapperTest {
    @Autowired
    CategoryMapper categoryMapper;

    @Test
    public void save() {
        Category category = new Category();
        category.setName("test");
        category.setArticles("1,");
        Assert.assertEquals(1, categoryMapper.insert(category));
        Assert.assertEquals(0, categoryMapper.insert(new Category()));
    }

    @Test
    public void delete() {
        Assert.assertEquals(1, categoryMapper.delete(1));
        Assert.assertEquals(2, categoryMapper.delete(2));
    }

    @Test
    public void update() {
    }

    @Test
    public void existsByName() {
    }

    @Test
    public void existsById() {
    }

    @Test
    public void findCategoryByName() {
    }

    @Test
    public void findCategoryById() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void getAllName() {
    }

    @Test
    public void getNameById() {
    }

    @Test
    public void getIDByName() {
    }
}
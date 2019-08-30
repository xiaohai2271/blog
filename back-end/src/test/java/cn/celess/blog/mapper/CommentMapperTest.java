package cn.celess.blog.mapper;

import cn.celess.blog.entity.Comment;
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
public class CommentMapperTest {

    @Autowired
    CommentMapper commentMapper;


    @Test
    public void save() {
        Comment comment = new Comment();
        comment.setType(true);
        comment.setContent("test comment ");
        comment.setArticleID(2L);
        comment.setAuthorID(2L);
        comment.setDate(new Date());
        Assert.assertEquals(1, commentMapper.insert(comment));
        Assert.assertEquals(0, commentMapper.insert(new Comment()));
    }

    @Test
    public void updateContent() {
        Assert.assertEquals(1, commentMapper.updateContent("test update", 15));
        Assert.assertEquals(0, commentMapper.updateContent("test update", 55));

    }

    @Test
    public void updateResponder() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void existsById() {
    }

    @Test
    public void findCommentById() {
    }

    @Test
    public void findAllByAuthorIDAndType() {
    }

    @Test
    public void findAllByPId() {
    }

    @Test
    public void findAllByArticleID() {
    }

    @Test
    public void findAllByArticleIDAndPId() {
    }

    @Test
    public void findCommentsByTypeAndPId() {
    }

    @Test
    public void findAllByPId1() {
    }

    @Test
    public void findAllByType() {
    }

    @Test
    public void countByType() {
    }
}
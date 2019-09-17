package cn.celess.blog.mapper;


import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Article;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArticleMapperTest extends BaseTest {

    @Autowired
    ArticleMapper articleMapper;

    private static Article mArticle;

    @Test
    public void save() {
        Article article = new Article();
        article.setTitle("unit test for articleMapper");
        article.setAuthorId(1L);
        article.setSummary("unit test");
        article.setMdContent("### hello this is the unit ");
        article.setCategoryId(1L);
        List list = new ArrayList();
        article.setTagsId("1,2,3,4");
        article.setNextArticleId(-1L);
        article.setPreArticleId(articleMapper.getLastestArticleId());
        article.setPublishDate(new Date());

        //成功插入
        Assert.assertEquals(1, articleMapper.insert(article));
        mArticle = articleMapper.getLastestArticle();
    }

    @Test
    public void saveForEmoji() {
        Article article = new Article();
        article.setTitle("❤");
        article.setAuthorId(1L);
        article.setSummary("unit test");
        article.setMdContent("❤");
        article.setCategoryId(1L);
        article.setTagsId("1,2,3,4");
        article.setNextArticleId(-1L);
        article.setPreArticleId(articleMapper.getLastestArticleId());
        article.setPublishDate(new Date());

        assertEquals(1, articleMapper.insert(article));
    }


    @Test
    public void update() {
        Article article = articleMapper.findArticleById(1288);
        // test for article title
        article.setTitle("test Update");
        article.setAuthorId(2L);
        article.setCategoryId(article.getCategoryId());
        Assert.assertEquals(1, articleMapper.update(article));

    }

    @Test
    public void updateNextArticleId() {
        Long nextArticleId = mArticle.getNextArticleId();
        Assert.assertEquals(1, articleMapper.updateNextArticleId(mArticle.getId(), 12));
        // reset to the right value
        Assert.assertEquals(1, articleMapper.updateNextArticleId(mArticle.getId(), nextArticleId));
    }

    @Test
    public void updatePreArticleId() {
        Long preArticleId = mArticle.getPreArticleId();
        Assert.assertEquals(1, articleMapper.updatePreArticleId(mArticle.getId(), 12));
        // reset to the right value again
        Assert.assertEquals(1, articleMapper.updatePreArticleId(mArticle.getId(), preArticleId));
    }

    @Test
    public void getLatestArticleId() {
        Assert.assertNotEquals(0, articleMapper.getLastestArticleId());
    }

    @Test
    public void findArticleById() {
        Article articleById = articleMapper.findArticleById(1296);
        Assert.assertNotNull(articleMapper.findArticleById(mArticle.getId()));
    }

    @Test
    public void existsByTitle() {
        assertTrue(articleMapper.existsByTitle(mArticle.getTitle()));
        assertFalse(articleMapper.existsByTitle("does not exist title "));
    }

    @Test
    public void existsById() {
        assertTrue(articleMapper.existsById(mArticle.getId()));
        assertFalse(articleMapper.existsById(-55));
    }

    @Test
    public void findAllByAuthorId() {
        // normal article`s author id is 1 but after updating the number should be 2
        // and also the author id of 2 may not only one in test data
        Assert.assertNotEquals(0, articleMapper.findAllByAuthorId(2).size());
        Assert.assertEquals(0, articleMapper.findAllByAuthorId(3).size());
    }

    @Test
    public void findAllByOpen() {
        Assert.assertNotEquals(0, articleMapper.findAllByOpen(true).size());
        Assert.assertEquals(0, articleMapper.findAllByOpen(false).size());
    }

    @Test
    public void getTitleById() {
        Assert.assertEquals(mArticle.getTitle(), articleMapper.getTitleById(mArticle.getId()));
        System.out.println("不存在id=50的title---->" + articleMapper.getTitleById(50));
    }

    @Test
    public void findAllByCategoryId() {
        Assert.assertNotEquals(0, articleMapper.findAllByCategoryId(2).size());
        Assert.assertEquals(0, articleMapper.findAllByCategoryId(10).size());
    }

    @Test
    public void findAll() {
        List<Article> all = articleMapper.findAll();
        for (Article a : all) {
            System.out.println(a.getMdContent());
        }
        Assert.assertNotEquals(0, all.size());
    }

    @Test
    public void delete() {
        Assert.assertEquals(1, articleMapper.delete(mArticle.getId()));
        // one data , delete twice. so the second assert should be 0
        Assert.assertEquals(0, articleMapper.delete(mArticle.getId()));
    }
}
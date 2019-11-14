package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.request.ArticleReq;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ArticleControllerTest extends BaseTest {


    @Test
    public void create() throws Exception {
        ArticleReq articleReq = new ArticleReq();
        JSONObject jsonObject = JSONObject.fromObject(articleReq);
        MvcResult mvcResult = mockMvc.perform(post("/admin/article/create").content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void delete() {
    }

    @Test
    public void update() {
    }

    @Test
    public void retrieveOneById() {
        try {
            int articleID = 3;
            mockMvc.perform(MockMvcRequestBuilders.get("/article/articleID/" + articleID))
                    .andExpect(status().is(200));
            mockMvc.perform(MockMvcRequestBuilders.get("/article/articleID/" + articleID + "?update=true"))
                    .andExpect(status().is(200));
            JSONObject jsonObject = JSONObject.fromObject(mockMvc.perform(MockMvcRequestBuilders.get("/article/articleID/-1"))
                    .andExpect(status().is(200)).andReturn().getResponse().getContentAsString());
            assertEquals(201, jsonObject.getInt(Code));


            MvcResult article = mockMvc.perform(MockMvcRequestBuilders.get("/article/articleID/" + articleID + "?update=false"))
                    .andExpect(status().is(200))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

            JSONObject articleJson = JSONObject.fromObject(article.getResponse().getContentAsString());
            // 断言获取数据成功
            assertEquals(0, articleJson.getInt(Code));
            assertNotNull(articleJson.getJSONObject(Result));

            ArticleModel a = (ArticleModel) JSONObject.toBean(articleJson.getJSONObject(Result), ArticleModel.class);
            assertNotNull(a.getTitle());
            assertNotNull(a.getId());
            assertNotNull(a.getSummary());
            assertNotNull(a.getMdContent());
            assertNotNull(a.getUrl());
            assertNotNull(a.getUpdateDateFormat());
            assertNotNull(a.getPreArticleId());
            assertNotNull(a.getPreArticleId());
            assertNotNull(a.getNextArticleId());
            assertNotNull(a.getNextArticleTitle());
            assertNotNull(a.getReadingNumber());
            // assertNotNull(a.getOpen());
            assertNotNull(a.getOriginal());
            assertNotNull(a.getPublishDateFormat());
            assertNotNull(a.getCategory());
            assertNotNull(a.getTags());
            assertNotNull(a.getAuthorName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void articles() {
        try {
            // 测试不带参数访问
            mockMvc.perform(MockMvcRequestBuilders.get("/articles"))
                    .andExpect(status().is(200));

            MvcResult articles = mockMvc.perform(MockMvcRequestBuilders.get("/articles?page=1&count=5"))
                    .andExpect(status().is(200))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            JSONObject articlesJSON = JSONObject.fromObject(articles.getResponse().getContentAsString());
            Response response = (Response) JSONObject.toBean(articlesJSON, Response.class);
            // 断言获取数据成功
            assertEquals(response.getCode(), 0);
            // 结果集非空
            assertNotNull(response.getResult());
            // 判断pageInfo是否包装完全
            JSONObject result = JSONObject.fromObject(response.getResult());
            PageInfo pageInfo = (PageInfo) JSONObject.toBean(result, PageInfo.class);
            assertNotEquals(0, pageInfo.getTotal());
            assertNotEquals(0, pageInfo.getStartRow());
            assertNotEquals(0, pageInfo.getEndRow());
            assertEquals(1, pageInfo.getPageNum());
            assertEquals(5, pageInfo.getPageSize());
            // 内容完整
            for (Object arc : pageInfo.getList()) {
                ArticleModel a = (ArticleModel) JSONObject.toBean(JSONObject.fromObject(arc), ArticleModel.class);
                assertNotNull(a.getTitle());
                assertNotNull(a.getId());
                assertNotNull(a.getSummary());
                assertNotNull(a.getOriginal());
                assertNotNull(a.getPublishDateFormat());
                assertNotNull(a.getCategory());
                assertNotNull(a.getTags());
                assertNotNull(a.getAuthorName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void adminArticles() {
    }

    @Test
    public void findByCategory() {
        try {
            String categoryName = "NoSuchCategory";
            JSONObject json = JSONObject.fromObject(mockMvc.perform(MockMvcRequestBuilders.get("/articles/category/" + categoryName + "?page=1&count=10"))
                    .andExpect(status().is(200)).andReturn().getResponse().getContentAsString());
            assertEquals(501,json.getInt(Code));

            categoryName = "linux";
            JSONObject jsonObject = JSONObject.fromObject(mockMvc.perform(MockMvcRequestBuilders.get("/articles/category/" + categoryName + "?page=1&count=10"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is(200)).andReturn().getResponse().getContentAsString());
            assertEquals(0, jsonObject.getInt(Code));
            PageInfo pageInfo = (PageInfo) JSONObject.toBean(jsonObject.getJSONObject(Result), PageInfo.class);
            assertNotEquals(0, pageInfo.getTotal());
            assertNotEquals(0, pageInfo.getStartRow());
            assertNotEquals(0, pageInfo.getEndRow());
            assertEquals(1, pageInfo.getPageNum());
            assertEquals(10, pageInfo.getPageSize());

            for (Object arc : pageInfo.getList()) {
                JSONObject jsonObject1 = JSONObject.fromObject(arc);
                assertNotEquals(0, jsonObject1.getInt("id"));
                assertNotNull(jsonObject1.getString("title"));
                assertNotNull(jsonObject1.getString("summary"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByTag() {
        try {
            String tagName = "NoSuchCategory";
            JSONObject json = JSONObject.fromObject(mockMvc.perform(MockMvcRequestBuilders.get("/articles/tag/" + tagName + "?page=1&count=10"))
                    .andExpect(status().is(200)).andReturn().getResponse().getContentAsString());
            assertEquals(401,json.getInt(Code));

            tagName = "linux";
            JSONObject jsonObject = JSONObject.fromObject(mockMvc.perform(MockMvcRequestBuilders.get("/articles/tag/" + tagName + "?page=1&count=10"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is(200)).andReturn().getResponse().getContentAsString());
            assertEquals(0, jsonObject.getInt(Code));
            PageInfo pageInfo = (PageInfo) JSONObject.toBean(jsonObject.getJSONObject(Result), PageInfo.class);
            assertNotEquals(0, pageInfo.getTotal());
            assertNotEquals(0, pageInfo.getStartRow());
            assertNotEquals(0, pageInfo.getEndRow());
            assertEquals(1, pageInfo.getPageNum());
            assertEquals(10, pageInfo.getPageSize());

            for (Object arc : pageInfo.getList()) {
                JSONObject jsonObject1 = JSONObject.fromObject(arc);
                assertNotEquals(0, jsonObject1.getInt("id"));
                assertNotNull(jsonObject1.getString("title"));
                assertNotNull(jsonObject1.getString("summary"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
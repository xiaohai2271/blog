package cn.celess.blog.service;

import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.request.ArticleReq;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;


/**
 * @author : xiaohai
 * @date : 2019/03/28 15:20
 */
@Service
public interface ArticleService {
    ArticleModel create(ArticleReq reqBody);

    boolean delete(long articleID);

    ArticleModel update(ArticleReq reqBody);

    ArticleModel retrieveOneByID(long articleID, boolean is4update);

    /**
     * @param count
     * @param page  -1
     * @return
     */
    PageInfo adminArticles(int count, int page);

    PageInfo retrievePageForOpen(int count, int page);

    PageInfo findByCategory(String name,int page,int count);

    PageInfo findByTag(String name,int page,int count);
}

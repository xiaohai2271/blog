package cn.celess.blog.service;

import org.springframework.stereotype.Service;

/**
 * @author : xiaohai
 * @date : 2019/04/02 22:04
 */
@Service
public interface CountService {
    long getCommentCount();

    long getArticleCount();

    long getCategoriesCount();

    long getTagsCount();

    long getLeaveMessageCount();

    long getUserCount();

    long getVisitorCount();

    long getDayVisitCount();
}

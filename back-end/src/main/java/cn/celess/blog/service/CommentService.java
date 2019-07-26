package cn.celess.blog.service;

import cn.celess.blog.entity.Comment;
import cn.celess.blog.entity.model.CommentModel;
import cn.celess.blog.entity.request.CommentReq;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : xiaohai
 * @date : 2019/03/29 16:58
 */
@Service
public interface CommentService {
    CommentModel create(CommentReq reqBody);

    boolean delete(long id);

    CommentModel update(CommentReq reqBody);

    /**
     * 分页查询
     *
     * @param page
     * @param count
     * @return
     */

    PageInfo<CommentModel> retrievePage(Boolean isComment, int page, int count);

    /**
     * 分页查询
     *
     * @param pid
     * @param page  默认-1
     * @param count
     * @return
     */
    PageInfo<CommentModel> retrievePageByPid(long pid, int page, int count);


    PageInfo<CommentModel> retrievePageByAuthor(Boolean isComment, int page, int count);

    /**
     * 分页查询评论/留言
     *
     * @return
     */
    PageInfo<CommentModel> retrievePageByArticle(long articleID, long pid, int page, int count);

    /**
     * 分页查询评论/留言
     */
    PageInfo<CommentModel> retrievePageByTypeAndPid(Boolean isComment, int pid, int page, int count);

    PageInfo<CommentModel> retrievePageByType(Boolean isComment, int page, int count);

}

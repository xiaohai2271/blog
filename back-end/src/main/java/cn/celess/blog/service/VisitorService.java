package cn.celess.blog.service;

import cn.celess.blog.entity.Visitor;
import cn.celess.blog.entity.model.VisitorModel;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : xiaohai
 * @date : 2019/04/02 23:03
 */
@Service
public interface VisitorService {
    long getViews();

    PageInfo<VisitorModel> visitorPage( int page, int count, boolean showLocation);

    Boolean addVisitor(HttpServletRequest request);

    String location(String ip);
}

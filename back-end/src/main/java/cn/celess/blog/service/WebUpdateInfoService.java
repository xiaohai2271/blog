package cn.celess.blog.service;

import cn.celess.blog.entity.model.WebUpdateModel;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:42
 */
@Service
public interface WebUpdateInfoService {
    Boolean create(String info);

    Boolean del(long id);

    WebUpdateModel update(long id, String info);

    PageInfo<WebUpdateModel> pages(int count, int page);

    List<WebUpdateModel> findAll();

    String getLastestUpdateTime();
}

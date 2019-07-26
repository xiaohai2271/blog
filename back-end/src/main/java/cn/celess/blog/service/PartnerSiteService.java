package cn.celess.blog.service;

import cn.celess.blog.entity.PartnerSite;
import cn.celess.blog.entity.request.LinkReq;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:42
 */
@Service
public interface PartnerSiteService {
    PartnerSite create(LinkReq reqBody);

    Boolean del(long id);

    PartnerSite update(LinkReq reqBody);

    PageInfo<PartnerSite> PartnerSitePages(int page, int count);

    List<PartnerSite> findAll();

}

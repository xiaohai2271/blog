package cn.celess.blog.service;

import cn.celess.blog.entity.Tag;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:23
 */
@Service
public interface TagService {
    /**
     * @param name 名称
     * @return
     */
    Tag create(String name);

    Tag create(Tag tag);

    boolean delete(long tagId);

    Tag update(Long id,String name);

    /**
     * @param tagId id
     * @return
     */
    Tag retrieveOneById(long tagId);

    /**
     * @param name tag的名称
     * @return
     */
    Tag retrieveOneByName(String name);


    PageInfo<Tag> retrievePage(int page, int count);

    List<Tag> findAll();

}

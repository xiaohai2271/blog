package cn.celess.blog.service;

import cn.celess.blog.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:42
 */
@Service
public interface CategoryService {
    Category create(String name);//增加一个分类

    Category create(Category category);//增加一个分类

    boolean delete(long id);//通过id删除分类

    Category update(Long id,String name);//编辑分类

    List<Category> retrievePage();

}

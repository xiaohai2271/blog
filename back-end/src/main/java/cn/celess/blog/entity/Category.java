package cn.celess.blog.entity;


import lombok.Data;

import javax.persistence.Entity;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:18
 */
@Data
public class Category {

    private Long id;

    private String name;

    private String articles;

}

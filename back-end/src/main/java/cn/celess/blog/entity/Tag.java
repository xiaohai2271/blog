package cn.celess.blog.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:19
 */
@Data
public class Tag {
    private Long id;

    private String name;

    private String articles;
}

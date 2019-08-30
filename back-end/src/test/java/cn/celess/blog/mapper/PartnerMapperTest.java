package cn.celess.blog.mapper;

import cn.celess.blog.entity.PartnerSite;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PartnerMapperTest {

    @Autowired
    PartnerMapper partnerMapper;

    @Test
    public void save() {
        PartnerSite site = new PartnerSite();
        site.setName("小海博客");
        site.setUrl("www.celess.cn");
        site.setOpen(true);
        Assert.assertEquals(1, partnerMapper.insert(site));
//        Assert.assertEquals(0, partnerMapper.save(new PartnerSite()));
    }

    @Test
    public void delete() {
        Assert.assertEquals(1, partnerMapper.delete(1503));
        Assert.assertEquals(0, partnerMapper.delete(15));
    }

    @Test
    public void update() {
        PartnerSite site = new PartnerSite();
        site.setId(1502L);
        site.setUrl("celess.site");
        site.setName("site");
        Assert.assertEquals(1, partnerMapper.update(site));
        Assert.assertEquals(0, partnerMapper.update(new PartnerSite()));
    }

    @Test
    public void existsById() {
        Assert.assertEquals(true, partnerMapper.existsById(1327));
        Assert.assertEquals(false, partnerMapper.existsById(15));
    }

    @Test
    public void existsByName() {
        Assert.assertEquals(true, partnerMapper.existsByName("微笑的丁总的个人博客"));
        Assert.assertEquals(false, partnerMapper.existsByName("test Exist"));
    }

    @Test
    public void existsByUrl() {
        Assert.assertEquals(true, partnerMapper.existsByUrl("http://www.csxll.top"));
        Assert.assertEquals(false, partnerMapper.existsByUrl("test Exist"));
    }


    @Test
    public void findById() {
        Assert.assertNotNull(partnerMapper.findById(1502));
        Assert.assertNull(partnerMapper.findById(1));
    }

    @Test
    public void findByName() {
        Assert.assertNotNull(partnerMapper.findByName("陈晓雷的技术博客"));
        Assert.assertNull(partnerMapper.findByName("xxx"));
    }

    @Test
    public void findByUrl() {
        Assert.assertNotNull(partnerMapper.findByUrl("http://www.csxll.top"));
        Assert.assertNull(partnerMapper.findByUrl("xxx"));
    }
}
package cn.celess.blog;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @Author: 小海
 * @Date： 2019/08/22 12:46
 * @Description： 测试基类
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class BaseTest {

    @Before
    public void before() {
        System.out.println("==========> 开始测试 <=========");
    }

}

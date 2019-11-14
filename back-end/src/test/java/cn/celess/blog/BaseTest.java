package cn.celess.blog;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @Author: 小海
 * @Date: 2019/08/22 12:46
 * @Description: 测试基类
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class BaseTest {

    protected MockMvc mockMvc;
    protected final static String Code = "code";
    protected final static String Result = "result";

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        System.out.println("==========> 开始测试 <=========");
    }

}

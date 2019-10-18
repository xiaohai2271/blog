package cn.celess.blog.configuration;

import cn.celess.blog.configuration.filter.MultipleSubmitFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: 小海
 * @Date: 2019/10/18 14:19
 * @Description:
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MultipleSubmitFilter()).addPathPatterns("/*");
    }
}

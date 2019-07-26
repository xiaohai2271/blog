package cn.celess.blog.configuration;

import cn.celess.blog.configuration.filter.RestAuthorizationFilter;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : xiaohai
 * @date : 2019/04/01 13:58
 */

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("roles", new RestAuthorizationFilter());

        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 设置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 设置路径对应的权限
        filterChainDefinitionMap.put("/admin/**", "roles[admin]");
        filterChainDefinitionMap.put("/user/**", "roles[admin,user]");
        filterChainDefinitionMap.put("/**", "anon");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(customRealm());
        return securityManager;
    }

    @Bean
    public CustomRealm customRealm() {
        return new CustomRealm();
    }

}
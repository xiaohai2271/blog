package cn.celess.blog.configuration;

import cn.celess.blog.entity.User;
import cn.celess.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;


/**
 * @author : xiaohai
 * @date : 2019/04/1 13:57
 */
public class CustomRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(CustomRealm.class);

    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest request;

    /**
     * 获取授权信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String email = (String) SecurityUtils.getSubject().getPrincipal();
        logger.info("====> {}  进行登录认证  <====", email);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获得该用户角色
        String role = userService.getUserRoleByEmail(email);
        Set<String> set = new HashSet<>();
        //需要将 role 封装到 Set 作为 info.setRoles() 的参数
        set.add(role);
        //设置该用户拥有的角色
        info.setRoles(set);
        return info;
    }

    /**
     * 获取身份验证信息
     * Shiro 中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。
     *
     * @param authenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.getUserInfoByEmail(token.getUsername());
        if (user == null) {
            //用户不存在
            throw new UnknownAccountException("此账户不存在或者密码错误");
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(token.getPrincipal(), user.getPwd(), getName());
        return simpleAuthenticationInfo;
    }
}

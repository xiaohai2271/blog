package cn.celess.blog.configuration.filter;


import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.util.ResponseUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;


/**
 * @author : xiaohai
 * @date : 2019/04/02 16:35
 */
public class RestAuthorizationFilter extends PermissionsAuthorizationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        response.setContentType("application/Json");
        response.setCharacterEncoding("utf-8");
        Subject subject = this.getSubject(request, response);

        if (subject.getPrincipal() == null) {
            // 未登录
            response.getWriter().println(ResponseUtil.response(ResponseEnum.HAVE_NOT_LOG_IN, null).toString());
        } else {
            String unauthorizedUrl = this.getUnauthorizedUrl();
            if (!StringUtils.hasText(unauthorizedUrl)) {
                // 没权限
                response.getWriter().print(ResponseUtil.response(ResponseEnum.PERMISSION_ERROR, null).toString());
            }
        }
        return false;
    }

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;
        if (rolesArray == null || rolesArray.length == 0) {
            return false;
        }
        for (String s : rolesArray) {
            if (subject.hasRole(s)) {
                return true;
            }
        }
        return false;
    }

}

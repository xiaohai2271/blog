package cn.celess.blog.configuration.filter;


import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.util.ResponseUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

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
        Boolean jsonRes = true;
        response.setContentType("application/Json");
        response.setCharacterEncoding("utf-8");
        Subject subject = this.getSubject(request, response);
        if (subject.getPrincipal() == null) {
            if (!jsonRes) {
                this.saveRequestAndRedirectToLogin(request, response);
            } else {
                response.getWriter().println(ResponseUtil.response(ResponseEnum.HAVE_NOT_LOG_IN, null).toString());
            }
        } else {
            if (jsonRes) {
                String unauthorizedUrl = this.getUnauthorizedUrl();
                if (StringUtils.hasText(unauthorizedUrl)) {
                } else {
                    response.getWriter().print(ResponseUtil.response(ResponseEnum.PERMISSION_ERROR, null).toString());
                }
            } else {
                String unauthorizedUrl = this.getUnauthorizedUrl();
                if (StringUtils.hasText(unauthorizedUrl)) {
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {
                    WebUtils.toHttp(response).sendError(401);
                }
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
        for (int i = 0; i < rolesArray.length; i++) {
            if (subject.hasRole(rolesArray[i])) {
                return true;
            }
        }
        return false;
    }

}

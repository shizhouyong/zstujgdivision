package com.szy.inceptor;

import com.szy.cache.Session;
import com.szy.service.SystemService;
import com.szy.service.UserService;
import com.szy.util.UserLimitUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * Created by Administrator on 2016/10/21.
 */
public class SystemSecurityInterceptor implements HandlerInterceptor {

    private static UserService userService;
    private static SystemService systemService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String number = ((Session)request.getSession().getAttribute("cache")).getNumber();
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        userService = (UserService) factory.getBean("userService");
        systemService = (SystemService) factory.getBean("systemService");
        if(userService.ifHasAccess(number, UserLimitUtil.USER_MANAGER)){
            systemService.checkout();
            return true;
        } else {
            response.sendRedirect("/noAccess");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}

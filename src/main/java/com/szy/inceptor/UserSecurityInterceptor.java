package com.szy.inceptor;

import com.szy.controller.TeacherController;
import com.szy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 登陆控制拦截器
 * Created by Administrator on 2016/10/21.
 */
public class UserSecurityInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(UserSecurityInterceptor.class);

    private static UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        int tag = 0;
        String number = request.getParameter("number");
        String password = request.getParameter("password");
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        userService = (UserService) factory.getBean("userService");
        if(!userService.ifExistsUser(number)){
            tag = 2;
        } else{
            if(userService.checkLogin(number, password)){
                if(userService.ifHasAccess(number,"login")){
                    tag = 1;
                } else {
                    tag = 3;
                }
            } else {
                tag = 4;
            }
        }

        if(tag == 1){
            return true;
        } else {
            PrintWriter out = response.getWriter();
            if(tag == 2){
                logger.info("noUser");
                out.print("noUser");
            } else if(tag == 3){
                logger.info("noAccess");
                out.print("noAccess");
            } else {
                logger.info("Error");
                out.print("Error");
            }
            out.flush();
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
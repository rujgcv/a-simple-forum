package com.fufunode.interceptor;

import com.fufunode.annotation.RequireAdmin;
import com.fufunode.context.BaseContext;
import com.fufunode.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.lang.reflect.Method;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if(method.isAnnotationPresent(RequireAdmin.class)){
            return checkAdminPermission(request,response);
        }

        return true;
    }

    private boolean checkAdminPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String role = BaseContext.getCurrentRole();

        if(!"admin".equals(role)){
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":0,\"msg\":\"权限不足\"}");
            return false;
        }

        return true;
    }
}

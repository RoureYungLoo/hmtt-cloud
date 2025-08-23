package com.heima.wemedia.interceptor;

import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.common.AppJwtUtil;
import com.heima.utils.threadlocal.WmThreadLocalUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义的拦截器
 */
public class WmTokenInterceptor implements HandlerInterceptor {
    /**
     * 执行目标方法之前执行的过滤器方法
     * @param request
     * @param response
     * @param handler
     * @return  true表示放行，false表示拦截
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //TODO
        //4.放行
        return true;
    }

    /**
     * 完成所有的请求之后，执行的方法
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //释放资源
      //TODO
    }
}

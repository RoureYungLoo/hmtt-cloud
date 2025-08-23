package com.heima.comment.interceptor;

import com.heima.common.exception.CustomException;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojo.ApUser;
import com.heima.utils.threadlocal.AppThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");
        if(StringUtils.isNotBlank(userId) && Integer.valueOf(userId)>0){
            //存入到当前线程中
            ApUser apUser = new ApUser();
            apUser.setId(Integer.valueOf(userId));
            AppThreadLocalUtil.setUser(apUser);
            return true;
        }
        String url = request.getRequestURI();
        //添加针对游客放行的路径配置，放行前设置游客用户ID为0
        if(url.equals("/api/v1/comment/load") || url.equals("/api/v1/article/search/search/") ||
                url.equals("/api/v1/history/load/") || url.equals("/api/v1/history/del/")){
            ApUser apUser = new ApUser();
            apUser.setId(0);
            AppThreadLocalUtil.setUser(apUser);
            return true;
        }

        throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AppThreadLocalUtil.clear();
    }
}

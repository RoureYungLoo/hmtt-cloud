package com.heima.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/*
全局过滤器，校验token是否有效
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    /**
     * 执行过滤的方法
     * @param exchange 交换机，可以获取请求和响应对象
     * @param chain 过滤器链
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
       //TODO
        return chain.filter(exchange);
    }

    /**
     * 过滤的执行的优先级，返回值越小优先级越高
     */
    @Override
    public int getOrder() {
        return 0;
    }
}

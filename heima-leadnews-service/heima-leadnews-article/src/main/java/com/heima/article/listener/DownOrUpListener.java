package com.heima.article.listener;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.mapper.ArticleConfigMapper;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.common.constants.WmNewsMessageConstants;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DownOrUpListener {

    @Autowired
    private ArticleConfigMapper articleConfigMapper;
    /*
    监听文章上下架功能
     */
   @RabbitListener(
           bindings = @QueueBinding(
                   value = @Queue(name = WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_QUEUE,durable = "true"),
                   exchange = @Exchange(name = WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_TOPIC,type = ExchangeTypes.TOPIC),
                   key = WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_ROUTINGKEY
           )
   )
    public void getMessage(String  message){
        //TODO
    }
}

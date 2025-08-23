package com.heima.comment.service.impl;

import com.heima.comment.domain.pojos.ApComment;
import com.heima.comment.service.ApCommentHotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ApCommentHotServiceImpl implements ApCommentHotService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 异步计算热点评论
     * @param apComment
     */
    @Async("taskExecutor")
    @Override
    public void computeHotComment(ApComment apComment) {

        //1.查询热点评论列表（条件：type=0、objectId、flag=1 结果：likes倒序）
        List<ApComment> apCommentHotList = mongoTemplate.find(Query.query(Criteria.where("type").is(apComment.getType())
                .and("objectId").is(apComment.getObjectId())
                .and("flag").is(1))
                .with(Sort.by(Sort.Direction.DESC,"likes")), ApComment.class);

        //2.如果热点评论不足5条，更新当前评论为热点评论
        if(apCommentHotList.size()<5){
            apComment.setFlag(1);
            apComment.setUpdatedTime(new Date());
            mongoTemplate.save(apComment);
        } else {
            //3.如果热点评论已足5条，比较当前评论和最后一条热点评论的点赞数
            //3.1 取出最后一条热点评论
            ApComment apCommentHotLast = apCommentHotList.get(apCommentHotList.size() - 1);

            //3.2 比较当前评论和最后一条热点评论的点赞数谁大，如果当前评论点赞数更大则替换
            if(apComment.getLikes()>apCommentHotLast.getLikes()){
                //更新最后一条热点评论为普通评论
                apCommentHotLast.setFlag(0);
                apCommentHotLast.setUpdatedTime(new Date());
                mongoTemplate.save(apCommentHotLast);

                //更新当前评论为热点评论
                apComment.setFlag(1);
                apComment.setUpdatedTime(new Date());
                mongoTemplate.save(apComment);
            }
        }

    }
}

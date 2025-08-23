package com.heima.comment.service.impl;

import com.heima.api.feign.ApUserFeignClient;
import com.heima.comment.domain.dtos.CommentDto;
import com.heima.comment.domain.pojos.ApComment;
import com.heima.comment.domain.pojos.ApCommentLike;
import com.heima.comment.service.ApCommentHotService;
import com.heima.comment.service.ApCommentService;
import com.heima.comment.domain.vos.CommentVo;
import com.heima.model.comment.dtos.CommentLikeDto;
import com.heima.model.comment.dtos.CommentSaveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojo.ApUser;
import com.heima.utils.threadlocal.AppThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApCommentServiceImpl implements ApCommentService {

    @Autowired
    private ApUserFeignClient userClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResponseResult save(CommentSaveDto dto) {
        //1.判断参数是否为空
        if (dto.getArticleId() == null || StringUtils.isBlank(dto.getContent())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

        //2.判断用户是否存在
        Integer userId = AppThreadLocalUtil.getUser().getId();
        ApUser apUser = userClient.one(userId); //feign接口路径一定要在feign服务提供方user的WebMvcConfig里放行
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户不存在");
        }

        //TODO 3.判断文章是否存在

        //4.判断评论内容是否超过长度限制
        if (dto.getContent().length() > 140) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "评论内容不能超过140字");
        }

        //TODO 5.判断评论内容是否合规

        //6.构建评论数据并保存到MongoDB集合中
        ApComment apComment = new ApComment();
        apComment.setUserId(userId);//评论的用户id
        apComment.setUserName(apUser.getName());//评论的用户名
        apComment.setImage(apUser.getImage());//评论的用户头像地址
        apComment.setType(0);//内容类型，0-文章
        apComment.setObjectId(dto.getArticleId());//评论的文章id
        apComment.setLikes(0);//点赞数量
        apComment.setReply(0);//回复数量
        apComment.setFlag(0);//文章标记，0-普通评论
        apComment.setCreatedTime(new Date());//创建时间（评论的发布时间）
        apComment.setUpdatedTime(new Date());//更新时间
        apComment.setContent(dto.getContent());//评论内容
        mongoTemplate.save(apComment);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Autowired
    private ApCommentHotService apCommentHotService;


    @Override
    public ResponseResult like(CommentLikeDto dto) {

        //1. 判断参数是否为空
        if (dto.getOperation() == null || StringUtils.isBlank(dto.getCommentId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

        //2. 判断评论是否存在
        ApComment apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);
        if (apComment == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "评论不存在");
        }

        //3. 查询点赞记录
        Integer userId = AppThreadLocalUtil.getUser().getId();;
        ApCommentLike apCommentLike = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(userId)
                .and("commentId").is(dto.getCommentId())), ApCommentLike.class);

        //3.1 点赞记录不存在
        if (apCommentLike == null) {
            //3.1.1 判断是否取消点赞
            if (dto.getOperation() == 1) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "没有点赞过，无法取消点赞");
            }

            //3.1.2 更新评论点赞数+1  ap_comment
            mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(dto.getCommentId())),
                    new Update().inc("likes", 1).set("updatedTime", new Date()),
                    ApComment.class);

            //3.1.3 保存评论的点赞记录 ap_comment_like
            apCommentLike = new ApCommentLike();
            apCommentLike.setUserId(userId);
            apCommentLike.setCommentId(dto.getCommentId());
            apCommentLike.setOperation(dto.getOperation());
            apCommentLike.setCreatedTime(new Date());
            apCommentLike.setUpdatedTime(new Date());
            mongoTemplate.save(apCommentLike);

        } else {
            //3.2 点赞记录存在

            //3.2.1 判断是否重复点赞
            if (dto.getOperation() == 0 && apCommentLike.getOperation() == 0) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "不允许重复点赞");
            }

            //3.2.2 判断是否重复取消点赞
            if (dto.getOperation() == 1 && apCommentLike.getOperation() == 1) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "不允许重复取消点赞");
            }

            //3.2.3 如果是点赞，更新评论点赞数+1 ap_comment
            if (dto.getOperation() == 0) {
                mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(dto.getCommentId())),
                        new Update().inc("likes", 1).set("updatedTime", new Date()),
                        ApComment.class);
            } else {
                //3.2.4 如果是取消点赞，更新评论点赞数-1 ap_comment
                mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(dto.getCommentId())),
                        new Update().inc("likes", -1).set("updatedTime", new Date()),
                        ApComment.class);
            }

            //3.2.5 更新点赞记录
            apCommentLike.setOperation(dto.getOperation());
            apCommentLike.setUpdatedTime(new Date());
            mongoTemplate.save(apCommentLike);
        }

        //4. 响应最新点赞数
        //4.1 重新查询评论
        apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);

        //4.2 封装点赞数并响应
        Map map = new HashMap();
        map.put("likes", apComment.getLikes());//最新点赞数

        //判断是否符合计算热点评论的准入要求
        if(dto.getOperation()==0 && apComment.getFlag()==0 && apComment.getLikes()>5){
            //异步计算热点评论
            apCommentHotService.computeHotComment(apComment);
        }



        return ResponseResult.okResult(map);
    }


    @Override
    public ResponseResult list(CommentDto dto) {
        int size = 10;

        //1.判断参数是否为空
        if (dto.getArticleId() == null || dto.getMinDate() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.判断是否首页来查询评论列表
        List<ApComment> apCommentList = new ArrayList<>();

        //2.1 是首页，则查询热点评论列表+普通评论列表
        if(dto.getIndex()==1){
            //查询热点评论列表（条件：type=0、objectId、createdTime、flag=1  结果：likes倒序 限制5条） ap_comment
            Query queryHot = Query.query(Criteria.where("type").is(0)
                    .and("objectId").is(dto.getArticleId())
                    .and("createdTime").lt(dto.getMinDate())
                    .and("flag").is(1)
            ).with(Sort.by(Sort.Direction.DESC, "likes")).limit(5);
            List<ApComment> apCommentHotList = mongoTemplate.find(queryHot, ApComment.class);

            apCommentList.addAll(apCommentHotList);


            //查询剩余普通评论列表（条件：type=0、objectId、createdTime、flag=0  结果：createdTime倒序 限制条数=10-热点评论列表数量） ap_comment
            Query queryCommon = Query.query(Criteria.where("type").is(0)
                    .and("objectId").is(dto.getArticleId())
                    .and("createdTime").lt(dto.getMinDate())
                    .and("flag").is(0)
            ).with(Sort.by(Sort.Direction.DESC, "createdTime")).limit(size-apCommentHotList.size());
            List<ApComment> apCommentCommonList = mongoTemplate.find(queryCommon, ApComment.class);

            apCommentList.addAll(apCommentCommonList);

        } else { //2.2 非首页，则查询普通评论列表（条件：type=0、objectId、createdTime、flag=0  结果：createdTime倒序 限制10条） ap_comment
            Query query = Query.query(Criteria.where("type").is(0)
                    .and("objectId").is(dto.getArticleId())
                    .and("createdTime").lt(dto.getMinDate())
                    .and("flag").is(0)
            ).with(Sort.by(Sort.Direction.DESC, "createdTime")).limit(size);
            apCommentList = mongoTemplate.find(query, ApComment.class);
        }

        //3.如果是游客，则直接响应评论列表
        Integer userId = AppThreadLocalUtil.getUser().getId();;
        if (userId == 0) {
            return ResponseResult.okResult(apCommentList);
        }

        //4.如果是正常用户，则需要标识出评论列表中被其点赞过数据

        //4.1 获取评论ID列表
        List<String> commentIdList = apCommentList.stream().map(ApComment::getId).collect(Collectors.toList());

        //4.2 查询评论点赞记录（条件：userId、commentId、operation=0） ap_comment_like
        List<ApCommentLike> apCommentLikeList = mongoTemplate.find(Query.query(Criteria.where("userId").is(userId)
                        .and("commentId").in(commentIdList)
                        .and("operation").is(0))
                , ApCommentLike.class);

        //4.3 遍历评论列表，标识出被点赞过的评论
        List<CommentVo> commentVoList = new ArrayList<>();
        for (ApComment apComment : apCommentList) {
            CommentVo commentVo = new CommentVo();//构建带有点赞标识的评论数据
            BeanUtils.copyProperties(apComment, commentVo);

            long count = apCommentLikeList.stream().filter(x -> x.getCommentId().equals(apComment.getId())).count();
            if(count>0){
                commentVo.setOperation(0);//标识当前这条评论被该用户点赞过
            }

            commentVoList.add(commentVo);
        }

        //4.4 响应带有点赞标识的评论列表数据
        return ResponseResult.okResult(commentVoList);
    }
}

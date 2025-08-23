package com.heima.comment.controller;

import com.heima.comment.domain.dtos.CommentDto;
import com.heima.comment.service.ApCommentService;
import com.heima.model.comment.dtos.CommentLikeDto;
import com.heima.model.comment.dtos.CommentSaveDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private ApCommentService commentService;
    /**
     * 发表评论--针对文章
     * @param dto
     * @return
     */
    @PostMapping("/save")
    public ResponseResult saveComment(@RequestBody CommentSaveDto dto){
        return commentService.save(dto);
    }

    /**
     * 点赞评论动作
     * @param dto
     * @return
     */
    @PostMapping("/like")
    public ResponseResult likeComment(@RequestBody CommentLikeDto dto){
        return commentService.like(dto);
    }

    /**
     * 查询文章的评论列表
     * @param dto
     * @return
     */
    @PostMapping("/load")
    public ResponseResult loadComment(@RequestBody CommentDto dto){
        return commentService.list(dto);
    }
}

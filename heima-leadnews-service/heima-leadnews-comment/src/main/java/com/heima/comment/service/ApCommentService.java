package com.heima.comment.service;

import com.heima.comment.domain.dtos.CommentDto;
import com.heima.model.comment.dtos.CommentLikeDto;
import com.heima.model.comment.dtos.CommentSaveDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ApCommentService {


    /**
     * 评论发布
     * @param dto
     * @return
     */
    ResponseResult save(CommentSaveDto dto);

    /**
     * 评论点赞或取消点赞
     * @param dto
     * @return
     */
    ResponseResult like(CommentLikeDto dto);

    /**
     * 查询评论列表
     * @param dto
     * @return
     */
    ResponseResult list(CommentDto dto);
}

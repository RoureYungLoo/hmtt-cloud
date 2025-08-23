package com.heima.comment.domain.vos;

import com.heima.comment.domain.pojos.ApComment;
import lombok.Data;

@Data
public class CommentVo extends ApComment {

    /**
     * 0：点赞
     * 1：取消点赞
     */
    private Integer operation;
}
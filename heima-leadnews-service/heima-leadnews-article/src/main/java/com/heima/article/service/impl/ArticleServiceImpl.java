package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ArticleConfigMapper;
import com.heima.article.mapper.ArticleContentMapper;
import com.heima.article.mapper.ArticleMapper;
import com.heima.article.service.ArticleService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ApArticle> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleContentMapper articleContentMapper;

    @Autowired
    private ArticleConfigMapper articleConfigMapper;



    /**
     * 加载首页-加载更多-加载更新 三位一体
     *
     * @param dto  type=1 认为是加载更多，type=2表示加载更新
     * @param type
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
       //TODO
        //3.返回数据
        return ResponseResult.okResult(null);
    }


    /**
     * 保存三剑客
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public ResponseResult save(ArticleDto dto) {
       //TODO
        //3.返回数据,要不要返回文章id ，因为wmnews表中需要
        return ResponseResult.okResult(null);
    }

}

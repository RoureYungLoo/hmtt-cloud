package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.utils.threadlocal.WmThreadLocalUtil;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 查询文章列表
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult list(WmNewsPageReqDto dto) {
        //1.非空判断
        dto.checkParam();
        //2.分页查询
        IPage<WmNews> page=new Page<>(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmNews> queryWrapper= Wrappers.lambdaQuery();
        //2.1 根据状态查询
        if(dto.getStatus()!=null){
            queryWrapper.eq(WmNews::getStatus,dto.getStatus());
        }

        //2.2 根据关键字查询
        if(!StringUtils.isEmpty(dto.getKeyword())){
            queryWrapper.like(WmNews::getTitle,dto.getKeyword());
        }

        //2.3 根据频道id查询
        if(dto.getChannelId()!=null){
            queryWrapper.eq(WmNews::getChannelId,dto.getChannelId());
        }

        //2.4 根据开始和结束时间查询
        if(dto.getBeginPubdate()!=null && dto.getEndPubdate()!=null){
            queryWrapper.between(WmNews::getPublishTime,dto.getBeginPubdate(),dto.getEndPubdate());
        }

        //2.5 根据登录用户id查询
        queryWrapper.eq(WmNews::getUserId, WmThreadLocalUtil.getUser().getId());
        queryWrapper.orderByDesc(WmNews::getCreatedTime);

        page=super.page(page,queryWrapper);

        //3.返回数据结果
        ResponseResult result=new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }

    /**
     * 保存-修改-提交草稿为一体的方法
     *  主方法
     * @param dto
     * @return
     */
    @Override
    public ResponseResult submit(WmNewsDto dto) {
        //TODO
        //6.返回数据
        return ResponseResult.okResult("发布文章完成");
    }

    /**
     * 文章上下架
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult downOrUp(WmNewsDto dto) {
       //TODO
        return ResponseResult.okResult("文章上下架完成");
    }
}

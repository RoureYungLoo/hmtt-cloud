package com.heima.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojo.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import org.springframework.stereotype.Service;

@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    /**
     * 登录接口
     *   二合一，用户登录也是有游客登录
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(LoginDto dto) {


        return ResponseResult.okResult(null);
    }

    /**
     * 根据用户id获取用户实体
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseResult<ApUser>findUserById(Integer userId) {
        ApUser apUser = getById(userId);
        return ResponseResult.okResult(apUser);
    }
}

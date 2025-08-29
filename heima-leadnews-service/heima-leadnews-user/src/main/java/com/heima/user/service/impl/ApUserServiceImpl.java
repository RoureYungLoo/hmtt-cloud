package com.heima.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.vo.ApUserVo;
import com.heima.model.user.vo.LoginVo;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojo.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import com.heima.utils.common.AppJwtUtil;
import com.heima.utils.common.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

  /**
   * 登录接口
   * 二合一，用户登录也是有游客登录
   *
   * @param dto
   * @return
   */
  @Override
  public ResponseResult login(LoginDto dto) {

    ResponseResult res = null;
    String token = null;
    String phone = dto.getPhone();
    String password = dto.getPassword();
    // 用户登录
    if (StrUtil.isNotBlank(phone) && StrUtil.isNotBlank(password)) {
      LambdaQueryWrapper<ApUser> wrapper = Wrappers.<ApUser>lambdaQuery();
      wrapper.eq(ApUser::getPhone, phone);
      ApUser apUser = this.getOne(wrapper);
      if (ObjectUtil.isNotNull(apUser)) {
        if (BCrypt.checkpw(password, apUser.getPassword())) {
          token = AppJwtUtil.getToken(Long.valueOf(apUser.getId()));
          LoginVo loginVo = LoginVo.builder().user(
                  ApUserVo.builder().id(Long.valueOf(apUser.getId()))
                      .name(apUser.getName())
                      .phone(apUser.getPhone())
                      .build())
              .token(token).build();
          res = ResponseResult.okResult(loginVo);
        } else {
          res = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
      } else {
        res = ResponseResult.okResult(AppHttpCodeEnum.DATA_NOT_EXIST.getCode(), "用户不存在");
      }
    } else {
      // 游客登录
      token = AppJwtUtil.getToken(0L);
      LoginVo loginVo = LoginVo.builder().user(
              ApUserVo.builder().id(0L)
                  .name("")
                  .phone("")
                  .build())
          .token(token).build();
      res = ResponseResult.okResult(loginVo);
    }
    return res;
  }

  /**
   * 根据用户id获取用户实体
   *
   * @param userId
   * @return
   */
  @Override
  public ResponseResult<ApUser> findUserById(Integer userId) {
    ApUser apUser = getById(userId);
    return ResponseResult.okResult(apUser);
  }
}

package com.heima.model.user.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author luruoyang
 */
@Data
@Builder
public class LoginVo {
  private String token;
  private ApUserVo user;
}

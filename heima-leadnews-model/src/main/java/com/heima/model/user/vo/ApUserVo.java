package com.heima.model.user.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author luruoyang
 */
@Data
@Builder
public class ApUserVo {
  private Long id;
  private String name;
  private String phone;
}

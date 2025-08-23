package com.heima.user;

import cn.hutool.core.util.RandomUtil;
import com.heima.utils.common.BCrypt;
import com.heima.utils.common.MD5Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author luruoyang
 */
@DisplayName("Pwd Enc/Dec Test")
public class PwdEncTest {
  private final String rawPwd = "this is a password for test";

  @Test
  @DisplayName("MD5加密")
  public void testMD5() {
    String encoded = MD5Utils.encode(rawPwd);
    System.out.println(encoded);
  }

  @Test
  @DisplayName("MD5加盐加密")
  public void testMD5Salt() {
    String encoded = MD5Utils.encodeWithSalt(rawPwd, RandomUtil.randomString(32).toLowerCase());
    System.out.println(encoded);

  }

  @Test
  @DisplayName("Bcrypt加密")
  public void testBcrypt() {
    String salt = BCrypt.gensalt();
    String hashpw = BCrypt.hashpw(rawPwd, salt);
    if (BCrypt.checkpw(rawPwd+"1", hashpw)) {
      System.out.println("密码正确");
    } else {
      System.out.println("密码错误");
    }
  }
}

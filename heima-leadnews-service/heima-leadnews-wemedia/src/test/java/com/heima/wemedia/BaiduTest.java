package com.heima.wemedia;

import com.heima.audit.baidu.BaiduImageScan;
import com.heima.audit.baidu.BaiduTextScan;
import com.heima.file.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class BaiduTest {

    @Autowired
    private BaiduImageScan baiduImageScan;

    @Autowired
    private BaiduTextScan baiduTextScan;

    @Autowired
    private FileStorageService fileStorageService;


    //文本审核
    @Test
    public void textScan(){
        Integer integer = baiduTextScan.textScan("针孔摄像,私人侦探");
        if(integer==1){//合规
            log.info("内容合规");
        }
        if(integer==2){//内容不合规
            log.info("内容不合规");
        }
        if(integer==3){//内容疑似违规
            log.info("内容疑似违规，需要人工审核");
        }
        if(integer==4){//内容审核失败
            log.info("内容审核失败");
        }


    }


    @Test
    public void imageScan(){
        //下载图片
        byte[] bytes = fileStorageService.downLoadFile("http://192.168.200.128:9000/leadnews/2023/06/17/01c991a1126543e6a358a89bfdcac3ee.jpg");
        //审核图片
        Integer integer = baiduImageScan.imageScan(bytes);
        if(integer==1){//合规
            log.info("内容图片合规");
        }
        if(integer==2){//内容不合规
            log.info("内容图片不合规");
        }
        if(integer==3){//内容疑似违规
            log.info("内容图片疑似违规，需要人工审核");
        }
        if(integer==4){//内容审核失败
            log.info("内容图片审核失败");
        }
    }

}

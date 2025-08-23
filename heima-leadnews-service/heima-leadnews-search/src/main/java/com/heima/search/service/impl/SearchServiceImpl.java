package com.heima.search.service.impl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.service.SearchService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 基本搜索业务
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult search(UserSearchDto dto) {
       //TODO
        return ResponseResult.okResult(null);
    }



    /**
     * 自动补全功能
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult load(UserSearchDto dto) {
       //TODO
        return null;
    }
}

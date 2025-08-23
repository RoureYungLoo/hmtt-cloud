package com.heima.comment.test;

import com.heima.comment.domain.pojos.ApComment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoTest {


    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 测试添加文档
     */
    @Test
    public void testAddDocument(){

        for (int i = 1; i <= 10; i++) {
            ApComment apComment = new ApComment();
            apComment.setUserId(i);//评论的用户id
            apComment.setUserName("测试用户"+i);//测试用户名
            apComment.setType(0);//类型-0表示文章
            apComment.setObjectId(Long.valueOf(10+i));//文章id
            apComment.setContent("测试内容"+i);
            apComment.setLikes(100+i);//点赞数量
            apComment.setCreatedTime(new Date());//创建时间
            apComment.setUpdatedTime(new Date());//更新时间

            mongoTemplate.save(apComment);
        }

    }


    /**
     * 测试查询文档
     */
    @Test
    public void testGetDocument(){
        //查询一条数据：根据主键ID查询
        ApComment apComment = mongoTemplate.findById("64473c4342e2e400d613830d", ApComment.class);
        System.out.println(apComment);

        System.out.println("=======================");

        //查询一条数据：根据条件查询
        apComment = mongoTemplate.findOne(Query.query(Criteria.where("userName").is("测试用户8").and("likes").is(108)), ApComment.class);
        System.out.println(apComment);

        System.out.println("=======================");

        //查询列表数据：查询全部数据
        List<ApComment> apCommentList = mongoTemplate.findAll(ApComment.class);
        apCommentList.forEach(System.out::println);

        System.out.println("=======================");

        //查询列表数据：根据条件查询
        apCommentList = mongoTemplate.find(Query.query(Criteria.where("likes").gt(105)
                .and("type").is(0))
                .with(Sort.by(Sort.Direction.DESC, "likes"))
                .limit(3), ApComment.class);
        apCommentList.forEach(System.out::println);
    }


    /**
     * 测试更新文档
     */
    @Test
    public void testUpdateDocument(){
        //更新方式1：根据id更新
//        ApComment apComment = mongoTemplate.findById("64473c4342e2e400d613830e", ApComment.class);
//        apComment.setUserName("测试用户666");
//        mongoTemplate.save(apComment);

        //更新方式2：根据条件更新（参数1：更新条件  参数2：更新的域和值   参数3：POJO类型）
//        mongoTemplate.updateFirst(Query.query(Criteria.where("userName").is("测试用户8")),
//                new Update().set("likes",111)
//                , ApComment.class);

        //更新方式3：根据条件更新-线程安全的更新（参数1：更新条件  参数2：更新的域和值   参数3：POJO类型）
        mongoTemplate.findAndModify(Query.query(Criteria.where("userName").is("测试用户8")),
                new Update().inc("likes",-1)
                , ApComment.class);
    }

    /**
     * 测试删除文档
     */
    @Test
    public void testDeleteDocument(){
        //删除方式1：根据ID删除
//        ApComment apComment = mongoTemplate.findById("64473c4342e2e400d613830e", ApComment.class);
//        mongoTemplate.remove(apComment);

        //删除方式2：根据条件删除
        mongoTemplate.remove( Query.query(Criteria.where("userName").is("测试用户8")), ApComment.class);
    }
}

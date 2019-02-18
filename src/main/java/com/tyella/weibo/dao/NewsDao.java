package com.tyella.weibo.dao;

import com.tyella.weibo.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by tyella on 2019/2/11.
 */
@Mapper
public interface NewsDao {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title,link,image,create_time,user_id,comment_count,like_count";
    String SELECT_FIELDS = "id " + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") " +
            "values(#{title},#{link},#{image},#{createTime},#{userId},#{commentCount},#{likeCount})"})
    void addNews(News news);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"where id=#{id}"})
    News selectById(int id);

    @Update({"update ",TABLE_NAME,"set commentCount=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Update({"update ",TABLE_NAME,"set likeCount=#{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id,@Param("likeCount") int likeCount);

    //TODO
    //@Select({})
    List<News> selectByUserIdAndOffset(@Param("id") int userId,@Param("offset") int offset,@Param("limit") int limit);

}

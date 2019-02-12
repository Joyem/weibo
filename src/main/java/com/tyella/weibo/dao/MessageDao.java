package com.tyella.weibo.dao;


import com.tyella.weibo.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by tyella on 2019/2/12.
 */
@Mapper
public interface MessageDao {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = "from_id,to_id,content,create_time,has_read,conversation_id";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values(#{fromId},#{toId},#{content},#{createTime},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    //TODO
    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from " +
            "( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt " +
            "group by conversation_id order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationList(int user_id, int offset, int limit);

    @Select({"select count(id) from ", TABLE_NAME, "where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnReadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select count(id) from ", TABLE_NAME, "where has_read=0 and to_id=#{user_id}"})
    int getConversationTotalCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            "where conversation_id=#{conversation} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);
}
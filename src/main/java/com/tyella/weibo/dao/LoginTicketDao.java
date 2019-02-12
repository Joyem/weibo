package com.tyella.weibo.dao;

import com.tyella.weibo.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by tyella on 2019/2/12.
 */
@Mapper
public interface LoginTicketDao {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = "user_id,ticket,expired,status";
    String SELECT_FIELDS = "id" + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ")," +
            "values(#{user_id},#{ticket},#{expired},#{status})"})
    int addTicket(LoginTicket loginTicket);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where id=#{id}"})
    LoginTicket selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where userId=#{userId}"})
    LoginTicket selectByName(int userId);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param(value = "ticket") String ticket, @Param(value = "status") int status);

    @Delete({"delete from ", TABLE_NAME, "where id=#{id}"})
    void deleteById(int id);

}

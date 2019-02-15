package com.tyella.weibo.service;


import com.tyella.weibo.dao.MessageDao;
import com.tyella.weibo.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tyella on 2019/2/13.
 */
@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    public int addMessage(Message message){
        return messageDao.addMessage(message);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDao.getConversationList(userId,offset,limit);
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDao.getConversationDetail(conversationId,offset,limit);
    }

    public int getUnreadCount(int userId,String conversationId){
        return messageDao.getConversationUnReadCount(userId,conversationId);
    }

    public int getTotalCount(int userId,String conversationId){
        return messageDao.getConversationTotalCount(userId,conversationId);
    }
}

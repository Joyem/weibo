package com.tyella.weibo.async.handler;

import com.tyella.weibo.async.EventHandler;
import com.tyella.weibo.async.EventModel;
import com.tyella.weibo.async.EventType;
import com.tyella.weibo.model.Message;
import com.tyella.weibo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    private MessageService messageService;

    @Override
    public void doHandle(EventModel eventModel){
        Message message=new Message();
        message.setFromId(eventModel.getEntityOwnerId());
        message.setToId(eventModel.getActorId());
        message.setCreateDate(new Date());
        message.setContent("登陆异常");
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportedEventTypes(){
        return Arrays.asList(EventType.LOGIN);
    }
}

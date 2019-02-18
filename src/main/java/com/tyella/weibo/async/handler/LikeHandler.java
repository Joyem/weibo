package com.tyella.weibo.async.handler;

import com.tyella.weibo.async.EventHandler;
import com.tyella.weibo.async.EventModel;
import com.tyella.weibo.async.EventType;
import com.tyella.weibo.model.Message;
import com.tyella.weibo.model.User;
import com.tyella.weibo.service.MessageService;
import com.tyella.weibo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    private static final Logger logger= LoggerFactory.getLogger(LikeHandler.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Override
    //实际业务上需要通知被点赞的人
    public void doHandle(EventModel eventModel){
        Message message=new Message();
        message.setFromId(eventModel.getActorId());
        message.setToId(eventModel.getEntityOwnerId());
        User user=userService.getUser(eventModel.getActorId());
        message.setContent("用户,"+user.getName()+",赞了你的资讯" +
                "http://127.0.0.1:8088/news/"+eventModel.getEntityId());
        message.setCreateDate(new Date());
        messageService.addMessage(message);

        logger.info(eventModel.toString());
    }

    @Override
    public List<EventType> getSupportEventTypes(){
        return Arrays.asList(EventType.LIKE);
    }
}

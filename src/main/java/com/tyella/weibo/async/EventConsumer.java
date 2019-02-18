package com.tyella.weibo.async;

import com.alibaba.fastjson.JSONObject;
import com.tyella.weibo.util.JedisUtil;
import com.tyella.weibo.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger= LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private JedisUtil jedisUtil;

    //Spring上下文
    //consumer需要一个映射，也就是需要事先知道是哪个handler来处理
    private ApplicationContext applicationContext;

    //事件统一处理，需要把一个事件交给handler处理，需要一个Map存储所有符合要求的handler
    private Map<EventType, List<EventHandler>> config=new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception{

        //找到所有实现了handler接口的实现类，Spring自带的容器已经提供了这么一个方法
        Map<String,EventHandler> beans=applicationContext.getBeansOfType(EventHandler.class);
        if(beans!=null){
            //便利所有eventHandler，找出每个handler支持的type
            for(Map.Entry<String,EventHandler> entry:beans.entrySet()){
                List<EventType> eventTypes =entry.getValue().getSupportEventTypes();
                //根据type和handler形成映射表，完成map
                for(EventType type:eventTypes){
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                        config.get(type).add(entry.getValue());
                    }else{
                        config.get(type).add(entry.getValue());
                    }
                }
            }
        }

        //根据前面的map可以知道取出的事件由谁来处理
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key= RedisKeyUtil.getEventQueueKey();
                    List<String> events=jedisUtil.brpop(0,key);
                    for(String msg:events){
                        //redis自带消息key要过滤掉
                        if(msg.equals(key)){
                            continue;
                        }
                        EventModel eventModel=JSONObject.parseObject(msg,EventModel.class);
                        if(!config.containsKey(eventModel.getEventType())){
                            logger.error("不能识别的事件");
                            continue;
                        }
                        for(EventHandler eventHandler:config.get(eventModel.getEventType())){
                            eventHandler.doHandle(eventModel);
                        }
                    }
                }
            }
        }
        );
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        this.applicationContext=applicationContext;
    }

}

package com.tyella.weibo.controller;

import com.tyella.weibo.model.Message;
import com.tyella.weibo.model.User;
import com.tyella.weibo.model.UserHolder;
import com.tyella.weibo.model.ViewObject;
import com.tyella.weibo.service.MessageService;
import com.tyella.weibo.service.UserService;
import com.tyella.weibo.util.WeiboUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    UserHolder userHolder;

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String ConsersationDetail(String converstionId, Model model) {
        try {
            List<ViewObject> messages = new ArrayList<>();
            List<Message> messageList = messageService.getConversationDetail(converstionId, 0, 10);
            for (Message msg : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = new User();
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            return "letterDetail";
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String ConversationList(Model model) {
        try {
            int localUserId = userHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> messageList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                vo.set("targetId", targetId);
                vo.set("totalCount", messageService.getTotalCount(localUserId, msg.getConversationId()));
                vo.set("setUnreadCount", messageService.getUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
            return "letter";
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("formId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        Message message = new Message();
        message.setContent(content);
        message.setFromId(fromId);
        message.setToId(toId);
        message.setCreateDate(new Date());
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        messageService.addMessage(message);
        return WeiboUtil.getJSONString(message.getId());
    }

}

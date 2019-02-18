package com.tyella.weibo;

import com.tyella.weibo.model.User;
import com.tyella.weibo.util.JedisUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WeiboApplication.class)
public class JedisTest {

    @Autowired
    private JedisUtil jedisUtil;

    @Test
    public void testJedis() {
        jedisUtil.set("hello", "world");
        Assert.assertEquals("world", jedisUtil.get("hello"));
    }
    @Test
    public void testObject() {
        User user = new User();
        user.setHeadUrl("http://images.nowcoder.com/head/100t.png");
        user.setName("user1");
        user.setPassword("abc");
        user.setSalt("def");
        jedisUtil.setObject("user1", user);

        User u = jedisUtil.getObject("user1", User.class);
        System.out.print(ToStringBuilder.reflectionToString(u));

    }
}

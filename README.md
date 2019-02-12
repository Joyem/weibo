# weibo
仿照weibo做的一个Java web项目。使用SpringBoot加MyBatis开发。主要内容包括：

## 项目基础配置


    创建一个SpringBoot工程。
    
    生成Maven项目,pom.xml包含web,mysql,redis,MyBatis,mail依赖。
    
    应用名称是weibo，小组id是com.tyella。
    

## 基本框架开发
    
    创建基本的controller,serivce,model层。
    
    controller中使用注解配置，requestmapping,responsebody
    
    用于解决请求转发以及响应内容的渲染。
    
## AOP和IOC

    IOC解决对象实例化以及依赖传递问题，解耦。
    
    AOP负责纵向切面问题，主要实现日志和权限控制功能。
    
    aspect实现切面，并且使用logger记录日志，用该切面的切面方法来监听controller。
    
## 数据库的配置

    使用mysql创建数据库和表。创建数据库wiebo，首先创建news和user两张表。
    
    后续开发增加其他功能时再新建其他表。
    
    接下来写controller,dao,service。注意MyBatis的注解语法以及xml配置要求。xml要求写在resource中并且与dao接口在相同的包路径。
    
    application.peoperties增加spring配置数据库链接地址。
    
## 用户注册登录以及使用token
    
    完成用户注册和登录的controller，service和dao层代码。
    
    新建数据表login_ticket用来存储ticket字段。该字段在用户登录成功时被生成并存入数据库，并被设置为cookie，
    
    下次用户登录时会带上这个ticket，ticket是随机的UUID，过期时间以及有效状态。
    
    使用拦截器interceptor来拦截所有的用户请求，判断请求中是否有有效的ticket，如果有的话则将用户信息写入ThreadLocal.
    
    所有线程的threadlocal都被存在一个叫做hostholder的实例中，根据该实例就可以在全局任意位置获取用户信息。
    
    该ticket的功能类似session，也是通过cookie写回浏览器，浏览器请求时再通过cookie传递，区别是该字段是存在数据库中的。
    
    通过用户访问权限拦截器来拦截用户的越界访问，比如用户没有管理员权限就不能访问管理员页面。
    
    配置了用户的webconfiguration来设置启动时的配置，这里可以将上述的两个拦截器加到启动项里。
    
    配置了JSON工具类和MD5工具类，并且使用Java自带的盐生成api将用户密码加密成密文。保证密码安全。
    
    数据安全性的保障手段：HTTPS使用公钥加密私钥解密，比如支付宝的密码加密，单点登录验证，验证码机制等。
    
    ajax异步加载数据，json数据传输等。

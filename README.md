## weibo
仿照weibo做的一个Java web项目。

## 项目基础配置
> 新建一个


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
    
    
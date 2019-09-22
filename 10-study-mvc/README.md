# GitHub源码地址


# 1.[★] 创建一个DispatherServlet继承HttpServlet
1. 重写**init**方法
```java
@Override
public void init(ServletConfig config) throws ServletException {
    // 1.加载配置文件
    doLoadConfig(config.getInitParameter("contextConfigLocation"));

    // 2.初始化所有相关联的类
    doScanner(properties.getProperty("scanPackage"));

    // 3.拿到扫描到的类，通过反射机制，实例化，并且放到IOC容器中（k-v beanName-bean）beanName默认是首字母小写
    doInstance();//com.chimm.mvc.controller.UserController

    // 4.初始化HandlerMapping（将url和method对应上）
    initHandlerMapping();

    // 5.实现注入，主要针对service注入到controller
    doAutowired();
}
```
2. 重写**doGet**、**doPost**方法，让其调用我们自己创建的dispatcher方法

# 2. 在web.xml中配置自己的控制器
```xml
<!-- 自定义前端控制器 -->
<servlet>
    <servlet-name>StudySpringMVC</servlet-name>
    <servlet-class>com.chimm.mvc.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>application.properties</param-value>
    </init-param>
    <!-- 启动时加载 -->
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>StudySpringMVC</servlet-name>
    <url-pattern>/*</url-pattern>
</servlet-mapping>
```
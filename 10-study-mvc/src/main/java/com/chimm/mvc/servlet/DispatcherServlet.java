package com.chimm.mvc.servlet;

import com.chimm.mvc.annotation.Autowired;
import com.chimm.mvc.annotation.Controller;
import com.chimm.mvc.annotation.RequestMapping;
import com.chimm.mvc.annotation.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author chimm
 * @date 2019/9/21 0021
 */
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /** 读取配置 */
    private Properties properties = new Properties();

    /** 类的全路径名集合 */
    private List<String> classNames = new ArrayList<String>();

    /** ioc */
    private Map<String, Object> iocMap = new HashMap<String, Object>();

    /** 保存url和controller的关系 */
    private Map<String, Method> handlerMapping = new HashMap<String, Method>();
    private Map<String, Object> controllerMap = new HashMap<String, Object>();

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理请求
        this.doDispatch(req, resp);
    }


    /**
     * 处理请求
     * @param req 请求
     * @param resp 响应
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("---------------- 处理请求-开始 ----------------");

        if (handlerMapping.isEmpty()) {
            return;
        }
        //  /menu/xxx.do
        String uri = req.getRequestURI();
        //  /menu
        String contextPath = req.getContextPath();

        //xxx.do
        uri = uri.replace(contextPath, "").replaceAll("/+", "/");

        if (!this.handlerMapping.containsKey(uri)) {
            resp.getWriter().write("404 NOT FOUND!");
            return;
        }

        Method method = this.handlerMapping.get(uri);

        //获取方法的参数列表
        Class<?>[] parameterTypes = method.getParameterTypes();

        //获取请求的参数
        Map<String, String[]> parameterMap = req.getParameterMap();

        //保存参数值
        Object[] paramValues = new Object[parameterTypes.length];

        //方法的参数列表
        for (int i = 0; i < parameterTypes.length; i++) {
            /*
                根据参数名称，做某些处理
                getSimpleName : 返回类的简单名称，如String（java.lang.String）
             */
            String simpleName = parameterTypes[i].getSimpleName();

            if (simpleName.equals("HttpServletRequest")) {
                //参数类型已经明确，这边做强转类型
                paramValues[i] = req;
                continue;
            }

            if (simpleName.equals("HttpServletResponse")) {
                paramValues[i] = resp;
                continue;
            }

            if (simpleName.equals("String")) {
                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                    /*
                        \\[\\] -->  将空数组[]转换为："" （第一个\是转义）
                        ,\\s   -->  \\s代表正则表达式中的一个空白字符（可能是空格、制表符、其他空白）（第一个\是转义）
                     */
                    String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                    paramValues[i] = value;
                }
            }
        }
        //利用反射机制来调用
        try {
            method.invoke(this.controllerMap.get(uri), paramValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        System.out.println("---------------- 处理请求-结束 ----------------");
    }

    /**
     * 加载配置文件
     * @param location 配置文件路径
     */
    private void doLoadConfig(String location) {

        System.out.println("---------------- 加载配置文件-开始 ----------------");

        //把web.xml中的contextConfigLocation对应value值的文件加载到流里面
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(location);

        try {
            //用Properties文件加载文件里的内容
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (null != resourceAsStream) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("---------------- 加载配置文件-结束 ----------------");
    }

    /**
     * 包扫描
     * @param packageName 包名称
     */
    private void doScanner(String packageName) {

        System.out.println("---------------- 包扫描-开始 ----------------");

        //把所有的.替换成 /  如： com.chimm.mvc  要转换成 com/chimm/mvc（对应硬盘上的文件地址）
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        //递归扫描该路径下的包或类
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                //递归读取包
                doScanner(packageName + "." + file.getName());
            } else {
                String className = packageName + "." + file.getName().replace(".class", "");
                classNames.add(className);
                System.out.println("Spring容器扫描到的类有：" + packageName + file.getName());
            }
        }

        System.out.println("---------------- 包扫描-结束 ----------------");
    }

    /**
     * 实例化包扫描到的类
     */
    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        System.out.println("---------------- 实例化包扫描的类-开始 ----------------");
        for (String className : classNames) {
            try {
                Class<?> aClass = Class.forName(className);

                if (aClass.isAnnotationPresent(Controller.class)) {
                    //反射实例化（只有加@Controller注解的需要实例化）（SpringMVC只需要关注Controller）
                    Controller controller = aClass.getAnnotation(Controller.class);
                    String key = controller.value();
                    if (!"".equals(key) && key != null) {
                        //自定义controller名称
                        iocMap.put(key, aClass.newInstance());
                    } else {
                        //首字母小写注入
                        iocMap.put(toLowerFirstWord(aClass.getSimpleName()), aClass.newInstance());
                    }
                } else if (aClass.isAnnotationPresent(Service.class)){
                    //反射实例化（只有加@Service注解的需要实例化）
                    Service service = aClass.getAnnotation(Service.class);
                    String key = service.value();
                    if (!"".equals(key) && key != null) {
                        //自定义service名称
                        iocMap.put(key, aClass.newInstance());
                    } else {
                        //接口首字母小写注入
                        Class<?>[] interfaces = aClass.getInterfaces();
                        for (Class<?> anInterface : interfaces) {
                            iocMap.put(toLowerFirstWord(anInterface.getSimpleName()), aClass.newInstance());
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------------- 实例化包扫描的类-结束 ----------------");
    }

    /**
     * 初始化映射处理器
     */
    private void initHandlerMapping() {
        if (iocMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : iocMap.entrySet()) {
            Class<?> aClass = entry.getValue().getClass();
            if (!aClass.isAnnotationPresent(Controller.class)) {
                continue;
            }

            //拼接url时，是controller头上的url拼上方法的上的url
            String baseUrl = "";
            if (aClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = aClass.getAnnotation(RequestMapping.class);
                baseUrl = annotation.value();
            }
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                /*
                    key = /study/xxx.do  value = method
                 */
                String url = annotation.value();
                //替换为1个斜杠（错误示例：/aaa///bb.do）
                url = (baseUrl + "/" + url).replaceAll("/+", "/");
                //添加HandlerMapping
                handlerMapping.put(url, method);
                String key = toLowerFirstWord(aClass.getSimpleName());
                controllerMap.put(url, iocMap.get(key));
                System.out.println(url + "," + method);

            }
        }
    }

    /**
     * 实现依赖注入
     */
    private void doAutowired() {
        if (iocMap.isEmpty()) {
            return;
        }

        //遍历所有IOC容器中对象
        for (Map.Entry<String, Object> entry : iocMap.entrySet()) {
            //类名
            String key = entry.getKey();
            //实例对象
            Object instance = entry.getValue();

            //遍历，看成员变量是否有注入其他的类
            Field[] fields = instance.getClass().getDeclaredFields();
            for (Field field : fields) {
                //判断字段上是否有Autowired注解
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }
                Autowired annotation = field.getAnnotation(Autowired.class);
                String beanName = annotation.value();
                if ("".equals(beanName)) {
                    beanName = toLowerFirstWord(field.getType().getSimpleName());
                }

                //反射击穿
                field.setAccessible(true);

                Object obj = iocMap.get(beanName);
                try {
                    field.set(instance, obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 把字符串的首字母小写
     * @param name 字符串
     */
    private String toLowerFirstWord(String name) {
        char[] charArray = name.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }
}

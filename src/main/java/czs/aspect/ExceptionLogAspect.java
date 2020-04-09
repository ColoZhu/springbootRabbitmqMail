package czs.aspect;

import com.alibaba.fastjson.JSONObject;
import czs.annotation.ExceptionLogAnnotation;
import czs.service.exception.ExceptionLogSender;
import czs.utils.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;


@Aspect
@Component
public class ExceptionLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionLogAspect.class);

    @Autowired
    ExceptionLogSender exceptionLogSender;

    /**
     * 这里切点是web包 并且加了自定义注解(ExceptionLogAnnotation)
     */
    @Pointcut("execution(public * czs.web..*(..)) && @annotation(czs.annotation.ExceptionLogAnnotation)")
    public void exceptionLog() {
    }

    @AfterThrowing(pointcut = "exceptionLog()", throwing = "e")
    public void handleThrowing(JoinPoint joinPoint, Exception e) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.error("ExceptionLogAspect切面获取到异常:" + e.getMessage(), e);

        //开始打log
        //logger.error("异常:" + e.getMessage());
        //logger.error("异常所在类：" + className);
        // logger.error("异常所在方法：" + methodName);
        //logger.error("异常中的参数：");
        //logger.error(methodName);

        Class targetClass = null;
        String operationType = "";
        String operationName = "";
        Method[] methods = null;
        try {
            targetClass = Class.forName(className);
            methods = targetClass.getMethods();
        } catch (ClassNotFoundException e2) {
            e.printStackTrace();
        }
        if (methods != null) {
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs != null && clazzs.length == args.length &&
                            method.getAnnotation(ExceptionLogAnnotation.class) != null) {
                        operationName = method.getAnnotation(ExceptionLogAnnotation.class).operationName();
                        operationType = method.getAnnotation(ExceptionLogAnnotation.class).operationType();
                        break;
                    }
                }
            }
        }

        logger.info("operationName :" + operationName);
        logger.info("operationType :" + operationType);

        //异常收集的StringBuffer
        StringBuffer execBuff = new StringBuffer();
        //异常的具体信息
        String exceTrace = ExceptionUtils.getTrace(e);
        //拼接信息
        execBuff.append("[切面捕获异常信息]").append("<br/>")
                .append("异常:").append(e.getMessage()).append("<br/>")
                .append("异常所在类:").append(className).append("<br/>")
                .append("异常所在方法:").append(methodName).append("<br/>")
                .append("异常中的参数:").append(JSONObject.toJSONString(args)).append("<br/>")
                .append("操作类型:").append(operationType).append("<br/>")
                .append("操作名称:").append(operationName).append("<br/>")
                .append("具体异常信息如下:").append("<br/>")
                .append(exceTrace)
        ;

        //发消息
        exceptionLogSender.send(execBuff.toString());

    }


    /**
     * @param joinPoint
     * @throws Throwable
     */
    @Before("exceptionLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();         // 记录下请求内容
        logger.info("开始记录请求数据-------->>>>> ");
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

}

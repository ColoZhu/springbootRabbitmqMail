package czs.annotation;


import java.lang.annotation.*;


/**
 * 自定义异常日志的注解
 * @author 600336
 */
@Documented

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface ExceptionLogAnnotation {

    /**
     * 操作类型
     * @return
     */
    String operationType() default "";

    /**
     * 操作名称
     */
      String operationName() default "";
}
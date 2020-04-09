package czs.web.exception;


import com.alibaba.fastjson.JSONObject;

import czs.annotation.ExceptionLogAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/exception")
public class ExceptionController {

    /**
     * 测试地址:http://localhost:8888/rabbitmq/exception/test/10
     * 测试地址:http://localhost:8888/rabbitmq/exception/test/0
     * ***这里加了注解ExceptionLogAnnotation
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ExceptionLogAnnotation(operationType = "测试操作", operationName = "测试异常或者测试返回")
    public JSONObject test(@PathVariable Integer id) {
        JSONObject result = new JSONObject();
        result.put("success", "true");
        result.put("test", "test");
        //id可以传0 测试异常
        int aa = 10 / id;
        return result;
    }


    /**
     * 测试地址:http://localhost:8888/rabbitmq/exception/test2/10
     * 测试地址:http://localhost:8888/rabbitmq/exception/test2/0
     * ***这里不加注解ExceptionLogAnnotation
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/test2/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject test2(@PathVariable Integer id) {

        JSONObject result = new JSONObject();
        result.put("success", "true");
        result.put("test2", "test2");
        //id可以传0 测试异常
        int aa = 10 / id;
        return result;
    }

}

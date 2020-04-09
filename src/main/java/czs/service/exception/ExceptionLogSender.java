package czs.service.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 单生产者-单消费者
 * 异常日志信息-生产者
 */
@Component
public class ExceptionLogSender {

    private static final Logger log = LoggerFactory.getLogger(ExceptionLogSender.class);
    @Autowired
    public AmqpTemplate amqpTemplate;

    public void send(String info) {
        log.info("生产者 发送异常日志信息:" + info);
        this.amqpTemplate.convertAndSend("exception.log", info);
    }


}





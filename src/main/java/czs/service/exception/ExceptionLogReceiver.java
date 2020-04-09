package czs.service.exception;


import czs.utils.SendMailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 单生产者-单消费者
 * 异常日志信息-消费
 */
@Component
public class ExceptionLogReceiver {

    private static final Logger log = LoggerFactory.getLogger(ExceptionLogReceiver.class);
    @Autowired
    public AmqpTemplate amqpTemplate;


    //监听器监听指定的Queue
    @RabbitListener(queues = "exception.log")
    public void process(String info) {
        log.info("消费者 收到异常日志信息:" + info);
        //发送邮件
        try {
            SendMailUtil.sendMail("系统异常日志信息报告", info);
        } catch (Exception e) {
            log.error("发送异常日志信息邮件失败!",e);
        }

    }


}





package czs.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    /*异常日志的队列*/
    @Bean
    public Queue exceptionLogQueue() {
        return new Queue("exception.log");
    }
}

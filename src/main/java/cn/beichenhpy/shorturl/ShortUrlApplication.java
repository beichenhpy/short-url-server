package cn.beichenhpy.shorturl;

import cn.beichenhpy.shorturl.utils.SnowFlake;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShortUrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortUrlApplication.class, args);
    }
    @Bean("idWorker")
    SnowFlake snowFlake(){
        return new SnowFlake(1);
    }
}

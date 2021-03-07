package cn.beichenhpy.shorturl;

import cn.beichenhpy.shorturl.service.IShortUrlService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ShortUrlApplicationTests {

    @Resource(name = "defaultShortUrlServiceImpl")
    private IShortUrlService defaultShortUrlServiceImpl;

    @Test
    void contextLoads() {

    }

}

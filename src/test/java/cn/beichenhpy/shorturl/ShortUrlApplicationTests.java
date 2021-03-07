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
        String su = defaultShortUrlServiceImpl.addUrlInfo("https://bilibili.com/?from=bill_top_mnav&spm_id_from=888.29530.b_696e7465726e6174696f6e616c486561646572.6");
        System.out.println(su);
    }

}

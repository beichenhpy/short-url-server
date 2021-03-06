package cn.beichenhpy.shorturl.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO 加载黑名单到全局变量
 * @since 2021/3/1 11:05
 */
@Component
public class BlackUrlLoad implements ApplicationRunner {
    public static final Set<String> BLACK_URLS = new CopyOnWriteArraySet<>();
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ClassPathResource pathResource = new ClassPathResource("static/blackUrls.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pathResource.getInputStream()));
        String str;
        while ((str = bufferedReader.readLine()) != null){
            //以逗号分割
            String[] split = str.split(",");
            BLACK_URLS.addAll(Arrays.asList(split));
        }
        bufferedReader.close();
    }
}

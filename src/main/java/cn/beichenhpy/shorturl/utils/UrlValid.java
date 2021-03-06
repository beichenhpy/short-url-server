package cn.beichenhpy.shorturl.utils;

import cn.beichenhpy.shorturl.config.BlackUrlLoad;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author beichenhpy
 * @description todo url验证
 */
public class UrlValid {
    private final static String URL_REGEX = "(http|https)://(\\w+:?\\w*@)?(\\S+)(:[0-9]+)?(/|/([\\w#!:.?+=&%@\\-/]))?";

    /**
     * 验证URL地址
     *
     * @param url url
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkUrl(String url) {
        boolean matches = Pattern.matches(URL_REGEX, url);
        Set<String> blackUrls = BlackUrlLoad.BLACK_URLS;
        boolean result = true;
        if (matches){
            if (!blackUrls.isEmpty()){
                for (String blackUrl : blackUrls) {
                    //这里只需要返回false的情况，有一个不行，则直接返回false
                    if (url.contains(blackUrl)) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }
}

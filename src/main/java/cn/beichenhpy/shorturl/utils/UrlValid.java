package cn.beichenhpy.shorturl.utils;

import cn.beichenhpy.shorturl.config.BlackUrlLoad;

import java.util.regex.Pattern;

/**
 * @author beichenhpy
 * @description todo url验证
 */
public class UrlValid {
    private final static String URL_REGEX = "(ftp|http|https)://(\\w+:?\\w*@)?(\\S+)(:[0-9]+)?(/|/([\\w#!:.?+=&%@\\-/]))?";

    /**
     * 验证URL地址
     *
     * @param url url
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkUrl(String url) {
        boolean matches = Pattern.matches(URL_REGEX, url);
        if (matches){
            return !BlackUrlLoad.BLACK_URLS.contains(url);
        }else {
            return false;
        }
    }
}

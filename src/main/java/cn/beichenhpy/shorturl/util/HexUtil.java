package cn.beichenhpy.shorturl.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Stack;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/3/1 14:37
 */
public class HexUtil {
    private static final char[] CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    /**
     * 将10进制转化为62进制
     *
     * @param number 数
     * @return 返回字符串
     */
    public static String convertDecimalToBase62(long number) {
        Stack<Character> stack = new Stack<Character>();
        StringBuilder result = new StringBuilder(0);
        while (number != 0) {
            stack.add(CHAR_SET[new Long((number - (number / 62) * 62)).intValue()]);
            number = number / 62;
        }
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }
        return result.toString();
    }


    /**
     * 将62进制转换成10进制数
     *
     * @param ident62 62进制
     * @return 10进制
     */
    public static String convertBase62ToDecimal(String ident62) {
        int decimal = 0;
        int base = 62;
        int keisu = 0;
        int cnt = 0;

        byte ident[] = ident62.getBytes();
        for (int i = ident.length - 1; i >= 0; i--) {
            int num = 0;
            if (ident[i] > 48 && ident[i] <= 57) {
                num = ident[i] - 48;
            } else if (ident[i] >= 65 && ident[i] <= 90) {
                num = ident[i] - 65 + 10;
            } else if (ident[i] >= 97 && ident[i] <= 122) {
                num = ident[i] - 97 + 10 + 26;
            }
            keisu = (int) java.lang.Math.pow((double) base, (double) cnt);
            decimal += num * keisu;
            cnt++;
        }
        return String.format("%08d", decimal);
    }

    public static void main(String[] args) {
        System.out.println(HexUtil.convertDecimalToBase62(1));
        System.out.println(HexUtil.convertBase62ToDecimal("1"));
    }
}
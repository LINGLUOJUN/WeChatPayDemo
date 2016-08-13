package com.ys.utils;

import java.util.Random;

/**
 * @author 杨爽
 * @ClassName RandomStringGenerator
 * @date 2016/8/11 13:49
 * @Description 随机数生成算法
 */
public class RandomStringGenerator {

    private RandomStringGenerator(){

    }
    /**
     * 返回一个定长的随机字符串(包含大小写字母数字)
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}

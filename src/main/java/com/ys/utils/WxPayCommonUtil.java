package com.ys.utils;

import java.net.*;
import java.util.Enumeration;
import java.util.List;

/**
 * @author 杨爽
 * @ClassName WeChatPayCommonUtil
 * @date 2016/8/11 13:49
 * @Description 自定义的通用工具类
 */
public class WxPayCommonUtil {

    /**
     * 防止构造函数被调用
     */
    private WxPayCommonUtil(){

    }
    /**
     * 用于获取本机IP 这里需要注意的是 暂时不清楚获取的地址是内网地址还是外网
     * @author 杨爽
     * @return
     */
    public static String getLocalIp(){
        String ip = null;
        Enumeration allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
                for (InterfaceAddress add : InterfaceAddress) {
                    InetAddress Ip = add.getAddress();
                    if (Ip != null && Ip instanceof Inet4Address) {
                        ip = Ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
           //logger.warn("获取本机Ip失败:异常信息:"+e.getMessage());
        }
        return ip;
    }
}

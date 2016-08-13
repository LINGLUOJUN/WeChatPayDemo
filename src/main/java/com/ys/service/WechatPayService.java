package com.ys.service;


import java.io.UnsupportedEncodingException;

/**
 *
 */

public interface WechatPayService {

    /**
     * 生成统一下单xml格式的信息
     * @return
     * @param orderId
     */
    String createUnifiedOrder(String orderId) throws IllegalAccessException, UnsupportedEncodingException;

    /**
     * 由订单信息产生与支付交易链接
     * @param orderInfo 统一下单的xml格式的字符串
     * @return
     */
    String createQRCodeUrl(String orderInfo);
}

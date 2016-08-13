package com.ys.wechat.test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.ys.constant.WxPayConfig;
import com.ys.utils.RandomStringGenerator;
import com.ys.utils.Signature;
import com.ys.utils.WxPayCommonUtil;
import com.ys.vo.WxPayUnifiedOrderReqData;


/**
 * @author 杨爽
 * @ClassName
 * @date 2016/8/11 13:55
 * @Description
 */
public class WeChatPayTest {



    public static void main(String[] args) throws IllegalAccessException {
       WeChatPayTest.signatureTest();
    }

    /**
     * 测试订单返回生成xml
     */
    public static void signatureTest() throws IllegalAccessException {
        //生成订单对象
        WxPayUnifiedOrderReqData wxPayUnifiedOrderReqData=new WxPayUnifiedOrderReqData();

        wxPayUnifiedOrderReqData.setAppid(WxPayConfig.getAppid());//公众账号
        wxPayUnifiedOrderReqData.setMch_id(WxPayConfig.getMchid());//商户账号
        wxPayUnifiedOrderReqData.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));// 随机字符串
        wxPayUnifiedOrderReqData.setBody("xxxxx这是商品的描述");//商品描述
        wxPayUnifiedOrderReqData.setOut_trade_no("xxxxxxx订单号");//商户订单号
        wxPayUnifiedOrderReqData.setTotal_fee("x");  //金额需要扩大100倍:1代表支付时是0.01
        wxPayUnifiedOrderReqData.setSpbill_create_ip(WxPayCommonUtil.getLocalIp());//终端IP
        wxPayUnifiedOrderReqData.setNotify_url("xxxxxxxxxxxxxx");//通知地址
        wxPayUnifiedOrderReqData.setTrade_type("NATIVE");//JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
        wxPayUnifiedOrderReqData.setSign(Signature.getSign(wxPayUnifiedOrderReqData));//签名

        //将订单对象转换为xml格式
        XStream xStream = new XStream(new XppDriver((new XmlFriendlyNameCoder("_-", "_"))));
        xStream.alias("xml",WxPayUnifiedOrderReqData.class);
        String xmlString=xStream.toXML(wxPayUnifiedOrderReqData);
        System.out.println(xmlString);
    }
}

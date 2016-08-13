package com.ys.service.impl;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.ys.constant.WxPayConfig;
import com.ys.service.WechatPayService;
import com.ys.utils.*;
import com.ys.vo.WxPayUnifiedOrderReqData;
import com.ys.vo.WxPayUnifiedOrderResData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.HttpURLConnection;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;


@Service
public class WechatPayServiceImpl implements WechatPayService {

    /**
     * 统一下单信息
     *
     * @param orderId
     * @return
     */
    public String createUnifiedOrder(String orderId) throws IllegalAccessException, UnsupportedEncodingException {

        //生成订单对象
        WxPayUnifiedOrderReqData wxPayUnifiedOrderReqData = new WxPayUnifiedOrderReqData();

        wxPayUnifiedOrderReqData.setAppid(WxPayConfig.getAppid());//公众账号
        wxPayUnifiedOrderReqData.setMch_id(WxPayConfig.getMchid());//商户账号
        wxPayUnifiedOrderReqData.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));// 随机字符串
        String body = "这个是中文描述";
        wxPayUnifiedOrderReqData.setBody(body);
       //  wxPayUnifiedOrderReqData.setBody(URLEncoder.encode(body, "UTF-8"));//商品描述  正确的形式
        wxPayUnifiedOrderReqData.setOut_trade_no(orderId);//商户订单号

        //TODO 关于金额这里应该需要调用 用户的
        wxPayUnifiedOrderReqData.setTotal_fee("100");  //金额需要扩大100倍:1代表支付时是0.01
        wxPayUnifiedOrderReqData.setSpbill_create_ip(WxPayCommonUtil.getLocalIp());//终端IP

        wxPayUnifiedOrderReqData.setNotify_url("https://www.baidu.com/");//通知地址
        wxPayUnifiedOrderReqData.setTrade_type("NATIVE");//JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
        wxPayUnifiedOrderReqData.setSign(Signature.getSign(wxPayUnifiedOrderReqData));//签名
        //将订单对象转换为xml格式

        XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xStream.alias("xml", WxPayUnifiedOrderReqData.class);
        return xStream.toXML(wxPayUnifiedOrderReqData);
    }

    /**
     * 由订单信息产生与支付交易链接
     *
     * @param orderInfo 统一下单的xml格式的字符串
     * @return
     */
    @Override
    public String createQRCodeUrl(String orderInfo) {
        String url = WxPayConfig.PAY_UNIFIED_ORDER_API;//统一下单的url
        try {
            orderInfo=  new String(orderInfo.getBytes("utf-8"), "iso8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            //加入数据
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-type", "text/html");
            conn.setRequestProperty("ContentType", "utf-8");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setDoOutput(true);
        	/*conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Content-type", "text/html");
			conn.setRequestProperty("contentType", "utf-8");*/




            BufferedOutputStream buffOutStr = new BufferedOutputStream(conn.getOutputStream());
            buffOutStr.write(orderInfo.getBytes());
            buffOutStr.flush();
            buffOutStr.close();

            //获取输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            //BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line;//按行获取输入流
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }


            XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
            //将返回的内容通过xmStream转化为WxPayUnifiedOrderResData对象
            xStream.alias("xml", WxPayUnifiedOrderResData.class);
            WxPayUnifiedOrderResData unifiedOrderResData = (WxPayUnifiedOrderResData) xStream.fromXML(sb.toString());

            //据微信文档return_code 和result_code都为SUCCESS的时候才会返回二维码链接地址code_url
            if (null != unifiedOrderResData &&
                    "SUCCESS".equals(unifiedOrderResData.getReturn_code()) &&
                    "SUCCESS".equals(unifiedOrderResData.getResult_code())) {
                return unifiedOrderResData.getCode_url();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

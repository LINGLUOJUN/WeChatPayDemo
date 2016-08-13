package com.ys.wechat.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author 杨爽
 * @ClassName
 * @date 2016/8/12 16:22
 * @Description
 */
public class CodeDD {
    public static void main(String[] args) throws UnsupportedEncodingException {

        //wxPayUnifiedOrderReqData.setBody(new String(body.getBytes("ISO-8859-1") ,"UTF-8"));//商品描述*/

        String url="这是中文的编码";
  /*     url= new String(url.getBytes("UTF-8") ,"ISO-8859-1");
        url = StringUtil.convertCharset(url, "ISO-8859-1", "UTF-8");*/
        //
        //String url2=URLEncoder.encode(url,"UTF-8");
        System.out.println(url);
    }
}

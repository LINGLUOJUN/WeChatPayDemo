package com.ys.controller;



import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.ys.service.WechatPayService;
import com.ys.service.WechatPayService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Hashtable;


@Controller
public class WechatPayController {

   @Autowired
    private WechatPayService1 wechatPayService;


	/**
	 *创建二维码 (orderID)
	 * @param response
	 * @return 返回url的二维码路径
	 * @throws IllegalAccessException
     */
	@RequestMapping("createQRCode")
	public void createQRCode( HttpServletResponse response) {
		String tempOrderId="xxx123abcabd123456ddbd";
         //生成订单信息
		String orderInfo= null;
		try {
			orderInfo = wechatPayService.createUnifiedOrder(tempOrderId);
		} catch (Exception e) {
			e.printStackTrace();

		}
		//调用统一下单API
		//将返回预支付交易的二维码链接（code_url）
		String code_url=wechatPayService.createQRCodeUrl(orderInfo);

		//URLEncoder
        //生成二维码图片
		try {
			int width=200;
			int height=200;
			String format="png";
			Hashtable hints= new Hashtable();
			//hints.put(EncodeHintType.CHARACTER_SET,"utf-8");
			hints.put(DecodeHintType.CHARACTER_SET,"UTF-8");

			BitMatrix bitMatrix=new MultiFormatWriter().encode(code_url, BarcodeFormat.QR_CODE,width,height,hints);
			OutputStream out=null;
			out=response.getOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, format, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}







}


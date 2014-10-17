/**
 * 
 */
package com.sosgps.wzt.system.action;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.util.RandomPassword;

/**
 * @author xiaojun.luan
 * 
 */
public class ValidateCodeAction extends DispatchWebActionSupport {

	private Color getRandColor(int min, int max) { // 随机产生指定区域内的RGB颜色
		Random random1 = new Random();
		if (min >= 255)
			min = 255;
		if (max >= 255)
			max = 255;
		int r = min + random1.nextInt(max - min);
		int g = min + random1.nextInt(max - min);
		int b = min + random1.nextInt(max - min);
		return new Color(r, g, b);
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 在缓存中创建图形对象，然后输出
		 int width=60,height=20;  // 输出图片的大小
		 BufferedImage buff=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);  // 指定缓冲的图片和颜色结构
		 Graphics g=buff.getGraphics();   // 得到绘图对象

		//利用graphics对象我们就可以画图了:

		//矩形:

		g.setColor(getRandColor(200,250));
		 g.fillRect(0,0,width,height);

		//干扰线:(循环的画出细小的线条)
		
		Random rand = new Random();

		for(int i=1;i<=30;i++){
		  int x=rand.nextInt(width);  // 线条的起始位置
		  int y=rand.nextInt(height);
		  int tx=rand.nextInt(12);
		  int ty=rand.nextInt(12);
		  g.drawLine(x,y,x+tx,y+ty);
		 }

		//验证码:

		String coding="";  // 保存得到的验证码字符串
		 for(int i=0;i<4;i++){
		  String temp=String.valueOf(rand.nextInt(10));  // 0-9的数字
		  coding+=temp;
		  // 显示验证码,20-140色段
		  g.setColor(getRandColor(20,140));
		  g.drawString(temp,13*i+6,16);
		 }
		 // 信息存入session
		 request.getSession().setAttribute("ValidateCode",coding);

		//清空缓存区:(这一步非常重要,不然服务器会报错误)

		
		 g.dispose();
		 ServletOutputStream out=response.getOutputStream();
		 ImageIO.write(buff,"jpeg",out);
		 out.flush();  // 强行将缓冲区的内容输入到页面
		 out.close();
		 out=null;
		 response.flushBuffer();
		 


		return null;
	}
}

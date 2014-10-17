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

	private Color getRandColor(int min, int max) { // �������ָ�������ڵ�RGB��ɫ
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
		// �ڻ����д���ͼ�ζ���Ȼ�����
		 int width=60,height=20;  // ���ͼƬ�Ĵ�С
		 BufferedImage buff=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);  // ָ�������ͼƬ����ɫ�ṹ
		 Graphics g=buff.getGraphics();   // �õ���ͼ����

		//����graphics�������ǾͿ��Ի�ͼ��:

		//����:

		g.setColor(getRandColor(200,250));
		 g.fillRect(0,0,width,height);

		//������:(ѭ���Ļ���ϸС������)
		
		Random rand = new Random();

		for(int i=1;i<=30;i++){
		  int x=rand.nextInt(width);  // ��������ʼλ��
		  int y=rand.nextInt(height);
		  int tx=rand.nextInt(12);
		  int ty=rand.nextInt(12);
		  g.drawLine(x,y,x+tx,y+ty);
		 }

		//��֤��:

		String coding="";  // ����õ�����֤���ַ���
		 for(int i=0;i<4;i++){
		  String temp=String.valueOf(rand.nextInt(10));  // 0-9������
		  coding+=temp;
		  // ��ʾ��֤��,20-140ɫ��
		  g.setColor(getRandColor(20,140));
		  g.drawString(temp,13*i+6,16);
		 }
		 // ��Ϣ����session
		 request.getSession().setAttribute("ValidateCode",coding);

		//��ջ�����:(��һ���ǳ���Ҫ,��Ȼ�������ᱨ����)

		
		 g.dispose();
		 ServletOutputStream out=response.getOutputStream();
		 ImageIO.write(buff,"jpeg",out);
		 out.flush();  // ǿ�н����������������뵽ҳ��
		 out.close();
		 out=null;
		 response.flushBuffer();
		 


		return null;
	}
}

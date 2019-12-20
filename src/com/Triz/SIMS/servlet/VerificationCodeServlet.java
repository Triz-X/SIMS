package com.Triz.SIMS.servlet;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
/*
 * @auther Triz-X
 * 验证码servlet
 * */
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Triz.SIMS.util.VerificationCodeUtil;




public class VerificationCodeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4919529414762301338L;
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String mothed = request.getParameter("mothed") ;
		if("loginVerification".equals(mothed)){
			generateLoginVerification(request,response);
			return;
		}
		response.getWriter().write("error mothed");
	}
	private void generateLoginVerification(HttpServletRequest request,HttpServletResponse response) throws IOException{
		VerificationCodeUtil verificationCodeUtil = new VerificationCodeUtil();
		String generatorVCode = verificationCodeUtil.generatorVCode();
		request.getSession().setAttribute("loginVerification", generatorVCode);
		BufferedImage generatorRotateVCodeImage = verificationCodeUtil.generatorRotateVCodeImage(generatorVCode,true);
		ImageIO.write(generatorRotateVCodeImage, "gif", response.getOutputStream());
	}
		
}


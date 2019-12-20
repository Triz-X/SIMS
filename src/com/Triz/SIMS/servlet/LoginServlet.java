package com.Triz.SIMS.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Triz.SIMS.dao.AdminDao;
import com.Triz.SIMS.model.Admin;

/**
 * 
 * @author TriZ-X 
 */
public class LoginServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		doPost(request,response);
	}
	
	
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		AdminDao adminDao = new AdminDao();
		String name = request.getParameter("username");
		String password = request.getParameter("password");
		int type = Integer.parseInt(request.getParameter("type"));
		
		
		
		switch (type) {
		case 1:{
			Admin admin = adminDao.login(name, password);
			adminDao.closeCon();//及时关闭数据库
			
			if(admin == null){
				response.getWriter().write("loginError");
				return;
			}
			HttpSession session = request.getSession();
			session.setAttribute("user", admin);
			session.setAttribute("usertype", type);
			response.getWriter().write("admin");
			break;
		}
		/*case 2:{
			
		}*/
		default:
			break;
		}
	
	}
	
}

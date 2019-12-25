package com.Triz.SIMS.servlet;

/**
 * 
 * @author TriZ-X 
 */
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Triz.SIMS.dao.AdminDao;
import com.Triz.SIMS.dao.StudentDao;
import com.Triz.SIMS.dao.TeacherDao;
import com.Triz.SIMS.model.Admin;
import com.Triz.SIMS.model.Student;
import com.Triz.SIMS.model.Teacher;


public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		if("logout".equals(method)){
			logout(request, response);
			return;
		}
		String name = request.getParameter("account");
		String password = request.getParameter("password");
		int type = Integer.parseInt(request.getParameter("type"));
	
		String loginStatus = "loginFaild";
		switch (type) {
			case 1:{
				AdminDao adminDao = new AdminDao();
				Admin admin = adminDao.login(name, password);
				adminDao.closeCon();
				if(admin == null){
					response.getWriter().write("loginError");
					return;
				}
				HttpSession session = request.getSession();
				session.setAttribute("user", admin);
				session.setAttribute("userType", type);
				loginStatus = "loginSuccess";
				break;
			}
			case 2:{
				StudentDao studentDao = new StudentDao();
				Student student = studentDao.login(name, password);
				studentDao.closeCon();
				if(student == null){
					response.getWriter().write("loginError");
					return;
				}
				HttpSession session = request.getSession();
				session.setAttribute("user", student);
				session.setAttribute("userType", type);
				loginStatus = "loginSuccess";
				break;
			}
			case 3:{
				TeacherDao teahcerDao = new TeacherDao();
				Teacher teacher = teahcerDao.login(name, password);
				teahcerDao.closeCon();
				if(teacher == null){
					response.getWriter().write("loginError");
					return;
				}
				HttpSession session = request.getSession();
				session.setAttribute("user", teacher);
				session.setAttribute("userType", type);
				loginStatus = "loginSuccess";
				break;
			}
			default:
				break;
			}
		response.getWriter().write(loginStatus);
		
	}
	
	private void logout(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.getSession().removeAttribute("user");
		request.getSession().removeAttribute("userType");
		response.sendRedirect("index.jsp");
	}
}

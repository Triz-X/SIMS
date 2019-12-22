package com.Triz.SIMS.servlet;

/**
 * 
 * @author Triz-X
 *登录后主界面
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Triz.SIMS.dao.AdminDao;
import com.Triz.SIMS.dao.StudentDao;
import com.Triz.SIMS.dao.TeacherDao;
import com.Triz.SIMS.model.Admin;
import com.Triz.SIMS.model.Student;
import com.Triz.SIMS.model.Teacher;

public class SystemServlet extends HttpServlet {

	
	private static final long serialVersionUID = -7258264317769166483L;
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		if("toPersonalView".equals(method)){
			personalView(request,response);
			return;
		}else if("EditPasswod".equals(method)){
			editPassword(request,response);
			return;
		}
		try {
			request.getRequestDispatcher("view/system.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	private void editPassword(HttpServletRequest request,
			HttpServletResponse response) {
		String password = request.getParameter("password");
		String newPassword = request.getParameter("newpassword");
		response.setCharacterEncoding("UTF-8");
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		if(userType == 1){

			Admin admin = (Admin)request.getSession().getAttribute("user");
			if(!admin.getPassword().equals(password)){
				try {
					response.getWriter().write("");
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			AdminDao adminDao = new AdminDao();
			if(adminDao.editPassword(admin, newPassword)){
				try {
					response.getWriter().write("success");
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally{
					adminDao.closeCon();
				}
			}else{
				try {
					response.getWriter().write("");
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					adminDao.closeCon();
				}
			}
		}
		if(userType == 2){

			Student student = (Student)request.getSession().getAttribute("user");
			if(!student.getPassword().equals(password)){
				try {
					response.getWriter().write("");
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			StudentDao studentDao = new StudentDao();
			if(studentDao.editPassword(student, newPassword)){
				try {
					response.getWriter().write("success");
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally{
					studentDao.closeCon();
				}
			}else{
				try {
					response.getWriter().write("");
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					studentDao.closeCon();
				}
			}
		}
		if(userType == 3){

			Teacher teacher = (Teacher)request.getSession().getAttribute("user");
			if(!teacher.getPassword().equals(password)){
				try {
					response.getWriter().write("");
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			TeacherDao teacherDao = new TeacherDao();
			if(teacherDao.editPassword(teacher, newPassword)){
				try {
					response.getWriter().write("success");
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally{
					teacherDao.closeCon();
				}
			}else{
				try {
					response.getWriter().write("");
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					teacherDao.closeCon();
				}
			}
		}
	}
	private void personalView(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.getRequestDispatcher("MVC-View/personalView.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

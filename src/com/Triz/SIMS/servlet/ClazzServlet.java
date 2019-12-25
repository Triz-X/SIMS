package com.Triz.SIMS.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Triz.SIMS.dao.ClazzDao;
import com.Triz.SIMS.model.Clazz;
import com.Triz.SIMS.model.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author Triz-X
 *班级信息管理
 */



public class ClazzServlet extends HttpServlet {
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		String method = request.getParameter("method");
		if("toClazzListView".equals(method)){
			clazzList(request,response);
		}else if("getClazzList".equals(method)){
			getClazzList(request, response);
		}else if("AddClazz".equals(method)){
			addClazz(request, response);
		}else if("DeleteClazz".equals(method)){
			deleteClazz(request, response);
		}else if("EditClazz".equals(method)){
			editClazz(request, response);
		}
	}
	private void editClazz(HttpServletRequest request,HttpServletResponse response) {
		Integer id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name"); 
		String info = request.getParameter("info");
		Clazz clazz = new Clazz();
		clazz.setName(name);
		clazz.setInfo(info);
		clazz.setId(id);
		ClazzDao clazzDao = new ClazzDao();
		if(clazzDao.editClazz(clazz)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				clazzDao.closeCon();
			}
		}
	}
	private void deleteClazz(HttpServletRequest request,HttpServletResponse response) {
		Integer id = Integer.parseInt(request.getParameter("clazzid"));
		ClazzDao clazzDao = new ClazzDao();
		if(clazzDao.deleteClazz(id)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				clazzDao.closeCon();
			}
		}
	}
	private void addClazz(HttpServletRequest request,HttpServletResponse response) {
		String name = request.getParameter("name"); 
		String info = request.getParameter("info");
		Clazz clazz = new Clazz();
		clazz.setName(name);
		clazz.setInfo(info);
		ClazzDao clazzDao = new ClazzDao();
		if(clazzDao.addClazz(clazz)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				clazzDao.closeCon();
			}
		}
		
	}
	private void clazzList(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
			request.getRequestDispatcher("MVC-View/clazzList.jsp").forward(request, response);
		
	}
	private void getClazzList(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String name = request.getParameter("clazzName");
		Integer currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null ? 999 : Integer.parseInt(request.getParameter("rows"));
		Clazz clazz = new Clazz();
		clazz.setName(name);
		ClazzDao clazzDao = new ClazzDao();
		List<Clazz> clazzList = clazzDao.getClazzList(clazz, new Page(currentPage, pageSize));
		int total = clazzDao.getClazzListTotal(clazz);
		clazzDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", clazzList);
			String from = request.getParameter("from");
			if("combox".equals(from)){
				response.getWriter().write(JSONArray.fromObject(clazzList).toString());
			}else{
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
	}
}


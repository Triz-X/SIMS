package com.Triz.SIMS.filter;

/**
 * 拦截用户未登录下的操作
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest rea, ServletResponse reb, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)rea;
		HttpServletResponse response = (HttpServletResponse)reb;
		Object user = request.getSession().getAttribute("user");
		if(user == null){
			//未登录
			response.sendRedirect("index.jsp");
		}else{
			chain.doFilter(request, response);
		}

	}

}

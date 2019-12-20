package com.Triz.SIMS.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	/**
	 * 数据库连接util
	 * 
	 */
	private String dbUrl = "jdbc:mysql://localhost:3306/db_student_manager_web?useUnicode=true&serverTimezone=UTC&characterEncoding=utf8";
	private String dbUser = "root";
	private String dbPassword = "123456";
	private String jdbcName = "com.mysql.cj.jdbc.Driver";
	
	private Connection connection =null;
	public Connection getConnection(){
		
		try{
			Class.forName(jdbcName);
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			System.out.println("数据库连接成功");
		}catch(Exception e){
			System.out.println("数据库连接失败");
			e.printStackTrace();
		}
		return connection;
	}
	
	public void closeCon(){
		if(connection != null)
			try{
				connection.close();
				System.out.println("数据库连接关闭");
			}catch(SQLException e){
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) {
		DBUtil dbUtil =new DBUtil();
		dbUtil.getConnection();

	}

}

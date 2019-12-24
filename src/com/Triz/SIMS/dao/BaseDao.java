package com.Triz.SIMS.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Triz.SIMS.util.DbUtil;

public class BaseDao {
	private DbUtil dbUtil = new DbUtil();
	public void closeCon(){
		dbUtil.closeCon();
	}
	/**
	 * 
	 * 基础查询，多条查询
	 */
	public ResultSet query(String sql){
		try {
			PreparedStatement prepareStatement = dbUtil.getConnection().prepareStatement(sql);
			return prepareStatement.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	} 
	/**
	 * 数据库的添加与修改
	 */
	public boolean update(String sql){
		try {
			return dbUtil.getConnection().prepareStatement(sql).executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public Connection getConnection(){
		return dbUtil.getConnection();
	}
}

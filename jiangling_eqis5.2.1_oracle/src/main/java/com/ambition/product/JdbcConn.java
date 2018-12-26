package com.ambition.product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConn {
	//数据库连接
	public static Connection getConnection(String driverClass,String url,String user,String password){
		
		try {
			try {
				Class.forName(driverClass).newInstance();
			} catch (InstantiationException e) {
				
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		try {
			Connection conn=DriverManager.getConnection(url, user, password);
			return conn;
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	//关闭连接
	public static void closeConnection(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	}
}

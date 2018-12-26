package com.ambition.product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.norteksoft.product.util.PropUtils;

public class JdbcConnTest {
	
	static Connection conn;  
    static PreparedStatement  st; 
    
    static String driverClass = PropUtils.getProp("jdbc.driver.class");
    static String url = PropUtils.getProp("jdbc.connection.url");
    static String user = PropUtils.getProp("jdbc.user");
    static String password = PropUtils.getProp("jdbc.password");
	
	public static void query(){
		
		conn = JdbcConn.getConnection(driverClass, url, user, password);
		
		try {  
            String sql = "select * from view1";  // 查询数据的sql语句  
            
            st = (PreparedStatement ) conn.prepareStatement(sql); 
            
            //st.setString(1, "mfg-wf001");
            
            System.out.println("---------"+st.toString());
            
            ResultSet rs = st.executeQuery(sql);
            
            System.out.println("最后的查询结果为：");  
            while (rs.next()) {// 判断是否还有下一个数据  
                
                // 根据字段名获取相应的值  
                String code = rs.getString("code");  
                String name = rs.getString("name");  
                
                //输出查到的记录的各个字段的值  
                System.out.println(name + " " + code + " ");
              
            }  
            JdbcConn.closeConnection(conn);   //关闭数据库连接  
              
        } catch (SQLException e) {
            System.out.println("查询数据失败" + e.getMessage());
        } 
	}
	
	public static void insert(){
		
		conn = JdbcConn.getConnection(driverClass, url, user, password);
		
		try {  
            String sql = "INSERT INTO wf_type (code,company_id,creator,name) values('11','1','11','22')";  // 查询数据的sql语句  
              
            st = (PreparedStatement ) conn.prepareStatement(sql); 
            
            System.out.println("---------"+st.toString());
            
            int count = st.executeUpdate(sql);
            
            System.out.println("最后的查询结果为："+count);  
            JdbcConn.closeConnection(conn);   //关闭数据库连接  
              
        } catch (SQLException e) {  
            System.out.println("查询数据失败" + e.getMessage());  
        } 
	}
	public static void update(){
		
		conn = JdbcConn.getConnection(driverClass, url, user, password);
		
		try {  
            String sql = "update  wf_type set code='22' where id='81'";  // 查询数据的sql语句  
              
            st = (PreparedStatement ) conn.prepareStatement(sql); 
            
            System.out.println("---------"+st.toString());
            
            int count = st.executeUpdate(sql);
            
            System.out.println("最后的查询结果为："+count);  
            JdbcConn.closeConnection(conn);   //关闭数据库连接  
              
        } catch (SQLException e) {  
            System.out.println("查询数据失败" + e.getMessage());  
        } 
	}
	public static void delete(){
		
		conn = JdbcConn.getConnection(driverClass, url, user, password);
		
		try {  
            String sql = "delete from  wf_type  where id='81'";  // 查询数据的sql语句  
              
            st = (PreparedStatement ) conn.prepareStatement(sql); 
            
            System.out.println("---------"+st.toString());
            
            int count = st.executeUpdate(sql);
            
            System.out.println("最后的查询结果为："+count);  
            JdbcConn.closeConnection(conn);   //关闭数据库连接  
              
        } catch (SQLException e) {  
            System.out.println("查询数据失败" + e.getMessage());  
        } 
	}
	public static void main(String[] args){
		query();
		//insert();
		//update();
		//delete();
	}
	
}

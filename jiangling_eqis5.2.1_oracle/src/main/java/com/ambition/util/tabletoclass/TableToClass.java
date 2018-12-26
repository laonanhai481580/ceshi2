package com.ambition.util.tabletoclass;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 类名: TableToClass 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：从数据库生成实体</p>
 * @author  刘承斌
 * @version 1.00 2014-7-23 下午4:48:34  发布
 */
public class TableToClass {
	private static final String TAB = "\t";
	private static final String LINE = "\n";
	
	  //数据库连接
	 private static final String URL ="jdbc:oracle:thin:@localhost:1521:orcl";
	 private static final String NAME = "jiangling";
	 private static final String PASS = "Ambition800";
	 private static final String DRIVER ="oracle.jdbc.driver.OracleDriver";
	 private static final String TABLENAME = "afs_oldpart_in_info";


	public static void main(String args[]) {
		FileOutputStream fo = null;
		DataOutputStream ds = null;
		try {
			Class.forName(DRIVER);
			Connection conn = null;
			conn = DriverManager.getConnection(URL, NAME, PASS);
			String tableName = TABLENAME;// 表名
			File file = new File("d:\\" + tableName + ".java");
			String sql = "select * from " + tableName + " where 1 <> 1";
			PreparedStatement ps = null;
			ResultSet rs = null;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			tableName = tableName.substring(0, 1).toUpperCase()
					+ tableName.substring(1);
			StringBuffer buff = new StringBuffer();
			buff.append("public class " + tableName + " {");
			buff.append(LINE);
			for (int i = 1; i <= columnCount; i++) {
//				buff.append(TAB).append("/*");
//				buff.append(LINE);
//				buff.append(TAB).append(
//						"* " + md.getColumnName(i).toLowerCase());
//				buff.append(LINE);
//				buff.append(TAB).append("*/");
//				buff.append(LINE);
				buff.append(TAB);
				buff.append("private "
						+ TableToClass.toType(md.getColumnTypeName(i)) + " "
						+ tojavabeanname(md.getColumnName(i).toLowerCase()) + ";");
				buff.append("//").append(LINE);
			}
			for (int i = 1; i <= columnCount; i++) {
				buff.append(TAB);
				String pojoType = TableToClass.toType(md.getColumnTypeName(i));
				String columnName = tojavabeanname(md.getColumnName(i).toLowerCase());
				String getName = null;
				String setName = null;
				if (columnName.length() > 1) {
					getName = "public " + pojoType + " get"
							+ columnName.substring(0, 1).toUpperCase()
							+ columnName.substring(1).toLowerCase() + "() {";
					setName = "public void set"
							+ columnName.substring(0, 1).toUpperCase()
							+ columnName.substring(1).toLowerCase() + "("
							+ pojoType + " " + columnName + ") {";
				} else {
					getName = "public get" + columnName.toUpperCase() + "() {";
					setName = "public set" + columnName.toUpperCase() + "("
							+ pojoType + " " + columnName + ") {";
				}
				buff.append(LINE).append(TAB).append(getName);
				buff.append(LINE).append(TAB).append(TAB);
				buff.append("return " + columnName + ";");
				buff.append(LINE).append(TAB).append("}");
				buff.append(LINE);
				buff.append(LINE).append(TAB).append(setName);
				buff.append(LINE).append(TAB).append(TAB);
				buff.append("this." + columnName + " = " + columnName + ";");
				buff.append(LINE).append(TAB).append("}");
				buff.append(LINE);
			}
			buff.append("}");
			fo = new FileOutputStream(file);
			ds = new DataOutputStream(fo);
			ds.writeBytes(buff.toString());
			System.out.println(buff.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				ds.flush();
				ds.close();
				fo.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static String toType(String sqlType) {
		String result = null;
		sqlType = sqlType.toLowerCase();
		if (sqlType.equalsIgnoreCase("bit")) {
			result = "boolean";
		} else if (sqlType.equalsIgnoreCase("tinyint")) {
			result = "byte";
		} else if (sqlType.equalsIgnoreCase("smallint")) {
			result = "short";
		} else if (sqlType.equalsIgnoreCase("int")) {
			result = "int";
		} else if (sqlType.equalsIgnoreCase("bigint")
				|| sqlType.equalsIgnoreCase("timestamp")) {
			result = "long";
		} else if (sqlType.equalsIgnoreCase("float")) {
			result = "float";
		} else if (sqlType.equalsIgnoreCase("decimal")
				|| sqlType.equalsIgnoreCase("numeric")
				|| sqlType.equalsIgnoreCase("real")
				|| sqlType.equalsIgnoreCase("money")
				|| sqlType.equalsIgnoreCase("smallmoney")) {
			result = "double";
		} else if (sqlType.equalsIgnoreCase("varchar")
				||sqlType.equalsIgnoreCase("varchar2")
				|| sqlType.equalsIgnoreCase("char")
				|| sqlType.equalsIgnoreCase("nvarchar")
				|| sqlType.equalsIgnoreCase("nchar")
				|| sqlType.equalsIgnoreCase("text")) {
			result = "String";
		} else if (sqlType.equalsIgnoreCase("datetime")) {
			result = "Date";
		} else if (sqlType.equalsIgnoreCase("image")) {
			result = "Blod";
		}else if(sqlType.equalsIgnoreCase("tinyint")){
			result = "byte";
		}else if(sqlType.equalsIgnoreCase("date") || sqlType.equalsIgnoreCase("timestamp")
				|| sqlType.equalsIgnoreCase("timestamp with local time zone")
				|| sqlType.equalsIgnoreCase("timestamp with time zone")) {
			result = "Date";
		} else if (sqlType.equalsIgnoreCase("number")) {
			result = "Long";
		}

		return result;
	}
	
	public static String tojavabeanname(String columnName){
		String result="";
		String[] strs = columnName.split("_");
		int flag = 0;
		for(String str : strs){
			if(flag == 0){
				result = result +str;
			}else{
				result = result +str.substring(0, 1).toUpperCase()+str.substring(1).toLowerCase();
			}
			flag ++;
		}
		
		return result;
	}

}

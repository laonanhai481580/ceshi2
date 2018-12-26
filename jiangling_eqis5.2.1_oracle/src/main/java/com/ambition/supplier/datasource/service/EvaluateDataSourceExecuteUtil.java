package com.ambition.supplier.datasource.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ambition.util.exception.AmbFrameException;
/**
 * 类名:数据来源执行代码生成类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：数据来源执行代码执行器对象</p>
 * @author  赵骏
 * @version 1.00 2013-4-19 发布
 */
@Service
public class EvaluateDataSourceExecuteUtil {
	/**
	  * 方法名: 根据源码生成数据来源执行的对象
	  * <p>功能说明：生成数据来源执行器对象</p>
	  * @param id 
	  * @param executeCode java执行代码
	  * @return
	 */
	public EvaluateDataSourceExecuteBase generateObject(long id,String executeCode) throws Exception{
		String className = "EvaluateDataSource" + id;
		String classPath = this.getClass().getResource("/").getFile();
		String packagePath = this.getClass().getPackage().getName().replace(".","/");
		String javaFilePath = classPath + packagePath + "/" + className + ".java"; 
		String classFilePath = classPath + packagePath + "/" + className + ".class";
		try {
			createJava(className,javaFilePath,executeCode);
			compileClass(classPath,javaFilePath);
			Class<?> cla = getClass(className,classFilePath);
			EvaluateDataSourceExecuteBase dataSourceExecute = (EvaluateDataSourceExecuteBase)cla.newInstance();
			return dataSourceExecute;
		}finally{
			File file = new File(javaFilePath);
			if(file.exists()){
				file.delete();
			}
			file = new File(classFilePath);
			if(file.exists()){
				file.delete();
			}
		}
	}
	/**
	  * 方法名:创建源文件 
	  * <p>功能说明：根据类名,文件路径,执行代友生成新的java文件</p>
	  * @param className 类名
	  * @param filePath	存放临时java文件的路径
	  * @param executeCode java执行代码
	  * @throws IOException
	 */
	private static void createJava(String className,String filePath,String executeCode) throws IOException {
		FileWriter aWriter = null;
		try {
			aWriter = new FileWriter(filePath,false);
			aWriter.write("package "+EvaluateDataSourceExecuteBase.class.getPackage().getName() + ";");
			aWriter.write("import java.util.*;import java.text.SimpleDateFormat;");
			aWriter.write("public class " +  className + " extends "+EvaluateDataSourceExecuteBase.class.getName()+" {");
			aWriter.write("public Map<String,Object> execute(String supplierCode,Date startDate,Date endDate,String materialType) throws Exception   {");
			aWriter.write(executeCode.trim().replace("\\n",""));
			aWriter.write("}}");
			aWriter.flush();
		} finally{
			if(aWriter != null){
				aWriter.close();
			}
		}
	}
	
	/**
	  * 方法名:编译java源文件 
	  * <p>功能说明：根据classPath和java源文件的路径编译源文件</p>
	  * @param classPath classpath
	  * @param javaFileName java源文件的路径
	  * @throws IOException
	 * @throws AmbFrameException 
	 * @throws InterruptedException 
	 */
	private void compileClass(String classPath,String javaFilePath) throws IOException, InterruptedException{
		Process process = Runtime.getRuntime().exec("javac -encoding UTF-8 -classpath "+classPath+" " + javaFilePath);
		InputStream err = process.getErrorStream();
		InputStreamReader reader = null;
		BufferedReader buffered = null;
		try {
			reader = new InputStreamReader(err,"GBK");
			buffered = new BufferedReader(reader);
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line = buffered.readLine()) != null){
				sb.append(line);
			}
			int ret = process.waitFor();
			if(ret != 0){
				throw new AmbFrameException(sb.toString());
			}
		} finally {
			if(buffered != null){
				buffered.close();
			}
			if(reader != null){
				reader.close();
			}
			if(err != null){
				err.close();
			}
		}
	}
	/**
	  * 方法名: 根据类名和class文件的路径获取class对象
	  * <p>功能说明：根据类名和class文件的路径获取class对象</p>
	  * @param className
	  * @param classFilePath
	  * @return
	  * @throws IOException
	 * @throws ClassNotFoundException 
	  * @throws InterruptedException
	 */
	private Class<?> getClass(String className,String classFilePath) throws IOException, ClassNotFoundException{
		return this.getClass().getClassLoader().loadClass(this.getClass().getPackage().getName() + "." + className);
	}
//	private static class MyClassLoader extends ClassLoader{
//		public Class<?> defineClassByName(String name,byte[] b,int off,int len){ 
//			  //由于defineClass是protected，所以需要继承后来调用 
//			  return super.defineClass(name,b,off,len); 
//		} 
//	}
}

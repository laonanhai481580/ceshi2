/**   
* @Title: Test.java 
* @Package com.ambition.util.erp.entity 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 刘承斌   
* @date 2014-9-2 下午11:28:45 
* @version V1.0   
*/ 
package com.ambition.util.erp.entity;

import java.io.IOException;

/**
 * 类名: Test 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：通过调度bat文件调度job</p>
 * @author  刘承斌
 * @version 1.00  2014-9-2 下午11:28:45  发布
 */
public class Test {

	/**
	 * @throws IOException  
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param args    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Runtime.getRuntime().exec("cmd /c C:\\Users\\Administrator\\Desktop\\kettle.bat");
	}

}

package com.ambition.util.erp.service;

/**
 * 类名:集成的接口
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：定义了集成的接口模式</p>
 * @author  赵骏
 * @version 1.00 2013-5-24 发布
 */
public interface IntegrationService {
	/**是否正在更新*/
	public boolean isIntegration();
	
	/**开始集成**/
	public void beginIntegration(Long companyId,String userLoginName);
	
	/**
	  * 方法名:获取进度 
	  * <p>功能说明：获取当前的更新进度</p>
	  * @return
	 */
	public int getProgressbar();
}

package com.ambition.util.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.norteksoft.acs.base.utils.log.LogUtilDao;

@Service
@Transactional
public class LogAspectManager {
	@Autowired
	private LogUtilDao logUtilDao;
	/**
		  * 方法名: 
		  * <p>功能说明：</p>
		  * @return
	 */
	public void debugLog(String optType,String message,Long systemId){
		logUtilDao.debugLog(optType,message,systemId);
	}
}

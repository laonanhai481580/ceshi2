package com.ambition.gsm.base.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.gsm.base.dao.GsmStandardAttachDao;
import com.ambition.gsm.entity.GsmStandardAttach;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:检验标准文件服务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  LPF
 * @version 1.00 2016-11-25 发布
 */
@Service
public class GsmStandardAttachManager{
	@Autowired
	private GsmStandardAttachDao standardAttachDao;
	
	/**
	  * 方法名:检验标准文件
	  * <p>功能说明：</p>
	  * @param file
	  * @param figureNumber
	  * @param figureVersion
	  * @param iisVersion
	  * @return
	 * @throws IOException 
	 */
	public GsmStandardAttach addStandardAttach(File file,String fileName,String measurementName,String measurementSpecification,String manufacturer,Long companyId) throws IOException{
		GsmStandardAttach attach = new GsmStandardAttach();
		attach.setCompanyId(companyId);
		attach.setCreatedTime(new Date());
		attach.setCreator(ContextUtils.getUserName());
		attach.setMeasurementName(measurementName);
		attach.setMeasurementSpecification(measurementSpecification);
		attach.setManufacturer(manufacturer);
		attach.setFileName(fileName);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			attach.setBlobValue(Hibernate.createBlob(inputStream));
			standardAttachDao.save(attach);
			Session session=standardAttachDao.getSession();
			session.flush();
			session.refresh(attach, LockMode.UPGRADE);
			session.save(attach);
		}finally{
			inputStream.close();
		}
		return attach;
	}
}

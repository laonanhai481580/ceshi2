package com.ambition.carmfg.plantparameter.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.entity.PlantAttach;
import com.ambition.carmfg.plantparameter.dao.PlantAttachDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:设备参数附件服务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  LPF
 * @version 1.00 2016-9-3 发布
 */
@Service
public class PlantAttachManager{
	@Autowired
	private PlantAttachDao plantAttachDao;
	
	/**
	  * 方法名:设备参数附件 
	  * <p>功能说明：</p>
	  * @param file
	  * @param figureNumber
	  * @param figureVersion
	  * @param iisVersion
	  * @return
	 * @throws IOException 
	 */
	public PlantAttach addPlantAttach(File file,String fileName,String model,String modelName,Long companyId) throws IOException{
		PlantAttach attach = new PlantAttach();
		attach.setCompanyId(companyId);
		attach.setCreatedTime(new Date());
		attach.setCreator(ContextUtils.getUserName());
		attach.setModelName(modelName);
		attach.setModel(model);
		attach.setFileName(fileName);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			attach.setBlobValue(Hibernate.createBlob(inputStream));
			plantAttachDao.save(attach);
			Session session=plantAttachDao.getSession();
			session.flush();
			session.refresh(attach, LockMode.UPGRADE);
			session.save(attach);
		}finally{
			inputStream.close();
		}
		return attach;
	}
}

package com.ambition.carmfg.inspectionbase.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.entity.IndicatorAttach;
import com.ambition.carmfg.inspectionbase.dao.IndicatorAttachDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:进货检验IIS的服务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：处理IIS文件整合</p>
 * @author  赵骏
 * @version 1.00 2013-11-17 发布
 */
@Service
public class IndicatorAttachManager{
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private IndicatorAttachDao indicatorAttachDao;
	
	/**
	  * 方法名:添加IIS附件 
	  * <p>功能说明：</p>
	  * @param file
	  * @param figureNumber
	  * @param figureVersion
	  * @param iisVersion
	  * @return
	 * @throws IOException 
	 */
	public IndicatorAttach addIndicatorAttach(File file,String fileName,String model,String modelName,Long companyId) throws IOException{
		IndicatorAttach attach = new IndicatorAttach();
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
			indicatorAttachDao.save(attach);
			Session session=indicatorAttachDao.getSession();
			session.flush();
			session.refresh(attach, LockMode.UPGRADE);
			session.save(attach);
		}finally{
			inputStream.close();
		}
		return attach;
	}
	/**
	  * 方法名:添加IIS附件 
	  * <p>功能说明：</p>
	  * @param file
	  * @param figureNumber
	  * @param figureVersion
	  * @param iisVersion
	  * @return
	 * @throws IOException 
	 */
	public IndicatorAttach addIndicatorAttachCCM(File file,String fileName,String materiType,Long companyId) throws IOException{
		IndicatorAttach attach = new IndicatorAttach();
		attach.setCompanyId(companyId);
		attach.setCreatedTime(new Date());
		attach.setCreator(ContextUtils.getUserName());
		attach.setModel(materiType);
		attach.setFileName(fileName);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			attach.setBlobValue(Hibernate.createBlob(inputStream));
			indicatorAttachDao.save(attach);
			Session session=indicatorAttachDao.getSession();
			session.flush();
			session.refresh(attach, LockMode.UPGRADE);
			session.save(attach);
		}finally{
			inputStream.close();
		}
		return attach;
	}
	
	/**
	  * 方法名:根据检验标准文件ID下载标准文件 
	  * <p>功能说明：</p>
	  * @param indicatorAttachId
	 * @throws IOException 
	 */
	public void downloadIISByAttachId(HttpServletResponse response,Long attachId) throws IOException{
		try {
			IndicatorAttach attach = indicatorAttachDao.getIndicatorAttachById(attachId);
			if(attach == null){
				throw new AmbFrameException("IIS标准文件不存在!");
			}
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(new String(attach.getFileName().getBytes("GBK"),"ISO8859_1")).append("\"")
					.toString());
			response.getOutputStream().write(attach.getBlobValue().getBytes(1,(int)attach.getBlobValue().length()));
		} catch (Exception e) {
			log.error("下载模板出错!",e);
			response.reset();
			response.setContentType("application/text");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append("下载错误.txt").append("\"")
					.toString());
			response.getOutputStream().write((e.getMessage()==null?"出错了!":e.getMessage()).getBytes());
		}
	}
	
	/**
	  * 方法名:添加IIS附件 
	  * <p>功能说明：</p>
	  * @param file
	  * @param figureNumber
	  * @param figureVersion
	  * @param iisVersion
	  * @return
	 * @throws IOException 
	 */
	public IndicatorAttach copyIndicatorAttach(Long indicatorId){
		if(indicatorId == null){
			return null;
		}
		IndicatorAttach sourceAttach = indicatorAttachDao.getIndicatorAttachById(indicatorId);
		if(sourceAttach == null){
			return null;
		}
		IndicatorAttach attach = new IndicatorAttach();
		attach.setCompanyId(sourceAttach.getCompanyId());
		attach.setCreatedTime(new Date());
		attach.setCreator(ContextUtils.getUserName());
		attach.setModelName(sourceAttach.getModelName());
		attach.setModel(sourceAttach.getModel());
		attach.setFileName(sourceAttach.getFileName());
		attach.setBlobValue(sourceAttach.getBlobValue());
		indicatorAttachDao.save(attach);
		return attach;
	}
}

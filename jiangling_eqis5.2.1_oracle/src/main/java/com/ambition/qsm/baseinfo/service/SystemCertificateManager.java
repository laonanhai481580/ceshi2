package com.ambition.qsm.baseinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.qsm.baseinfo.dao.SystemCertificateDao;
import com.ambition.qsm.entity.SystemCertificate;
import com.norteksoft.product.orm.Page;

/**
 * 
 * 类名:公司证书Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Service
@Transactional
public class SystemCertificateManager {
	@Autowired
	private SystemCertificateDao systemCertificateDao;	
	public SystemCertificate getSystemCertificate(Long id){
		return systemCertificateDao.get(id);
	}
	
	public void deleteSystemCertificate(SystemCertificate systemCertificate){
		systemCertificateDao.delete(systemCertificate);
	}

	public Page<SystemCertificate> search(Page<SystemCertificate>page){
		return systemCertificateDao.search(page);
	}

	public List<SystemCertificate> listAll(){
		return systemCertificateDao.getAllSystemCertificate();
	}
		
	public void deleteSystemCertificate(Long id){
		systemCertificateDao.delete(id);
	}
	public void deleteSystemCertificate(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			SystemCertificate  systemCertificate = systemCertificateDao.get(Long.valueOf(id));
			if(systemCertificate.getId() != null){
				systemCertificateDao.delete(systemCertificate);
			}
		}
	}
	public void saveSystemCertificate(SystemCertificate systemCertificate){
		systemCertificateDao.save(systemCertificate);
	}
	
}

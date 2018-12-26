package com.ambition.carmfg.baseinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.dao.SamplingNumberDao;
import com.ambition.carmfg.entity.SamplingNumber;
import com.norteksoft.product.orm.Page;

/**
 * 
 * 类名:抽检数量维护Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Service
@Transactional
public class SamplingNumberManager {
	@Autowired
	private SamplingNumberDao samplingNumberDao;	
	public SamplingNumber getSamplingNumber(Long id){
		return samplingNumberDao.get(id);
	}
	
	public void deleteSamplingNumber(SamplingNumber samplingNumber){
		samplingNumberDao.delete(samplingNumber);
	}

	public Page<SamplingNumber> search(Page<SamplingNumber>page){
		return samplingNumberDao.search(page);
	}

	public List<SamplingNumber> listAll(){
		return samplingNumberDao.getAllSamplingNumber();
	}
		
	public void deleteSamplingNumber(Long id){
		samplingNumberDao.delete(id);
	}
	public void deleteSamplingNumber(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			SamplingNumber  samplingNumber = samplingNumberDao.get(Long.valueOf(id));
			if(samplingNumber.getId() != null){
				samplingNumberDao.delete(samplingNumber);
			}
		}
	}
	public void saveSamplingNumber(SamplingNumber samplingNumber){
		samplingNumberDao.save(samplingNumber);
	}
	
}

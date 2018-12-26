package com.ambition.iqc.inspectionbase.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.iqc.entity.TestFrequency;
import com.ambition.iqc.inspectionbase.dao.TestFrequencyDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class TestFrequencyManager {
	@Autowired
	private TestFrequencyDao testFrequencyDao;	
	@Autowired
	private ProductBomDao productBomDao;
	public TestFrequency getTestFrequency(Long id){
		return testFrequencyDao.get(id);
	}
	
	public void deleteTestFrequency(TestFrequency testFrequency){
		testFrequencyDao.delete(testFrequency);
	}

	public Page<TestFrequency> search(Page<TestFrequency>page){
		return testFrequencyDao.search(page);
	}

	public List<TestFrequency> listAll(){
		return testFrequencyDao.getAllTestFrequency();
	}
		
	public void deleteTestFrequency(Long id){
		testFrequencyDao.delete(id);
	}
	public void deleteTestFrequency(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			TestFrequency  testFrequency = testFrequencyDao.get(Long.valueOf(id));
			if(testFrequency.getId() != null){
				testFrequencyDao.delete(testFrequency);
			}
		}
	}
	public void saveTestFrequency(TestFrequency testFrequency){
		if(isExistTestFrequency(testFrequency.getId(),testFrequency.getSupplierCode(),testFrequency.getCheckBomCode())){
			throw new AmbFrameException("已经存在相同的测试频率信息!");
		}
		testFrequencyDao.save(testFrequency);
	}
	/**
	 * 检查是否存在相同的测试频率
	 * @param id
	 * @param name
	 * @return
	 */
	public Boolean isExistTestFrequency(Long id,String supplierCode,String checkBomCode){
		String hql = "select count(i.id) from TestFrequency i where i.companyId = ? and i.supplierCode = ? and i.checkBomCode=?  ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(supplierCode);
		searchParams.add(checkBomCode);
		if(id != null){
			hql += " and i.id <> ?";
			searchParams.add(id);
		}
		List<?> list = testFrequencyDao.find(hql,searchParams.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 查询测试频率
	 * @param id
	 * @param name
	 * @return
	 */
	public TestFrequency searchFrequency(String supplierCode,String checkBomCode){
		String hql = "select i from TestFrequency i where i.companyId = ? and i.supplierCode = ? and i.checkBomCode=?   ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(supplierCode);
		searchParams.add(checkBomCode);
		List<TestFrequency> list = testFrequencyDao.find(hql,searchParams.toArray());
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
}

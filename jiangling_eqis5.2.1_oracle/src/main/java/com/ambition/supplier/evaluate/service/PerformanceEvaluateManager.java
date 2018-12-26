package com.ambition.supplier.evaluate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.PerformanceEvaluate;
import com.ambition.supplier.evaluate.dao.PerformanceEvaluateDao;
import com.norteksoft.product.orm.Page;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * 类名: PerformanceEvaluateManager 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：供应商评价排名</p>
 * @author  刘承斌
 * @version 1.00  2015-3-23 下午5:52:09  发布
 */
@Service
@Transactional
public class PerformanceEvaluateManager {
	@Autowired
	private PerformanceEvaluateDao performanceEvaluateDao;
	

	public PerformanceEvaluate getPerformanceEvaluate(Long id){
		return performanceEvaluateDao.get(id);
	}

	public void savePerformanceEvaluate(PerformanceEvaluate performanceEvaluate){
		performanceEvaluateDao.save(performanceEvaluate);
	}
	
	public void deletePerformanceEvaluate(PerformanceEvaluate performanceEvaluate){
		performanceEvaluateDao.delete(performanceEvaluate);
	}

	public Page<PerformanceEvaluate> search(Page<PerformanceEvaluate>page, String where){
		return performanceEvaluateDao.search(page,where);
	}

	public List<PerformanceEvaluate> listAll(){
		return performanceEvaluateDao.getAllPerformanceEvaluate();
	}
		
	public void deletePerformanceEvaluate(Long id){
		performanceEvaluateDao.delete(id);
	}
	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deletePerformanceEvaluate(String ids) {
		String[] deleteIds = ids.split(",");
		int deleteNum=0;
		int failNum=0;
		for (String id : deleteIds) {
			PerformanceEvaluate  performanceEvaluate = performanceEvaluateDao.get(Long.valueOf(id));
				performanceEvaluateDao.delete(performanceEvaluate);
				deleteNum++;
		}
		return deleteNum+"条数据成功删除，"+failNum+"条没有数据权限删除";
	}

	
}

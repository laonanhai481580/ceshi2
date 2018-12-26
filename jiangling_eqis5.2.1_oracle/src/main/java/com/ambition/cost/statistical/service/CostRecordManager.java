package com.ambition.cost.statistical.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.cost.entity.CostRecord;
import com.ambition.cost.statistical.dao.CostRecordDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:成本数据业务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2014-11-29 发布
 */
@Service
@Transactional
public class CostRecordManager {
	@Autowired
	private CostRecordDao costRecordDao;
	public CostRecord getCostRecord(Long id){
		return costRecordDao.get(id);
	}
	public void saveCostRecord(CostRecord costRecord){
		//相同月份,相同的来源类型,相同的成本科目不能重复
		String hql = "select count(*) from CostRecord  c where c.sourceType = ? and c.levelTwoCode = ? and c.code = ? and c.occurringMonthStr = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(costRecord.getSourceType());
		params.add(costRecord.getLevelTwoCode());
		params.add(costRecord.getCode());
		params.add(costRecord.getOccurringMonthStr());
		if(costRecord.getId() != null){
			hql += " and c.id <> ?";
			params.add(costRecord.getId());
		}
		List<?> objs = costRecordDao.find(hql,params.toArray());
		if(Integer.valueOf(objs.get(0).toString())>0){
			throw new AmbFrameException("本月已经录入了相同的成本!");
		}
		costRecord.setModifiedTime(new Date());
		costRecord.setModifier(ContextUtils.getLoginName());
		costRecord.setModifierName(ContextUtils.getUserName());
		if(StringUtils.isNotEmpty(costRecord.getOccurringMonthStr())){
			costRecord.setOccurringMonth(Integer.valueOf(costRecord.getOccurringMonthStr().replaceAll("-","")));
		}
		costRecordDao.save(costRecord);
	}
	public void deleteCostRecord(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id:ids){
			costRecordDao.delete(Long.valueOf(id));
		}
	}
	public Page<CostRecord> getListDatas(Page<CostRecord> page,Integer year){
		String hql = "from CostRecord g where g.companyId = ? and g.year = ?";
		return costRecordDao.searchPageByHql(page, hql, new Object[]{ContextUtils.getCompanyId(),year});
	}
	public Page<CostRecord> list(Page<CostRecord> page,String sourceType){
		return costRecordDao.list(page,sourceType);
	}
}

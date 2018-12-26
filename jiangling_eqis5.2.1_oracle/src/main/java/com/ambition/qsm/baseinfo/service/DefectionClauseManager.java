package com.ambition.qsm.baseinfo.service;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.qsm.baseinfo.dao.DefectionClauseDao;
import com.ambition.qsm.entity.DefectionClause;
import com.norteksoft.product.orm.Page;

/**
 * 
 * 类名:不符合条款Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Service
@Transactional
public class DefectionClauseManager {
	@Autowired
	private DefectionClauseDao defectionClauseDao;	
	public DefectionClause getDefectionClause(Long id){
		return defectionClauseDao.get(id);
	}
	
	public void deleteDefectionClause(DefectionClause defectionClause){
		defectionClauseDao.delete(defectionClause);
	}

	public Page<DefectionClause> search(Page<DefectionClause>page){
		return defectionClauseDao.search(page);
	}

	public List<DefectionClause> listAll(){
		return defectionClauseDao.getAllDefectionClause();
	}
		
	public void deleteDefectionClause(Long id){
		defectionClauseDao.delete(id);
	}
	public void deleteDefectionClause(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			DefectionClause  defectionClause = defectionClauseDao.get(Long.valueOf(id));
			if(defectionClause.getId() != null){
				defectionClauseDao.delete(defectionClause);
			}
		}
	}
	public void saveDefectionClause(DefectionClause defectionClause){
		defectionClauseDao.save(defectionClause);
	}

	public List<?> searchClauseNnames(JSONObject params) {
		return defectionClauseDao.searchClauseNnames(params);
	}
	
}

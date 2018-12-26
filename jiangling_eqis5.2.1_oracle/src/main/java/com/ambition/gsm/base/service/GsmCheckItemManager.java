package com.ambition.gsm.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.base.dao.GsmCheckItemDao;
import com.ambition.gsm.entity.GsmCheckItem;
import com.norteksoft.product.orm.Page;

/**
 * 
 * 类名:检验项目Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author LPF
 * @version 1.00 2016年11月24日 发布
 */
@Service
@Transactional
public class GsmCheckItemManager {
	@Autowired
	private GsmCheckItemDao checkItemDao;
	
	public GsmCheckItem getGsmCheckItem(Long id){
		return checkItemDao.get(id);
	}
	
	/**
	  * 方法名: 保存检验项目
	  * <p>功能说明：</p>
	  * @param 
	 */
	public void saveGsmCheckItem(GsmCheckItem checkItem){
		checkItemDao.save(checkItem);
	}
	
	public void deleteGsmCheckItem(GsmCheckItem oqcInspection){
		checkItemDao.delete(oqcInspection);
	}

	public Page<GsmCheckItem> search(Page<GsmCheckItem>page){
		return checkItemDao.search(page);
	}

	public List<GsmCheckItem> listAll(){
		return checkItemDao.getAllGsmCheckItem();
	}
		
	public void deleteGsmCheckItem(Long id){
		checkItemDao.delete(id);
	}
	public void deleteGsmCheckItem(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			GsmCheckItem  oqcInspection = checkItemDao.get(Long.valueOf(id));
			if(oqcInspection.getId() != null){
				checkItemDao.delete(oqcInspection);
			}
		}
	}
}

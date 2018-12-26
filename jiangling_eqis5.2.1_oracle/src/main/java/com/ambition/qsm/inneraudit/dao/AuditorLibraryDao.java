package com.ambition.qsm.inneraudit.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.qsm.entity.AuditorLibrary;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 
 * 类名:内审员备选库DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月31日 发布
 */
@Repository
public class AuditorLibraryDao extends HibernateDao<AuditorLibrary,Long>{
	public Page<AuditorLibrary> list(Page<AuditorLibrary> page){
		return findPage(page, "from AuditorLibrary d");
	}
	
	public List<AuditorLibrary> getAllAuditorLibrary(){
		return find("from AuditorLibrary d");
	}

    public Page<AuditorLibrary> search(Page<AuditorLibrary> page) {
        return searchPageByHql(page, "from AuditorLibrary d ");
    }
}

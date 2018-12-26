package com.ambition.carmfg.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.SamplingNumber;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 
 * 类名:抽检数量维护DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月27日 发布
 */
@Repository
public class SamplingNumberDao extends HibernateDao<SamplingNumber,Long>{
	public Page<SamplingNumber> list(Page<SamplingNumber> page){
		return findPage(page, "from SamplingNumber d");
	}
	
	public List<SamplingNumber> getAllSamplingNumber(){
		return find("from SamplingNumber d");
	}

    public Page<SamplingNumber> search(Page<SamplingNumber> page) {
        return searchPageByHql(page, "from SamplingNumber d ");
    }
}

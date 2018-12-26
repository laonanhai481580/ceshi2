package com.ambition.gsm.base.dao;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import com.ambition.gsm.entity.IntransitedEquipmentBase;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 在途量检具基类(DAO)
 * @author 张顺治
 *
 */
@Repository
public class IntransitedEquipmentBaseDao extends HibernateDao<IntransitedEquipmentBase, Long> {
		
	public Page<IntransitedEquipmentBase> list(Page<IntransitedEquipmentBase> page){
		return findPage(page, "from IntransitedEquipmentBase i");
	}
	
	public List<IntransitedEquipmentBase> getAllIntransitedEquipmentBase(){
		return find("from IntransitedEquipmentBase t");
	}

    public Page<IntransitedEquipmentBase> search(Page<IntransitedEquipmentBase> page) {
        return searchPageByHql(page, "from IntransitedEquipmentBase i ");
    }
    
    public boolean ModifyNo(String hql){
		Session session = this.getSessionFactory().getCurrentSession();
		Query q= session.createQuery(hql);
		int i =q.executeUpdate();
		return i>0;
    }

    public IntransitedEquipmentBase getIntransitedEquipmentBaseNo(String measurementNo){
    	String hql=" from IntransitedEquipmentBase as i where i.measurementType =? ";
    	List<IntransitedEquipmentBase> li= this.find(hql, new Object[]{measurementNo});
    	if(!li.isEmpty()){
    		return li.get(0);
    	}else{
    		return null;
    	}
    }
    
}

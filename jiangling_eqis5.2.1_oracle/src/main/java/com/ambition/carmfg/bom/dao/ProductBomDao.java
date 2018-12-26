package com.ambition.carmfg.bom.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.ProductBom;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Repository
public class ProductBomDao extends HibernateDao<ProductBom, Long> {
	
	/**
	 * 查询
	 * @param page,parentId
	 * @return
	 */
	public Page<ProductBom> search(Page<ProductBom> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from ProductBom p where p.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and (p.materielCode like ? or p.materielName like ?)");
			params.add("%" + customSearch.trim() + "%");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId == null){
			hql.append(" and p.bomParent is null");
		}else{
			hql.append(" and p.bomParent.id = ?");
			params.add(parentId);
		}
		hql.append(" order by p.hasChild desc,p.orderNum asc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	/**
	 * 查询
	 * @param page,parentId
	 * @return
	 */
	public Page<ProductBom> searchProduct(Page<ProductBom> page,Long parentId,String type){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from ProductBom p where p.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and (p.materielCode like ? or p.materielName like ?)");
			params.add("%" + customSearch.trim() + "%");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId == null){
			hql.append(" and p.bomParent is null");
		}else{
			hql.append(" and p.bomParent.id = ?");
			params.add(parentId);
		}
		if(StringUtils.isNotEmpty(type)){
			hql.append(" and p.materialType like '成品.%'");
		}
		hql.append(" order by p.hasChild desc,p.orderNum asc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	
	public Page<ProductBom> searchByParams(Page<ProductBom> page){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from ProductBom p where p.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String searchParameter = Struts2Utils.getParameter("searchParameters");
		if(searchParameter!=null&&searchParameter.isEmpty()){
			hql.append(" and p.bomParent is null");
		}
		hql.append(" order by p.hasChild desc,p.orderNum asc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	public Page<ProductBom> searchByParams(Page<ProductBom> page,String materielName,String materielCode){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from ProductBom p where p.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		if(materielName!=null&&!"".equals(materielName)){
			hql.append(" and p.materielName like ?");
			params.add("%" + materielName.trim() + "%");
		}
		if(materielCode!=null&&!"".equals(materielCode)){
			hql.append(" and p.materielCode like ?");
			params.add("%" + materielCode.trim() + "%");
		}		
		hql.append(" order by p.hasChild desc,p.orderNum asc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}	
	public Page<ProductBom> searchProductByParams(Page<ProductBom> page,String type){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from ProductBom p where p.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String searchParameter = Struts2Utils.getParameter("searchParameters");
		if(searchParameter.isEmpty()){
			hql.append(" and p.bomParent is null");
		}
		if(StringUtils.isNotEmpty(type)){
			hql.append(" and p.materialType like '成品.%'");
		}
		hql.append(" order by p.hasChild desc,p.orderNum asc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	
	public Page<ProductBom> searchModelByParams(Page<ProductBom> page){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from ProductBom p where p.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String searchParameter = Struts2Utils.getParameter("searchParameters");
		if(searchParameter.isEmpty()){
			hql.append(" and p.bomParent.bomParent is null");
		}
		hql.append(" order by p.hasChild desc,p.orderNum asc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	
	public Page<ProductBom> searchModel(Page<ProductBom> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from ProductBom p where p.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and (p.materielCode like ? or p.materielName like ?)");
			params.add("%" + customSearch.trim() + "%");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId == null){
			hql.append(" and p.materielLevel = 3");
		}else{
			hql.append(" and (p.bomParent.bomParent.id = ? or p.bomParent.id = ?) and p.materielLevel = 3");
			params.add(parentId);
			params.add(parentId);
		}
		hql.append(" order by p.hasChild desc,p.orderNum asc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	
	/**
	 * 根据产品结构查询物料BOM
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductBom> searchProductBoms(JSONObject params){
		StringBuffer hql = new StringBuffer("from ProductBom p where p.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and p." + pro + " like ?");
				searchParams.add(params.getString(pro.toString()) + "%");
			}
		}
		hql.append(" order by p.materielCode");
		Query query = createQuery(hql.toString(),searchParams.toArray());
		query.setMaxResults(20);
		return query.list();
	}
	
	/**
	 * 根据产品结构查询物料BOM
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductBom> searchMaterials(JSONObject params){
		StringBuffer hql = new StringBuffer("from ProductBom p where p.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and p." + pro + " like ?");
				searchParams.add(params.getString(pro.toString()) + "%");
			}
		}
		hql.append(" order by p.materielCode");
		Query query = createQuery(hql.toString(),searchParams.toArray());
		query.setMaxResults(20);
		return query.list();
	}
	
	/**
	 * 根据产品结构获取顶级物料BOM
	 * @return
	 */
	public List<ProductBom> getProductBomParentsByStructure(Long productStructureId,JSONObject params){
		StringBuffer hql = new StringBuffer("from ProductBom p where p.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and p." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		hql.append(" order by p.materielCode");
		return find(hql.toString(),searchParams.toArray());
	}

	/**
	 * 根据父级名称查询物料BOM
	 * @return
	 */
	public List<ProductBom> searchProductBomByParent(Long parentId,JSONObject params){
		StringBuffer hql = new StringBuffer("from ProductBom p where 1=1 ");
		List<Object> searchParams = new ArrayList<Object>();
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and p." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}else{
			hql.append(" and p.bomParent.id = ? ");
			searchParams.add(parentId);
		}
		hql.append(" order by p.materielCode");
		return find(hql.toString(),searchParams.toArray());
	}
	
	public ProductBom getProductBomPrice(String code){
	    String hql="from ProductBom as p where p.materielCode=?";
	    List<ProductBom> li =this.find(hql, new Object[]{code});
	    if(!li.isEmpty()){
	        return li.get(0);
	    }else{
	        return null;
	    }
	        
	}
}

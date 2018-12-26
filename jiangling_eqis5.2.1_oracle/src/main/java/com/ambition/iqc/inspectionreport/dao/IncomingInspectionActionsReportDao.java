package com.ambition.iqc.inspectionreport.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.entity.ItemIndicator;
import com.ambition.supplier.utils.DateUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
* 检验报告DAO
* @authorBy wlongfeng
*
*/

@Repository
public class IncomingInspectionActionsReportDao extends HibernateDao<IncomingInspectionActionsReport, Long> {
	public Page<IncomingInspectionActionsReport> list(Page<IncomingInspectionActionsReport> page,String checkItem){
//		  String unitCodes = CommonUtil.getCurrentBusinessCodesForSearch();
		  String hql = "from IncomingInspectionActionsReport o where o.inspectionState != ? and o.hiddenState = 'N'";
//		  if(StringUtils.isNotEmpty(unitCodes)){
//			  hql += " where o.businessUnitCode in (" + unitCodes + ")";
//		  }
		  if(checkItem!=null){
			  hql += " and o.checkItems==null";
		  }
		  return searchPageByHql(page, hql,IncomingInspectionActionsReport.INPECTION_STATE_DEFAULT);
	}
/*	public Page<IncomingInspectionActionsReport> list(Page<IncomingInspectionActionsReport> page,String checkItem){
//		  String unitCodes = CommonUtil.getCurrentBusinessCodesForSearch();
		  String hql = "from IncomingInspectionActionsReport o";
//		  if(StringUtils.isNotEmpty(unitCodes)){
//			  hql += " where o.businessUnitCode in (" + unitCodes + ")";
//		  }
		  if(checkItem!=null){
			  hql += " and o.checkItems==null";
		  }
		  return searchPageByHql(page, hql);
	}*/
	public Page<IncomingInspectionActionsReport> itemIsNullList(Page<IncomingInspectionActionsReport>page,String startStr,String endStr){
////		String sql="select q.* from iqc_iiar as q left join iqc_check_item as item "+
////					"on q.id=item.fk_incoming_inspection_actions_report_id where "+
////					"item.fk_incoming_inspection_actions_report_id is null "; 
//		
//		//String hql = "select o from IncomingInspectionActionsReport o left join o.checkItems c where   c.incomingInspectionActionsReport.id is null ";
//		String hql="select o from IncomingInspectionActionsReport o where o.inspectionDate between ? and ? and o.companyId=? and o.inspectionState=? and o.sampleSchemeType not like ? and o.id not in (select i.incomingInspectionActionsReport.id from CheckItem i where i.createdTime >=?)";
//		if(StringUtils.isNotEmpty(unitCodes)){
//			  hql += " and o.businessUnitCode in (" + unitCodes + ") ";
//		  }
//		 // hql+=" and o.inspectionDate>= ? and o.inspectionDate<= ? and o.companyId=? and o.inspectionState=? and o.sampleSchemeType not like ?";
//		return  searchPageByHql(page, hql,DateUtil.parseDate(startStr),DateUtil.parseDate(endStr),ContextUtils.getCompanyId(),IncomingInspectionActionsReport.INPECTION_STATE_AUDIT,"%免检%",DateUtil.parseDate(startStr));
		String sql="select o.company_id,o.inspection_no ,o.request_check_no, o.business_unit_name,o.inspector,o.inspection_date,o.supplier_name,"+
		"o.check_bom_code,o.check_bom_name,o.model_specification,o.stock_amount,o.sample_scheme_type,o.inspection_amount,o.qualified_amount,"+ 
		"o.inspection_conclusion,o.processing_result,o.processing_receive_qty,o.approved_by,o.approved_time "+ 
		"from (select * from iqc_iiar o where o.inspection_date>= ? and o.inspection_date<= ? ";
		sql=sql+" and o.inspection_state=? and o.sample_scheme_type not like ?) o "+
		"left join IQC_CHECK_ITEM i on i.created_time > ? and o.id = i.fk_incoming_inspection_actions_report_id "+ 
		"where i.id is null";
		Query query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter(0, DateUtil.parseDate(startStr));
		query.setParameter(1, DateUtil.parseDate(endStr));
		query.setParameter(2, "已完成");
		query.setParameter(3, "%免检%");
		query.setParameter(4, DateUtil.parseDate(startStr));
		List list=query.list();
		List<IncomingInspectionActionsReport> incomingInspectionActionsReports=new ArrayList<IncomingInspectionActionsReport>();
		for(int i=0;i<list.size();i++){
			Object[] obj = (Object[])list.get(i);
			IncomingInspectionActionsReport iar=new IncomingInspectionActionsReport();
			iar.setCompanyId(Long.parseLong(obj[0].toString()));
			if(obj[1]!=null){
				iar.setInspectionNo(obj[1].toString());
			}
			if(obj[2]!=null){
			iar.setRequestCheckNo(obj[2].toString());
			}
			if(obj[3]!=null){
			iar.setBusinessUnitName(obj[3].toString());
			}
			if(obj[4]!=null){
			iar.setInspector(obj[4].toString());
			}
			if(obj[5]!=null){
			iar.setInspectionDate(DateUtil.parseDate(obj[5].toString()));
			}
			if(obj[6]!=null){
			iar.setSupplierName(obj[6].toString());
			}
			if(obj[7]!=null){
			iar.setCheckBomCode(obj[7].toString());
			}
			if(obj[8]!=null){
			iar.setCheckBomName(obj[8].toString());
			}
			if(obj[9]!=null){
			iar.setModelSpecification(obj[9].toString());
			}
			if(obj[10]!=null){
			iar.setStockAmount(Double.parseDouble(obj[10].toString()));
			}
			if(obj[11]!=null){
			iar.setSampleSchemeType(obj[11].toString());
			}
			if(obj[12]!=null){
			iar.setInspectionAmount(Double.parseDouble(obj[12].toString()));
			}
			if(obj[13]!=null){
			iar.setQualifiedAmount(Double.parseDouble(obj[13].toString()));
			}
			if(obj[14]!=null){
			iar.setInspectionConclusion(obj[14].toString());
			}
			if(obj[15]!=null){
			iar.setProcessingResult(obj[15].toString());
			}
			if(obj[16]!=null){
			iar.setProcessingReceiveQty(Double.parseDouble(obj[16].toString()));
			}
			if(obj[17]!=null){
			iar.setApprovedBy(obj[17].toString());
			}
			if(obj[18]!=null){
			iar.setApprovedTime(DateUtil.parseDate(obj[18].toString()));
			}
			incomingInspectionActionsReports.add(iar);
		}
		page.setResult(incomingInspectionActionsReports);
		return page;
	
	}
	
	public Page<IncomingInspectionActionsReport> listUnCheck(Page<IncomingInspectionActionsReport> page,String checkItem){
//		  String unitCodes = CommonUtil.getCurrentBusinessCodesForSearch();
		  String hql = "from IncomingInspectionActionsReport o where o.hiddenState= 'N' and o.inspectionState in ('" + IncomingInspectionActionsReport.INPECTION_STATE_DEFAULT + "')";
//		  if(StringUtils.isNotEmpty(unitCodes)){
//			  hql += " and o.businessUnitCode in (" + unitCodes + ")";
//		  }
		  if(checkItem!=null){
			  hql += " and o.checkItems==null";
		  }
		  return searchPageByHql(page, hql);
	}
	public Page<IncomingInspectionActionsReport> listReCheck(Page<IncomingInspectionActionsReport> page,String checkItem){
		  String hql = "from IncomingInspectionActionsReport o where o.hiddenState = 'N' and o.inspectionState in ('"
				 + IncomingInspectionActionsReport.INPECTION_STATE_RE_CHECK + "')";
		  if(checkItem!=null){
			  hql += " and o.checkItems==null";
		  }
		  return searchPageByHql(page, hql);
	}
	public Page<IncomingInspectionActionsReport> listChecked(Page<IncomingInspectionActionsReport> page,String checkItem){
//		  String unitCodes = CommonUtil.getCurrentBusinessCodesForSearch();
		  String hql = "from IncomingInspectionActionsReport o where o.inspectionState in ('" + IncomingInspectionActionsReport.INPECTION_STATE_AUDIT + "','"
				 + IncomingInspectionActionsReport.INPECTION_STATE_AUDIT + "')";
//		  if(StringUtils.isNotEmpty(unitCodes)){
//			  hql += " and o.businessUnitCode in (" + unitCodes + ")";
//		  }
		  if(checkItem!=null){
			  hql += " and o.checkItems==null";
		  }
		  return searchPageByHql(page, hql);
	}
	public Page<IncomingInspectionActionsReport> listWaitAudit(Page<IncomingInspectionActionsReport> page,String checkItem){
		  String hql = "from IncomingInspectionActionsReport o where o.hiddenState = 'N' and o.inspectionState = ?  ";
		  if(checkItem!=null){
			  hql += " and o.checkItems!=null";
		  }
		  return searchPageByHql(page, hql,IncomingInspectionActionsReport.INPECTION_STATE_SUBMIT);
	}
	public Page<IncomingInspectionActionsReport> listLastWaitAudit(Page<IncomingInspectionActionsReport> page,String checkItem){
		  String hql = "from IncomingInspectionActionsReport o where o.hiddenState = 'N' and o.inspectionState = ? ";
		  if(checkItem!=null){
			  hql += " and o.checkItems!=null";
		  }
		  return searchPageByHql(page, hql,IncomingInspectionActionsReport.INPECTION_STATE_LAST_SUBMIT);
	}
	public Page<IncomingInspectionActionsReport> listComplete(Page<IncomingInspectionActionsReport> page,String checkItem){
		  String hql = "from IncomingInspectionActionsReport o where o.inspectionState = ?";
		  if(checkItem!=null){
			  hql += " and o.checkItems==null";
		  }
		  return searchPageByHql(page, hql,IncomingInspectionActionsReport.INPECTION_STATE_AUDIT);
	}
	public Page<IncomingInspectionActionsReport> listDiscards(Page<IncomingInspectionActionsReport> page){
		  String hql = "from IncomingInspectionActionsReport o where o.hiddenState = 'N' and o.storageType = ? and o.processingResult = ?";
		  return searchPageByHql(page, hql,IncomingInspectionActionsReport.FORM_TYPE_BACK,"报废");
	}
	
	public Page<IncomingInspectionActionsReport> unlist(Page<IncomingInspectionActionsReport> page){
		 String hql = "from IncomingInspectionActionsReport o where o.hiddenState = 'N' and o.inspectionConclusion=? and o.inspectionState=? ";
		 return searchPageByHql(page, hql,"NG",IncomingInspectionActionsReport.INPECTION_STATE_AUDIT);
	}
	public Page<IncomingInspectionActionsReport> oklist(Page<IncomingInspectionActionsReport> page){
		 String hql = "from IncomingInspectionActionsReport o where o.hiddenState = 'N' and o.inspectionConclusion=? and o.inspectionState=? ";
		 return searchPageByHql(page, hql,"OK",IncomingInspectionActionsReport.INPECTION_STATE_AUDIT);
	}
	/**
	  * 方法名: 快速检索查询页面
	  * <p>功能说明：</p>
	  * @param page
	  * @return
	 */
	public Page<IncomingInspectionActionsReport> quickSelectList(Page<IncomingInspectionActionsReport> page){
		 String hql = "from IncomingInspectionActionsReport o where o.inspectionState in ('" + IncomingInspectionActionsReport.INPECTION_STATE_DEFAULT + "','"
				 + IncomingInspectionActionsReport.INPECTION_STATE_RE_CHECK + "')";
		 List<Object> searchParams = new ArrayList<Object>();
		 //如果查询参数是空的,则从会话的缓存中取
		 String searchParameters = Struts2Utils.getParameter("searchParameters");
		 if(searchParameters == null){
			 searchParameters = (String)Struts2Utils.getSession().getAttribute("searchParameters");
			 if(StringUtils.isNotEmpty(searchParameters)){
				 JSONArray array = JSONArray.fromObject(searchParameters);
				 for(Object obj : array){
					 JSONObject json = (JSONObject)obj;
						String propName = json.getString("propName");
						String propValue = json.getString("propValue");
						String optSign = json.getString("optSign");
						String dataType = json.getString("dataType");
						hql += " and o." + propName + " " + optSign + " ? ";
						if("like".equals(optSign)){
							searchParams.add("%" + propValue + "%");
						}else if("TIME".equals(dataType)){
							searchParams.add(DateUtil.parseDate(propValue,"yyyy-MM-dd HH:mm:ss"));
						}else{
							searchParams.add(propValue);
						}
				 }
			 }
		 }else{
			 Struts2Utils.getSession().setAttribute("searchParameters",searchParameters);
		 }
		 return searchPageByHql(page, hql,searchParams.toArray());
	}
	
	public List<IncomingInspectionActionsReport> getAllIncomingInspectionActionsReport(){
		 String hql = "from IncomingInspectionActionsReport o where o.companyId=?";
		return find(hql, ContextUtils.getCompanyId());
	}
	
	@SuppressWarnings("unchecked")
	public List<IncomingInspectionActionsReport> getAllSupplier(){
		String hql = "select distinct i.supplierCode from IncomingInspectionActionsReport i where i.companyId=?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, ContextUtils.getCompanyId());
		query.setMaxResults(10);
		return query.list();
	}
	@SuppressWarnings("unchecked")
	public List<IncomingInspectionActionsReport> getAllSupplierName(){
		String hql = "select distinct i.supplierName from IncomingInspectionActionsReport i where i.companyId=?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, ContextUtils.getCompanyId());
		query.setMaxResults(10);
		return query.list();
	}
	@SuppressWarnings("unchecked")
	public List<IncomingInspectionActionsReport> getAllMaterial(){
		String hql = "select distinct i.checkBomCode from IncomingInspectionActionsReport i where i.companyId=?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, ContextUtils.getCompanyId());
		query.setMaxResults(10);
		return query.list();
	}
	
	public List<IncomingInspectionActionsReport> getAllMaterialBySupplier(String dutySupplier){
		return find("select distinct i.checkBomCode from IncomingInspectionActionsReport i where i.companyId=? and i.supplierCode=?", new Object[]{ContextUtils.getCompanyId(),dutySupplier});
	}
	
	public List<IncomingInspectionActionsReport> getAllSupplierByMaterial(String materiel){
		return find("select distinct i.supplierCode from IncomingInspectionActionsReport i where i.companyId=? and i.checkBomCode=?", new Object[]{ContextUtils.getCompanyId(),materiel});
	}
	public List<IncomingInspectionActionsReport> getAllSupplierNameByMaterial(String materiel){
		return find("select distinct i.supplierName from IncomingInspectionActionsReport i where i.companyId=? and i.checkBomCode=?", new Object[]{ContextUtils.getCompanyId(),materiel});
	}
	public List<IncomingInspectionActionsReport> getIncomingInspectionActionsReportByBatchNo(String batchNo) {
        return find("from IncomingInspectionActionsReport i where i.batchNo=?",batchNo);
    }
	
	public Page<IncomingInspectionActionsReport> unsearch(Page<IncomingInspectionActionsReport> page) {
        return searchPageByHql(page, "from IncomingInspectionActionsReport i where i.inspectionConclusion=?","NG");
    }
	
	public Page<IncomingInspectionActionsReport> getQualifiedIncomingInspectionActionsReport(Page<IncomingInspectionActionsReport> page,Date startDate,Date endDate,String state,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		String hql="select i from IncomingInspectionActionsReport i,Supplier s where i.supplierCode=s.code and  i.companyId=? and i.inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(state);
		if(startDate!=null && endDate!=null){
			hql += " and i.inspectionDate>=? and i.inspectionDate<?"; 
			searchParams.add(new java.sql.Date(startDate.getTime()));
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}else if(startDate!=null && endDate==null){
			hql += " and i.inspectionDate>=?"; 
			searchParams.add(new java.sql.Date(startDate.getTime()));
		}else if(startDate==null && endDate!=null){
			hql += " and i.inspectionDate<?"; 
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}
		if(!materiel.equals("")){
			hql += " and i.checkBomName=?";
			searchParams.add(materiel);
		}
		if(!dutySupplier.equals("")){
			hql += " and i.supplierName=?";
			searchParams.add(dutySupplier);
		}	
		if(!checkBomMaterialType.equals("")){
			hql += " and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}	
//		hql += " group by i.inspectionNo";
		return findPage(page, hql, searchParams.toArray());
	}
	public Page<IncomingInspectionActionsReport> getQualifiedIncomingInspectionActionsReportByCheckBomName(Page<IncomingInspectionActionsReport> page,Date startDate,Date endDate,String state,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		String hql="select i from IncomingInspectionActionsReport i,Supplier s where i.supplierId=s.id and  i.companyId=? and i.inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(state);
		if(startDate!=null && endDate!=null){
			hql=hql+" and i.inspectionDate >= ? and i.inspectionDate < ?";
			searchParams.add(new java.util.Date(startDate.getTime()));
			searchParams.add(new java.util.Date(endDate.getTime()));
		}else if(startDate!=null && endDate==null){
			hql=hql+" and i.inspectionDate >= ?";
			searchParams.add(new java.util.Date(startDate.getTime()));
		}else if(startDate==null && endDate!=null){
			hql=hql+" and i.inspectionDate < ?";
			searchParams.add(new java.util.Date(endDate.getTime()));
		}
		if(!materiel.equals("")){
			hql=hql+" and i.checkBomName=?";
			searchParams.add(materiel);
		}
		if(!dutySupplier.equals("")){
			hql=hql+" and i.supplierCode=?";
			searchParams.add(dutySupplier);
		}	
		if(!checkBomMaterialType.equals("")){
			hql=hql+" and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}	
		return findPage(page, hql, searchParams.toArray());
	}
	public Page<IncomingInspectionActionsReport> getQualifiedIncomingInspectionActionsReportBySupplierName(Page<IncomingInspectionActionsReport> page,Date startDate,Date endDate,String state,String materiel,String dutySupplierName,String checkBomMaterialType,String importance){
		String hql="select i from IncomingInspectionActionsReport i,Supplier s where i.supplierId=s.id and  i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(startDate!=null){
			searchParams.add(new java.util.Date(startDate.getTime()));
		}
		if(endDate!=null){
			searchParams.add(new java.util.Date(endDate.getTime()));
		}
		searchParams.add(state);
		if(!materiel.equals("")){
			hql=hql+" and i.checkBomCode=?";
			searchParams.add(materiel);
		}
		if(!dutySupplierName.equals("")){
			hql=hql+" and i.supplierName=?";
			searchParams.add(dutySupplierName);
		}	
		if(!checkBomMaterialType.equals("")){
			hql=hql+" and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}	
		return findPage(page, hql, searchParams.toArray());
	}
	
	public Page<IncomingInspectionActionsReport> getUnQualifiedIncomingInspectionActionsReport(Page<IncomingInspectionActionsReport> page,Date startDate,Date endDate,String state,String processingResult,String inspector){
		String hql="from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(state);
		if(startDate!=null && endDate!=null){
			hql += " and i.inspectionDate >= ? and i.inspectionDate < ?";
			searchParams.add(new java.sql.Date(startDate.getTime()));
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}else if(startDate!=null && endDate!=null){
			hql += " and i.inspectionDate >= ?";
			searchParams.add(new java.sql.Date(startDate.getTime()));
		}else if(startDate!=null && endDate!=null){
			hql += " i.inspectionDate < ?";
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}
		if(!processingResult.equals("")){
			hql=hql+" and i.processingResult=?";
			searchParams.add(processingResult);
		}
		if(!inspector.equals("")){
			hql=hql+" and i.inspector=?";
			searchParams.add(inspector);
		}	
		return findPage(page, hql, searchParams.toArray());
	}
	
	public Page<IncomingInspectionActionsReport> getUnQualifiedIncomingInspectionActionsReportForEmpty(Page<IncomingInspectionActionsReport> page,Date startDate,Date endDate,String state,String processingResult,String inspector){
		String hql="from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(state);
		if(startDate!=null && endDate!=null){
			hql += " and i.inspectionDate >= ? and i.inspectionDate < ? ";
			searchParams.add(new java.sql.Date(startDate.getTime()));
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}else if(startDate!=null && endDate==null){
			hql += " and i.inspectionDate >= ? ";
			searchParams.add(new java.sql.Date(startDate.getTime()));
		}else if(startDate==null && endDate!=null){
			hql += " i.inspectionDate < ? ";
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}
		if(!processingResult.equals("")){
			hql=hql+" and i.processingResult=?";
			searchParams.add(processingResult);
		}else{
			hql +=" and (i.processingResult is null or i.processingResult='')";
		}
		if(!inspector.equals("")){
			hql=hql+" and i.inspector=?";
			searchParams.add(inspector);
		}	
		return findPage(page, hql, searchParams.toArray());
	}
	
	public Page<IncomingInspectionActionsReport> getUnQualifiedIncomingInspectionActionsReportByUseProducts(Page<IncomingInspectionActionsReport> page,Date startDate,Date endDate,String state,String processingResult,String useProducts){
		String hql="from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(state);
		if(startDate!=null && endDate!=null){
			hql += "and i.inspectionDate >= ? and i.inspectionDate < ? ";
			searchParams.add(new java.sql.Date(startDate.getTime()));
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}else if(startDate!=null && endDate==null){
			hql += "and i.inspectionDate >= ?";
			searchParams.add(new java.sql.Date(startDate.getTime()));
		}else if(startDate==null && endDate!=null){
			hql += "and i.inspectionDate < ? ";
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}
		if(!processingResult.equals("")){
			hql=hql+" and i.processingResult=?";
			searchParams.add(processingResult);
		}
		if(!useProducts.equals("")){
			hql=hql+" and i.usedProducts=?";
			searchParams.add(useProducts);
		}else{
			hql += " and (i.usedProducts is null or i.usedProducts = '')";
		}
		return findPage(page, hql, searchParams.toArray());
	}
	
	public Page<IncomingInspectionActionsReport> getUnQualifiedIncomingInspectionActionsReport(Page<IncomingInspectionActionsReport> page,Date startDate,Date endDate,String name){
		String hql="select distinct c.incomingInspectionActionsReport from CheckItem c where c.companyId=? and c.incomingInspectionActionsReport.inspectionDate >= ? and c.incomingInspectionActionsReport.inspectionDate < ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(new java.sql.Date(startDate.getTime()));
		searchParams.add(new java.sql.Date(endDate.getTime()));
		if(name != null){
			hql += " and c.conclusion='NG' and c.inspectionType=?";
			searchParams.add(name);
		}
		return findPage(page, hql, searchParams.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<IncomingInspectionActionsReport> getQualifiedIncomingInspectionActionsReport(Date startDate,Date endDate,String state,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		String hql="select s from IncomingInspectionActionsReport i,Supplier s  where i.supplierId=s.id and i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(new java.sql.Date(startDate.getTime()));
		searchParams.add(new java.sql.Date(endDate.getTime()));
		searchParams.add(state);
		if(!materiel.equals("")){
			hql=hql+" and i.checkBomCode=?";
			searchParams.add(materiel);
		}
		if(!dutySupplier.equals("")){
			hql=hql+" and i.supplierCode=?";
			searchParams.add(dutySupplier);
		}
		if(!checkBomMaterialType.equals("")){
			hql=hql+" and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}
		String isNewBom = Struts2Utils.getParameter("params.isNewBom");
		if(StringUtils.isNotEmpty(isNewBom)){
			hql += " and i.isNewBom = ?";
			searchParams.add(isNewBom);
		}
		Query query = this.getSession().createQuery(hql.toString());
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();	
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<IncomingInspectionActionsReport> getQualifiedIncomingInspectionActionsReportBySupplierName(Date startDate,Date endDate,String state,String materiel,String dutySupplierName,String checkBomMaterialType,String importance){
		String hql="select s from IncomingInspectionActionsReport i,Supplier s  where i.supplierId=s.id and i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(new java.sql.Date(startDate.getTime()));
		searchParams.add(new java.sql.Date(endDate.getTime()));
		searchParams.add(state);
		if(!materiel.equals("")){
			hql=hql+" and i.checkBomCode=?";
			searchParams.add(materiel);
		}
		if(!dutySupplierName.equals("")){
			hql=hql+" and i.supplierName=?";
			searchParams.add(dutySupplierName);
		}
		if(!checkBomMaterialType.equals("")){
			hql=hql+" and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}
		String isNewBom = Struts2Utils.getParameter("params.isNewBom");
		if(StringUtils.isNotEmpty(isNewBom)){
			hql += " and i.isNewBom = ?";
			searchParams.add(isNewBom);
		}
		Query query = this.getSession().createQuery(hql.toString());
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();	
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<IncomingInspectionActionsReport> getQualifiedIncomingInspectionActionsReport(Date startDate,Date endDate,String state,String materiel,String dutySupplier,String checkBomMaterialType,String importance,String defectLevel){
		String hql="select i from IncomingInspectionActionsReport i where  i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(new java.sql.Date(startDate.getTime()));
		searchParams.add(new java.sql.Date(endDate.getTime()));
		searchParams.add(state);
		if(!materiel.equals("")){
			hql=hql+" and i.checkBomCode=?";
			searchParams.add(materiel);
		}
		if(!dutySupplier.equals("")){
			hql=hql+" and i.supplierCode=?";
			searchParams.add(dutySupplier);
		}
		if(!checkBomMaterialType.equals("")){
			hql=hql+" and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}
		if(defectLevel!=null && !defectLevel.equals("")){
			hql=hql+" and i.defectLevel=?";
			searchParams.add(defectLevel);
		}
		Query query = this.getSession().createQuery(hql.toString());
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();	
		return list;
	}
	
	public List<IncomingInspectionActionsReport> getQualifiedIncomingInspectionActionsReport(Date startDate,Date endDate,String classification,String state){
		String isNewBom = Struts2Utils.getParameter("params.isNewBom");
		if(StringUtils.isNotEmpty(isNewBom)){
			if(classification != null && !classification.equals("")){
				return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.defectLevel=? and i.inspectionConclusion=? and i.isNewBom = ? and i.defectLevel is not null", ContextUtils.getCompanyId(),startDate,endDate,classification,state,isNewBom);
			}else{
				return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=? and i.isNewBom = ?", ContextUtils.getCompanyId(),startDate,endDate,state,isNewBom);
			}
		}else{
			if(classification != null && !classification.equals("")){
				return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.defectLevel=? and i.inspectionConclusion=? and i.defectLevel is not null", ContextUtils.getCompanyId(),startDate,endDate,classification,state);
			}else{
				return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=?", ContextUtils.getCompanyId(),startDate,endDate,state);
			}
		}
	}
	
	public List<IncomingInspectionActionsReport> getIncomingInspectionActionsReportByInspectionNo(String inspectionNo){
		return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionNo=?", ContextUtils.getCompanyId(),inspectionNo);
	}
	
	public Page<IncomingInspectionActionsReport> getAllIncomingInspectionActionsReport(Page<IncomingInspectionActionsReport> page,Date startDate,Date endDate,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		String hql="select i from IncomingInspectionActionsReport i ,Supplier s where i.supplierCode=s.code and i.companyId=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(startDate!=null && endDate!=null){
			hql += " and i.inspectionDate >= ? and i.inspectionDate < ?";
			searchParams.add(new java.sql.Date(startDate.getTime()));
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}else if(startDate!=null && endDate==null){
			hql += " and i.inspectionDate >= ?";
			searchParams.add(new java.sql.Date(startDate.getTime()));
		}else if(startDate==null && endDate!=null){
			hql += " and i.inspectionDate < ?";
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}
		if(!materiel.equals("")){
			hql += " and i.checkBomName = ?";
			searchParams.add(materiel);
		}
		if(!dutySupplier.equals("")){
			hql += " and i.supplierName = ?";
			searchParams.add(dutySupplier);
		}	
		if(!checkBomMaterialType.equals("")){
			hql += " and i.checkBomMaterialType = ?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql += " and s.importance = ?";
			searchParams.add(importance);
		}	
//		hql += " group by i.inspectionNo";
		return findPage(page, hql, searchParams.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<IncomingInspectionActionsReport> getAllIncomingInspectionActionsReport(Date startDate,Date endDate,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		String hql="select i from IncomingInspectionActionsReport i where  i.inspectionConclusion is not null and i.companyId=? and i.inspectionDate >= ? and i.inspectionDate< ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(new java.sql.Date(startDate.getTime()));
		searchParams.add(new java.sql.Date(endDate.getTime()));
		if(!materiel.equals("")){
			hql=hql+" and i.checkBomCode=?";
			searchParams.add(materiel);
		}
		if(!dutySupplier.equals("")){
			hql=hql+" and i.supplierCode=?";
			searchParams.add(dutySupplier);
		}
		if(!checkBomMaterialType.equals("")){
			hql=hql+" and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}
		String isNewBom = Struts2Utils.getParameter("params.isNewBom");
		if(StringUtils.isNotEmpty(isNewBom)){
			hql += " and i.isNewBom = ?";
			searchParams.add(isNewBom);
		}
		Query query = this.getSession().createQuery(hql.toString());
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<IncomingInspectionActionsReport> getAllIncomingInspectionActionsReportBySupplierName(Date startDate,Date endDate,String materiel,String supplierName,String checkBomMaterialType,String importance){
		String hql="select s from IncomingInspectionActionsReport i,Supplier s where i.supplierId=s.id and i.companyId=? and i.inspectionDate >= ? and i.inspectionDate< ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(new java.sql.Date(startDate.getTime()));
		searchParams.add(new java.sql.Date(endDate.getTime()));
		if(!materiel.equals("")){
			hql=hql+" and i.checkBomCode=?";
			searchParams.add(materiel);
		}
		if(!supplierName.equals("")){
			hql=hql+" and i.supplierName=?";
			searchParams.add(supplierName);
		}
		if(!checkBomMaterialType.equals("")){
			hql=hql+" and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}
		String isNewBom = Struts2Utils.getParameter("params.isNewBom");
		if(StringUtils.isNotEmpty(isNewBom)){
			hql += " and i.isNewBom = ?";
			searchParams.add(isNewBom);
		}
		Query query=null; 
		try {
			 query = this.getSession().createQuery(hql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		return list;
	}
	
	public List<IncomingInspectionActionsReport> getAllIncomingInspectionActionsReport(Date startDate,Date endDate){
		String isNewBom = Struts2Utils.getParameter("params.isNewBom");
		if(StringUtils.isNotEmpty(isNewBom)){
			return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.isNewBom = ? and i.inspectionConclusion is not null", ContextUtils.getCompanyId(),new java.sql.Date(startDate.getTime()),new java.sql.Date(endDate.getTime()),isNewBom);
		}else{
			return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion is not null", ContextUtils.getCompanyId(),new java.sql.Date(startDate.getTime()),new java.sql.Date(endDate.getTime()));
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getAllCheckType(Date startDate,Date endDate){
		String sql = "select t.a,count(t.b) as total from " +
				     "(select item.inspection_type a,i.batch_no b from IQC_IIAR i join IQC_CHECK_ITEM item on i.id = item.FK_INCOMING_INSPECTION_ACTIONS_REPORT_ID and item.conclusion='NG'" +
				     "and i.company_id=? and i.inspection_date>=? and i.inspection_date<? group by a,b) as t " +
				     "group by t.a order by total desc";
		Query query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter(0, ContextUtils.getCompanyId());
		query.setParameter(1, new java.sql.Date(startDate.getTime()));
		query.setParameter(2, new java.sql.Date(endDate.getTime()));
		query.setMaxResults(15);
		return query.list();
	}
	
	//发起预警
	public List<IncomingInspectionActionsReport> getAllIncomingInspectionActionsReportByItems(Date startDate,Date endDate,String supplierName,String checkBomName,String checkItemName){
		return find("from IncomingInspectionActionsReport i left join i.checkItems item on item.conclusion=? where where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.supplierName=? and i.checkBomName=? and item.checkItemName=? and i.inspectionConclusion=?", "NG",ContextUtils.getCompanyId(),startDate,endDate,supplierName,checkBomName,checkItemName,"NG");
	}
	
	//发起改进
	public List<IncomingInspectionActionsReport> getAllIncomingInspectionActionsReportByOrder(Date startDate,Date endDate,String supplierName,String checkBomName){
		return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<=? and i.supplierName=? and i.checkBomName=? and i.inspectionConclusion=?", ContextUtils.getCompanyId(),startDate,endDate,supplierName,checkBomName,"NG");
	}
	
	public void decreaseIndex(Integer start, Integer end) {
        createQuery("update GoalItem goalItem set goalItem.displayIndex=goalItem.displayIndex-1 where goalItem.displayIndex>? and goalItem.displayIndex<=?",
            start,end).executeUpdate();
	}

	public void increaseIndex(Integer start, Integer end) {
	    createQuery("update GoalItem goalItem set goalItem.displayIndex=goalItem.displayIndex+1 where goalItem.displayIndex>=? and goalItem.displayIndex<?",
	            start,end).executeUpdate();
	}
	
	public void updateIndex(Integer originalIndex, Integer newIndex) {
	    createQuery("update GoalItem goalItem set goalItem.displayIndex=? where goalItem.displayIndex=?",
	            newIndex,originalIndex).executeUpdate();
	}
	
	@SuppressWarnings("unused")
	public Page<IncomingInspectionActionsReport> queryIinspectionReportDetail(Page<IncomingInspectionActionsReport> page, JSONObject params) {
		String type = (String)params.get("type");
		String myType = (String)params.get("myType");
		String inspectionPoint="";
		if(type!=null){
			inspectionPoint=(String)params.get("type");
		}else{
			inspectionPoint=(String)params.get("inspectionPoint");
		}
		String inspectionDate=(String)params.get("inspectionDate");
		String searchname=(String)params.get("searchname");
		String materiel=(String)params.get("itemdutyPart_equals");
		String dutySupplier=(String)params.get("dutySupplier");
		String checkBomMaterialType=(String)params.get("checkBomMaterialType");
		String importance=(String)params.get("importance");
		String checkItemName=(String)params.get("checkItemName");
		String otherstartDateString=(String)params.get("startDate_ge_date");
		String otherendDateString=(String)params.get("endDate_le_date");
		String destartDateString=(String)params.get("startDate_ge_date");
		String deendDateString=(String)params.get("endDate_le_date");
		
		if(materiel==null){
			materiel="";
		}
		if(dutySupplier==null){
			dutySupplier="";
		}
		if(checkBomMaterialType==null){
			checkBomMaterialType="";
		}
		if(importance==null){
			importance="";
		}
		String state=(String)params.get("state");
		if(state==null){
			state="limit";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		Date startDate = new Date();
		Date endDate = new Date();
		Date otherstartDate=new Date();
		Date otherendDate=new Date();
		Date destartDate=new Date();
		Date deendDate=new Date();
		try {
			if(otherstartDateString!=null && !"month".equals(myType) && !"week".equals(myType)){
				otherstartDate = sdf.parse(otherstartDateString+" 00:00:00");
			}
			if(otherendDateString!=null && !"month".equals(myType) && !"week".equals(myType)){
				otherendDate = sdf.parse(otherendDateString+" 23:59:59");
			}
			if(destartDateString!=null && !"month".equals(myType) && !"week".equals(myType)){
				destartDate = sdf.parse(destartDateString+" 00:00:00");
			}
			if(deendDateString!=null && !"month".equals(myType) && !"week".equals(myType)){
				deendDate = sdf.parse(deendDateString+" 23:59:59");
			}
			if(state.equals("one")||state.equals("all")){
				if(inspectionDate!=null && !"month".equals(myType) && !"week".equals(myType)){
					startDate = sdf.parse(inspectionDate+" 00:00:00");
					endCal.setTime(startDate);
					endCal.set(Calendar.HOUR_OF_DAY,23);
					endCal.set(Calendar.MINUTE,59);
					endCal.set(Calendar.SECOND,59);
					endDate = endCal.getTime();
				}else if("month".equals(myType)){
					int year = 0, month = 0;
					year =  Integer.valueOf(inspectionDate.substring(0, 4));
					month = Integer.valueOf(inspectionDate.substring(5));
					
					startCal.set(Calendar.YEAR,year);
					startCal.set(Calendar.MONTH,month-1);
					startCal.set(Calendar.DATE,1);
					startCal.set(Calendar.HOUR_OF_DAY,0);
					startCal.set(Calendar.MINUTE,0);
					startCal.set(Calendar.SECOND,0);
					
					endCal.set(Calendar.YEAR,year);
					endCal.set(Calendar.MONTH,month);
					endCal.set(Calendar.DATE,1);
					endCal.add(Calendar.DATE,-1);
					endCal.set(Calendar.HOUR_OF_DAY,23);
					endCal.set(Calendar.MINUTE,59);
					endCal.set(Calendar.SECOND,59);
					startDate = startCal.getTime();
					endDate = endCal.getTime();
					
					otherstartDate = startDate;
					destartDate = startDate;
					otherendDate = endDate;
					deendDate = endDate;
				}else if("week".equals(myType)){
					int startyear = 0, startweek = 0, endyear = 0, endweek = 0;
					startyear = Integer.valueOf(params.get("year_ge").toString());
					endyear = Integer.valueOf(params.get("year_le").toString());
					if(params.get("week_ge")!=null){
						startweek = Integer.valueOf(params.get("week_ge").toString());
					}
					if(params.get("week_le")!=null){
						endweek = Integer.valueOf(params.get("week_le").toString());
					}
					startCal = Calendar.getInstance();
					startCal.set(Calendar.YEAR,startyear);     
					startCal.set(Calendar.WEEK_OF_YEAR,startweek);
					startCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					startCal.setFirstDayOfWeek(Calendar.MONDAY);
					startCal.set(Calendar.HOUR_OF_DAY,0);
					startCal.set(Calendar.MINUTE,0);
					startCal.set(Calendar.SECOND,0);
					startDate = startCal.getTime();
					
					endCal = Calendar.getInstance();
					startCal.set(Calendar.YEAR,endyear);     
					startCal.set(Calendar.WEEK_OF_YEAR,endweek);
					startCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					startCal.setFirstDayOfWeek(Calendar.MONDAY);
					endCal.add(Calendar.DATE, 6);
					endCal.set(Calendar.HOUR_OF_DAY,23);
					endCal.set(Calendar.MINUTE,59);
					endCal.set(Calendar.SECOND,59);
					endDate = endCal.getTime();
					
					otherstartDate = startDate;
					destartDate = startDate;
					otherendDate = endDate;
					deendDate = endDate;
				}
			}else{
				if(!"month".equals(myType) && !"week".equals(myType)){
					startDate = sdf.parse(params.get("startDate_ge_date").toString()+" 00:00:00");
					endDate = sdf.parse(params.get("endDate_le_date").toString()+" 23:59:59");
					otherstartDate = startDate;
					destartDate = startDate;
					otherendDate = endDate;
					deendDate = endDate;
				}else if("month".equals(myType)){
					int startyear = 0, startmonth = 0, endyear = 0, endmonth = 0;
					startyear =  Integer.valueOf(params.get("startDate_ge_date").toString().substring(0, 4));
					startmonth = Integer.valueOf(params.get("startDate_ge_date").toString().substring(5, 7));
					endyear = Integer.valueOf(params.get("endDate_le_date").toString().substring(0, 4));
					endmonth = Integer.valueOf(params.get("endDate_le_date").toString().substring(5, 7));
					
					startCal.set(Calendar.YEAR,startyear);
					startCal.set(Calendar.MONTH,startmonth-1);
					startCal.set(Calendar.DATE,1);
					startCal.set(Calendar.HOUR_OF_DAY,0);
					startCal.set(Calendar.MINUTE,0);
					startCal.set(Calendar.SECOND,0);
					
					endCal.set(Calendar.YEAR,endyear);
					endCal.set(Calendar.MONTH,endmonth);
					endCal.set(Calendar.DATE,1);
					endCal.add(Calendar.DATE,-1);
					endCal.set(Calendar.HOUR_OF_DAY,23);
					endCal.set(Calendar.MINUTE,59);
					endCal.set(Calendar.SECOND,59);
					startDate = startCal.getTime();
					endDate = endCal.getTime();
					otherstartDate = startDate;
					destartDate = startDate;
					otherendDate = endDate;
					deendDate = endDate;
				}else if("week".equals(myType)){
					int startyear = 0, startweek = 0, endyear = 0, endweek = 0;
					startyear = Integer.valueOf(params.get("year_ge").toString());
					endyear = Integer.valueOf(params.get("year_le").toString());
					if(params.get("week_ge")!=null){
						startweek = Integer.valueOf(params.get("week_ge").toString());
					}
					if(params.get("week_le")!=null){
						endweek = Integer.valueOf(params.get("week_le").toString());
					}
					startCal = Calendar.getInstance();
					startCal.set(Calendar.YEAR,startyear);     
					startCal.set(Calendar.WEEK_OF_YEAR,startweek);
					startCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					startCal.setFirstDayOfWeek(Calendar.MONDAY);
					startCal.set(Calendar.HOUR_OF_DAY,0);
					startCal.set(Calendar.MINUTE,0);
					startCal.set(Calendar.SECOND,0);
					startDate = startCal.getTime();
					
					endCal = Calendar.getInstance();
					endCal.set(Calendar.YEAR,endyear);     
					endCal.set(Calendar.WEEK_OF_YEAR,endweek);
					endCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					endCal.setFirstDayOfWeek(Calendar.MONDAY);
					endCal.add(Calendar.DATE, 6);
					endCal.set(Calendar.HOUR_OF_DAY,23);
					endCal.set(Calendar.MINUTE,59);
					endCal.set(Calendar.SECOND,59);
					endDate = endCal.getTime();
					
					otherstartDate = startDate;
					destartDate = startDate;
					otherendDate = endDate;
					deendDate = endDate;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<IncomingInspectionActionsReport> incomingInspectionActionsReport = new ArrayList<IncomingInspectionActionsReport>();
		if(inspectionPoint.equals("检查批数")&&state.equals("one")){
			page= findPage(page,"from IncomingInspectionActionsReport incomingInspectionActionsReport where companyId=? and inspectionDate>=? and inspectionDate<? ", ContextUtils.getCompanyId(),startDate,endDate);
		}
		if(inspectionPoint.equals("合格批数")&&state.equals("one")){
			page= findPage(page,"from IncomingInspectionActionsReport incomingInspectionActionsReport where companyId=? and inspectionDate>=? and inspectionDate<? and inspectionConclusion=? ", ContextUtils.getCompanyId(),startDate,endDate,"OK");	
		}
		if(inspectionPoint.equals("检验批数")&&state.equals("all")){
			try {
				materiel = (String)params.get("itemdutyPartCode_equals");
				if(materiel==null){
					materiel = "";
				}
				dutySupplier=(String)params.get("dutySupplierCode");
				if(dutySupplier==null){
					dutySupplier="";
				}
				page= getAllIncomingInspectionActionsReport(page,startDate,endDate,materiel,dutySupplier,checkBomMaterialType,importance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(inspectionPoint.equals("合格批数")&&state.equals("all")){
			materiel = (String)params.get("itemdutyPartCode_equals");
			if(materiel==null){
				materiel = "";
			}
			dutySupplier=(String)params.get("dutySupplierCode");
			if(dutySupplier==null){
				dutySupplier="";
			}
			page = getQualifiedIncomingInspectionActionsReport(page,startDate,endDate,"OK",materiel,dutySupplier,checkBomMaterialType,importance);
		}
		if(inspectionPoint.equals("检查批数")&&state.equals("supplier")){
			materiel=params.get("materiel").toString();
			page= getAllIncomingInspectionActionsReport(page,otherstartDate,otherendDate,materiel,dutySupplier,"","");	
		}
		if(inspectionPoint.equals("合格批数")&&state.equals("supplier")){
			materiel=params.get("materiel").toString();
			page= getQualifiedIncomingInspectionActionsReport(page,otherstartDate,otherendDate,"OK",materiel,dutySupplier,"","");
		}
		if(inspectionPoint.equals("检查批数")&&state.equals("material")){
			page= getAllIncomingInspectionActionsReport(page,otherstartDate,otherendDate,materiel,dutySupplier,"","");	
		}
		if(inspectionPoint.equals("合格批数")&&state.equals("material")){
			page= getQualifiedIncomingInspectionActionsReport(page,otherstartDate,otherendDate,"OK",materiel,dutySupplier,"","");
		}
		//不合格
		if(inspectionPoint.equals("materiel")){
			page= getQualifiedIncomingInspectionActionsReport(page,destartDate,deendDate,"NG",searchname,"","","");
		}
		if(inspectionPoint.equals("supplier")){
			page= getQualifiedIncomingInspectionActionsReportBySupplierName(page,destartDate,deendDate,"NG","",searchname,"","");
		}
		if(inspectionPoint.equals("result")){
			page= getUnQualifiedIncomingInspectionActionsReportForEmpty(page,destartDate,deendDate,"NG",searchname,"");
		}
		if(inspectionPoint.equals("inspector")){
			page= getUnQualifiedIncomingInspectionActionsReport(page,destartDate,deendDate,"NG","",searchname);
		}
		if(inspectionPoint.equals("usedProducts")){
			page= getUnQualifiedIncomingInspectionActionsReportByUseProducts(page,destartDate,deendDate,"NG","",searchname);
		}
		if(inspectionPoint.equals("checkBomName")){
			page= getQualifiedIncomingInspectionActionsReportByCheckBomName(page,destartDate,deendDate,"NG",searchname,"","","");
		}
		if(inspectionPoint.equals("checkBomCode")){
			page= getQualifiedIncomingInspectionActionsReport(page,destartDate,deendDate,"NG",searchname,"","","");
		}
		if(inspectionPoint.equals("checkType")){
			searchname = checkItemName;
			page= getUnQualifiedIncomingInspectionActionsReport(page,destartDate,deendDate,searchname);
		}
		return page;
	}
	
	public Page<IncomingInspectionActionsReport> search(Page<IncomingInspectionActionsReport> page) {
	    return searchPageByHql(page, "from IncomingInspectionActionsReport incomingInspectionActionsReport");
	}

	public List<IncomingInspectionActionsReport> getQualifiedIncomingInspectionActionsReportByResult(Date startDate, Date endDate, String state, String processingResult) {
		return find("from IncomingInspectionActionsReport incomingInspectionActionsReport where companyId=? and inspectionDate>=? and inspectionDate<? and inspectionConclusion=? and processingResult=? ", ContextUtils.getCompanyId(),startDate,endDate,state,processingResult);
	}
	
	public List<IncomingInspectionActionsReport> getQualifiedIncomingInspectionActionsReportByResult(Date startDate, Date endDate, String state, String processingResult, String classification) {
		if(classification != null && !classification.equals("")){
			return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=? and i.processingResult=? and i.defectLevel=?", ContextUtils.getCompanyId(),startDate,endDate,state,processingResult,classification);
		}else{
			return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=? and i.processingResult=?", ContextUtils.getCompanyId(),startDate,endDate,state,processingResult);
		}
	}
	
	public List<IncomingInspectionActionsReport> getQualifiedIncomingInspectionActionsReportByInspector(Date startDate, Date endDate, String state, String inspector) {
		return find("from IncomingInspectionActionsReport incomingInspectionActionsReport where companyId=? and inspectionDate>=? and inspectionDate<? and inspectionConclusion=? and inspector=?", ContextUtils.getCompanyId(),startDate,endDate,state,inspector);
	}
	
	public List<IncomingInspectionActionsReport> getQualifiedIncomingInspectionActionsReportByInspector(Date startDate, Date endDate, String state, String inspector, String classification) {
		if(classification != null && !classification.equals("")){
			return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=? and i.inspector=? and i.defectLevel=?", ContextUtils.getCompanyId(),startDate,endDate,state,inspector,classification);
		}else{
			return find("from IncomingInspectionActionsReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=? and i.inspector=?", ContextUtils.getCompanyId(),startDate,endDate,state,inspector);
		}
	}
	public List<IncomingInspectionActionsReport> getListByInspectingItemIndicator(
			Date startDate, Date endDate,
			ItemIndicator inspectingItemIndicator) {
		String hql = "from IncomingInspectionActionsReport a where a.companyId = ? and a.checkBomCode=? and a.inspectionDate >= ? and a.inspectionDate <= ? ";
		return this.find(hql, new Object[]{ContextUtils.getCompanyId(),inspectingItemIndicator.getInspectingIndicator().getMaterielCode(),  startDate, endDate});
	}
	public Page<IncomingInspectionActionsReport> listHid(Page<IncomingInspectionActionsReport> page){
		  String hql = "from IncomingInspectionActionsReport o where o.hiddenState = 'Y'";
		  return searchPageByHql(page, hql);
	}
	@SuppressWarnings("unchecked")
	public List<IncomingInspectionActionsReport> getSupplierAndCode(){
		String hql = "select distinct i.supplierName,i.supplierCode,i.checkBomName,i.checkBomCode from IncomingInspectionActionsReport i where i.companyId=? ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, ContextUtils.getCompanyId());
		return query.list();
	}
}
	

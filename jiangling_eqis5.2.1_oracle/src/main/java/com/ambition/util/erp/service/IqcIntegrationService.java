package com.ambition.util.erp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.entity.TestFrequency;
import com.ambition.util.common.DateUtil;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.PropUtils;

/**
 * 类名:来料检验单
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：处理QIS与ERP系统来料检验单信息的整合</p>
 * @author  LPF
 * @version 1.00 2013-8-19 发布
 */
@Service
public class IqcIntegrationService implements IntegrationService,Runnable {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private ProductBomDao bomDao;       
	@Resource(name="ordbDataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Override
	public void run() {
		try{
			/*ThreadParameters threadParameters = new ThreadParameters();
			threadParameters.setLoginName("ofilm.systemAdmin");
			threadParameters.setPageNumber(1);
			threadParameters.setUserName("systemAdmin");
			threadParameters.setUserId(2227L);
			threadParameters.setCompanyId(2224L);
			ParameterUtils.setParameters(threadParameters);*/
			beginIntegration(ScheduleJob.getScheduleCompanyId(),ScheduleJob.getScheduleUserName());
		}catch (Exception e) {
			log.error("自动更新来料检验单失败!",e);
		}
		ScheduleJob
		.addSchedule(this,ScheduleJob.getIqcScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 导入物料
	 */
	@SuppressWarnings("unchecked")
	public void beginIntegration(Long companyId,String userLoginName){
		if(integrationFlag){
			return;
		}
		integrationFlag = true;
		Session session = null;
		Transaction transaction = null;
		//导入
		System.out.println("begin");
		try {
			//生成销售数据
			String tableName = "iqc";
			session = bomDao.getSessionFactory().openSession();
			Query query = session.createQuery("from UpdateTimestamp u where u.tableName = ?");
			query.setParameter(0,tableName);
			List<UpdateTimestamp> updateStamp = query.list();
			Date updateTime = null;
			/*Date updateTime1 = DateUtil.parseDate("2015-02-01");
			Date updateTime2 = DateUtil.parseDate("2015-06-10");*/
			Date saveDate = null;
			if(!updateStamp.isEmpty()){
				updateTime = updateStamp.get(0).getUpdateTime();
				saveDate = updateTime;
//				//提前一周
//				Calendar calendar = Calendar.getInstance();
//				calendar.setTime(updateTime);
//				calendar.add(Calendar.DATE,-3);
//				updateTime = calendar.getTime();
			}else{
				updateTime = DateUtil.parseDate("2016-11-29");
				saveDate = updateTime;
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//String companyName = ContextUtils.getCompanyName();
			String companyName =PropUtils.getProp("companyName");
			System.out.println("companyName:"+companyName);
			List<Object> search = new ArrayList<Object>();
			//从接口查询符合条件的销售数据
			String sql = "SELECT godownentrynumber as F_NUMBER,PO_NO as F_PO_NO,entryid as F_ENTRY_ID,suppliercode as F_SUPPLIER_CODE,suppliername as F_SUPPLIER_NAME, materialcode as F_MATERIAL_CODE,materialname as F_MATERIAL_NAME,enterdate as F_ENTER_DATE," 
						+ "units as F_UNITS,amount as F_AMOUNT,To_Organization_Id as F_ORGANIZATION_ID,Organization_Name as F_ORGANIZATION_NAME,inorganization as F_INORGANIZATION,modelspecification as F_MODEL_SPRCIFICATION,purchaseman as F_PURCHASE_MAN,purchaseno as F_PURCHASE_NO, "
						+ " Line_Id as F_LINE_ID,LAST_UPDATE_DATE as LAST_UPDATE_DATE FROM apps.xxqis_inv_iqc_inspection_v where LAST_UPDATE_DATE >? ";
			if("CCM".equals(companyName)){
				sql += " and (inorganization=? or inorganization=? or inorganization=? ) order by LAST_UPDATE_DATE asc";
				search.add(saveDate);
				search.add("216");
				search.add("297");
				search.add("228");
			}else if("SQ".equals(companyName)){
				sql += " and inorganization=?  order by LAST_UPDATE_DATE asc";
				search.add(saveDate);
				search.add("282");
			}else if("FPM".equals(companyName)){
				sql += " and (inorganization=? or inorganization=? or inorganization=? or inorganization=? or inorganization=? or inorganization=? or inorganization=? )  order by LAST_UPDATE_DATE asc";
				search.add(saveDate);
				search.add("296");
				search.add("211");
				search.add("298");
				search.add("209");
				search.add("581");
				search.add("582");
				search.add("299");
			}
			/*String sql = "SELECT v.godownentrynumber as F_NUMBER,v.PO_NO as F_PO_NO,v.entryid as F_ENTRY_ID,v.suppliercode as F_SUPPLIER_CODE,v.suppliername as F_SUPPLIER_NAME, v.materialcode as F_MATERIAL_CODE,v.materialname as F_MATERIAL_NAME,v.enterdate as F_ENTER_DATE," 
					+ "v.units as F_UNITS,v.amount as F_AMOUNT,v.To_Organization_Id as F_ORGANIZATION_ID,v.Organization_Name as F_ORGANIZATION_NAME,v.inorganization as F_INORGANIZATION,v.modelspecification as F_MODEL_SPRCIFICATION,v.purchaseman as F_PURCHASE_MAN,v.purchaseno as F_PURCHASE_NO, "
					+ " v.Line_Id as F_LINE_ID,v.LAST_UPDATE_DATE as LAST_UPDATE_DATE,m.cate_code as F_ITEM_TYPE_CODE,m.cate_desc as F_ITEM_TYPE,m.description as F_NAME,m.SEGMENT1 as F_CODE FROM apps.xxqis_inv_iqc_inspection_v  v inner join apps.XXQIS_ITEM_V m on v.materialcode=m.SEGMENT1 and  v.LAST_UPDATE_DATE >? ";
			if("CCM".equals(companyName)){
				sql += " and v.inorganization in('216','297','228') order by  v.LAST_UPDATE_DATE asc";
				search.add(saveDate);
			}else if("SQ".equals(companyName)){
				sql += " and v.inorganization in('282')  order by  v.LAST_UPDATE_DATE asc";
				search.add(saveDate);
			}else if("LCM".equals(companyName)){
				sql += " and v.inorganization in('296','211','298','209','581','582','299')  order by  v.LAST_UPDATE_DATE asc";
				search.add(saveDate);
			}*/
			List<?> bomList = jdbcTemplate.queryForList(sql,search.toArray());
			//List<?> bomList = jdbcTemplate.queryForList(sql);
			int record = 0;
			int rsize=bomList.size();
			for(Object obj : bomList){
				record++;
				//System.out.println(formatter.format(updateTime)+"到"+formatter.format(new Date())+":"+record+"/"+rsize+"----------------");
				Map<String,Object> map = (Map<String,Object>)obj;
				//String materialTypeCode = (String)map.get("F_ITEM_TYPE_CODE");//物料类别代码
				//String materialType = (String)map.get("F_ITEM_TYPE");//物料类别代码
				//String code = (String)map.get("F_CODE");//物料编号
				//String name = (String)map.get("F_NAME");//物料说明
				String godownentrynumber = (String)map.get("F_NUMBER");//接收单号
				String suppliercode = (String)map.get("F_SUPPLIER_CODE");//供应商编码
				String suppliername = (String)map.get("F_SUPPLIER_NAME");//供应商名称				
				String entryid = (String)map.get("F_ENTRY_ID").toString();//分录号
				String materialcode = (String)map.get("F_MATERIAL_CODE");//物料编码
				String materialname = (String)map.get("F_MATERIAL_NAME");//物料名称
				//String enterdate = (String)map.get("F_ENTER_DATE");//到货日期
				String unit =(String)map.get("F_UNIT");//单位
				//String amount =(String)map.get("F_AMOUNT");//进料数量
				//Long organizationId =(Long)map.get("F_ORGANIZATION_ID");//组织id
				String organizationName =(String)map.get("F_ORGANIZATION_NAME");//组织名称
				String inorganization =(String)map.get("F_INORGANIZATION");//组织代码
				String modelspecification =(String)map.get("F_MODEL_SPRCIFICATION");//物料型号
				String purchaseman = (String)map.get("F_PURCHASE_MAN");//采购员
				String purchaseno = (String)map.get("F_PURCHASE_NO").toString();//采购行号
				String orderNo = (String)map.get("F_PO_NO").toString();//订单号
				String lineId = map.get("F_LINE_ID").toString();//行号
				//Date lastUpdateTime = (Date)map.get("LAST_UPDATE_DATE");//日期
				if("SQ".equals(companyName)){
					String existHqlBom = "from ProductBomInspection s where s.materielCode=? ";
					List<?> listBom = session.createQuery(existHqlBom)
							.setParameter(0,materialcode)
							.list();
					if(listBom.size()==0){
						continue;
					}
				}
				IncomingInspectionActionsReport report = null;
				String inspectionNo = formCodeGenerated.generateIncomingInspectionRecordCode(session,companyId);
				String existHql = "from IncomingInspectionActionsReport s where s.acceptNo=? and s.businessUnitCode=?";
				List<?> list = session.createQuery(existHql)
				.setParameter(0,map.get("F_LINE_ID").toString())
				.setParameter(1,map.get("F_INORGANIZATION").toString())
				.list();
				if(list.isEmpty()){
					report = new IncomingInspectionActionsReport();
					report.setCreatedTime(new Date());
					report.setCreator(userLoginName);
					report.setSubCompanyId(companyId);
					report.setCompanyId(companyId);
					report.setInspectionNo(inspectionNo);
					report.setOrderNo(orderNo);
					report.setAcceptNo(lineId);
					report.setModifiedTime(new Date());				
					report.setModifier(userLoginName);
					report.setEntryId(entryid);
					report.setErpInspectionNo(godownentrynumber);
					report.setSupplierName(suppliername);
					report.setSupplierCode(suppliercode);
					//report.setMaterialType(materialType);
					//report.setMaterialTypeCode(materialTypeCode);
					report.setCheckBomCode(materialcode.trim());
					report.setCheckBomName(materialname);
					report.setEnterDate((Date)map.get("F_ENTER_DATE"));
					report.setUnits(unit);
					report.setSubCompanyId(Long.valueOf(map.get("F_ORGANIZATION_ID").toString()));
					report.setErpStockAmount(Double.valueOf(map.get("F_AMOUNT").toString()));
					report.setStockAmount(Double.valueOf(map.get("F_AMOUNT").toString()));
					report.setBusinessUnitName(organizationName);
					report.setBusinessUnitCode(inorganization);
					report.setModelSpecification(modelspecification);
					if("CCM".equals(companyName)){
						if(inorganization.equals("216")){
							report.setProcessSection("下罗IQC");
						}else if(inorganization.equals("297")){
							report.setProcessSection("高新IQC");
						}else if(inorganization.equals("228")){
							report.setProcessSection("培训中心IQC");
						}
					}
				}else{
					continue;
				}
								
				TestFrequency testFrequency=null;
				String existHql2 = "from TestFrequency s where s.checkBomCode=? and s.supplierCode=? ";
				List<?> list2 = session.createQuery(existHql2)
				.setParameter(0,materialcode.trim())
				.setParameter(1,suppliercode)
				.list();
				if(list2.isEmpty()){
					testFrequency = new TestFrequency();
					testFrequency.setCreatedTime(new Date());
					testFrequency.setCreator(userLoginName);
					testFrequency.setSubCompanyId(companyId);
					testFrequency.setCompanyId(companyId);
					testFrequency.setCheckBomCode(materialcode.trim());
					testFrequency.setCheckBomName(materialname);
					testFrequency.setSupplierCode(suppliercode);
					testFrequency.setSupplierName(suppliername);
					testFrequency.setIsErp("是");
				}	
				
				//每次一个事务避免长时间锁住这张表影响前台的查询显示
				transaction = session.beginTransaction();
				session.save(report);
				if(testFrequency!=null){
					session.save(testFrequency);
				}
				transaction.commit();
			    session.flush();
	            session.clear();
	            Date lastUpdateDate = (Date)map.get("LAST_UPDATE_DATE");
				if(lastUpdateDate != null 
						&& lastUpdateDate.getTime()>saveDate.getTime()){
					saveDate = lastUpdateDate;
				}
			}
			query = session.createQuery("from UpdateTimestamp u where u.tableName = ?");
			query.setParameter(0,tableName);
			updateStamp = query.list();
			UpdateTimestamp updateTimestamp = null;
			if(updateStamp.isEmpty()){
				updateTimestamp = new UpdateTimestamp();
				updateTimestamp.setTableName(tableName);
				updateTimestamp.setCompanyId(companyId);
			}else{
				updateTimestamp = (UpdateTimestamp)updateStamp.get(0);
			}
			updateTimestamp.setLastModifier(userLoginName);
			updateTimestamp.setLastModifiedTime(new Date());
			updateTimestamp.setUpdateTime(saveDate);
			//保存变更信息
			transaction = session.beginTransaction();
			session.save(updateTimestamp);
			transaction.commit();
			session.flush();
			session.clear();
		}catch (Exception e) {
			e.printStackTrace();
			integrationFlag = false;
			//回滚事务
			if(transaction != null){
				transaction.rollback();
			}
			throw new AmbFrameException("获取来料检验单数据失败!",e);
		}finally{
			integrationFlag = false;
			if(session != null){
				session.close();
			}
		}
	}
	private volatile boolean integrationFlag = false;
	@Override
	public boolean isIntegration() {
		return integrationFlag;
	}
	public int getProgressbar() {
		// TODO Auto-generated method stub
		return 0;
	}

}

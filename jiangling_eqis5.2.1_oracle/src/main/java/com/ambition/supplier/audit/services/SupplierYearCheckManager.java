package com.ambition.supplier.audit.services;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.audit.dao.SupplierMonthCheckDao;
import com.ambition.supplier.audit.dao.SupplierYearCheckDao;
import com.ambition.supplier.entity.SupplierMonthCheck;
import com.ambition.supplier.entity.SupplierYearCheck;
import com.ambition.supplier.entity.SupplierYearCheckPlan;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月27日 发布
 */
@Service
@Transactional
public class SupplierYearCheckManager extends AmbWorkflowManagerBase<SupplierYearCheck>{
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SupplierYearCheckDao supplierYearCheckDao;
	@Autowired
	private SupplierMonthCheckDao supplierMonthCheckDao;
	@Autowired
	private SupplierMonthCheckManager supplierMonthCheckManager;
	public SupplierYearCheck getSupplierYearCheck(Long id) {
		// TODO Auto-generated method stub
		return supplierYearCheckDao.get(id);
	}
	public SupplierYearCheck getSupplierYearCheckBySupplierId(Long supplierId,Integer year) {
		// TODO Auto-generated method stub
		String hql = " from SupplierYearCheck s where s.supplierId=? and year=?";
		List<SupplierYearCheck> supplierYearChecks = supplierYearCheckDao.find(hql,supplierId,year);
		if(supplierYearChecks.size()==0){
			return null;
		}else{
			return supplierYearChecks.get(0);
		}
	}
	/**
     * 方法名: setProperty 
     * <p>功能说明：设置属性</p>
     * @return void
     * @throws   
      */
     private void setProperty(Object obj,String property,Object value) throws Exception{
         Class<?> type = PropertyUtils.getPropertyType(obj,property);
         if(type != null){
             if(value==null||StringUtils.isEmpty(value.toString())){
                 PropertyUtils.setProperty(obj,property,null);
             }else{
                  if(String.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,value.toString());
                 }else if(Integer.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Integer.valueOf(value.toString()));
                 }else if(Double.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Double.valueOf(value.toString()));
                 }else if(Float.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Float.valueOf(value.toString()));
                 }else if(Boolean.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Boolean.valueOf(value.toString()));
                 }else if(Date.class.getName().equals(type.getName())){
                     if(Date.class.getName().equals(value.getClass().getName())){
                         PropertyUtils.setProperty(obj,property,value);
                     }else if(String.class.getName().equals(value.getClass().getName())&&value.toString().length()==10){
                         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                         PropertyUtils.setProperty(obj,property,sdf.parse(value.toString()));
                     }
                 }else{
                     PropertyUtils.setProperty(obj,property,value);
                 }
             }
         }
     }
	public void saveSupplierYearCheck(SupplierYearCheck supplierYearCheck) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException {
		// TODO Auto-generated method stub
		supplierYearCheckDao.save(supplierYearCheck);
		String childParams=Struts2Utils.getParameter("childParams");
	    JSONArray itemStrArray=null;
	    if(!childParams.isEmpty()){
	        itemStrArray=JSONArray.fromObject(childParams);
	    }
//	    if(supplierYearCheck.getSupplierCheckPlans()==null){
//	    	supplierYearCheck.setSupplierCheckPlans(new ArrayList<SupplierYearCheckPlan>());
//        }else{
//        	supplierYearCheck.getSupplierCheckPlans().clear();
//        }
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!itemStrArray.isEmpty()){
            for(int i = 0;i<itemStrArray.size();i++){
                JSONObject jso = itemStrArray.getJSONObject(i);
                SupplierYearCheckPlan supplierYearCheckPlan = new SupplierYearCheckPlan();
                supplierYearCheckPlan.setCompanyId(ContextUtils.getCompanyId());
                supplierYearCheckPlan.setCreatedTime(new Date());
                supplierYearCheckPlan.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
                supplierYearCheckPlan.setCreator(ContextUtils.getUserName());
                Integer planMonth = null;
                Integer designMonth = null;
                Date newDate = null;
                Integer year = null;
                Date plan = null;
                Date design = null;
                Integer planDate = null;
                Integer designDate = null;
                for(Object key : jso.keySet()){
                    String value= jso.getString(key.toString());
                    if(value!=null&&value.length()!=0){
                    	 newDate = sdf.parse(value);
                    	 year = newDate.getYear() + 1900;
                         if(key.equals("planDate")){
                        	 plan = newDate;
                        	 planMonth = newDate.getMonth() + 1;
                        	 planDate = newDate.getDate();
                         }else if(key.equals("designDate")){
                        	 design = newDate;
                        	 designMonth = newDate.getMonth() + 1;
                        	 designDate = newDate.getDate();
                         }
                    }
                    try {
                        setProperty(supplierYearCheckPlan, key.toString(), value);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                setsupplierYearCheckDate(planMonth,planDate,designMonth,designDate,supplierYearCheck);
             	SupplierMonthCheck supplierMonthCheckOld = supplierMonthCheckManager.findSupplierMonthCheck(supplierYearCheck,planMonth);
             	if(supplierMonthCheckOld!=null){
             		supplierMonthCheckDao.delete(supplierMonthCheckOld);
             	}
             	supplierMonthCheckOld = null;
             	/*if(supplierMonthCheckOld!=null){
             		supplierMonthCheckOld.setFirstCheckDate(supplierYearCheck.getFirstCheckDate());
             		supplierMonthCheckOld.setSecondCheckDate(supplierYearCheck.getSecondCheckDate());
             		supplierMonthCheckOld.setFinalCheckResult(supplierYearCheck.getFinalCheckResult());
             		supplierMonthCheckOld.setChecker(supplierYearCheck.getChecker());
             		supplierMonthCheckOld.setCheckFile(supplierYearCheck.getCheckFile());
             		supplierMonthCheckOld.setRemark(supplierYearCheck.getRemark());
             	}*/
             	saveSupplierMonthCheck(supplierYearCheck, supplierMonthCheckOld,planMonth,plan,design);
             	supplierYearCheckPlan.setSupplierYearCheck(supplierYearCheck);
//                supplierYearCheck.getSupplierCheckPlans().add(supplierYearCheckPlan);
                supplierYearCheckDao.getSession().save(supplierYearCheckPlan);
                
            }
        }
        System.out.println(supplierYearCheck.getYear());
        supplierYearCheckDao.save(supplierYearCheck);
	}
	private void setsupplierYearCheckDate(Integer planMonth, Integer planDate,Integer designMonth, Integer designDate,SupplierYearCheck supplierYearCheck) {
		// TODO Auto-generated method stub
		if(planMonth!=null){
			if(planMonth==1){
				supplierYearCheck.setJanuaryPlan(planDate);
			}else if(planMonth==2){
				supplierYearCheck.setFebruaryPlan(planDate);
			}else if(planMonth==3){
				supplierYearCheck.setMarchPlan(planDate);
			}else if(planMonth==4){
				supplierYearCheck.setAprilPlan(planDate);
			}else if(planMonth==5){
				supplierYearCheck.setMayPlan(planDate);
			}else if(planMonth==6){
				supplierYearCheck.setJunePlan(planDate);
			}else if(planMonth==7){
				supplierYearCheck.setJulyPlan(planDate);
			}else if(planMonth==8){
				supplierYearCheck.setAugustPlan(planDate);
			}else if(planMonth==9){
				supplierYearCheck.setSeptemberPlan(planDate);
			}else if(planMonth==10){
				supplierYearCheck.setOctoberPlan(planDate);
			}else if(planMonth==11){
				supplierYearCheck.setNovemberPlan(planDate);
			}else if(planMonth==12){
				supplierYearCheck.setDecemberPlan(planDate);
			}
		}
		if(designMonth!=null){
			if(designMonth==1){
				supplierYearCheck.setJanuaryDesign(designDate);
			}else if(designMonth==2){
				supplierYearCheck.setFebruaryDesign(designDate);
			}else if(designMonth==3){
				supplierYearCheck.setMarchDesign(designDate);
			}else if(designMonth==4){
				supplierYearCheck.setAprilDesign(designDate);
			}else if(designMonth==5){
				supplierYearCheck.setMayDesign(designDate);
			}else if(designMonth==6){
				supplierYearCheck.setJuneDesign(designDate);
			}else if(designMonth==7){
				supplierYearCheck.setJulyDesign(designDate);
			}else if(designMonth==8){
				supplierYearCheck.setAugustDesign(designDate);
			}else if(designMonth==9){
				supplierYearCheck.setSeptemberDesign(designDate);
			}else if(designMonth==10){
				supplierYearCheck.setOctoberDesign(designDate);
			}else if(designMonth==11){
				supplierYearCheck.setNovemberDesign(designDate);
			}else if(designMonth==12){
				supplierYearCheck.setDecemberDesign(designDate);
			}
		}
		
	}
	private void saveSupplierMonthCheck(SupplierYearCheck supplierYearCheck,SupplierMonthCheck supplierMonthCheckOld,Integer planMonth, Date plan, Date design) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		//一月
		if(supplierMonthCheckOld==null&&planMonth==1){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"januaryPlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"januaryDesign",design,plan.getDate());
			}else{
				supplierMonthCheckDao.save(supplierMonthCheck);
			}
		}else if(supplierMonthCheckOld!=null&&planMonth==1){
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"januaryPlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"januaryDesign",design,plan.getDate());
		}
		//二月
		if(supplierMonthCheckOld==null&&planMonth==2){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"februaryPlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"februaryDesign",design,plan.getDate());
			}else{
				supplierYearCheckDao.getSession().save(supplierMonthCheck);
			}
		}else if(supplierMonthCheckOld!=null&&planMonth==2){
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"februaryPlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"februaryDesign",design,plan.getDate());
		}
		//三月
		if(supplierMonthCheckOld==null&&planMonth==3){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"marchPlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"marchDesign",design,plan.getDate());
			}else{
				supplierYearCheckDao.getSession().save(supplierMonthCheck);
			}
		}else if(supplierMonthCheckOld!=null&&planMonth==3) {
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"marchPlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"marchDesign",design,plan.getDate());
		}
		//四月
		if(supplierMonthCheckOld==null&&planMonth==4){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"aprilPlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"aprilDesign",design,plan.getDate());
			}else{
				supplierYearCheckDao.getSession().save(supplierMonthCheck);
			}
		}else if(supplierMonthCheckOld!=null&&planMonth==4){
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"aprilPlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"aprilDesign",design,plan.getDate());
		}
		//五月
		if(supplierMonthCheckOld==null&&planMonth==5){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"mayPlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"mayDesign",design,plan.getDate());
			}else{
				supplierYearCheckDao.getSession().save(supplierMonthCheck);
			}
		}else if(supplierMonthCheckOld!=null&&planMonth==5){
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"mayPlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"mayDesign",design,plan.getDate());
		}
		//六月
		if(supplierMonthCheckOld==null&&planMonth==6){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"junePlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"juneDesign",design,plan.getDate());
			}else{
				supplierYearCheckDao.getSession().save(supplierMonthCheck);
			}
			
		}else if(supplierMonthCheckOld!=null&&planMonth==6){
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"junePlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"juneDesign",design,plan.getDate());
		}
		//七月
		if(supplierMonthCheckOld==null&&planMonth==7){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"julyPlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"julyDesign",design,plan.getDate());
			}else{
				supplierYearCheckDao.getSession().save(supplierMonthCheck);
			}
		}else if(supplierMonthCheckOld!=null&&planMonth==7){
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"julyPlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"julyDesign",design,plan.getDate());
		}
		//八月
		if(supplierMonthCheckOld==null&&planMonth==8){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"augustPlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"augustDesign",design,plan.getDate());
			}else{
				supplierYearCheckDao.getSession().save(supplierMonthCheck);
			}
		}else if(supplierMonthCheckOld!=null&&planMonth==8){
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"augustPlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"augustDesign",design,plan.getDate());
		}
		//九月
		if(supplierMonthCheckOld==null&&planMonth==9){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"septemberPlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"septemberDesign",design,plan.getDate());
			}else{
				supplierYearCheckDao.getSession().save(supplierMonthCheck);
			}
		}else if(supplierMonthCheckOld!=null&&planMonth==9){
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"septemberPlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"septemberDesign",design,plan.getDate());
		}
		//十月
		if(supplierMonthCheckOld==null&&planMonth==10){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"octoberPlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"octoberDesign",design,plan.getDate());
			}else{
				supplierYearCheckDao.getSession().save(supplierMonthCheck);
			}
		}else if(supplierMonthCheckOld!=null&&planMonth==10){
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"octoberPlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"octoberDesign",design,plan.getDate());
		}
		//十一月
		if(supplierMonthCheckOld==null&&planMonth==11){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"novemberPlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"novemberDesign",design,plan.getDate());
			}else{
				supplierYearCheckDao.getSession().save(supplierMonthCheck);
			}
		}else if(supplierMonthCheckOld!=null&&planMonth==11) {
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"novemberPlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"novemberDesign",design,plan.getDate());
		}
		//十二月
		if(supplierMonthCheckOld==null&&planMonth==12){
			SupplierMonthCheck supplierMonthCheck = new SupplierMonthCheck();
			createNewMonth(supplierMonthCheck,supplierYearCheck,planMonth);
			setPlanValue(supplierMonthCheck,supplierYearCheck,"decemberPlan",plan,plan.getDate());
			if(design!=null){
				setDesignValue(supplierMonthCheck,supplierYearCheck,"decemberDesign",design,plan.getDate());
			}else{
				supplierYearCheckDao.getSession().save(supplierMonthCheck);
			}
		}else if(supplierMonthCheckOld!=null&&planMonth==12){
			setPlanValue(supplierMonthCheckOld,supplierYearCheck,"decemberPlan",plan,plan.getDate());
			setDesignValue(supplierMonthCheckOld,supplierYearCheck,"decemberDesign",design,plan.getDate());
		}
	}
	private void createNewMonth(SupplierMonthCheck supplierMonthCheck, SupplierYearCheck supplierYearCheck,Integer planMonth) {
		// TODO Auto-generated method stub
		supplierMonthCheck.setCreatedTime(new Date());
		supplierMonthCheck.setCreator(ContextUtils.getLoginName());
		supplierMonthCheck.setCreatorName(ContextUtils.getUserName());
		supplierMonthCheck.setYear(supplierYearCheck.getYear());
		supplierMonthCheck.setMonthOfYear(planMonth);
		supplierMonthCheck.setYearCheckId(supplierYearCheck.getId());
		supplierMonthCheck.setSupplierId(supplierYearCheck.getSupplierId());
		supplierMonthCheck.setCompanyId(ContextUtils.getCompanyId());
		supplierMonthCheck.setSupplierName(supplierYearCheck.getSupplierName());
		supplierMonthCheck.setSupplierCode(supplierYearCheck.getSupplierCode());
		supplierMonthCheck.setSupplyFactory(supplierYearCheck.getSupplyFactory());
		supplierMonthCheck.setSupplyMaterial(supplierYearCheck.getSupplyMaterial());
		supplierMonthCheck.setFirstCheckDate(supplierYearCheck.getFirstCheckDate());
		supplierMonthCheck.setSecondCheckDate(supplierYearCheck.getSecondCheckDate());
		supplierMonthCheck.setFinalCheckResult(supplierYearCheck.getFinalCheckResult());
		supplierMonthCheck.setChecker(supplierYearCheck.getChecker());
		supplierMonthCheck.setCheckFile(supplierYearCheck.getCheckFile());
		supplierMonthCheck.setRemark(supplierYearCheck.getRemark());
	}
	private void setDesignValue(SupplierMonthCheck supplierMonthCheck,
			SupplierYearCheck supplierYearCheck, String monthStr,Date design, Integer planDate) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// TODO Auto-generated method stub
		if(planDate!=null){
			if(planDate==1){
				supplierMonthCheck.setFirstDayDesign(design);
			}else if(planDate==2){
				supplierMonthCheck.setSecondDayDesign(design);
			}else if(planDate==3){
				supplierMonthCheck.setThirdDayDesign(design);
			}else if(planDate==4){
				supplierMonthCheck.setForthDayDesign(design);
			}else if(planDate==5){
				supplierMonthCheck.setFifthDayDesign(design);
			}else if(planDate==6){
				supplierMonthCheck.setSixthDayDesign(design);
			}else if(planDate==7){
				supplierMonthCheck.setSeventhDayDesign(design);
			}else if(planDate==8){
				supplierMonthCheck.setEighthDayDesign(design);
			}else if(planDate==9){
				supplierMonthCheck.setNinthDayDesign(design);
			}else if(planDate==10){
				supplierMonthCheck.setTenthDayDesign(design);
			}else if(planDate==11){
				supplierMonthCheck.setEleventhDayDesign(design);
			}else if(planDate==12){
				supplierMonthCheck.setTwelfthDayDesign(design);
			}else if(planDate==13){
				supplierMonthCheck.setThirtheenthDayDesign(design);
			}else if(planDate==14){
				supplierMonthCheck.setFourtheenthDayDesign(design);
			}else if(planDate==15){
				supplierMonthCheck.setFiftheenthDayDesign(design);
			}else if(planDate==16){
				supplierMonthCheck.setSixtheenthDayDesign(design);
			}else if(planDate==17){
				supplierMonthCheck.setSeventeenthDayDesign(design);
			}else if(planDate==18){
				supplierMonthCheck.setEighteenthDayDesign(design);
			}else if(planDate==19){
				supplierMonthCheck.setNineteenthDayDesign(design);
			}else if(planDate==20){
				supplierMonthCheck.setTwentiethDayDesign(design);
			}else if(planDate==21){
				supplierMonthCheck.setTwentyFirstDayDesign(design);
			}else if(planDate==22){
				supplierMonthCheck.setTwentySecondDayDesign(design);
			}else if(planDate==23){
				supplierMonthCheck.setTwentyThirdDayDesign(design);
			}else if(planDate==24){
				supplierMonthCheck.setTwentyFourthDayDesign(design);
			}else if(planDate==25){
				supplierMonthCheck.setTwentyFifthDayDesign(design);
			}else if(planDate==26){
				supplierMonthCheck.setTwentySixthDayDesign(design);
			}else if(planDate==27){
				supplierMonthCheck.setTwentySeventhDayDesign(design);
			}else if(planDate==28){
				supplierMonthCheck.setTwentyEighthDayDesign(design);
			}else if(planDate==29){
				supplierMonthCheck.setTwentyNinethDayDesign(design);
			}else if(planDate==30){
				supplierMonthCheck.setThirtiethDayDesign(design);
			}else if(planDate==31){
				supplierMonthCheck.setThirtyFirstDayDesign(design);
			}
		}
		supplierMonthCheck.setFirstCheckDate(supplierYearCheck.getFirstCheckDate());
		supplierMonthCheck.setSecondCheckDate(supplierYearCheck.getSecondCheckDate());
		supplierMonthCheck.setFinalCheckResult(supplierYearCheck.getFinalCheckResult());
		supplierMonthCheck.setChecker(supplierYearCheck.getChecker());
		supplierMonthCheck.setCheckFile(supplierYearCheck.getCheckFile());
		supplierMonthCheck.setRemark(supplierYearCheck.getRemark());
		supplierYearCheckDao.getSession().save(supplierMonthCheck);
	}
	private void setPlanValue(SupplierMonthCheck supplierMonthCheck,
			SupplierYearCheck supplierYearCheck, String monthStr, Date plan,Integer planDate) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// TODO Auto-generated method stub
		if(planDate!=null){
			if(planDate==1){
				supplierMonthCheck.setFirstDayPlan(plan);
			}else if(planDate==2){
				supplierMonthCheck.setSecondDayPlan(plan);
			}else if(planDate==3){
				supplierMonthCheck.setThirdDayPlan(plan);
			}else if(planDate==4){
				supplierMonthCheck.setForthDayPlan(plan);
			}else if(planDate==5){
				supplierMonthCheck.setFifthDayPlan(plan);
			}else if(planDate==6){
				supplierMonthCheck.setSixthDayPlan(plan);
			}else if(planDate==7){
				supplierMonthCheck.setSeventhDayPlan(plan);
			}else if(planDate==8){
				supplierMonthCheck.setEighthDayPlan(plan);
			}else if(planDate==9){
				supplierMonthCheck.setNinthDayPlan(plan);
			}else if(planDate==10){
				supplierMonthCheck.setTenthDayPlan(plan);
			}else if(planDate==11){
				supplierMonthCheck.setEleventhDayPlan(plan);
			}else if(planDate==12){
				supplierMonthCheck.setTwelfthDayPlan(plan);
			}else if(planDate==13){
				supplierMonthCheck.setThirtheenthDayPlan(plan);
			}else if(planDate==14){
				supplierMonthCheck.setFourtheenthDayPlan(plan);
			}else if(planDate==15){
				supplierMonthCheck.setFiftheenthDayPlan(plan);
			}else if(planDate==16){
				supplierMonthCheck.setSixtheenthDayPlan(plan);
			}else if(planDate==17){
				supplierMonthCheck.setSeventeenthDayPlan(plan);
			}else if(planDate==18){
				supplierMonthCheck.setEighteenthDayPlan(plan);
			}else if(planDate==19){
				supplierMonthCheck.setNineteenthDayPlan(plan);
			}else if(planDate==20){
				supplierMonthCheck.setTwentiethDayPlan(plan);
			}else if(planDate==21){
				supplierMonthCheck.setTwentyFirstDayPlan(plan);
			}else if(planDate==22){
				supplierMonthCheck.setTwentySecondDayPlan(plan);
			}else if(planDate==23){
				supplierMonthCheck.setTwentyThirdDayPlan(plan);
			}else if(planDate==24){
				supplierMonthCheck.setTwentyFourthDayPlan(plan);
			}else if(planDate==25){
				supplierMonthCheck.setTwentyFifthDayPlan(plan);
			}else if(planDate==26){
				supplierMonthCheck.setTwentySixthDayPlan(plan);
			}else if(planDate==27){
				supplierMonthCheck.setTwentySeventhDayPlan(plan);
			}else if(planDate==28){
				supplierMonthCheck.setTwentyEighthDayPlan(plan);
			}else if(planDate==29){
				supplierMonthCheck.setTwentyNinethDayPlan(plan);
			}else if(planDate==30){
				supplierMonthCheck.setThirtiethDayPlan(plan);
			}else if(planDate==31){
				supplierMonthCheck.setThirtyFirstDayPlan(plan);
			}
		}
		supplierMonthCheck.setFirstCheckDate(supplierYearCheck.getFirstCheckDate());
		supplierMonthCheck.setSecondCheckDate(supplierYearCheck.getSecondCheckDate());
		supplierMonthCheck.setFinalCheckResult(supplierYearCheck.getFinalCheckResult());
		supplierMonthCheck.setChecker(supplierYearCheck.getChecker());
		supplierMonthCheck.setCheckFile(supplierYearCheck.getCheckFile());
		supplierMonthCheck.setRemark(supplierYearCheck.getRemark());
	}
	public Page<SupplierYearCheck> getSupplierYearCheckPage(
			Page<SupplierYearCheck> page) {
		// TODO Auto-generated method stub
		String hql = " from SupplierYearCheck s where s.companyId=?";
		return supplierYearCheckDao.searchPageByHql(page, hql, ContextUtils.getCompanyId());
	}
//	public void delete(String ids) {
//		// TODO Auto-generated method stub
//		String[] deleteIds = ids.split(",");
//		for(String id : deleteIds){
//			SupplierYearCheck supplierYearCheck = supplierYearCheckDao.get(Long.valueOf(id));
//			if(supplierYearCheck!=null){
//				logUtilDao.debugLog("删除", supplierYearCheckDao.get(Long.valueOf(id)).toString());
//				List<SupplierYearCheckPlan> plans = supplierYearCheck.getSupplierCheckPlans();
//				for(SupplierYearCheckPlan s : plans){
//					supplierYearCheckDao.getSession().delete(s);
//				}
//				supplierYearCheckDao.delete(supplierYearCheck);
//			}
//		}
//	}
	@Override
	public HibernateDao<SupplierYearCheck, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return supplierYearCheckDao;
	}
	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "SUPPLIER_YEAR_CHECK";
	}
	@Override
	public Class<SupplierYearCheck> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return SupplierYearCheck.class;
	}
	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "supplier-year-check";
	}
	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "供应商年度稽核流程";
	}

}

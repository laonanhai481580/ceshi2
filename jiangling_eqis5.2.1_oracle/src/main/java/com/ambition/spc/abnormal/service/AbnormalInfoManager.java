package com.ambition.spc.abnormal.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.abnormal.dao.AbnormalInfoDao;
import com.ambition.spc.abnormal.util.BaseUtil;
import com.ambition.spc.abnormal.util.ConstDefine;
import com.ambition.spc.abnormal.util.ConstantsOfSPC;
import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.entity.AbnormalForRealTime;
import com.ambition.spc.entity.AbnormalInfo;
import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.entity.FeaturePerson;
import com.ambition.spc.entity.FeatureRules;
import com.ambition.spc.entity.ProcessPoint;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.jlanalyse.service.ControlLimitManager;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;

/**    
 * AbnormalInfoManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class AbnormalInfoManager {
	@Autowired
	private AbnormalInfoDao abnormalInfoDao;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private SpcSubGroupManager spcSubGroupManager;
	@Autowired
	private ControlLimitManager controlLimitManager;
	
	public AbnormalInfo getAbnormalInfo(Long id){
		return abnormalInfoDao.get(id);
	}
	
	public void deleteAbnormalInfo(String deleteIds) {
		String ids[] = deleteIds.split(",");
		for(String id:ids){
			AbnormalInfo abnormalInfo = abnormalInfoDao.get(Long.valueOf(id));
			if(abnormalInfo != null){
				abnormalInfoDao.delete(abnormalInfo);
			}
		}
	}
	
	public Page<AbnormalInfo> getPage(Page<AbnormalInfo> page){
		return abnormalInfoDao.getPage(page);
	}
	/**
	 * 过程监控-异常消息	 列表数据
	 */
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public Page<AbnormalInfo> searchByPage(Page<AbnormalInfo> page,JSONObject params){
		StringBuffer sb = new StringBuffer("from AbnormalInfo a where a.companyId = ?");
		params = convertJsonObject(params);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		//查询条件
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			//发生时间 occurDate
			if(params.containsKey("startDate_ge_date") && params.containsKey("endDate_le_date")){
				if(params.getString("startDate_ge_date") != null && params.getString("endDate_le_date") != null){
					try {
						startDate = sdf.parse(params.getString("startDate_ge_date"));
						endDate = DateUtil.parseDate(params.getString("endDate_le_date").toString()+" 23:59:59", "yyyy-MM-dd hh:mm:ss");;
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				searchParams.add(startDate);
				searchParams.add(endDate);
				sb.append(" and a.occurDate between ? and ?");
			}
			//处理单号 num
			if(params.containsKey("num") && params.getString("num") != null){
				String  num = params.getString("num");
				searchParams.add(num);
				sb.append(" and a.num = ?");
			}
			//处理状态 priState
			if(params.containsKey("priState") && params.getString("priState") != null){
				String  priState = params.getString("priState");
				searchParams.add(priState);
				sb.append(" and a.priState = ?");
			}
			//工厂名称OneName
			if(params.containsKey("OneName") && params.getString("OneName") != null){
				String  OneName = params.getString("OneName");
//				searchParams.add(OneName);
				sb.append(" and a.qualityFeature.processPoint.parent.parent.name like '%"+OneName+"%'");
			}
			//质量特性模块TwoName
			if(params.containsKey("TwoName") && params.getString("TwoName") != null){
				String TwoName = params.getString("TwoName");
//				searchParams.add(TwoName);
				sb.append(" and a.qualityFeature.processPoint.parent.name like '%"+TwoName+"%'");
			}
			//质量特性总称ThreeName
			if(params.containsKey("ThreeName") && params.getString("ThreeName") != null){
				String ThreeName = params.getString("ThreeName");
//				searchParams.add(ThreeName);
				sb.append(" and a.qualityFeature.processPoint.name like '%"+ThreeName+"%'");
			}
			//质量特性 qualityFeature
			if(params.containsKey("qualityFeatures") && params.getString("qualityFeatures") != null){
				Long featureId = Long.valueOf(params.getString("qualityFeatures"));
				QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(featureId);
				searchParams.add(qualityFeature);
				sb.append(" and a.qualityFeature = ?");
			}
		}
		page = abnormalInfoDao.searchPageByHql(page, sb.toString(), searchParams.toArray());
		return page;
	}
	public JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}else{
			for(Object key : params.keySet()){
				resultJson.put(key,params.getJSONArray(key.toString()).get(0));
			}
			return resultJson;
		}
	}
	/**
	  * 方法名: 根据条件查询异常消息
	  * <p>功能说明：</p>
	  * @param page
	  * @param qualityFeatureId
	  * @param startDate
	  * @param endDate
	  * @param lastAmount
	  * @return
	 */
	public Page<AbnormalInfo> getPage(Page<AbnormalInfo> page,Long qualityFeatureId,Date startDate,Date endDate,Integer lastAmount){
		return abnormalInfoDao.getPage(page,qualityFeatureId,startDate,endDate,lastAmount);
	}
	
	private String getRuleName(QualityFeature qualityFeature,int abnormity) {
		String result = "";
		Abnormity[] abnormityArray = judgeCollectDataOfAbnormity(qualityFeature,true,0);
		for(int i=0;i<abnormityArray.length;i++){
			if(abnormity == abnormityArray[i].getAbnormity()){
				result = abnormityArray[i].getAbnormityCN();
			}
		}
		if(StringUtils.isEmpty(result)){
			result = "无规则！";
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void saveAbnormalInfo(List excepList,QualityFeature qualityFeature,String num, int alarmType,Session session){
		AbnormalForRealTime abnor = new AbnormalForRealTime();
		AbnormalInfo info = new AbnormalInfo();
		SpcSubGroup group = spcSubGroupManager.queryGroupByNumAndFeature(qualityFeature, Integer.valueOf(num),session);
		int ab_num = 0;
		if(excepList.get(0) != null){
			abnor = (AbnormalForRealTime) excepList.get(0);
			ab_num = abnor.getAbnormity();
			info.setCompanyId(group==null?ContextUtils.getCompanyId():group.getCompanyId());
			info.setCreator(group==null?null:group.getCreator());
			info.setCreatedTime(group==null?new Date():group.getCreatedTime());
			info.setModifiedTime(new Date());
			info.setModifier(group==null?null:group.getCreator());
			info.setByParaName(qualityFeature.getName());
			info.setByParaNum(qualityFeature.getCode());
			info.setNum(num);
			List<FeatureRules> ruleSet = qualityFeature.getFeatureRules();
			if (ruleSet != null && ruleSet.size() > 0) {
				String ruleName = "";
				String ruleNo = "";
				for(FeatureRules rule:ruleSet){
					if (rule != null) {
						if (!"".equals(ruleName)) {
							ruleName += ";";
						}
						if (!"".equals(ruleNo)) {
							ruleNo += ";";
						}
						ruleName += rule.getName();
						ruleNo += rule.getNo();
					}
				}
				info.setByRuleName(ruleName);
				info.setByRuleNum(ruleNo);
			}
			SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			abnor.setHappenTime(group==null?sdf.format(new Date()):sdf.format(group.getCreatedTime()));
			info.setName(this.getRuleName(qualityFeature,abnor.getAbnormity()));
			info.setDsc(abnor.getAbnormityCN(true));
			if(abnor.getHappenTime() != null && abnor.getHappenTime().equals("")){
				info.setOccurTime(abnor.getHappenTime());
			}else{
				info.setOccurTime(group==null?sdf.format(new Date()):group.getCreatedTime().toString());
			}
//			Calendar calendar = Calendar.getInstance();
			info.setOccurDate(group==null?new Date():group.getCreatedTime());
			info.setType(String.valueOf(alarmType));
			info.setPriState("2");//初始状态
			info.setQualityFeature(qualityFeature);
			if(group != null){
				info.setSpcSubGroup(group);
			}
			if(group!=null&&this.isExist(info.getQualityFeature().getId(), info.getNum(),info.getName()).isEmpty()){
				session.save(info);
			}
		}
		for(int i=0;i<excepList.size();i++){
			abnor = (AbnormalForRealTime) excepList.get(i);
			if(ab_num != 0 && abnor.getAbnormity() != 0 && ab_num != abnor.getAbnormity()){
				info = new AbnormalInfo();
				info.setCompanyId(group==null?ContextUtils.getCompanyId():group.getCompanyId());
				info.setCreator(group==null?null:group.getCreator());
				info.setModifier(group==null?null:group.getCreator());
				info.setCreatedTime(group==null?new Date():group.getCreatedTime());
				info.setModifiedTime(new Date());
				info.setByParaName(qualityFeature.getName());
				info.setByParaNum(qualityFeature.getCode());
				info.setNum(num);
				List<FeatureRules> ruleSet = qualityFeature.getFeatureRules();
				if (ruleSet != null && ruleSet.size() > 0) {
					String ruleName = "";
					String ruleNo = "";
					for(FeatureRules rule:ruleSet){
						if (rule != null) {
							if (!"".equals(ruleName)) {
								ruleName += ";";
							}
							if (!"".equals(ruleNo)) {
								ruleNo += ";";
							}
							ruleName += rule.getName();
							ruleNo += rule.getNo();
						}
					}
					info.setByRuleName(ruleName);
					info.setByRuleNum(ruleNo);
				}
				if(abnor != null){
					SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					abnor.setHappenTime(group==null?sdf.format(new Date()):sdf.format(group.getCreatedTime()));
					info.setName(this.getRuleName(qualityFeature,abnor.getAbnormity()));
					info.setDsc(abnor.getAbnormityCN(true));
					if(abnor.getHappenTime() != null && abnor.getHappenTime().equals("")){
						info.setOccurTime(abnor.getHappenTime());
					}else{
						info.setOccurTime(group==null?sdf.format(new Date()):group.getCreatedTime().toString());
					}
					info.setOccurDate(group==null?new Date():group.getCreatedTime());
				}
				info.setType(String.valueOf(alarmType));
				info.setPriState("2");//初始状态
				info.setQualityFeature(qualityFeature);
				if(group != null){
					info.setSpcSubGroup(group);
				}
				if(group!=null&&this.isExist(info.getQualityFeature().getId(), info.getNum(),info.getName()).isEmpty()){
					session.save(info);
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public List<SpcSubGroup> querySpcSubGroupByQualityTime(QualityFeature qualityFeature,Integer num){
		String totalHql ="select Max(ab.createdTime) as timeMax from AbnormalInfo ab where ab.qualityFeature = ?";//   
		List<?> totalList = abnormalInfoDao.createQuery(totalHql).setParameter(0,qualityFeature).list();
		String crTime = null;//最近时间
		if(totalList.size()>0&&!totalList.isEmpty()&&totalList.get(0)!=null){
			crTime=totalList.get(0).toString();
		}
		String hql = "from SpcSubGroup s where s.companyId = ?";
//		and s.judgeState = 0
		List<Object> searchParams = new ArrayList<Object>();
			searchParams.add(ContextUtils.getCompanyId());
		if(crTime!=null){
			searchParams.add(crTime);
			totalHql ="select Max(s.createdTime) as timeMax from SpcSubGroup s where s.qualityFeature = ? ";//   and s.subGroupOrderNum > ?
			totalList = abnormalInfoDao.createQuery(totalHql)
									   .setParameter(0,qualityFeature)
//									   .setParameter(1,num)
									   .list();
			if(totalList.size()>0&&!totalList.isEmpty()&&totalList.get(0)!=null){
				crTime=totalList.get(0).toString();
			}
			searchParams.add(crTime);
//			hql = hql + " and s.createdTime between ? and ?";
			hql = hql + " and to_char(s.createdTime,'YYYY-MM-DD HH24:MI:SS') > ? and to_char(s.createdTime,'YYYY-MM-DD HH24:MI:SS') <= ?";
		}else{
			totalHql ="select Min(s.createdTime) as timeMax from SpcSubGroup s where s.qualityFeature = ? and s.subGroupOrderNum = ?";//   
			totalList = abnormalInfoDao.createQuery(totalHql)
									   .setParameter(0,qualityFeature)
									   .setParameter(1,num)
									   .list();
			if(totalList.size()>0&&!totalList.isEmpty()&&totalList.get(0)!=null){
				crTime=totalList.get(0).toString();
			}
			searchParams.add(crTime);
			hql = hql + " and to_char(s.createdTime,'YYYY-MM-DD HH24:MI:SS') <= ?";
		}
			
			
		if(qualityFeature != null){
			searchParams.add(qualityFeature);
			hql = hql + " and s.qualityFeature = ?";
		}
		Query query =  abnormalInfoDao.createQuery(hql.toString()+"order by s.createdTime asc");
		for(int i = 0;i < searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		return query.list();
	}
	@SuppressWarnings("unchecked")
	public List<SpcSubGroup> querySpcSubGroupByQuality(QualityFeature qualityFeature){
		String hql = "from SpcSubGroup s where s.companyId = ? ";
//		and s.judgeState = 0
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(qualityFeature != null){
			searchParams.add(qualityFeature);
			hql = hql + " and s.qualityFeature = ?";
		}
//		String queryHist = Struts2Utils.getParameter("queryHist");//查询标识
//		if(StringUtils.isEmpty(queryHist)){
//			Calendar calendar = Calendar.getInstance();
//			searchParams.add(calendar.getTime());
//			calendar.add(Calendar.DATE, -10);
//			searchParams.add(searchParams.size()-1, calendar.getTime());
//			hql = hql + " and s.createdTime between ? and ?";
//		}
		Query query =  abnormalInfoDao.createQuery(hql.toString()+"order by s.subGroupOrderNum asc");
		for(int i = 0;i < searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		return query.list();
	}
	/**
	 * 判断【质量特性ID+子组号+异常名称】是否已经生成异常
	  * 方法名: 
	  * <p>功能说明：</p>
	  * @return
	 */
	private List<AbnormalInfo> isExist(Long qualityFeatureId,String num,String name) {
		String hql = "from AbnormalInfo s where s.qualityFeature.id = ? and s.num = ? and s.name = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(qualityFeatureId);
		searchParams.add(num);
		searchParams.add(name);
		return abnormalInfoDao.find(hql,searchParams.toArray());
	}
	/**
	  * 方法名: spc数据采集预警
	  * <p>功能说明：</p>
	  * @param spcSubOrderNum 子组号
	  * @param qualityFeature 质量特性
	  * @param list
	 */
	public void lanchAbnormal(String spcSubOrderNum,QualityFeature qualityFeature,List<?> list,Session session){
		if(null == session){
			session = abnormalInfoDao.getSession();
		}
		//根据规则检测所查询的数据  异常报警
		List<?> excepList = judgeCollectData(qualityFeature,list,true,0,session);
		//保存异常信息
		try {
			if(null != excepList && excepList.size() != 0){
				saveAbnormalInfo(excepList,qualityFeature,spcSubOrderNum,0,session);
				//发送邮件(这里发送事务死锁-暂时注释-处理之后再放开)
				/**/AbnormalInfo info = getInfoByFeature(qualityFeature,session);
				//邮件内容
				StringBuffer emailContent = new StringBuffer("您好：\n\t");
				if(null != info){
					emailContent.append("\r\n"+info.getDsc());
				}
				//邮件主题
				StringBuffer subject = new StringBuffer("");
				List<FeaturePerson> persons = qualityFeature.getFeaturePersons();
				for(FeaturePerson person:persons){
					String email = ApiFactory.getAcsService().getUserById(Long.valueOf(person.getCode())).getEmail();
					if(StringUtils.isNotEmpty(email)){
					    //发送邮件(目前异常注释)
						if(qualityFeature.getProcessPoint()!=null){
							//质量特性总称ThreeName
							subject.insert(0,qualityFeature.getProcessPoint().getName());
							if(qualityFeature.getProcessPoint().getParent()!=null){
								//质量特性模块TwoName
								subject.insert(0,qualityFeature.getProcessPoint().getParent().getName());
								if(qualityFeature.getProcessPoint().getParent().getParent()!=null){
									//工厂名称
									subject.insert(0,qualityFeature.getProcessPoint().getParent().getParent().getName());
								}
							}
						}
						AsyncMailUtils.sendMail(email,subject.append(qualityFeature.getName()+"异常预警").toString(),emailContent.toString());
					}
				}
			}
		} catch (Exception e) {
			throw new AmbFrameException("Exception-触发SPC预警失败!",e);
		} catch (Throwable t){
			throw new AmbFrameException("Throwable-触发SPC预警失败!",t);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List judgeCollectData(QualityFeature qualityFeature,List subList,Boolean testflag,int alarmType,Session session){
		Abnormity[] abnormityArray = null;
		//解析规则 返回规则数组
		abnormityArray = judgeCollectDataOfAbnormity(qualityFeature,testflag,alarmType);

		//获取控制图类型1、均值极差 2、均值标准差 4、单值移动极差
		//int chartType = ConstDefine.getChartType(qualityFeature.getControlChart());
		int chartType = Integer.valueOf(qualityFeature.getControlChart());
		
		//其他类型或者是计量类型
		Integer controlState = ConstantsOfSPC.SPC_SAMPLING_STATE_ANALYSIS_I;
		Double ucl = 0.00;
		Double lcl = 0.00;
		Double cl = 0.00;
		Double ucl1 = 0.00;
		Double lcl1 = 0.00;
		Double cl1 = 0.00;
//		Double up = new Double(0.00), mid = new Double(0.00), low = new Double(0.00);
		Double up = null, mid = null, low = null;
		
		if(qualityFeature.getUpperTarge()!=null){//目标值上限
			up = Double.valueOf(qualityFeature.getUpperTarge());
		}
		if(qualityFeature.getTargeValue()!=null){//规格线目标值
			mid = Double.valueOf(qualityFeature.getTargeValue());
		}
		if(qualityFeature.getLowerTarge()!=null){//目标值下限
			low = Double.valueOf(qualityFeature.getLowerTarge());
		}
		
		//计算控制限ucl、lcl、cl
		List<ControlLimit> controlLimits = controlLimitManager.getControlLimitDesc(qualityFeature.getId(),session);
		if (controlLimits.size() > 0) {
			ControlLimit c = controlLimits.get(0);
			ucl = c.getXucl();
			lcl = c.getXlcl();
			cl = c.getXcl();
			ucl1 = c.getSucl();
			lcl1 = c.getSlcl();
			cl1 = c.getScl();
		}
		ArrayList result = new ArrayList();
		result = BaseUtil.getNormalAbnor(subList, abnormityArray, chartType, controlState, up, low, mid, ucl, lcl, cl, ucl1, lcl1, cl1, qualityFeature,testflag);
		return result;
	}
	
	/**
	  * 方法名: spc数据采集预警
	  * <p>功能说明：</p>
	  * @param spcSubOrderNum 子组号
	  * @param qualityFeature 质量特性
	  * @param list
	 */
	public void lanchAbnormalCCM(String spcSubOrderNum,QualityFeature qualityFeature,List<?> list,Session session,String equipmentNo){
		if(null == session){
			session = abnormalInfoDao.getSession();
		}
		//根据规则检测所查询的数据  异常报警
		List<?> excepList = judgeCollectData(qualityFeature,list,false,0,session);
		Calendar calendar = Calendar.getInstance();
		Date be = calendar.getTime();
		calendar.add(Calendar.DATE, -10);
		Date af = calendar.getTime();
		//保存异常信息
		try {
			if(null != excepList && excepList.size() != 0){
				saveAbnormalInfoCCM(excepList,qualityFeature,spcSubOrderNum,0,session, equipmentNo);
				//发送邮件(这里发送事务死锁-暂时注释-处理之后再放开)
				AbnormalInfo info = getInfoByFeature(qualityFeature,session);
				//邮件内容
				String emailContent = "";
				if(null != info){
					emailContent = info.getDsc();
				}
				List<FeaturePerson> persons = qualityFeature.getFeaturePersons();
				for(FeaturePerson person:persons){
					String email = ApiFactory.getAcsService().getUserById(Long.valueOf(person.getCode())).getEmail();
					if(StringUtils.isNotEmpty(email)){
					    //发送邮件(目前异常注释)
						ProcessPoint point = qualityFeature.getProcessPoint();
						AsyncMailUtils.sendMail(email,qualityFeature.getName()+be +"至"+af+ "机台"+equipmentNo+"的数据异常预警","机种:"+point.getParent()==null?point.getName():point.getParent()+",工序:"+point.getName() +":"+emailContent.toString());
					}
				}
			}
		} catch (Exception e) {
			throw new AmbFrameException("Exception-触发SPC预警失败!",e);
		} catch (Throwable t){
			throw new AmbFrameException("Throwable-触发SPC预警失败!",t);
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void saveAbnormalInfoCCM(List excepList,QualityFeature qualityFeature,String num, int alarmType,Session session,String equipmentNo){
		AbnormalForRealTime abnor = new AbnormalForRealTime();
		AbnormalInfo info = new AbnormalInfo();
		SpcSubGroup group = spcSubGroupManager.queryGroupByNumAndFeature(qualityFeature, Integer.valueOf(num),session);
		int ab_num = 0;
		if(excepList.get(0) != null){
			abnor = (AbnormalForRealTime) excepList.get(0);
			ab_num = abnor.getAbnormity();
			info.setCompanyId(group==null?ContextUtils.getCompanyId():group.getCompanyId());
			info.setEquipmentNo(equipmentNo);
			info.setCreator(group==null?null:group.getCreator());
			info.setCreatedTime(new Date());
			info.setModifiedTime(new Date());
			info.setModifier(group==null?null:group.getCreator());
			info.setByParaName(qualityFeature.getName());
			info.setByParaNum(qualityFeature.getCode());
			info.setNum(num);
			List<FeatureRules> ruleSet = qualityFeature.getFeatureRules();
			if (ruleSet != null && ruleSet.size() > 0) {
				String ruleName = "";
				String ruleNo = "";
				for(FeatureRules rule:ruleSet){
					if (rule != null) {
						if (!"".equals(ruleName)) {
							ruleName += ";";
						}
						if (!"".equals(ruleNo)) {
							ruleNo += ";";
						}
						ruleName += rule.getName();
						ruleNo += rule.getNo();
					}
				}
				info.setByRuleName(ruleName);
				info.setByRuleNum(ruleNo);
			}
			info.setName(this.getRuleName(qualityFeature,abnor.getAbnormity()));
			info.setDsc(abnor.getAbnormityCN(true));
			SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(abnor.getHappenTime() != null && abnor.getHappenTime().equals("")){
				info.setOccurTime(abnor.getHappenTime());
			}else{
				info.setOccurTime(sdf.format(new Date()));
			}
//			Calendar calendar = Calendar.getInstance();
			info.setOccurDate(new Date());
			info.setType(String.valueOf(alarmType));
			info.setPriState("2");//初始状态
			info.setQualityFeature(qualityFeature);
			if(group != null){
				info.setSpcSubGroup(group);
			}
			session.save(info);
		}
		
		for(int i=0;i<excepList.size();i++){
			abnor = (AbnormalForRealTime) excepList.get(i);
			if(ab_num != 0 && abnor.getAbnormity() != 0 && ab_num != abnor.getAbnormity()){
				info = new AbnormalInfo();
				info.setCompanyId(group==null?ContextUtils.getCompanyId():group.getCompanyId());
				info.setCreator(group==null?null:group.getCreator());
				info.setModifier(group==null?null:group.getCreator());
				info.setCreatedTime(new Date());
				info.setModifiedTime(new Date());
				info.setByParaName(qualityFeature.getName());
				info.setByParaNum(qualityFeature.getCode());
				info.setNum(num);
				List<FeatureRules> ruleSet = qualityFeature.getFeatureRules();
				if (ruleSet != null && ruleSet.size() > 0) {
					String ruleName = "";
					String ruleNo = "";
					for(FeatureRules rule:ruleSet){
						if (rule != null) {
							if (!"".equals(ruleName)) {
								ruleName += ";";
							}
							if (!"".equals(ruleNo)) {
								ruleNo += ";";
							}
							ruleName += rule.getName();
							ruleNo += rule.getNo();
						}
					}
					info.setByRuleName(ruleName);
					info.setByRuleNum(ruleNo);
				}
				if(abnor != null){
					info.setName(this.getRuleName(qualityFeature,abnor.getAbnormity()));
					info.setDsc(abnor.getAbnormityCN(true));
					SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(abnor.getHappenTime() != null && abnor.getHappenTime().equals("")){
						info.setOccurTime(abnor.getHappenTime());
					}else{
						info.setOccurTime(sdf.format(new Date()));
					}
					info.setOccurDate(new Date());
				}
				info.setType(String.valueOf(alarmType));
				info.setPriState("2");//初始状态
				info.setQualityFeature(qualityFeature);
				if(group != null){
					info.setSpcSubGroup(group);
				}
				session.save(info);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static Abnormity[] judgeCollectDataOfAbnormity(QualityFeature qualityFeature,Boolean testflag,int alarmType){
		Abnormity[] abnormityArray = null;
		if(alarmType<2){
			List list = new ArrayList();
			for(FeatureRules rule:qualityFeature.getFeatureRules()){
				if (rule != null) {
					String exp = rule.getExpression();
					if (StringUtils.isNotEmpty(exp) && exp.indexOf("..")==-1) {
						ConstDefine.parseRule(exp, list, qualityFeature.getParamType(),qualityFeature.getControlChart());
					}
				}
			}
			if (list.size() == 0) {
				return null;
			}
		    abnormityArray = new Abnormity[list.size()];
			for (int i = 0; i < list.size(); i++) {
				abnormityArray[i] = (Abnormity) list.get(i);
			}
		}
		return abnormityArray;
	}
	
	public Object findSpcSubGroupByNum(QualityFeature qualityFeature,int num){
		String hql = "from SpcSubGroup s where s.companyId = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(qualityFeature != null){
			searchParams.add(qualityFeature);
			hql = hql + " and s.qualityFeature = ?";
		}
		if(num != 0){
			searchParams.add(num);
			hql = hql + " and s.subGroupOrderNum = ?";
		}
		Query query =  abnormalInfoDao.getSession().createQuery(hql.toString());
		for(int i = 0;i < searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		return query.list().get(0);
	}
	
	@SuppressWarnings("unchecked")
	public AbnormalInfo getInfoByFeature(QualityFeature qualityFeature,Session session){
		String hql = "from AbnormalInfo a where 1 = 1 ";
		List<Object> searchParams = new ArrayList<Object>();
		if(null != qualityFeature){
			if(null != qualityFeature.getCompanyId()){
				hql = hql + " and a.companyId = ? ";
				searchParams.add(qualityFeature.getCompanyId());
			}
			hql = hql + " and a.qualityFeature = ? ";
			searchParams.add(qualityFeature);
		}
		hql += " order by id desc ";
		Query query =  session.createQuery(hql);
		for(int i = 0;i < searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		List<AbnormalInfo> list = query.list();
		if(null == list || list.size() == 0){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	public List<AbnormalInfo> getAbnormalInfo(Long qualityFeatureId,Date startDate,Date endDate){
		return abnormalInfoDao.getAbnormalInfo(qualityFeatureId, startDate, endDate);
	}
	public List<AbnormalInfo> getHandleAbnormalInfo(Long qualityFeatureId,Date startDate,Date endDate,String priState){
		return abnormalInfoDao.getHandleAbnormalInfo(qualityFeatureId, startDate, endDate, priState);
	}
	
	/**
	  * 方法名: 监控异常信息
	  * <p>功能说明：spc数据采集时的异常准则监控,目前仅支持规格限的监控</p>
	  * @param group
	 */
	public void moniterFeatureRules(SpcSubGroup group,List<FeatureRules> featureRules){
//		for(FeatureRules featureRule : featureRules){
//			//以9结束,表示是在规格限监控
//			if(featureRule.getExpression().endsWith("9")){
//				monitorAbnormalInfoForSpecifications(group,featureRule);
//			}
//		}
	}
	/**
	  * 方法名: 监控规格限超限
	  * <p>功能说明：</p>
	  * @param group
	 */
	@SuppressWarnings("unused")
	private void monitorAbnormalInfoForSpecifications(SpcSubGroup group,FeatureRules featureRule){
		//判断是否超出规格限
		if(group.getQualityFeature()==null){
			return;
		}
		QualityFeature qualityFeature = group.getQualityFeature();
		if(qualityFeature.getUpperTarge()==null||qualityFeature.getLowerTarge()==null){
			return;
		}
		boolean isError = Boolean.FALSE;
		double min = qualityFeature.getLowerTarge(),max = qualityFeature.getUpperTarge();
		if(group.getSigma()>max||group.getSigma()<min){
			isError = Boolean.TRUE;
		}
		if(!isError){
			return;
		}
		//判断是否符合监控条件,表达式格式如:"0.1.2.1.9"
		String strs[] = featureRule.getExpression().split("\\.");
		int lastCount = Integer.valueOf(strs[2]);
		int errorCount = Integer.valueOf(strs[3]);
		boolean canAdd = Boolean.FALSE;
		if(errorCount == 1){
			canAdd = Boolean.TRUE;
		}else{
			String hql = "from SpcSubGroup s where s.qualityFeature = ? and s.subGroupOrderNum <=? order by s.subGroupOrderNum desc";
			Query query = abnormalInfoDao.createQuery(hql,qualityFeature,group.getSubGroupOrderNum());
			query.setFirstResult(0);
			query.setMaxResults(lastCount);
			@SuppressWarnings("unchecked")
			List<SpcSubGroup> groups = query.list();
			int count = 0;
			for(SpcSubGroup spcSubGroup : groups){
				if(spcSubGroup.getSigma()>max||spcSubGroup.getSigma()<min){
					count++;
				}
			}
			if(count>=errorCount){
				canAdd = Boolean.TRUE;
			}
		}
		//符合监控条件
		if(canAdd){
			AbnormalInfo abnormalInfo = new AbnormalInfo();
			abnormalInfo.setCompanyId(group.getCompanyId());
			abnormalInfo.setCreatedTime(new Date());
			abnormalInfo.setCreator(group.getModifier());
			abnormalInfo.setModifiedTime(new Date());
			abnormalInfo.setModifier(group.getModifier());
			abnormalInfo.setQualityFeature(qualityFeature);
			abnormalInfo.setSpcSubGroup(group);
			
			abnormalInfo.setName(qualityFeature.getName());
			abnormalInfo.setNum(group.getSubGroupOrderNum()+"");
			abnormalInfo.setOccurDate(group.getCreatedTime());
			abnormalInfo.setByRuleID(featureRule.getId());
			abnormalInfo.setByRuleName(featureRule.getName());
			abnormalInfo.setByRuleNum(featureRule.getNo());
			abnormalInfo.setDsc("目标上限为" + max + ",目标下限为" + min + ",采集数据的均值为" + group.getSigma());
			abnormalInfoDao.save(abnormalInfo);
		}
	}
}
